/* Generated By:JavaCC: Do not edit this line. ParseException.java Version 5.0 */
/* JavaCCOptions:KEEP_LINE_COL=null */
package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * This exception is thrown when an object is coerced to another type
 * and the object cannot be coerced to that type.
 *
 */
public class CoercionException extends CatchableEvaluationException {

	private static final long serialVersionUID = 1L;

	public CoercionException(@Nonnull final ALangObject from, @Nonnull final ELangObjectType to, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.stringFormat(CmnCnst.Error.COERCION, from.toString(), from.getType(), to));
		this.from = from;
		this.to = to;
	}

	public final ALangObject from;
	public final ELangObjectType to;
}
