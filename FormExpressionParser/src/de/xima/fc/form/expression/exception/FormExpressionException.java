package de.xima.fc.form.expression.exception;

public class FormExpressionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public FormExpressionException() {
		super();
	}
	public FormExpressionException(final String message) {
		super(message);
	}
	public FormExpressionException(final String message, final Throwable cause) {
		super(message, cause);
	}
	public FormExpressionException(final Throwable cause) {
		super(cause);
	}
}