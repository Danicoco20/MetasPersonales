package com.metaspersonales.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;

/** Valida firma, emisor, expiración y audiencia del ID token de Google. */
public final class GoogleTokenVerifier {
    private GoogleTokenVerifier() {}
    public static GoogleIdToken.Payload verificar(String credential) throws Exception {
        String clientId = System.getenv("GOOGLE_CLIENT_ID");
        if (clientId == null || clientId.isBlank()) return null;
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(), GsonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(clientId)).build();
        GoogleIdToken token = verifier.verify(credential);
        return token == null ? null : token.getPayload();
    }
}
