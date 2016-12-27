package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IClosure {
	@Nullable
	public IClosure getParent();
	public IClosure copy();
	public ALangObject getObject(int source) throws ArrayIndexOutOfBoundsException;
	public void setObject(int source, ALangObject object) throws ArrayIndexOutOfBoundsException;
}