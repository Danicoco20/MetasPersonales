/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.metaspersonales.controller;

import com.metaspersonales.dao.UsuarioDAO;
import com.metaspersonales.model.Usuario;
import com.metaspersonales.security.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/registro.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String nombres = valor(req, "nombres");
        String apellidos = valor(req, "apellidos");
        String fecha = valor(req, "fechaNacimiento");
        String telefono = valor(req, "telefono");
        String username = valor(req, "nombreUsuario");
        String email = valor(req, "email").toLowerCase();
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");

        String error = validar(nombres, apellidos, fecha, telefono,
                               username, email, password, confirm);

        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/WEB-INF/views/registro.jsp")
               .forward(req, resp);
            return;
        }

        try {
            if (dao.existeEmailOUsuario(email, username)) {
                req.setAttribute("error",
                    "El correo o nombre de usuario ya está registrado.");
                req.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                   .forward(req, resp);
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setFechaNacimiento(LocalDate.parse(fecha));
            usuario.setTelefono(telefono);
            usuario.setUsername(username);
            usuario.setEmail(email);

            dao.registrar(usuario, PasswordUtil.hash(password));

            resp.sendRedirect(req.getContextPath() +
                              "/login?registro=exitoso");

        } catch (RuntimeException e) {
            log("Error en el registro de usuario", e);
            req.setAttribute("error",
                "No fue posible completar el registro. Intenta nuevamente.");
            req.getRequestDispatcher("/WEB-INF/views/registro.jsp")
               .forward(req, resp);
        }
    }

    private String validar(String nombres, String apellidos, String fecha,
                           String telefono, String username, String email,
                           String password, String confirm) {

        if (nombres.isEmpty() || apellidos.isEmpty() || fecha.isEmpty()
            || telefono.isEmpty() || username.isEmpty() || email.isEmpty()
            || password == null || confirm == null) {
            return "Completa todos los campos.";
        }

        try {
            if (LocalDate.parse(fecha).isAfter(LocalDate.now())) {
                return "La fecha de nacimiento no puede ser futura.";
            }
        } catch (Exception e) {
            return "La fecha de nacimiento no es válida.";
        }

        if (!Pattern.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", email))
            return "Ingresa un correo electrónico válido.";

        if (!Pattern.matches("^\\d{10,20}$", telefono))
            return "El teléfono debe contener entre 10 y 20 dígitos.";

        if (!Pattern.matches("^[A-Za-z0-9_.-]{3,100}$", username))
            return "El nombre de usuario debe tener entre 3 y 100 caracteres.";

        if (password.length() < 8)
            return "La contraseña debe tener al menos 8 caracteres.";

        if (!password.equals(confirm))
            return "Las contraseñas no coinciden.";

        return null;
    }

    private String valor(HttpServletRequest req, String nombre) {
        String valor = req.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }
}

