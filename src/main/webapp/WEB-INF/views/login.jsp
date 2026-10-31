<%-- 
    Document   : login
    Created on : 30 oct 2026, 2:35:40 p.m.
    Author     : USUARIO
--%>



<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión | Metas Personales</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">

    <script defer
          src="${pageContext.request.contextPath}/js/app.js"></script>
</head>
<body>

<main class="auth-page">
    <section class="auth-card">

        <h1>Metas Personales</h1>

        <nav class="auth-tabs">
            <a href="${pageContext.request.contextPath}/registro"
               class="auth-tab">
                Registrarse
            </a>

            <a href="${pageContext.request.contextPath}/login"
               class="auth-tab active">
                Iniciar sesión
            </a>
        </nav>

        

        <% if ("exitoso".equals(request.getParameter("registro"))) { %>
            <p class="form-message success">
                Tu cuenta fue creada. Ya puedes iniciar sesión.
            </p>
        <% } %>

        <% if (request.getAttribute("error") != null) { %>
            <p class="form-message error">
                <%= request.getAttribute("error") %>
            </p>
        <% } %>

        <form id="loginForm"
              class="auth-form"
              action="${pageContext.request.contextPath}/login"
              method="post">

            <div class="field">
                <label for="email" class="sr-only">
                        Correo electrónico
                    </label>

                <input type="email"
                       id="email"
                       name="email"
                       placeholder="Correo electrónico"
                       required>
            </div>

            <div class="field">
                <label for="password" class="sr-only">
                        Contraseña
                    </label>
                <input type="password"
                       id="password"
                       name="password"
                       placeholder="Contraseña"
                       required>
            </div>

            <button type="submit" class="primary-button">
                Ingresar
            </button>

        </form>

        <p>
            ¿No tienes una cuenta?
            <a href="${pageContext.request.contextPath}/registro">
                Regístrate
            </a>
        </p>

    </section>
</main>

</body>
</html>


