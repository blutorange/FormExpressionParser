package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

public class NullObjectAssignException extends NoSuchAttrAssignerException {
	public NullObjectAssignException(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) {
		super(object.inspect(), NullLangObject.getInstance(), accessedViaDot, ec);
	}
}