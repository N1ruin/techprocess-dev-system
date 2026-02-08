package by.niruin.techprocessSystem.domain.exceptionHandler;

import by.niruin.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleExpiredJwt(ExpiredJwtException e) {
        var response = new ErrorResponse();
        response.setError("TOKEN_EXPIRED");
        response.setMessage("Срок действия токена истек. Пожалуйста, войдите снова.");
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
}
