package com.example.store.security.oauth2.handle;

import com.example.store.entity.Provider;
import com.example.store.entity.Role;
import com.example.store.entity.User;
import com.example.store.mapper.IUserMapper;
import com.example.store.repository.IRoleRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.security.oauth2.CustomOAuth2User;
import com.example.store.service.IUserService;
import com.example.store.util.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class Success extends SavedRequestAwareAuthenticationSuccessHandler {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final IUserMapper userMapper;

    public Success(IUserRepository userRepository, IRoleRepository roleRepository, JwtUtils jwtUtils, IUserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Provider provider = Provider.valueOf(oAuth2User.getOAuth2ClientName().toUpperCase());

        Optional<User> user = userRepository.findByEmail(oAuth2User.getEmail());
        if(user.isEmpty()){
            String token = processAddUser(oAuth2User, provider);
            response.sendRedirect(generateRedirectURL(true, token, provider, ""));
        } else {
            try {
                if(EnumUtils.isValidEnum(Provider.class, user.get().getProvider().name())
                && !user.get().getProvider().name().equals(Provider.LOCAL)) {
                    String token = jwtUtils.generateToken(user.get().getUsername());
                    response.sendRedirect(generateRedirectURL(true, token, provider, ""));
                } else {
                    String mess = user.get().getEmail() + " already have an account with " +user.get().getProvider();
                    response.sendRedirect(generateRedirectURL(false, "", user.get().getProvider(), mess));
                }
            } catch (NullPointerException e) {
                String mess = user.get().getEmail() + " already have an account with " +user.get().getProvider();
                response.sendRedirect(generateRedirectURL(false, "", user.get().getProvider(), mess));


            }
        }

    }

    private String generateRedirectURL(boolean success, String token, Provider provider, String messenger) {
        logger.debug(messenger);
        String CLIENT_HOST_REDIRECT = "http://localhost:3000/oauth2/redirect?token=" + token;
        return CLIENT_HOST_REDIRECT + token + "&success=" + success + "&provider=" + provider.toString();
    }

    private String processAddUser(CustomOAuth2User oAuth2User, Provider provider) {
        User newUser = new User();
        Optional<Role> role = roleRepository.findByName("USER");
        newUser.setEmail(oAuth2User.getEmail());
        newUser.setUsername(oAuth2User.getName());
        newUser.setRoles(Collections.singleton(role.get()));
        newUser.setUrlAvatar(oAuth2User.getProfilePicture());
        newUser.setProvider(provider);
        userRepository.save(newUser);

        String token = JwtUtils.generateToken(oAuth2User.getName());
        return token;
    }
}
