package com.ifpb.sorrisus.exception;

public class CPFAlreadyExistsException extends BusinessException {
    public CPFAlreadyExistsException(String cpf) {
        super("O CPF '" + cpf + "' já está cadastrado.");
    }
}
