package de.xima.fc.form.expression.exception;

public class IllegalVariableTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    public IllegalVariableTypeException() {
        super();
    }
    public IllegalVariableTypeException(final String message) {
        super(message);
    }
    public IllegalVariableTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public IllegalVariableTypeException(final Throwable cause) {
        super(cause);
    }
}