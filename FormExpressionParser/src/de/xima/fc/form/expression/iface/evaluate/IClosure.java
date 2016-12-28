package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.object.ALangObject;

@NonNullByDefault
public interface IClosure {
	@Nullable
	public IClosure getParent();
	public IClosure copy();
	public ALangObject getObject(int source) throws ArrayIndexOutOfBoundsException;
	public void setObject(int source, ALangObject object) throws ArrayIndexOutOfBoundsException;
}