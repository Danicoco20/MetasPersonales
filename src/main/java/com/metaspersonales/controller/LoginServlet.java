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
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.trim().isEmpty()
            || password == null || password.isEmpty()) {
            req.setAttribute("error", "Ingresa tu correo y contraseña.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(req, resp);
            return;
        }

        email = email.trim().toLowerCase();

        try {
            Usuario usuario = dao.buscarPorEmail(email);
            String hash = dao.buscarHashPorEmail(email);

            if (usuario == null || !PasswordUtil.verificar(password, hash)) {
                req.setAttribute("error",
                    "Correo electrónico o contraseña incorrectos.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(req, resp);
                return;
            }

            HttpSession anterior = req.getSession(false);
            if (anterior != null) anterior.invalidate();

            HttpSession sesion = req.getSession(true);
            sesion.setAttribute("usuario", usuario);
            sesion.setMaxInactiveInterval(1800);

            resp.sendRedirect(req.getContextPath() + "/inicio");

        } catch (SQLException e) {
            log("Error al iniciar sesión", e);
            req.setAttribute("error",
                "No fue posible iniciar sesión. Intenta más tarde.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(req, resp);
        }
    }
}

