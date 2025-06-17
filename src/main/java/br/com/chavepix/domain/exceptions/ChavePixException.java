package br.com.chavepix.domain.exceptions;

public class ChavePixException extends RuntimeException{

    public ChavePixException(String message) {
        super(message);
    }

    public ChavePixException(String message, Throwable cause) {
        super(message, cause);
    }
}
