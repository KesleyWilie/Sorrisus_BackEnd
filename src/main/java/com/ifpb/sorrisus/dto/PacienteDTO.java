package com.ifpb.sorrisus.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PacienteDTO extends UsuarioDTO {
    private String cpf;
    private String telefone;
    private String dataNascimento;
}
