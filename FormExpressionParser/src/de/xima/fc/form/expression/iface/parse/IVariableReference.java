package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.object.ALangObject;

public interface IVariableReference {
	@Nonnull
	public ALangObject getCurrentObject();
	public void setCurrentObject(@Nonnull ALangObject object);
}
