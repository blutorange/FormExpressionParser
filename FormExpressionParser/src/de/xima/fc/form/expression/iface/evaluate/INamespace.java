package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * <p>
 * A namespace that contains all available functions and attribute accessors for each type of
 * language object. For example, an array has got functions such as <code>length</code> or
 * <code>sort</code>, and the attribute accessor that return the i-th element <code>myArray[i]</code>.
 * </p><p>
 * Note that attribute accessors may be written in two ways: for a language object of type hash
 * <code>myHash.myValue</code> and <code>myHash["myValue"]</code> are equivalent. Whether an attribute
 * is accessed via the dotNotation or not is indicated by the boolean argument <code>accessedViaDot</code>
 * all attribute accessors and assigners get passed.
 * </p><p>
 * An attribute assigner is the opposite of an attribute accessor assigns a value to some attribute
 * of an object, eg <code>myArray[0] = 1</code>.
 * </p>
 * <b>Must be immutable.</b>
 * @author madgaksha
 *
 */
@Immutable
@ParametersAreNonnullByDefault
public interface INamespace {
	/**
	 * @param method Method to get.
	 * @param type Type for which to get the method.
	 * @return The expression method for the given type. Must also look it up in any supertypes.
	 * @throws EvaluationException
	 */
	@Nullable
	public <T extends ALangObject> IFunction<T> expressionMethod(EMethod method, T type) throws EvaluationException;

	/**
	 * @param method Accessor to get.
	 * @param type Type for which to get the accessor.
	 * @return The attribute accessor for the given type. Must also look it up in any supertypes.
	 * @throws EvaluationException
	 */
	@Nullable
	public <T extends ALangObject> IFunction<T> attrAccessor(ALangObject name, boolean accessedViaDot, T type)
			throws EvaluationException;

	/**
	 * @param method Assigner to get.
	 * @param type Type for which to get the assigner.
	 * @return The attribute assigner for the given type. Must also look it up in any supertypes.
	 * @throws EvaluationException
	 */
	@Nullable
	public <T extends ALangObject> IFunction<T> attrAssigner(ALangObject name, boolean accessedViaDot, T type)
			throws EvaluationException;

	/**
	 * @param method Method to get.
	 * @param type Type for which to get the method.
	 * @return The expression method for the given type. Must also look it up in any supertypes.
	 * @throws EvaluationException
	 */
	@Nullable
	public IFunction<?> expressionMethod(EMethod method, ILangObjectClass type) throws EvaluationException;

	/**
	 * @param method Accessor to get.
	 * @param type Type for which to get the accessor.
	 * @return The attribute accessor for the given type. Must also look it up in any supertypes.
	 * @throws EvaluationException
	 */
	@Nullable
	public IFunction<?> attrAccessor(ALangObject name, boolean accessedViaDot, ILangObjectClass type)
			throws EvaluationException;
	/**
	 * @param method Assigner to get.
	 * @param type Type for which to get the assigner.
	 * @return The attribute assigner for the given type. Must also look it up in any supertypes.
	 * @throws EvaluationException
	 */
	@Nullable
	public IFunction<?> attrAssigner(ALangObject name, boolean accessedViaDot, ILangObjectClass type)
			throws EvaluationException;
}