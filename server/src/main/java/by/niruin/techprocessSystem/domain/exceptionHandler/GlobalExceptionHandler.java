package by.niruin.techprocessSystem.domain.exceptionHandler;

import by.niruin.dto.ErrorResponse;
import by.niruin.techprocessSystem.exception.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleExpiredJwt(TokenExpiredException e) {
        var response = new ErrorResponse();
        response.setError("Token expired error");
        response.setMessage(e.getMessage());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        var response = new ErrorResponse();
        response.setError("Validation exception");
        response.setMessage(e.getMessage());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception e) {
        var response = new ErrorResponse();
        response.setError("Error");
        response.setMessage(e.getMessage());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }
}
