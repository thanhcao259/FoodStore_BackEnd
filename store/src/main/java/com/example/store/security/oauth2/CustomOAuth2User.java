package com.example.store.security.oauth2;

import com.example.store.entity.Provider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter @Setter
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    @Getter
    private final String oauth2ClientName;

    public CustomOAuth2User(OAuth2User oauth2User, String clientName) {
        this.oauth2User = oauth2User;
        this.oauth2ClientName = clientName;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>(oauth2User.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Thêm vai trò OAUTH2_USER
        return authorities;
//        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail(){
        return oauth2User.getAttribute("email");
    }

    public String getProfilePicture(){
        if(Provider.valueOf(oauth2ClientName.toUpperCase(Locale.ROOT))==Provider.FACEBOOK){
            if(oauth2User.getAttributes().containsKey("picture")){
                Map<String, Object> picture = (Map<String, Object>) oauth2User.getAttributes().get("picture");
                if(picture.containsKey("data")){
                    Map<String, Object> dataObj = (Map<String, Object>) picture.get("data");
                    if(dataObj.containsKey("url")){
                        return (String) dataObj.get("url");
                    }
                }
            } return "";
        } else return oauth2User.getAttribute("picture");
    }

    public String getOAuth2ClientName(){
        return this.oauth2ClientName;
    }

}
