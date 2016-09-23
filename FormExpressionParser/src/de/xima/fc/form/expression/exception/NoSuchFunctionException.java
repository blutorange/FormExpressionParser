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
public class NoSuchFunctionException extends CatchableEvaluationException {

	private static final long serialVersionUID = 1L;

	public NoSuchFunctionException(final String type, final String name, final IEvaluationContext ec) {
		super(ec, String.format("No such %s named %s. Evaluation context is %s", type, name, System.lineSeparator(), ec));
		this.name = name;
		thisContext = null;
	}

	public NoSuchFunctionException(final String type, final String name, final ALangObject thisContext, final IEvaluationContext ec) {
		super(ec, String.format("No such %s named <%s> for %s %s %sNamespace is %s",
				type, name, thisContext.getType().name(), thisContext.toString(), System.lineSeparator(), ec.getNamespace()));
		this.name = name;
		this.thisContext = thisContext;
	}

	public final String name;
	public final ALangObject thisContext;
}
