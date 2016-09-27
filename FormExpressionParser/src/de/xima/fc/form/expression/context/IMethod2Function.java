package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.impl.function.EExpressionMethodString;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * Helper interface allowing you to write an enum with
 * expression methods (or possibly attribute accessors).
 * See {@link EExpressionMethodString} for a sample implementation.
 * @author madgaksha
 *
 * @param <T> This-context type of the function.
 */
public interface IMethod2Function<T extends ALangObject> {
	public EMethod getMethod();
	public IFunction<T> getFunction();
}