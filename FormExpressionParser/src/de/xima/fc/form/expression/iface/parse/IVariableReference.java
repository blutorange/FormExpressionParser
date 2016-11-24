package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.object.ALangObject;

public interface IVariableReference {
	/** @return The current object held by this variable reference. */
	@Nonnull
	public ALangObject getCurrentObject();
	/** @param object The object this variable reference should hold. */
	public void setCurrentObject(@Nonnull ALangObject object);
}
