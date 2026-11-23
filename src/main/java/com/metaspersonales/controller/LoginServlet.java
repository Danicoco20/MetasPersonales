package com.metaspersonales.controller;

import com.metaspersonales.dao.UsuarioDAO;
import com.metaspersonales.model.Usuario;
import com.metaspersonales.security.CaptchaUtil;
import com.metaspersonales.security.PasswordUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final int MAX_INTENTOS = 5;
    private static final int MINUTOS_BLOQUEO = 15;
    private final UsuarioDAO dao = new UsuarioDAO();
    

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String siteKey = System.getenv("RECAPTCHA_SITE_KEY");
    String secretKey = System.getenv("RECAPTCHA_SECRET_KEY");

    log("========== DIAGNÓSTICO reCAPTCHA ==========");
    log("LOGIN SERVLET ACTUALIZADO: DOGET EJECUTADO");
    log("RECAPTCHA_SITE_KEY disponible: "
            + (siteKey != null && !siteKey.isBlank()));
    log("RECAPTCHA_SECRET_KEY disponible: "
            + (secretKey != null && !secretKey.isBlank()));
    log("============================================");

    req.setAttribute("captchaSiteKey", siteKey);
    req.setAttribute("googleClientId", System.getenv("GOOGLE_CLIENT_ID"));

    req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
}

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = valor(req.getParameter("email")).toLowerCase(java.util.Locale.ROOT);
        String password = req.getParameter("password");

        if (email.isBlank() || password == null || password.isEmpty()) {
            mostrar(req, resp, "Ingresa tu correo y contraseña."); return;
        }
        if (!CaptchaUtil.verificar(req.getParameter("g-recaptcha-response"), req.getRemoteAddr())) {
            mostrar(req, resp, "Completa la verificación CAPTCHA antes de continuar."); return;
        }

        try {
            if (dao.estaBloqueado(email)) {
                mostrar(req, resp, "La cuenta está bloqueada temporalmente por varios intentos fallidos. Intenta más tarde."); return;
            }
            Usuario usuario = dao.buscarPorEmail(email);
            if (usuario == null || !PasswordUtil.verificar(password, usuario.getPasswordHash())) {
                dao.registrarIntentoFallido(email, MAX_INTENTOS, MINUTOS_BLOQUEO);
                mostrar(req, resp, "Correo electrónico o contraseña incorrectos. Tras cinco intentos fallidos, el acceso se bloquea durante 15 minutos.");
                return;
            }
            dao.limpiarIntentosFallidos(usuario.getUserId());
            iniciarSesion(req, resp, usuario);
        } catch (RuntimeException e) {
            log("Error al iniciar sesión", e);
            mostrar(req, resp, "No fue posible iniciar sesión. Intenta más tarde.");
        }
    }

    static void iniciarSesion(HttpServletRequest req, HttpServletResponse resp, Usuario usuario)
            throws IOException {
        usuario.setPasswordHash(null);
        HttpSession anterior = req.getSession(false);
        if (anterior != null) anterior.invalidate();
        HttpSession sesion = req.getSession(true);
        sesion.setAttribute("usuario", usuario);
        sesion.setMaxInactiveInterval(1800);
        resp.sendRedirect(req.getContextPath() + "/inicio");
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, String error)
            throws ServletException, IOException {
        req.setAttribute("error", error);
        req.setAttribute("captchaSiteKey", System.getenv("RECAPTCHA_SITE_KEY"));
        req.setAttribute("googleClientId", System.getenv("GOOGLE_CLIENT_ID"));
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
    private String valor(String s) { return s == null ? "" : s.trim(); }
}
