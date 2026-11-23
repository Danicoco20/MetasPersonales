package com.metaspersonales.security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/** Verificación de reCAPTCHA v2 en servidor. Nunca confiar solo en el navegador. */
public final class CaptchaUtil {
    private CaptchaUtil() {}

    public static boolean verificar(String responseToken, String remoteIp) {
        String secret = System.getenv("RECAPTCHA_SECRET_KEY");
        if (secret == null || secret.isBlank() || responseToken == null || responseToken.isBlank())
            return false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL("https://www.google.com/recaptcha/api/siteverify").openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            String body = "secret=" + java.net.URLEncoder.encode(secret, StandardCharsets.UTF_8)
                + "&response=" + java.net.URLEncoder.encode(responseToken, StandardCharsets.UTF_8);
            if (remoteIp != null && !remoteIp.isBlank())
                body += "&remoteip=" + java.net.URLEncoder.encode(remoteIp, StandardCharsets.UTF_8);
            try (OutputStream out = con.getOutputStream()) {
                out.write(body.getBytes(StandardCharsets.UTF_8));
            }
            try (InputStream in = con.getInputStream()) {
                JsonObject json = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
                return json.has("success") && json.get("success").getAsBoolean();
            }
        } catch (Exception e) {
            return false;
        } finally { if (con != null) con.disconnect(); }
    }
}
