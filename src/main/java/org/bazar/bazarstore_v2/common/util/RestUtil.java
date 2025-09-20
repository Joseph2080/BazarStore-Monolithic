package org.bazar.bazarstore_v2.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class RestUtil {
    private static final String MESSAGE_KEY = "message";

    public static ResponseEntity<Map<String, Object>> handleException(Exception exception, HttpStatus status) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(MESSAGE_KEY, exception.getMessage());
        return generateResponseEntity(status, payload);
    }

    public static ResponseEntity<Map<String, Object>> buildResponse(Object data, HttpStatus status, String message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(MESSAGE_KEY, message);
        if (data != null) {
            payload.put("data", data);
        }
        return generateResponseEntity(status, payload);
    }

    private static ResponseEntity<Map<String, Object>> generateResponseEntity(HttpStatus status, Map<String, Object> payload){
        return ResponseEntity.status(status).body(payload);
    }

    public static void validateAuthHeader(String authHeader) {
        String errorMessage = "Invalid Authorization header.";
        if (authHeader == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        if (!authHeader.startsWith("Bearer ")) {
            //throw new UnauthorizedChatroomAccessException(errorMessage);
        }
    }
}
