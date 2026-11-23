# Configuración y despliegue del módulo de autenticación

## Requisitos
- Java 21 o superior.
- Tomcat 9 (Servlet 4.0 / `javax.servlet`).
- MySQL 8 o superior.
- Maven 3.9 o superior.
- Acceso HTTPS público para el despliegue real.

## 1. Migración de base de datos
Respaldar `BD_Metas_personales` y ejecutar una sola vez:
`database/migration_auth_v2.sql`.
La aplicación usa `hibernate.hbm2ddl.auto=validate`: Hibernate no crea ni altera tablas automáticamente.

## 2. Variables de entorno
Configurar las variables de `.env.example` en el entorno donde se ejecuta Tomcat. No copiar secretos al repositorio ni incluirlos en el WAR.
- `DB_URL`, `DB_USER`, `DB_PASSWORD`: conexión MySQL.
- `RECAPTCHA_SITE_KEY` y `RECAPTCHA_SECRET_KEY`: claves de reCAPTCHA v2 Checkbox. Registrar los dominios correctos en Google reCAPTCHA. Sin ambas claves, el acceso con contraseña permanece deshabilitado.
- `GOOGLE_CLIENT_ID`: OAuth 2.0 Client ID de tipo aplicación web, con los orígenes JavaScript autorizados correspondientes (localhost para desarrollo y dominio HTTPS para producción).
- `APP_BASE_URL`: URL pública de la aplicación (recomendado en producción, por ejemplo `https://tudominio.com`); evita construir enlaces de recuperación a partir del encabezado Host de la petición.
- `SMTP_HOST`, `SMTP_PORT`, `SMTP_USER`, `SMTP_PASSWORD`, `SMTP_FROM`: servidor SMTP con STARTTLS. Para Gmail normalmente se requiere contraseña de aplicación o un proveedor SMTP autorizado; no usar la contraseña ordinaria de la cuenta.

## 3. Flujos implementados
- CAPTCHA: el navegador habilita el botón al completar el widget; el servidor verifica el token directamente con Google.
- Bloqueo: cinco fallos de contraseña para un correo registrado generan un bloqueo de 15 minutos. El contador se reinicia tras un inicio correcto o al vencer el bloqueo.
- Google: se valida la firma, audiencia y vigencia del ID token en el servidor. El inicio rápido se permite para cuentas Metas Personales ya registradas con el mismo correo verificado. No crea automáticamente cuentas nuevas porque el formulario exige fecha de nacimiento y teléfono que Google no proporciona necesariamente.
- Recuperación: token aleatorio, almacenado como hash SHA-256, válido 30 minutos y de un solo uso. La respuesta HTTP no revela si el correo existe. El email contiene un enlace de restauración.
- Registro: nombres y apellidos aceptan letras Unicode, espacios, apóstrofos y guiones; teléfono se guarda como prefijo internacional + número local.

## 4. Compilación y despliegue
Desde la carpeta raíz:
```bash
mvn clean package
```
Desplegar `target/MetasPersonales.war` en Tomcat 9. Configurar las variables de entorno en el servicio de Tomcat antes de iniciarlo.

## 5. Limitaciones y verificaciones pendientes
- El bloqueo es por cuenta; no implementa limitación distribuida por IP ni protección contra ataques coordinados de denegación de servicio.
- El CAPTCHA depende de conectividad con Google y de claves válidas para el dominio.
- El envío SMTP, Google Sign-In y CAPTCHA requieren credenciales externas y no pueden considerarse operativos hasta probarlos con sus claves reales.
- Configurar `APP_BASE_URL` en producción con HTTPS y el dominio canónico. Si se omite, el enlace se construye con la URL de la petición y depende de la configuración del proxy inverso.
- No se implementa MFA ni verificación de correo durante el registro.
- La integración con Google no enlaza cuentas automáticamente por nombre; exige correo verificado coincidente.
- Ejecutar pruebas de integración y seguridad antes de usar datos reales.
