# Migración del módulo de autenticación a Hibernate

## Qué cambió

- Se reemplazó la conexión JDBC manual (`ConexionBD`) por `HibernateUtil` y un `EntityManagerFactory`.
- `Usuario` ahora es una entidad JPA administrada por Hibernate.
- `UsuarioDAO` realiza consultas JPQL y operaciones de persistencia mediante `EntityManager`.
- `LoginServlet` obtiene el hash desde la entidad en una sola consulta y elimina el hash del objeto antes de guardarlo en la sesión.
- `PasswordUtil` conserva PBKDF2-HMAC-SHA256; Hibernate no reemplaza el algoritmo de hash de contraseñas.
- Se agregó un listener para cerrar el `EntityManagerFactory` al descargar la aplicación.
- `persistence.xml` registra la unidad `metasPersonalesPU`.

## Compatibilidad de APIs

La aplicación sigue usando `javax.servlet` porque se despliega en Tomcat 9. Hibernate 6 utiliza `jakarta.persistence`. Son APIs distintas y pueden coexistir; no se deben mezclar anotaciones `javax.persistence` con `jakarta.persistence`.

## Configuración de conexión

Configura estas variables de entorno en el proceso que ejecuta Tomcat (o como propiedades Java del proceso):

- `DB_URL` (opcional; valor predeterminado: `jdbc:mysql://localhost:3306/BD_Metas_personales?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true`)
- `DB_USER` (opcional; valor predeterminado: `root`)
- `DB_PASSWORD` (recomendado; valor predeterminado vacío para instalaciones locales sin contraseña)

No guardes contraseñas reales en Git. La contraseña de ejemplo de la versión anterior fue retirada del código fuente.

## Base de datos

El mapeo espera que exista la tabla `BD_Metas_personales.Usuario` con las columnas `user_id`, `nombres`, `apellidos`, `username`, `email`, `bday`, `phone_number` y `password_hash`, tal como define el script SQL del proyecto.

Hibernate está configurado con `hibernate.hbm2ddl.auto=validate`: verifica que el esquema existente sea compatible, pero no crea, modifica ni elimina tablas. Ejecuta primero el script SQL si la base de datos aún no existe.

## Compilar y probar

1. Configura MySQL y las variables de conexión.
2. Ejecuta **Clean and Build** en NetBeans (o `mvn clean package`).
3. Despliega `target/MetasPersonales.war` en Tomcat 9.
4. Prueba el registro de un usuario nuevo y luego inicia sesión con ese usuario.
5. Comprueba en MySQL que el registro se guardó y que `password_hash` contiene el formato PBKDF2 `iteraciones:salt:hash`, nunca la contraseña en texto plano.

La compilación del WAR no sustituye la prueba de ejecución contra una instancia real de MySQL.
