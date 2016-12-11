package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * This exception is thrown by a throw block .
 */
@ParametersAreNonnullByDefault
public class CustomRuntimeException extends CatchableEvaluationException {

	private static final long serialVersionUID = 1L;

	public CustomRuntimeException(final String message, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.CUSTOM_RUNTIME_EXCEPTION, message));
		this.message = message;
	}

	public final String message;
}