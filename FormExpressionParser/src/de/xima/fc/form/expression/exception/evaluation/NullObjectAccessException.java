package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

public class NullObjectAccessException extends NoSuchAttrAccessorException {
	private static final long serialVersionUID = 1L;

	public NullObjectAccessException(@Nonnull final ALangObject object,@Nonnull final IEvaluationContext ec) {
		super(object.inspect(), NullLangObject.getInstance(), ec);
	}
}
