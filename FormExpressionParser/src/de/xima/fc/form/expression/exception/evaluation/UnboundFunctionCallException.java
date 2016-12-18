package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class UnboundFunctionCallException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public UnboundFunctionCallException(final IFunction<ALangObject> value, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.UNBOUND_FUNCTION_CALL, value.getDeclaredName()));
		this.name = value.getDeclaredName();
	}

	public String name;
}