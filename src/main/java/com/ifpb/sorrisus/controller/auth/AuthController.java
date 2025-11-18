package com.ifpb.sorrisus.controller.auth;

import com.ifpb.sorrisus.dto.auth.LoginRequest;
import com.ifpb.sorrisus.dto.auth.LoginResponse;
import com.ifpb.sorrisus.model.Usuario;
import com.ifpb.sorrisus.repository.UsuarioRepository;
import com.ifpb.sorrisus.security.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final long jwtExpirationMs;

    public AuthController(AuthenticationManager authenticationManager,
                          JWTUtil jwtUtil,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          @Value("${jwt.expiration-ms}") long jwtExpirationMs) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .findFirst().map(Object::toString).orElse("");
        String token = jwtUtil.generateToken(userDetails.getUsername(), role);

        return ResponseEntity.ok(new LoginResponse(token, "Bearer", jwtExpirationMs, userDetails.getUsername(), role));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body("Email já cadastrado.");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario saved = usuarioRepository.save(usuario);
        return ResponseEntity.ok(saved);
    }
}
