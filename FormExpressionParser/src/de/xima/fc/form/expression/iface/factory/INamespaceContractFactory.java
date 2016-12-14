package de.xima.fc.form.expression.iface.factory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.parse.IVariableType;

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
	public IVariableType getReturnOfExpressionMethod(IVariableType lhs, EMethod method, IVariableType rhs);

	/**
	 * Returns whether an object of type <code>rhs</code> can be assigned to the
	 * property <code>property</code> of an object of type <code>lhs</code>. For
	 * example consider <code>array[0] = "foo";</code>: here the
	 * <code>lhs</code> is an <code>array</code>, the <code>property</code> is a
	 * <code>number</code>, and the <code>rhs</code> is a <code>string</code>.
	 *
	 * @param lhs
	 *            Object type to which a value is assigned.
	 * @param property
	 *            Property of the object.
	 * @param rhs
	 *            Value that is to be assigned.
	 *            Whether the property is accessed via dot notation
	 *            (<code>hash.key</code>) or via bracket notation
	 *            (<code>hash["key"]</code>).
	 * @return Whether the assignment is possible.
	 * @see #isDotAttributeAssignerDefined(IVariableType, IVariableType, IVariableType, boolean)
	 */
	public boolean isBracketAttributeAssignerDefined(IVariableType lhs, IVariableType property, IVariableType rhs);

	/**
	 * Same as {@link #isDotAttributeAssignerDefined(IVariableType, IVariableType, IVariableType, boolean)},
	 * but with the attribute accessed via dot notation. <code>hash.key</code>.
	 * @see #isBracketAttributeAssignerDefined(IVariableType, IVariableType, IVariableType, boolean)
	 */
	public boolean isDotAttributeAssignerDefined(IVariableType lhs, IVariableType property, IVariableType rhs);

}