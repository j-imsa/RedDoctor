package be.jimsa.reddoctor.config.exception.handler;


import be.jimsa.reddoctor.config.exception.*;
import be.jimsa.reddoctor.utility.constant.ProjectConstants;
import be.jimsa.reddoctor.ws.model.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.EXCEPTION_LOG_PATTERN;


@RestControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(String.format(EXCEPTION_LOG_PATTERN, "handleMethodArgumentNotValid", ex.getMessage()));
        Map<String, String> hashMap = new HashMap<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        errors.forEach(error ->
                hashMap.put(((FieldError) error).getField(),
                        error.getDefaultMessage()
                )
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(hashMap)
                                .build()
                );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(String.format(EXCEPTION_LOG_PATTERN, "handleHttpMessageNotReadable", ex.getMessage()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(ex.getMostSpecificCause().getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {
            BadFormatRequestException.class,
            ResourceAlreadyExistException.class
    })
    public ResponseEntity<ResponseDto> handleApp4xxExceptions(RuntimeException ex, HttpServletRequest webRequest) {
        log.error(String.format(EXCEPTION_LOG_PATTERN, "handleApp4xxExceptions", ex.getMessage()));
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(ProjectConstants.EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(ProjectConstants.EXCEPTION_PATH, String.format(ProjectConstants.EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(hashMap)
                                .build()
                );
    }

    @ExceptionHandler(value = {
            NotFoundResourceException.class
    })
    public ResponseEntity<ResponseDto> handleApp404Exceptions(NotFoundResourceException ex, HttpServletRequest webRequest) {
        log.error(String.format(EXCEPTION_LOG_PATTERN, "handleApp404Exceptions", ex.getMessage()));
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(ProjectConstants.EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(ProjectConstants.EXCEPTION_PATH,
                String.format(ProjectConstants.EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(hashMap)
                                .build()
                );
    }

    @ExceptionHandler(value = {
            ReservedResourceException.class
    })
    public ResponseEntity<ResponseDto> handleApp406Exceptions(ReservedResourceException ex, HttpServletRequest webRequest) {
        log.error(String.format(EXCEPTION_LOG_PATTERN, "handleApp406Exceptions", ex.getMessage()));
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(ProjectConstants.EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(ProjectConstants.EXCEPTION_PATH,
                String.format(ProjectConstants.EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(hashMap)
                                .build()
                );
    }

    @ExceptionHandler(value = {
            Exception.class,
            InternalServiceException.class
    })
    public ResponseEntity<ResponseDto> handleApp5xxExceptions(Exception ex, HttpServletRequest webRequest) {
        log.error(String.format(EXCEPTION_LOG_PATTERN, "handleApp5xxExceptions", ex.getMessage()));
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(ProjectConstants.EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(ProjectConstants.EXCEPTION_PATH, String.format(ProjectConstants.EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(hashMap)
                                .build()
                );
    }
}
