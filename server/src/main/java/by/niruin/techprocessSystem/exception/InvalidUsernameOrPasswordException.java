package by.niruin.techprocessSystem.exception;

public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException() {
        super("Неверный логин или пароль!");
    }
}
