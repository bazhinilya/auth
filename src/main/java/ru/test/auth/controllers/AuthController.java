package ru.test.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.test.auth.models.User;
import ru.test.auth.services.AuthSerice;

@RestController
public class AuthController {

    @Autowired
    private AuthSerice authSerice;

    @GetMapping
    public ResponseEntity<?> getToken(@RequestBody User user) throws Exception {
        return ResponseEntity.ok(authSerice.authenticate(user));
    }
}