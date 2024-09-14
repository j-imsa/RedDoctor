package be.jimsa.reddoctor.config.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
public class AppServiceException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;

    public AppServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
