package com.ifpb.sorrisus.exception;

public class InvalidCPFException extends BusinessException {
    public InvalidCPFException(String cpf) {
        super("O CPF '" + cpf + "' é inválido.");
    }
}
