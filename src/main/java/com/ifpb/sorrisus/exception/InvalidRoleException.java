package com.ifpb.sorrisus.exception;

public class InvalidRoleException extends BusinessException {
    public InvalidRoleException(String role) {
        super("A role '" + role + "' não é válida para este tipo de usuário.");
    }
}
