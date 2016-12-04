package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;

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
}
