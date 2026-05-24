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
        var error = "Token expired error";
        var message = e.getMessage();
        var time = LocalDateTime.now();
        return new ErrorResponse(error, message, time);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        var error = "Validation exception";
        var message = e.getMessage();
        var time = LocalDateTime.now();
        return new ErrorResponse(error, message, time);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        var error = "Error";
        var message = "Internal server error";
        var time = LocalDateTime.now();
        return new ErrorResponse(error, message, time);
    }
}
