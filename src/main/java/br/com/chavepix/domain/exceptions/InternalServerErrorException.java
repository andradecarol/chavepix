package br.com.chavepix.domain.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InternalServerErrorException extends RestException {

    public InternalServerErrorException(String code, String message) {
        super(code, message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
