<%-- 
    Document   : inicio
    Created on : 30 oct 2026, 2:36:57 p.m.
    Author     : USUARIO
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.metaspersonales.model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");

    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio | Metas Personales</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<main class="auth-container">
    <section class="auth-card">
        <h1>Metas Personales</h1>

        <h2>Bienvenido/a, <%= usuario.getNombres() %></h2>

        <p>Has iniciado sesión correctamente.</p>
        <p>Usuario: <%= usuario.getUsername() %></p>
        <p>Correo: <%= usuario.getEmail() %></p>

        <form action="${pageContext.request.contextPath}/logout"
              method="post">
            <button type="submit">Cerrar sesión</button>
        </form>

        <p>
            Esta es una pantalla inicial. Aquí se integrarán
            los módulos de Rutas, Agenda, Archivo y Métricas.
        </p>
    </section>
</main>
</body>
</html>

