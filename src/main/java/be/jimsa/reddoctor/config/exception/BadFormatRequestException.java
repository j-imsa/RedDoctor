package be.jimsa.reddoctor.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadFormatRequestException extends RuntimeException{
    public BadFormatRequestException(String message) {
        super(message);
    }
}
