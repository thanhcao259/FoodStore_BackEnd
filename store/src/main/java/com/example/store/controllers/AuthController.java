package com.example.store.controllers;

import com.example.store.filter.JwtAuthFilter;
import com.example.store.service.IAuthService;
import com.example.store.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        try{
            if(authentication == null){
                logger.error("Not found authentication");
            }
            String username = authentication.getName();
            return new ResponseEntity<>(authService.getRoleUser(username), HttpStatus.OK);

        } catch (AuthenticationException | ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
