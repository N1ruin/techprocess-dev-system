package by.niruin.techprocessSystem.domain.validation;

import by.niruin.dto.RegistrationRequest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrationValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        var factory = new LocalValidatorFactoryBean();
        factory.afterPropertiesSet();
        validator = factory;
    }

    @ParameterizedTest
    @MethodSource("validLoginProvider")
    void validLoginShouldPass(String validLogin) {
        var violations = validator.validateValue(RegistrationRequest.class, "login", validLogin);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidLoginProvider")
    void invalidLoginShouldFail(String invalidLogin) {
        var violations = validator.validateValue(RegistrationRequest.class, "login", invalidLogin);

        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("validPasswordProvider")
    void validPasswordShouldPass(String validPassword) {
        var violations = validator.validateValue(RegistrationRequest.class, "password", validPassword);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordProvider")
    void invalidPasswordsShouldFail(String invalidPassword) {
        var violations = validator.validateValue(RegistrationRequest.class, "password", invalidPassword);

        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("validFirstNameProvider")
    void validFirstNameShouldPass(String validFirstName) {
        var violations = validator.validateValue(RegistrationRequest.class, "firstName", validFirstName);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidFirstNameProvider")
    void invalidFirstNameShouldFail(String invalidFirstName) {
        var violations = validator.validateValue(RegistrationRequest.class, "firstName", invalidFirstName);

        assertFalse(violations.isEmpty());

    }

    @ParameterizedTest
    @MethodSource("validLastNameProvider")
    void validLastNameShouldPass(String validLastName) {
        var violations = validator.validateValue(RegistrationRequest.class, "lastName", validLastName);

        assertTrue(violations.isEmpty());

    }

    @ParameterizedTest
    @MethodSource("invalidLastNameProvider")
    void invalidLastNameShouldFail(String invalidLastName) {
        var violations = validator.validateValue(RegistrationRequest.class, "lastName", invalidLastName);

        assertFalse(violations.isEmpty());

    }

    @ParameterizedTest
    @MethodSource("validSurnameProvider")
    void validSurnameShouldPass(String validSurname) {
        var violations = validator.validateValue(RegistrationRequest.class, "surname", validSurname);

        assertTrue(violations.isEmpty());

    }

    @ParameterizedTest
    @MethodSource("invalidSurnameProvider")
    void invalidSurnameShouldFail(String invalidSurname) {
        var violations = validator.validateValue(RegistrationRequest.class, "surname", invalidSurname);

        assertFalse(violations.isEmpty());

    }

    @ParameterizedTest
    @MethodSource("validDateProvider")
    void validBirthDateShouldPass(LocalDate validDate) {
        var violations = validator.validateValue(RegistrationRequest.class, "birthDate", validDate);

        assertTrue(violations.isEmpty());

    }

    @ParameterizedTest
    @MethodSource("invalidDateProvider")
    void InvalidBirthDateShouldFail(LocalDate invalidDate) {
        var violations = validator.validateValue(RegistrationRequest.class, "birthDate", invalidDate);

        assertFalse(violations.isEmpty());

    }

    static Stream<Arguments> validLoginProvider() {
        return Stream.of(
                Arguments.of("user123"),
                Arguments.of("Admin"),
                Arguments.of("veryLongLogin123")
        );
    }

    static Stream<Arguments> invalidLoginProvider() {
        return Stream.of(
                Arguments.of("abc"),
                Arguments.of("user name"),
                Arguments.of("user!@#"),
                Arguments.of("логинРус"),
                Arguments.of("tooLongLoginName123"));
    }

    static Stream<Arguments> invalidPasswordProvider() {
        return Stream.of(Arguments.of("12345"),
                Arguments.of("onlyletters"),
                Arguments.of("ONLYLETTERS123"),
                Arguments.of("abc1"),
                Arguments.of("valid123!"),
                Arguments.of("aaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    static Stream<Arguments> validPasswordProvider() {
        return Stream.of(Arguments.of("aaad123"),
                Arguments.of("123321Fff^"),
                Arguments.of("asdfqw54$#"),
                Arguments.of("12QWEREqw&"));
    }

    static Stream<Arguments> validFirstNameProvider() {
        return Stream.of(
                Arguments.of("Иван"),
                Arguments.of("Александр"),
                Arguments.of("Я"));
    }

    static Stream<Arguments> invalidFirstNameProvider() {
        return Stream.of(
                Arguments.of("Ivan"),
                Arguments.of("Иван123"),
                Arguments.of("Иван!"),
                Arguments.of(""));
    }

    static Stream<Arguments> validLastNameProvider() {
        return Stream.of(Arguments.of("Иванов"), Arguments.of("Петров"));
    }

    static Stream<Arguments> invalidLastNameProvider() {
        return Stream.of(Arguments.of("Ivanov"), Arguments.of("12345"));
    }

    static Stream<Arguments> validSurnameProvider() {
        return Stream.of(Arguments.of("Иванович"), Arguments.of("Петрович"));
    }

    static Stream<Arguments> invalidSurnameProvider() {
        return Stream.of(Arguments.of("Ivanovich"), Arguments.of(""));
    }

    static Stream<Arguments> validDateProvider() {
        return Stream.of(
                Arguments.of(LocalDate.of(1990, 1, 1)),
                Arguments.of(LocalDate.now().minusDays(1)));
    }

    static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(LocalDate.now().plusDays(1)));
    }
}
