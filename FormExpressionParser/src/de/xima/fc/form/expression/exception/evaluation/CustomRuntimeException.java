package de.xima.fc.form.expression.exception.evaluation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;

/**
 * This exception is thrown by a throw block .
 */
@NonNullByDefault
public class CustomRuntimeException extends CatchableEvaluationException {

	private static final long serialVersionUID = 1L;

	public CustomRuntimeException(final String message, final IEvaluationContext ec) {
		super(ec, message);
		this.message = message;
	}

	public final String message;
}