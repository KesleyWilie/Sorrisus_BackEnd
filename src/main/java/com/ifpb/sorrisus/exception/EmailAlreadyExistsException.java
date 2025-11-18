package com.ifpb.sorrisus.exception;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String email) {
        super("O e-mail '" + email + "' já está em uso.");
    }
}
