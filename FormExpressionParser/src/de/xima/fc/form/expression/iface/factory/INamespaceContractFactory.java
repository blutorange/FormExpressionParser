package de.xima.fc.form.expression.iface.factory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

@ParametersAreNonnullByDefault
public interface INamespaceContractFactory extends IContractFactory<INamespace> {
	/**
	 * Returns the type of object resulting from the given expression method
	 * with the given left hand side and right hand side. For example
	 * <code>2+3</code>, here the left hand side is <code>2</code>, the right
	 * hand side <code>3</code>, and the method is <code>+</code> (PLUS).
	 *
	 * @param lhs
	 *            Type of the left hand side.
	 * @param method
	 *            Expression method.
	 * @param rhs
	 *            Type of the right hand side.
	 * @return Type resulting from applying the expression method with the given
	 *         lhs and rhs; or <code>null</code> when there is no such
	 *         expression method or it does not allow the given types.
	 */
	@Nullable
	public IVariableType getExpressionMethodReturnType(IVariableType lhs, EMethod method, IVariableType rhs);

	/**
	 * Returns whether an object of type <code>rhs</code> can be assigned to the
	 * property <code>property</code> of an object of type <code>lhs</code>. For
	 * example consider <code>array[0] = "foo";</code>: here the
	 * <code>lhs</code> is an <code>array</code>, the <code>property</code> is a
	 * <code>number</code>, and the <code>rhs</code> is a <code>string</code>.
	 *
	 * @param thisContext
	 *            Type to which a value is assigned.
	 * @param property
	 *            Type of the property to be accessed.
	 * @param rhs
	 *            Value that is to be assigned.
	 * @return Whether the assignment is possible.
	 * @see #isDotAttributeAssignerDefined(IVariableType, IVariableType,
	 *      IVariableType, boolean)
	 */
	public boolean isBracketAssignerDefined(IVariableType thisContext, IVariableType property, IVariableType value);

	/**
	 * Provides information about the available bracket accessors and their
	 * return types. The property accessed could be any {@link ALangObject}, eg.
	 * <code>hash[true]</code> or <code>hash["key"]</code>. The return type must
	 * not depend on the value but only its type. A hash of type
	 * <code>hash&lt;number, string&gt;</code> can only accept values of type
	 * {@link NumberLangObject} and always returns objects of type
	 * {@link StringLangObject} in that case.
	 *
	 * @param thisContext
	 *            Type to which a value is assigned.
	 * @param property
	 *            Type of the property to be accessed.
	 * @return The return type, or <code>null</code> when no such bracket
	 *         accessor is defined.
	 */
	@Nullable
	public IVariableType getBracketAccessorReturnType(IVariableType thisContext, IVariableType property);

	/**
	 * Provides information about the available attribute assigners, such as
	 * <code>array.length = 5</code>. Note that attribute assigners cannot
	 * return a value, assignment expressions always use the value on the rhs,
	 * eg. <code> a = array.length = 5</code>, assigns <code>5</code> to the
	 * variable <code>a</code>, no matter what.
	 *
	 * @param thisContext
	 *            Type of the variable being accessed.
	 * @param property
	 *            The name of the attribute to which an assignment is to be
	 *            made.
	 * @param value
	 *            Type of the value to be assigned.
	 * @return Whether such an attribute assigner exists.
	 */
	public boolean isDotAssignerDefined(IVariableType thisContext, String property, IVariableType value);

	/**
	 * Provides information about the variable type returned by attribute
	 * accessors, such as <code>array.push</code> or <code>hash.get</code>.
	 *
	 * @param thisContext
	 *            Type of the variable being accessed.
	 * @param property
	 *            The name of the accessed attribute.
	 * @return The variable type of the value the accessor returns or
	 *         <code>null</code> when no such dot accessor is defined.
	 */
	@Nullable
	public IVariableType getDotAccessorReturnType(IVariableType thisContext, String property);
}