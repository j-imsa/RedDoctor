package be.jimsa.reddoctor.config.exception;


import be.jimsa.reddoctor.ws.model.dto.ResponseDto;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
import java.util.Objects;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;


@RestControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(
                String.format(GENERAL_EXCEPTION_LOG_PATTERN, EXCEPTION_METHOD_METHOD_ARGUMENT_NOT_VALID, ex.getMessage())
        );
        Map<String, String> hashMap = new HashMap<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        errors.forEach(error -> {
                    if (error instanceof FieldError) {
                        hashMap.put(((FieldError) error).getField(),
                                error.getDefaultMessage()
                        );
                    } else if (error != null) {
                        String message = error.getDefaultMessage();
                        String start = Objects.requireNonNull(error.getArguments())[1].toString();
                        String end = error.getArguments()[2].toString();
                        hashMap.put(start + "-" + end, message);
                    }
                }
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
        log.error(
                String.format(GENERAL_EXCEPTION_LOG_PATTERN, EXCEPTION_METHOD_HTTP_MESSAGE_NOT_READABLE, ex.getMessage())
        );
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        log.error(
                String.format(GENERAL_EXCEPTION_LOG_PATTERN, EXCEPTION_METHOD_CONSTRAINT_VIOLATION_EXCEPTION, ex.getMessage())
        );
        Map<String, String> hashMap = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
                    String fullPath = violation.getPropertyPath().toString();
                    String fieldName = fullPath.substring(fullPath.lastIndexOf('.') + 1);
                    hashMap.put(fieldName, violation.getMessage());
                }
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

    @ExceptionHandler(value = {
            OptimisticLockException.class,
            ObjectOptimisticLockingFailureException.class
    })
    public ResponseEntity<ResponseDto> handleOptimisticLockException(OptimisticLockException ex, HttpServletRequest webRequest) {
        log.error(
                String.format(GENERAL_EXCEPTION_LOG_PATTERN, EXCEPTION_METHOD_OPTIMISTIC_LOCK_EXCEPTION, ex.getMessage())
        );
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(GENERAL_EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(GENERAL_EXCEPTION_PATH, String.format(GENERAL_EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(hashMap)
                                .build()
                );
    }

    @ExceptionHandler(value = AppServiceException.class)
    public ResponseEntity<ResponseDto> handleAppServiceExceptions(AppServiceException ex, HttpServletRequest webRequest) {
        log.error(
                String.format(GENERAL_EXCEPTION_LOG_PATTERN, EXCEPTION_METHOD_APP_SERVICE_EXCEPTION, ex.getMessage())
        );
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(GENERAL_EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(GENERAL_EXCEPTION_PATH, String.format(GENERAL_EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(hashMap)
                                .build()
                );
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseDto> handleExceptions(Exception ex, HttpServletRequest webRequest) {
        log.error(
                String.format(GENERAL_EXCEPTION_LOG_PATTERN, EXCEPTION_METHOD_APP_500_EXCEPTION, ex.getMessage())
        );
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(GENERAL_EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(GENERAL_EXCEPTION_PATH, String.format(GENERAL_EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
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
