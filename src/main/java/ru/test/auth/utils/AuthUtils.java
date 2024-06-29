package ru.test.auth.utils;

import org.springframework.stereotype.Component;

import ru.test.auth.models.RolesResponse;

import java.util.Base64;

@Component
public class AuthUtils {

    public String encode(String email, String code) {
        return Base64.getEncoder().encodeToString((email + ":" + code).getBytes());
    }

    public boolean isRoleExist(RolesResponse roles, String role) {
        return roles.getRoles().stream()
                .filter(roleFromList -> roleFromList == role)
                .findFirst()
                .isPresent();
    }
}