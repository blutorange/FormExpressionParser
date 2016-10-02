package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.object.NullLangObject;

public class NullObjectMethodException extends NoSuchMethodException {

	private static final long serialVersionUID = 1L;

	public NullObjectMethodException(final EMethod method, final IEvaluationContext ec) {
		super(method, NullLangObject.getInstance(), ec);
	}
}
