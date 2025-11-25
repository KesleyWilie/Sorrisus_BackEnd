package com.ifpb.sorrisus.controller.auth;

import com.ifpb.sorrisus.dto.auth.LoginRequest;
import com.ifpb.sorrisus.dto.auth.LoginResponse;
import com.ifpb.sorrisus.security.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final long jwtExpirationMs;
    private final com.ifpb.sorrisus.service.UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                          JWTUtil jwtUtil,
                          com.ifpb.sorrisus.service.UsuarioService usuarioService,
                          @Value("${jwt.expiration-ms}") long jwtExpirationMs) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
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

        var usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        int userId = usuario.getId() == null ? 0 : Math.toIntExact(usuario.getId());

        return ResponseEntity.ok(new LoginResponse(token, "Bearer", jwtExpirationMs, userId, userDetails.getUsername(), role));
    }

}
