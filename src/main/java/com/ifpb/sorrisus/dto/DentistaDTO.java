package com.ifpb.sorrisus.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DentistaDTO extends UsuarioDTO {
    private String cro;
    private String especialidade;
}
