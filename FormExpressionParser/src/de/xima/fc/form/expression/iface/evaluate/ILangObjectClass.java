package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface ILangObjectClass extends Serializable {

	public Integer getClassId();

	@Nullable
	public ILangObjectClass getSuperType();

	/** @return The class for objects of this type. */
	public Class<? extends ALangObject> getLangObjectClass();

	/**
	 * @return The name of this type used for variable declarations, eg.
	 *         <code>error myErrorVar;</code>.
	 */
	public String getSyntacticalTypeName();

	/** @return Whether objects of this type support iteration. */
	public boolean isIterable();

	/**
	 * A compound (generic) type requires additional type parameters, eg.
	 * <code>array&lt;string&gt;</code>.
	 *
	 * @return Whether this type is compound.
	 */
	public boolean allowsGenericsCount(int i);

	/**
	 * @param generics
	 *            Types for the generics.
	 * @return The type of the iteration item for the given generics.
	 * @throws UnsupportedOperationException
	 *             Iff {@link #isIterable()} returns <code>false</code>.
	 * @throws ArrayIndexOutOfBoundsException
	 *             Iff <code>allowsGenericsCount(generics.length)</code> return
	 *             <code>false</code>.
	 */
	public IVariableType getIterableItemType(IVariableType[] generics)
			throws IllegalVariableTypeException, ArrayIndexOutOfBoundsException;

	/**
	 * @return A simple type for no generics.
	 * @throws IllegalVariableTypeException When this type requires generic, ie. when {@link #allowsGenericsCount(int)} returns <code>false</code> for <code>0</code>.
	 */
	public IVariableType getSimpleVariableType() throws IllegalVariableTypeException;

	/**
	 * May return false when the object is actually immutable, but not vice-versa.
	 * @return Whether objects of this type are mutable or immutable.
	 */
	public boolean isImmutable();
}