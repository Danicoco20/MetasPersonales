<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.metaspersonales.security.HtmlUtil" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión | Metas Personales</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script defer src="${pageContext.request.contextPath}/js/app.js"></script>
    <% if (request.getAttribute("captchaSiteKey") != null && !((String)request.getAttribute("captchaSiteKey")).isBlank()) { %>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <% } %>
    <% if (request.getAttribute("googleClientId") != null && !((String)request.getAttribute("googleClientId")).isBlank()) { %>
    <script src="https://accounts.google.com/gsi/client" async defer></script>
    <% } %>
</head>
<body>
<main class="auth-page">
    <section class="auth-card">
        <h1>Metas Personales</h1>
        <nav class="auth-tabs">
            <a href="${pageContext.request.contextPath}/registro" class="auth-tab">Registrarse</a>
            <a href="${pageContext.request.contextPath}/login" class="auth-tab active">Iniciar sesión</a>
        </nav>

        <% if ("exitoso".equals(request.getParameter("registro"))) { %>
            <p class="form-message success">Tu cuenta fue creada. Ya puedes iniciar sesión.</p>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <p class="form-message error"><%= HtmlUtil.escape((String)request.getAttribute("error")) %></p>
        <% } %>

        <form id="loginForm" class="auth-form" action="${pageContext.request.contextPath}/login" method="post">
            <div class="field">
                <label for="email" class="sr-only">Correo electrónico</label>
                <input type="email" id="email" name="email" placeholder="Correo electrónico" maxlength="150" autocomplete="username" required>
            </div>
            <div class="field">
                <label for="password" class="sr-only">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Contraseña" autocomplete="current-password" required>
            </div>
            <a class="secondary-link" href="${pageContext.request.contextPath}/recuperar">¿Has olvidado tu contraseña?</a>

            <% String siteKey = (String)request.getAttribute("captchaSiteKey"); %>
            <% if (siteKey != null && !siteKey.isBlank()) { %>
                <div class="captcha-wrap">
                    <div class="g-recaptcha" data-sitekey="<%= HtmlUtil.escape(siteKey) %>" data-callback="onCaptchaVerified" data-expired-callback="onCaptchaExpired"></div>
                </div>
            <% } else { %>
                <p class="form-message error">El CAPTCHA no está configurado. Contacta al administrador.</p>
            <% } %>
            <button id="loginButton" type="submit" class="primary-button" <% if (siteKey == null || siteKey.isBlank()) { %>disabled<% } %>>Iniciar sesión</button>
        </form>

        <div class="social-login">
            <p>Acceso rápido</p>
            <% String clientId = (String)request.getAttribute("googleClientId"); %>
            <% if (clientId != null && !clientId.isBlank()) { %>
                <div id="g_id_onload" data-client_id="<%= HtmlUtil.escape(clientId) %>" data-callback="handleGoogleCredential" data-auto_prompt="false"></div>
                <div class="google-button-wrap"><div class="g_id_signin" data-type="standard" data-shape="rectangular" data-theme="outline" data-text="signin_with" data-size="large" data-logo_alignment="left"></div></div>
                <form id="googleLoginForm" action="${pageContext.request.contextPath}/oauth/google" method="post" hidden>
                    <input type="hidden" name="credential" id="googleCredential">
                </form>
            <% } else { %>
                <p class="form-message">El acceso con Google no está configurado.</p>
            <% } %>
        </div>
        <p>¿No tienes una cuenta? <a href="${pageContext.request.contextPath}/registro">Regístrate</a></p>
    </section>
</main>
</body>
</html>
