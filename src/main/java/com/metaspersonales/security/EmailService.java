package com.metaspersonales.security;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/** Envío SMTP. Requiere SMTP_HOST, SMTP_PORT, SMTP_USER, SMTP_PASSWORD y SMTP_FROM. */
public final class EmailService {
    private EmailService() {}

    public static void enviarRecuperacion(String destino, String nombre, String enlace) throws Exception {
        String host = requerido("SMTP_HOST");
        String port = valor("SMTP_PORT", "587");
        String user = requerido("SMTP_USER");
        String password = requerido("SMTP_PASSWORD");
        String from = valor("SMTP_FROM", user);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.connectiontimeout", "8000");
        props.put("mail.smtp.timeout", "8000");
        props.put("mail.smtp.writetimeout", "8000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from, "Metas Personales", StandardCharsets.UTF_8.name()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino, false));
        message.setSubject("Restauración de contraseña - Metas Personales", StandardCharsets.UTF_8.name());
        String saludo = (nombre == null || nombre.isBlank()) ? "Hola" : "Hola " + nombre;
        String html = "<div style='font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:28px;border:1px solid #ddd'>"
            + "<h2>Metas personales</h2><h3>" + escape(saludo) + "</h3>"
            + "<p>Recibimos una solicitud para restaurar tu contraseña. Si la realizaste, usa el siguiente botón:</p>"
            + "<p><a href='" + escape(enlace) + "' style='display:inline-block;padding:12px 20px;background:#e3e3e3;color:#222;text-decoration:none;border-radius:5px'>Restaurar contraseña</a></p>"
            + "<p>Este enlace vence en 30 minutos y solo puede utilizarse una vez. Si no solicitaste el cambio, ignora este mensaje.</p></div>";
        message.setContent(html, "text/html; charset=UTF-8");
        Transport.send(message);
    }

    private static String requerido(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) throw new IllegalStateException("Falta configurar " + key);
        return value;
    }
    private static String valor(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
    private static String escape(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
            .replace("\"","&quot;").replace("'","&#39;");
    }
}
