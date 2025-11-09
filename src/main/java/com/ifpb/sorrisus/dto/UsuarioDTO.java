package com.ifpb.sorrisus.dto;

import com.ifpb.sorrisus.model.Role;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private Role role;
}
