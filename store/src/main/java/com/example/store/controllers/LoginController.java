package com.example.store.controllers;

import com.example.store.dto.JwtResponseDTO;
import com.example.store.dto.LoginDTO;
import com.example.store.entity.Provider;
import com.example.store.entity.User;
import com.example.store.service.IUserService;
import com.example.store.security.oauth2.CustomOAuth2User;
import com.example.store.security.oauth2.CustomOAuth2UserService;
import com.example.store.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final IUserService userService;
    private final JwtUtils jwtUtils;
    private final InMemoryClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2UserService oAuth2UserService;

    public LoginController(AuthenticationManager authManager, UserDetailsService userDetailsService, IUserService userService, JwtUtils jwtUtils, DefaultOAuth2UserService defaultOAuth2UserService, InMemoryClientRegistrationRepository clientRegistrationRepository, OAuth2UserService oAuth2UserService, CustomOAuth2UserService customOAuth2UserService) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.defaultOAuth2UserService = defaultOAuth2UserService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.oAuth2UserService = oAuth2UserService;
        this.customOAuth2UserService = customOAuth2UserService;
    }


    private final CustomOAuth2UserService customOAuth2UserService;
    private final DefaultOAuth2UserService defaultOAuth2UserService;

//    @PostMapping
//    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
//        authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginDTO.getUsername(), loginDTO.getPassword()));
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
//
//        //Generate JWT Token
//        final String token = JwtUtils.generateToken(userDetails.getUsername());
//        System.out.println("This is token: "+token);
//        return new ResponseEntity<>(token, HttpStatus.OK);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            final String token = JwtUtils.generateToken(userDetails.getUsername());
//            log.info("Controller token: {}", token);
//            log.info("Controller username: {}", userDetails.getUsername());
//            log.info("principal: {}", userDetails.getAuthorities().toString());
            return ResponseEntity.ok(new JwtResponseDTO(token, userDetails.getUsername(), userDetails.getAuthorities()));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/facebook")
    public Map<String, Object> loginWithFacebook(@RequestBody Map<String, String> loginDTO) {
        String accessToken = loginDTO.get("access_token");

        // Create OAuth2 User request
        OAuth2UserRequest userRequest = new OAuth2UserRequest(
                clientRegistrationRepository.findByRegistrationId("facebook"),
                new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, null, null)
        );
        // Load user
        CustomOAuth2User oAuth2User = (CustomOAuth2User) customOAuth2UserService.loadUser(userRequest);

        // Check user exists
        User user = userService.findUserByEmail(oAuth2User.getEmail());
        if (user == null) {
            user = userService.loginWithFB(oAuth2User, Provider.FACEBOOK);
        }
        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("email", user.getEmail());
        response.put("name", user.getUsername());
        return response;
    }

    @GetMapping("/userOAuth")
    public Map<String, Object> currentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        log.info("Controller OAuth2 login user: {}", oAuth2User);
        return oAuth2User.getAttributes();
    }

}
