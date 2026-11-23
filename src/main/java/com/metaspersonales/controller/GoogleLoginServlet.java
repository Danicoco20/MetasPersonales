package com.metaspersonales.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.metaspersonales.dao.UsuarioDAO;
import com.metaspersonales.model.Usuario;
import com.metaspersonales.security.GoogleTokenVerifier;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/oauth/google")
public class GoogleLoginServlet extends HttpServlet {
    private final UsuarioDAO dao = new UsuarioDAO();

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String credential = req.getParameter("credential");
        try {
            GoogleIdToken.Payload payload = GoogleTokenVerifier.verificar(credential);
            if (payload == null || !Boolean.TRUE.equals(payload.getEmailVerified())) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No fue posible validar la cuenta de Google.");
                return;
            }
            String email = payload.getEmail();
            Usuario usuario = dao.buscarPorEmail(email.toLowerCase(java.util.Locale.ROOT));
            if (usuario == null) {
                req.setAttribute("error", "No existe una cuenta de Metas Personales asociada a ese correo. Regístrate primero con ese correo y vuelve a intentarlo.");
                req.setAttribute("captchaSiteKey", System.getenv("RECAPTCHA_SITE_KEY"));
                req.setAttribute("googleClientId", System.getenv("GOOGLE_CLIENT_ID"));
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }
            LoginServlet.iniciarSesion(req, resp, usuario);
        } catch (Exception e) {
            log("Error al validar el inicio de sesión con Google", e);
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No fue posible validar el inicio de sesión con Google.");
        }
    }
}
