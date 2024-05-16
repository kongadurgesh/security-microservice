package com.tprm.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tprm.security.dto.UserDTO;
import com.tprm.security.exception.UnauthorizedException;
import com.tprm.security.service.AuthenticationService;
import com.tprm.security.utils.AuthenticationResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authenticationService.register(userDTO));
    }

    @GetMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestParam String userName,
            @RequestParam String password) throws UnauthorizedException {
        return ResponseEntity
                .ok(authenticationService.authenticate(UserDTO.builder().email(userName).password(password).build()));
    }
}
