package com.smartContactManager.jwt;

//AuthController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

 private final AuthService authService;

 @Autowired
 public AuthController(AuthService authService) {
     this.authService = authService;
 }

 @PostMapping("/api/login")
 public String login(@RequestBody LoginRequest loginRequest) {
     String username = loginRequest.getUserName();
     String password = loginRequest.getPassword();
     return authService.authenticate(username, password);
 }
 
}

