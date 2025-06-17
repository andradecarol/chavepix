package br.com.chavepix.domain.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RestException {

    public BadRequestException(String code, String message) {
        super(code, message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
