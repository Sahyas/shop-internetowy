package com.example.shop.security;

import com.example.shop.model.User;
import com.example.shop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserService userService;
    
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Próba logowania: " + email);
        Optional<User> userOpt = userService.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            logger.error("Nie znaleziono użytkownika: " + email);
            throw new UsernameNotFoundException("Nie znaleziono użytkownika: " + email);
        }
        
        User user = userOpt.get();
        logger.info("Znaleziono użytkownika: " + email + ", rola: " + user.getRole());
        
        // Upewnij się, że rola zawiera prefix ROLE_
        String roleName = user.getRole();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        
        // Pobierz zaszyfrowane hasło z bazy danych Firebase
        String password = user.getPassword();
        if (password == null || password.isEmpty()) {
            logger.error("Brak hasła dla użytkownika: " + email);
            throw new UsernameNotFoundException("Hasło nie zostało ustawione dla użytkownika: " + email);
        }
        
        logger.info("Tworzę UserDetails dla: " + email + " z rolą: " + roleName);
        
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            password,
            Collections.singletonList(authority)
        );
    }
}
