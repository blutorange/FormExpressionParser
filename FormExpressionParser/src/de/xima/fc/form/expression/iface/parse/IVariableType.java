package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;

@Immutable
public interface IVariableType {
	public boolean equalsType(@Nonnull IVariableType other);
	/**
	 * @return The basic type, eg. <code>array</code> for <code>array&lt;string&gt;</code>.
	 */
	@Nonnull
	public ELangObjectType getBasicLangType();
	public int getGenericCount();
	@Nonnull
	public IVariableType getGeneric(int i) throws ArrayIndexOutOfBoundsException;
	/**
	 * @param typpe Type to check this type with.
	 * @return The merged type.
	 * @throws IllegalVariableTypeException When the types are not mutually compatible.
	 */
	@Nonnull
	public IVariableType union(@Nonnull IVariableType type) throws IllegalVariableTypeException;
}