/* Generated By:JavaCC: Do not edit this line. ParseException.java Version 5.0 */
/* JavaCCOptions:KEEP_LINE_COL=null */
package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * This exception is thrown when parse errors are encountered. You can
 * explicitly create objects of this exception type by calling the method
 * generateParseException in the generated parser.
 *
 * You can modify this class to customize your error reporting mechanisms so
 * long as you retain the public fields.
 */
@SuppressWarnings("all")
public class NoSuchMethodException extends NoSuchFunctionException {

	private static final long serialVersionUID = 1L;

	public NoSuchMethodException(final String name, final IEvaluationContext ec) {
		super("method", name, ec);
	}

	public NoSuchMethodException(final String name, final ALangObject thisContext, final IEvaluationContext ec) {
		super("method", name, thisContext, ec);
	}
}
