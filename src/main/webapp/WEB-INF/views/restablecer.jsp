<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.metaspersonales.security.HtmlUtil" %>
<!DOCTYPE html>
<html lang="es"><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Restaurar contraseña | Metas Personales</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
<script defer src="${pageContext.request.contextPath}/js/app.js"></script>
</head><body>
<main class="auth-page"><section class="auth-card recovery-card">
<h1>Metas Personales</h1><h2>Restauración de contraseña</h2>
<% if (request.getAttribute("error") != null) { %>
<p class="form-message error"><%= HtmlUtil.escape((String)request.getAttribute("error")) %></p>
<% } %>
<% if (session.getAttribute("resetToken") != null) { %>
<form id="resetForm" class="auth-form" action="${pageContext.request.contextPath}/restablecer" method="post">
<div class="field"><label class="sr-only" for="newPassword">Nueva contraseña</label>
<input type="password" id="newPassword" name="password" placeholder="Nueva contraseña" minlength="8" maxlength="128" autocomplete="new-password" required></div>
<div class="field"><label class="sr-only" for="confirmNewPassword">Confirmar contraseña</label>
<input type="password" id="confirmNewPassword" name="confirmPassword" placeholder="Confirmar contraseña" minlength="8" maxlength="128" autocomplete="new-password" required></div>
<button type="submit" class="primary-button">Guardar cambios</button>
</form>
<% } else { %>
<p><a href="${pageContext.request.contextPath}/recuperar">Solicitar un nuevo enlace</a></p>
<% } %>
</section></main></body></html>
