package br.com.chavepix.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends RestException {

    public UnprocessableEntityException(String code, String message) {
        super(code, message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

}
