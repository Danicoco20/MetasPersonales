package com.metaspersonales.controller;

import com.metaspersonales.dao.UsuarioDAO;
import com.metaspersonales.model.Usuario;
import com.metaspersonales.security.EmailService;
import com.metaspersonales.security.TokenUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/recuperar")
public class RecuperarPasswordServlet extends HttpServlet {
    private final UsuarioDAO dao = new UsuarioDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/recuperar.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        email = email == null ? "" : email.trim().toLowerCase(java.util.Locale.ROOT);
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            req.setAttribute("error", "Ingresa un correo electrónico válido.");
            doGetWithMessage(req, resp); return;
        }
        try {
            Usuario usuario = dao.buscarPorEmail(email);
            if (usuario != null) {
                String token = TokenUtil.generar();
                dao.crearTokenRecuperacion(usuario.getUserId(), TokenUtil.hash(token),
                    LocalDateTime.now().plusMinutes(30));
                String baseUrl = System.getenv("APP_BASE_URL");
                if (baseUrl == null || baseUrl.isBlank()) {
                    baseUrl = req.getScheme() + "://" + req.getServerName()
                        + (req.getServerPort() == 80 || req.getServerPort() == 443 ? "" : ":" + req.getServerPort());
                }
                String enlace = baseUrl.replaceAll("/+$", "") + req.getContextPath() + "/restablecer?token="
                    + java.net.URLEncoder.encode(token, java.nio.charset.StandardCharsets.UTF_8);
                EmailService.enviarRecuperacion(usuario.getEmail(), usuario.getNombres(), enlace);
            }
            // Respuesta idéntica exista o no la cuenta: evita enumeración de correos.
            req.setAttribute("enviado", true);
            req.getRequestDispatcher("/WEB-INF/views/recuperar.jsp").forward(req, resp);
        } catch (Exception e) {
            log("No se pudo procesar la solicitud de recuperación", e);
            req.setAttribute("error", "No fue posible procesar la solicitud en este momento. Inténtalo más tarde.");
            doGetWithMessage(req, resp);
        }
    }

    private void doGetWithMessage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/recuperar.jsp").forward(req, resp);
    }
}
