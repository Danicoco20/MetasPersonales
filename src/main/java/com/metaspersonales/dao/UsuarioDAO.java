package com.metaspersonales.dao;

import com.metaspersonales.config.HibernateUtil;
import com.metaspersonales.model.PasswordResetToken;
import com.metaspersonales.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

/** Acceso a usuarios y tokens de recuperación mediante Hibernate/JPA. */
public class UsuarioDAO {

    public boolean existeEmailOUsuario(String email, String username) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long cantidad = em.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email OR u.username = :username",
                Long.class)
                .setParameter("email", email)
                .setParameter("username", username)
                .getSingleResult();
            return cantidad > 0;
        } finally { em.close(); }
    }

    public void registrar(Usuario usuario, String passwordHash) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            usuario.setPasswordHash(passwordHash);
            em.persist(usuario);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    public Usuario buscarPorEmail(String email) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Usuario> resultados = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                .setParameter("email", email).setMaxResults(1).getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally { em.close(); }
    }

    public boolean estaBloqueado(String email) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<LocalDateTime> fechas = em.createQuery(
                "SELECT u.lockedUntil FROM Usuario u WHERE u.email = :email", LocalDateTime.class)
                .setParameter("email", email).setMaxResults(1).getResultList();
            return !fechas.isEmpty() && fechas.get(0) != null
                && fechas.get(0).isAfter(LocalDateTime.now());
        } finally { em.close(); }
    }

    /** Registra el fallo de forma transaccional para evitar actualizaciones perdidas. */
    public int registrarIntentoFallido(String email, int maxIntentos, int minutosBloqueo) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<Usuario> encontrados = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                .setParameter("email", email).setMaxResults(1)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
            if (encontrados.isEmpty()) { tx.commit(); return -1; }
            Usuario u = encontrados.get(0);
            if (u.getLockedUntil() != null && u.getLockedUntil().isAfter(LocalDateTime.now())) {
                tx.commit(); return maxIntentos;
            }
            if (u.getLockedUntil() != null && !u.getLockedUntil().isAfter(LocalDateTime.now())) {
                u.setLockedUntil(null);
                u.setFailedLoginAttempts(0);
            }
            int intentos = u.getFailedLoginAttempts() + 1;
            if (intentos >= maxIntentos) {
                u.setFailedLoginAttempts(0);
                u.setLockedUntil(LocalDateTime.now().plusMinutes(minutosBloqueo));
            } else {
                u.setFailedLoginAttempts(intentos);
            }
            tx.commit();
            return intentos;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    public void limpiarIntentosFallidos(int userId) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Usuario u = em.find(Usuario.class, userId, LockModeType.PESSIMISTIC_WRITE);
            if (u != null) {
                u.setFailedLoginAttempts(0);
                u.setLockedUntil(null);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    public void crearTokenRecuperacion(int userId, String tokenHash, LocalDateTime expiracion) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Usuario usuario = em.find(Usuario.class, userId);
            if (usuario == null) throw new IllegalArgumentException("Usuario inexistente");
            em.createQuery("UPDATE PasswordResetToken t SET t.usedAt = :ahora "
                + "WHERE t.usuario.userId = :id AND t.usedAt IS NULL")
                .setParameter("ahora", LocalDateTime.now()).setParameter("id", userId)
                .executeUpdate();
            PasswordResetToken token = new PasswordResetToken();
            token.setUsuario(usuario);
            token.setTokenHash(tokenHash);
            token.setExpiresAt(expiracion);
            em.persist(token);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    public boolean tokenValido(String tokenHash) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long n = em.createQuery(
                "SELECT COUNT(t) FROM PasswordResetToken t WHERE t.tokenHash = :hash "
                + "AND t.usedAt IS NULL AND t.expiresAt > :ahora", Long.class)
                .setParameter("hash", tokenHash).setParameter("ahora", LocalDateTime.now())
                .getSingleResult();
            return n > 0;
        } finally { em.close(); }
    }

    /** Consume el token y cambia la contraseña en una única transacción. */
    public boolean restablecerPassword(String tokenHash, String nuevoHash) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<PasswordResetToken> tokens = em.createQuery(
                "SELECT t FROM PasswordResetToken t WHERE t.tokenHash = :hash "
                + "AND t.usedAt IS NULL AND t.expiresAt > :ahora",
                PasswordResetToken.class)
                .setParameter("hash", tokenHash).setParameter("ahora", LocalDateTime.now())
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).setMaxResults(1).getResultList();
            if (tokens.isEmpty()) { tx.rollback(); return false; }
            PasswordResetToken token = tokens.get(0);
            Usuario usuario = em.find(Usuario.class, token.getUsuario().getUserId(),
                LockModeType.PESSIMISTIC_WRITE);
            usuario.setPasswordHash(nuevoHash);
            usuario.setFailedLoginAttempts(0);
            usuario.setLockedUntil(null);
            token.setUsedAt(LocalDateTime.now());
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }
}
