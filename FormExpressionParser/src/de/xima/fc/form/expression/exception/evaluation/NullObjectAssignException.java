package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

public class NullObjectAssignException extends NoSuchAttrAssignerException {
	private static final long serialVersionUID = 1L;

	public NullObjectAssignException(@Nonnull final ALangObject object, final boolean accessedViaDot,
			@Nonnull final IEvaluationContext ec) {
		super(object.inspect(), NullLangObject.getInstance(), accessedViaDot, ec);
	}
}
