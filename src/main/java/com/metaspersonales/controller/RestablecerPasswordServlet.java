package com.metaspersonales.controller;

import com.metaspersonales.dao.UsuarioDAO;
import com.metaspersonales.security.PasswordUtil;
import com.metaspersonales.security.TokenUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/restablecer")
public class RestablecerPasswordServlet extends HttpServlet {
    private final UsuarioDAO dao = new UsuarioDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null || token.isBlank() || !dao.tokenValido(TokenUtil.hash(token))) {
            HttpSession sesion = req.getSession(false);
            if (sesion != null) sesion.removeAttribute("resetToken");
            req.setAttribute("error", "El enlace de restauración no es válido o ha vencido. Solicita uno nuevo.");
            req.getRequestDispatcher("/WEB-INF/views/restablecer.jsp").forward(req, resp);
            return;
        }
        req.getSession(true).setAttribute("resetToken", token);
        req.getRequestDispatcher("/WEB-INF/views/restablecer.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String token = (String) req.getSession().getAttribute("resetToken");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");
        if (token == null || password == null || confirm == null
                || password.length() < 8 || password.length() > 128
                || !password.equals(confirm)) {
            req.setAttribute("error", "Verifica el enlace y confirma una contraseña de entre 8 y 128 caracteres.");
            req.getRequestDispatcher("/WEB-INF/views/restablecer.jsp").forward(req, resp);
            return;
        }
        try {
            boolean actualizado = dao.restablecerPassword(TokenUtil.hash(token), PasswordUtil.hash(password));
            req.getSession().removeAttribute("resetToken");
            if (!actualizado) {
                req.setAttribute("error", "El enlace ya fue utilizado o ha vencido. Solicita uno nuevo.");
                req.getRequestDispatcher("/WEB-INF/views/restablecer.jsp").forward(req, resp);
                return;
            }
            req.getRequestDispatcher("/WEB-INF/views/restablecimiento-exitoso.jsp").forward(req, resp);
        } catch (RuntimeException e) {
            log("Error al restablecer la contraseña", e);
            req.setAttribute("error", "No fue posible cambiar la contraseña. Inténtalo de nuevo.");
            req.getRequestDispatcher("/WEB-INF/views/restablecer.jsp").forward(req, resp);
        }
    }
}
