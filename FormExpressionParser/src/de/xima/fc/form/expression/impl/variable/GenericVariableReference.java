package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.parse.IVariableReference;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

public class GenericVariableReference implements IVariableReference {
	@Nonnull
	private ALangObject object = NullLangObject.getInstance();
	
	@Override
	public ALangObject getCurrentObject() {
		return object;
	}
	@Override
	public void setCurrentObject(@Nonnull final ALangObject object) {
		this.object = object;
	}
}