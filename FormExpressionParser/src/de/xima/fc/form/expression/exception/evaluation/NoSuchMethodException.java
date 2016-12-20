package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class NoSuchMethodException extends NoSuchFunctionException {
	private static final long serialVersionUID = 1L;

	public NoSuchMethodException(final EMethod method, final ALangObject thisContext, final IEvaluationContext ec) {
		super(CmnCnst.Name.METHOD,
				NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_METHOD, method.name(), method.methodName), thisContext,
				ec);
		this.method = method;
	}

	public final EMethod method;
}