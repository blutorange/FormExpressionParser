package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

@ParametersAreNonnullByDefault
public class NoSuchAttrAccessorException extends NoSuchFunctionException {
	private static final long serialVersionUID = 1L;

	public NoSuchAttrAccessorException(final String name, final ALangObject thisContext, final boolean accessedViaDot,
			final IEvaluationContext ec) {
		super(accessedViaDot ? CmnCnst.Name.DOT_ATTRIBUTE_ACCESSOR : CmnCnst.Name.BRACKET_ATTRIBUTE_ACCESSOR, name,
				thisContext, ec);
	}
}