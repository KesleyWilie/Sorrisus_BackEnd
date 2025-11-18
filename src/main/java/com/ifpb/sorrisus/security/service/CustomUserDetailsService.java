package com.ifpb.sorrisus.security.service;

import com.ifpb.sorrisus.model.Usuario;
import com.ifpb.sorrisus.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + usernameOrEmail));

        GrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRole().name());
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                Collections.singletonList(authority)
        );
    }
}
