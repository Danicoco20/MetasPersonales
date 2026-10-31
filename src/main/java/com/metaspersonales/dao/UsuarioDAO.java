/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.metaspersonales.dao;

import com.metaspersonales.config.ConexionBD;
import com.metaspersonales.model.Usuario;

import java.sql.*;
import java.time.LocalDate;

public class UsuarioDAO {

    public boolean existeEmailOUsuario(String email, String username)
            throws SQLException {

        String sql = "SELECT 1 FROM Usuario " +
                     "WHERE email = ? OR username = ? LIMIT 1";

        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void registrar(Usuario usuario, String passwordHash)
            throws SQLException {

        String sql = "INSERT INTO Usuario " +
            "(nombres, apellidos, username, email, bday, phone_number, password_hash) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombres());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getUsername());
            ps.setString(4, usuario.getEmail());

            // Conversión de LocalDate a SQL DATE.
            ps.setDate(5, Date.valueOf(usuario.getFechaNacimiento()));

            ps.setString(6, usuario.getTelefono());
            ps.setString(7, passwordHash);

            ps.executeUpdate();
        }
    }

    public Usuario buscarPorEmail(String email) throws SQLException {

        String sql = "SELECT user_id, nombres, apellidos, username, " +
            "email, bday, phone_number FROM Usuario WHERE email = ? LIMIT 1";

        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) return null;

                Date fecha = rs.getDate("bday");

                return new Usuario(
                    rs.getInt("user_id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("username"),
                    rs.getString("email"),
                    fecha == null ? null : fecha.toLocalDate(),
                    rs.getString("phone_number")
                );
            }
        }
    }

    public String buscarHashPorEmail(String email) throws SQLException {

        String sql = "SELECT password_hash FROM Usuario WHERE email = ? LIMIT 1";

        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("password_hash") : null;
            }
        }
    }
}

