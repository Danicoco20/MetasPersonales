# Plan de pruebas manuales — Autenticación

**Estado:** casos definidos; pendientes de ejecución funcional con MySQL, Tomcat y credenciales externas reales.

| ID | Escenario | Resultado esperado |
|---|---|---|
| AUTH-01 | Inicio con correo y contraseña correctos, CAPTCHA válido | Se crea sesión y se redirige a `/inicio`. |
| AUTH-02 | Inicio sin completar CAPTCHA | El servidor rechaza el intento. |
| AUTH-03 | CAPTCHA inválido, vencido o ausente | No se autentica al usuario. |
| AUTH-04 | Contraseña incorrecta cinco veces para una cuenta existente | Se activa bloqueo de 15 minutos. |
| AUTH-05 | Intento durante el bloqueo | Se rechaza el acceso sin verificar contraseña. |
| AUTH-06 | Acceso correcto después de finalizar el bloqueo | Se permite el acceso y se reinicia el contador. |
| AUTH-07 | Google Sign-In con correo verificado asociado a una cuenta existente | Se crea sesión y se redirige a `/inicio`. |
| AUTH-08 | Google Sign-In con token inválido o correo no verificado | Se rechaza la autenticación. |
| AUTH-09 | Google Sign-In con correo sin cuenta local | Se informa que debe registrarse primero con ese correo. |
| AUTH-10 | Solicitar recuperación con correo registrado | Se muestra confirmación y se envía un mensaje con enlace. |
| AUTH-11 | Solicitar recuperación con correo no registrado | Se muestra la misma confirmación, sin revelar existencia de la cuenta. |
| AUTH-12 | Abrir enlace vigente y establecer contraseña coincidente | Se cambia el hash y se muestra confirmación de éxito. |
| AUTH-13 | Reutilizar el enlace o abrirlo después de 30 minutos | Se rechaza el token. |
| AUTH-14 | Nueva contraseña y confirmación diferentes | Se impide el cambio. |
| AUTH-15 | Nombres o apellidos con dígitos | El servidor rechaza el registro aunque se omita la validación del navegador. |
| AUTH-16 | Nombres con tildes, espacios, apóstrofo o guion | Se aceptan si cumplen el límite de 100 caracteres. |
| AUTH-17 | Prefijo internacional de la lista y número local válido | Se almacena el número concatenado, por ejemplo `+573001234567`. |
| AUTH-18 | Prefijo no permitido o teléfono con letras | Se rechaza el registro. |
| AUTH-19 | Fecha de nacimiento futura o formato inválido | Se rechaza el registro. |
| AUTH-20 | Correo o usuario duplicado | Se rechaza el registro sin crear una segunda cuenta. |

## Evidencias que deben capturarse
1. Login con CAPTCHA sin marcar y luego marcado.
2. Mensaje de bloqueo después del quinto intento fallido.
3. Acceso rápido con Google en un dominio autorizado.
4. Formulario de recuperación, confirmación y correo recibido.
5. Formulario de nueva contraseña y pantalla de éxito.
6. Registro con nombre inválido y registro con nombre válido.
7. Prefijo y teléfono en el formulario, y valor almacenado en la base de datos.

No marcar estos casos como aprobados hasta ejecutarlos en el entorno de despliegue. Las pruebas de Google, reCAPTCHA y SMTP requieren claves y cuentas de prueba configuradas.
