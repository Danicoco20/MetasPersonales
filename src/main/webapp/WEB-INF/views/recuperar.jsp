<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.metaspersonales.security.HtmlUtil" %>
<!DOCTYPE html>
<html lang="es"><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Recuperar contraseña | Metas Personales</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head><body>
<main class="auth-page"><section class="auth-card recovery-card">
<h1>Metas Personales</h1>
<% if (Boolean.TRUE.equals(request.getAttribute("enviado"))) { %>
    <h2 class="form-message success">¡Solicitud procesada!</h2>
    <p>Si el correo está registrado, recibirás un mensaje con las instrucciones para restaurar tu contraseña. Revisa también la carpeta de spam.</p>
    <a class="primary-button button-link" href="${pageContext.request.contextPath}/recuperar">Enviar nuevo correo</a>
    <p><a href="${pageContext.request.contextPath}/login">Volver a iniciar sesión</a></p>
<% } else { %>
    <h2>Recuperación de contraseña</h2>
    <p>Ingresa el correo asociado a tu cuenta. Te enviaremos un enlace válido durante 30 minutos.</p>
    <% if (request.getAttribute("error") != null) { %><p class="form-message error"><%= HtmlUtil.escape((String)request.getAttribute("error")) %></p><% } %>
    <form class="auth-form" action="${pageContext.request.contextPath}/recuperar" method="post">
        <div class="field"><label class="sr-only" for="email">Correo electrónico</label>
            <input type="email" id="email" name="email" placeholder="Correo electrónico" maxlength="150" required></div>
        <button type="submit" class="primary-button">Enviar correo</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/login">Volver a iniciar sesión</a></p>
<% } %>
</section></main></body></html>
