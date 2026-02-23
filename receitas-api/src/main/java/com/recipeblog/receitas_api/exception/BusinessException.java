package com.recipeblog.receitas_api.exception;

public class BusinessException extends RuntimeException {
        public BusinessException(String message) {
        super(message);
    }
}
