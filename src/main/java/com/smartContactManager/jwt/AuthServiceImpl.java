package com.smartContactManager.jwt;

//AuthServiceImpl.java
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

 private final AuthenticationManager authenticationManager;
 private final UserDetailsService userDetailsService;
 private final BCryptPasswordEncoder passwordEncoder;

 private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

 @Value("${jwt.expiration}")
 private long jwtExpiration;

 public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
     this.authenticationManager = authenticationManager;
     this.userDetailsService = userDetailsService;
     this.passwordEncoder = passwordEncoder;
 }

 @Override
 public String authenticate(String username, String password) {
     Authentication authentication = authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(username, password)
     );

     UserDetails userDetails = (UserDetails) authentication.getPrincipal();

     return generateToken(userDetails);
 }

 private String generateToken(UserDetails userDetails) {
     Date now = new Date();
     Date expirationDate = new Date(now.getTime() + jwtExpiration);

     return Jwts.builder()
             .setSubject(userDetails.getUsername())
             .setIssuedAt(now)
             .setExpiration(expirationDate)
             .signWith(SignatureAlgorithm.HS512, secretKey)
             .compact();
 }
}

