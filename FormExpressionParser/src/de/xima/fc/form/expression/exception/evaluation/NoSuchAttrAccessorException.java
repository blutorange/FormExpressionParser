/* Generated By:JavaCC: Do not edit this line. ParseException.java Version 5.0 */
/* JavaCCOptions:KEEP_LINE_COL=null */
package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public class NoSuchAttrAccessorException extends NoSuchFunctionException {
	private static final long serialVersionUID = 1L;

	public NoSuchAttrAccessorException(@Nonnull final String name, @Nonnull final IEvaluationContext ec) {
		super(CmnCnst.Name.ATTRIBUTE_ACCESSOR, name, ec);
	}

	public NoSuchAttrAccessorException(@Nonnull final String name, final boolean accessedViaDot,
			@Nonnull final IEvaluationContext ec) {
		super(accessedViaDot ? CmnCnst.Name.DOT_ATTRIBUTE_ACCESSOR : CmnCnst.Name.BRACKET_ATTRIBUTE_ACCESSOR, name, ec);
	}

	public NoSuchAttrAccessorException(@Nonnull final String name, @Nonnull final ALangObject thisContext,
			@Nonnull final IEvaluationContext ec) {
		super(CmnCnst.Name.ATTRIBUTE_ACCESSOR, name, thisContext, ec);
	}

	public NoSuchAttrAccessorException(@Nonnull final String name, @Nonnull final ALangObject thisContext,
			final boolean accessedViaDot, @Nonnull final IEvaluationContext ec) {
		super(accessedViaDot ? CmnCnst.Name.DOT_ATTRIBUTE_ACCESSOR : CmnCnst.Name.BRACKET_ATTRIBUTE_ACCESSOR, name,
				thisContext, ec);
	}

}