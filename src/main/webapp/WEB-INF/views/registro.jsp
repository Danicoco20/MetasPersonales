<%-- 
    Document   : registro
    Created on : 30 oct 2026, 2:36:38 p.m.
    Author     : USUARIO
--%>



<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro | Metas Personales</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">

    <script defer
          src="${pageContext.request.contextPath}/js/app.js"></script>
</head>
<body>

<main class="auth-page">
    <section class="auth-card register-card">

        <h1>Metas Personales</h1>

        <nav class="auth-tabs">
            <a href="${pageContext.request.contextPath}/registro"
               class="auth-tab active">
                Registrarse
            </a>

            <a href="${pageContext.request.contextPath}/login"
               class="auth-tab">
                Iniciar sesión
            </a>
        </nav>

        
        <% if (request.getAttribute("error") != null) { %>
            <p class="form-message error">
                <%= request.getAttribute("error") %>
            </p>
        <% } %>

        <form id="registerForm"
              class="register-form"
              action="${pageContext.request.contextPath}/registro"
              method="post">

            <div class="field field-full">
                <label for="nombres"
                           class="sr-only">

                        Nombres

                    </label>

                <input type="text"
                       id="nombres"
                       name="nombres"
                       placeholder="Nombres"
                       maxlength="100"
                       required>
            </div>

            <div class="field field-full">
                 <label for="apellidos"
                           class="sr-only">

                        Apellidos

                    </label>
                <input type="text"
                       id="apellidos"
                       name="apellidos"
                       placeholder="Apellidos"
                       maxlength="100"
                       required>
            </div>

            <div class="field">
                <label for="fechaNacimiento"
                           class="sr-only">

                        Fecha de nacimiento

                    </label>
                <input type="date"
                       id="fechaNacimiento"
                       name="fechaNacimiento"
                       max="<%= java.time.LocalDate.now() %>"
                       required>
            </div>

            <div class="field">
                <label for="telefono"
                           class="sr-only">

                        Número de teléfono

                    </label>

                <input type="tel"
                       id="telefono"
                       name="telefono"
                       placeholder="Teléfono"
                       pattern="[0-9]{10,20}"
                       maxlength="20"
                       required>
            </div>

            <div class="field field-full">
                <label for="nombreUsuario"
                           class="sr-only">

                        Nombre de usuario

                    </label>
                <input type="text"
                       id="nombreUsuario"
                       name="nombreUsuario"
                       placeholder="Nombre de usuario"
                       minlength="3"
                       maxlength="100"
                       required>
            </div>

            <div class="field field-full">
                <label for="registroEmail"
                           class="sr-only">

                        Correo electrónico

                    </label>
                <input type="email"
                       id="registroEmail"
                       name="email"
                       placeholder="Correo electrónico"
                       maxlength="150"
                       required>
            </div>

            <div class="field">
                <label for="registroPassword"
                           class="sr-only">

                        Contraseña

                    </label>
                <input type="password"
                       id="registroPassword"
                       name="password"
                       placeholder="Contraseña"
                       minlength="8"
                       required>
            </div>

            <div class="field">
                <label for="confirmPassword"
                           class="sr-only">

                        Confirmar contraseña

                    </label>
                <input type="password"
                       id="confirmPassword"
                       name="confirmPassword"
                       placeholder="Confirmar contraseña"
                       minlength="8"
                       required>
            </div>

            <button type="submit"
                    class="primary-button register-button">
                Crear cuenta
            </button>

        </form>

        <p>
            ¿Ya tienes una cuenta?
            <a href="${pageContext.request.contextPath}/login">
                Inicia sesión
            </a>
        </p>

    </section>
</main>

</body>
</html>

