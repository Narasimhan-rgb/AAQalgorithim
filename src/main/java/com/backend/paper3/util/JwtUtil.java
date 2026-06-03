package com.backend.paper3.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.backend.paper3.exception.ApiException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtUtil {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.seconds}")
    private Long expirationSeconds;

    public String generateToken(
            String email
    ) {

        try {

            long issuedAt =
                    Instant.now().getEpochSecond();

            long expiresAt =
                    issuedAt + expirationSeconds;

            Map<String, Object> header =
                    new HashMap<>();

            header.put("alg", "HS256");
            header.put("typ", "JWT");

            Map<String, Object> payload =
                    new HashMap<>();

            payload.put("sub", email);
            payload.put("iat", issuedAt);
            payload.put("exp", expiresAt);

            String encodedHeader =
                    base64UrlEncode(
                            objectMapper.writeValueAsString(header)
                    );

            String encodedPayload =
                    base64UrlEncode(
                            objectMapper.writeValueAsString(payload)
                    );

            String unsignedToken =
                    encodedHeader + "." + encodedPayload;

            String signature =
                    sign(unsignedToken);

            return unsignedToken + "." + signature;

        } catch (Exception e) {

            throw new ApiException(
                    "Token generation failed : " + e.getMessage()
            );
        }
    }

    public String extractEmail(
            String token
    ) {

        try {

            validateToken(token);

            String[] parts =
                    token.split("\\.");

            String payloadJson =
                    new String(
                            Base64
                                    .getUrlDecoder()
                                    .decode(parts[1]),
                            StandardCharsets.UTF_8
                    );

            Map<String, Object> payload =
                    objectMapper.readValue(
                            payloadJson,
                            new TypeReference<Map<String, Object>>() {
                            }
                    );

            Object subject =
                    payload.get("sub");

            if (subject == null) {
                throw new ApiException("Invalid token subject");
            }

            return subject.toString();

        } catch (ApiException e) {

            throw e;

        } catch (Exception e) {

            throw new ApiException(
                    "Invalid token : " + e.getMessage()
            );
        }
    }

    public void validateToken(
            String token
    ) {

        try {

            if (token == null
                    || token.trim().isEmpty()) {

                throw new ApiException("Token is missing");
            }

            String[] parts =
                    token.split("\\.");

            if (parts.length != 3) {
                throw new ApiException("Invalid token format");
            }

            String unsignedToken =
                    parts[0] + "." + parts[1];

            String expectedSignature =
                    sign(unsignedToken);

            if (!expectedSignature.equals(parts[2])) {
                throw new ApiException("Invalid token signature");
            }

            String payloadJson =
                    new String(
                            Base64
                                    .getUrlDecoder()
                                    .decode(parts[1]),
                            StandardCharsets.UTF_8
                    );

            Map<String, Object> payload =
                    objectMapper.readValue(
                            payloadJson,
                            new TypeReference<Map<String, Object>>() {
                            }
                    );

            Object expValue =
                    payload.get("exp");

            if (expValue == null) {
                throw new ApiException("Token expiry missing");
            }

            long exp =
                    Long.parseLong(
                            expValue.toString()
                    );

            long now =
                    Instant.now().getEpochSecond();

            if (now > exp) {
                throw new ApiException("Token expired");
            }

        } catch (ApiException e) {

            throw e;

        } catch (Exception e) {

            throw new ApiException(
                    "Token validation failed : " + e.getMessage()
            );
        }
    }

    public String extractTokenFromHeader(
            String authorizationHeader
    ) {

        if (authorizationHeader == null
                || authorizationHeader.trim().isEmpty()) {

            throw new ApiException(
                    "Authorization header is required"
            );
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ApiException(
                    "Authorization header must start with Bearer"
            );
        }

        return authorizationHeader
                .substring(7)
                .trim();
    }

    private String base64UrlEncode(
            String value
    ) {

        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        value.getBytes(StandardCharsets.UTF_8)
                );
    }

    private String sign(
            String data
    ) throws Exception {

        Mac mac =
                Mac.getInstance(HMAC_ALGORITHM);

        SecretKeySpec secretKeySpec =
                new SecretKeySpec(
                        jwtSecret.getBytes(StandardCharsets.UTF_8),
                        HMAC_ALGORITHM
                );

        mac.init(secretKeySpec);

        byte[] signatureBytes =
                mac.doFinal(
                        data.getBytes(StandardCharsets.UTF_8)
                );

        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(signatureBytes);
    }
}