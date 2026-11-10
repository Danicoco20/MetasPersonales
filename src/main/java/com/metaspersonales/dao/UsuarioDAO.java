package com.metaspersonales.dao;

import com.metaspersonales.config.HibernateUtil;
import com.metaspersonales.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

/** Acceso a Usuario mediante JPA con Hibernate como proveedor ORM. */
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
        } finally {
            em.close();
        }
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
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorEmail(String email) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Usuario> resultados = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                .setParameter("email", email)
                .setMaxResults(1)
                .getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }
}
