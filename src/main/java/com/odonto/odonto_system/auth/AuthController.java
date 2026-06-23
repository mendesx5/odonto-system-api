package com.odonto.odonto_system.auth;

import com.odonto.odonto_system.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@RequestBody @Valid LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos"));

        if  (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }

        if (!user.isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedUser() {
        org.springframework.security.core.Authentication authentication =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        com.odonto.odonto_system.user.User loggedUser = (com.odonto.odonto_system.user.User) authentication.getPrincipal();

        return ResponseEntity.ok(java.util.Map.of(
                "id", loggedUser.getId(),
                "fullName", loggedUser.getFullName(),
                "email", loggedUser.getEmail(),
                "role", loggedUser.getRole()
        ));
    }
}
