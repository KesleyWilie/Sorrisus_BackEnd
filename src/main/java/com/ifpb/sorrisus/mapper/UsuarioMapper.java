package com.ifpb.sorrisus.mapper;

import com.ifpb.sorrisus.dto.UsuarioDTO;
import com.ifpb.sorrisus.model.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());
        return dto;
    }
}
