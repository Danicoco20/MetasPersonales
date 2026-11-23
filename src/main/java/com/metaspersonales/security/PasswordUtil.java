/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.metaspersonales.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {
        private static final int ITERACIONES = 210000;
    private static final int LONGITUD_BITS = 256;
    private static final int LONGITUD_SALT = 16;

    private PasswordUtil() {}

    public static String hash(String password) {
        byte[] salt = new byte[LONGITUD_SALT];
        new SecureRandom().nextBytes(salt);

        byte[] hash = derivar(password.toCharArray(), salt);

        return ITERACIONES + ":"
            + Base64.getEncoder().encodeToString(salt) + ":"
            + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String password, String almacenado) {
        if (password == null || almacenado == null) return false;

        try {
            String[] partes = almacenado.split(":");

            int iteraciones = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);

            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(), salt, iteraciones, LONGITUD_BITS
            );

            byte[] recibido = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .getEncoded();

            spec.clearPassword();

            if (esperado.length != recibido.length) return false;

            int diferencia = 0;

            for (int i = 0; i < esperado.length; i++) {
                diferencia |= esperado[i] ^ recibido[i];
            }

            return diferencia == 0;

        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] derivar(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                password, salt, ITERACIONES, LONGITUD_BITS
            );

            byte[] resultado = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .getEncoded();

            spec.clearPassword();
            return resultado;

        } catch (Exception e) {
            throw new IllegalStateException(
                "No fue posible procesar la contraseña.", e
            );
        }
    }
}
