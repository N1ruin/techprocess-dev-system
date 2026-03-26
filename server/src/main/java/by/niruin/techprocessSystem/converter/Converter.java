package by.niruin.techprocessSystem.converter;

public interface Converter<S, R> {
    R convert(S source);
}
