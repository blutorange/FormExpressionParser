package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.object.NullLangObject;

public class NullObjectMethodException extends NoSuchMethodException {

	private static final long serialVersionUID = 1L;

	public NullObjectMethodException(@Nonnull final EMethod method, @Nonnull final IEvaluationContext ec) {
		super(method, NullLangObject.getInstance(), ec);
	}
}
