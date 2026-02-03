package com.ifpb.sorrisus.exception;

public class InvalidFieldException extends BusinessException {
    public InvalidFieldException(String field, String message) {
        super("Campo '" + field + "': " + message);
    }
}
