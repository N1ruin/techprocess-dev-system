package by.niruin.techprocessSystem.domain.exceptionHandler;

import by.niruin.dto.error.ErrorResponse;
import by.niruin.techprocessSystem.exception.InvalidUsernameOrPasswordException;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException e) {
        return buildErrorResponse("Срок действия токена истек. Пожалуйста, войдите снова.");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {

        return buildErrorResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleInvalidUserCredentials(InvalidUsernameOrPasswordException e) {
        return buildErrorResponse(e.getMessage());
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException e) {
        return buildErrorResponse("Неверный логин или пароль");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String exceptionMessage) {
        var response = new ErrorResponse();
        response.setMessage(exceptionMessage);
        return ResponseEntity.badRequest().body(response);
    }
}
