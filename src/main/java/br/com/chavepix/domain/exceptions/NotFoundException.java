package br.com.chavepix.domain.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestException {

    public NotFoundException(String code, String message) {
        super(code, message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
