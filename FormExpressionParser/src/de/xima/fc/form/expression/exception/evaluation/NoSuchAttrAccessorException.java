package de.xima.fc.form.expression.exception.evaluation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

@NonNullByDefault
public class NoSuchAttrAccessorException extends NoSuchFunctionException {
	private static final long serialVersionUID = 1L;

	public NoSuchAttrAccessorException(final String name, final ALangObject thisContext, final boolean accessedViaDot,
			final IEvaluationContext ec) {
		super(accessedViaDot ? CmnCnst.Name.DOT_ATTRIBUTE_ACCESSOR : CmnCnst.Name.BRACKET_ATTRIBUTE_ACCESSOR, name,
				thisContext, ec);
	}
}