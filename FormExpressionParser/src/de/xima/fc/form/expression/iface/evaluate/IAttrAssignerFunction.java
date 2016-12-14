package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import de.xima.fc.form.expression.object.ALangObject;

public interface IAttrAssignerFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	/**
	 * Returns whether an object of type <code>rhs</code> can be assigned to the property <code>property</code>
	 * of an object of type <code>lhs</code>. For example consider <code>array[0] = "foo";</code>: here the
	 * <code>lhs</code> is an <code>array</code>, the <code>property</code> is a <code>number</code>, and the
	 * <code>rhs</code> is a <code>string</code>.
	 * @param lhs Object type to which a value is assigned.
	 * @param property Property of the object.
	 * @param rhs Value that is to be assigned.
	 * @return Whether the assignment is possible.
	 */
	//public boolean isBracketAssignable(IVariableType lhs, IVariableType property, IVariableType rhs);

	/**
	 * @see #isBracketAssignable(IVariableType, IVariableType, IVariableType)
	 */
	//public boolean isDotAssignable(IVariableType lhs, String name, IVariableType rhs);
}