package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IllegalThisContextException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public IllegalThisContextException(@Nonnull final ALangObject thisContext, @Nonnull final ELangObjectType expectedType,
			@Nonnull final IFunction<ALangObject> function, @Nonnull final IEvaluationContext ec) {
		super(ec,
				NullUtil.format(
						CmnCnst.Error.ILLEGAL_THIS_CONTEXT,
						thisContext.toString(), thisContext.getType(), expectedType,
						function.getDeclaredName().length() == 0 ? CmnCnst.TRACER_POSITION_NAME_ANONYMOUS_FUNCTION : function.getDeclaredName()));
		this.thisContext = thisContext;
		this.expectedType = expectedType;
		this.function = function;
	}

	public final ALangObject thisContext;
	public final ELangObjectType expectedType;
	public final IFunction<ALangObject> function;

}
