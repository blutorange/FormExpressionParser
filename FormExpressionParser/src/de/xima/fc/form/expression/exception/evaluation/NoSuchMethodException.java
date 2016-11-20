package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class NoSuchMethodException extends NoSuchFunctionException {

	private static final long serialVersionUID = 1L;

	public NoSuchMethodException(@Nonnull final EMethod method, @Nonnull final ALangObject thisContext,
			@Nonnull final IEvaluationContext ec) {
		super(CmnCnst.Name.METHOD, NullUtil.format(CmnCnst.Error.NO_SUCH_METHOD, method.name(), method.methodName),
				thisContext, ec);
		this.method = method;
	}

	public final EMethod method;
}
