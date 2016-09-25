package de.xima.fc.form.expression.context;

import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * A function that can be called with any number of arguments.
 * The thisContext of a function must be set when the function object is created.
 * @author madgaksha
 *
 */
public interface IFunction {
	@Nullable // Can be user-defined, need to account for when it returns null
	public ALangObject evaluate(IEvaluationContext ec, ALangObject... args) throws EvaluationException;
	public String[] getDeclaredArgumentList();
	/** @return May be null for native code. The node with the code of the function. */
	public Node getNode();
}
