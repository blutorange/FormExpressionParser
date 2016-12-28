package de.xima.fc.form.expression.iface.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.iface.factory.IContractFactory;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;

/**
 * A variable provider for providing values to the program, usually
 * by some system or external library. For example:
 * <pre>
 *   require scope math;
 *   math::pi;
 * </pre>
 * This makes the variable <code>pi</code> availabe to the program.
 * Some {@link ALangObject} such as {@link ArrayLangObject} are mutable.
 * Therefore we need to create a new copy each time a program is executed.
 * This interface provides a factory method for this.
 * @author madgaksha
 *
 * @param <T>
 */
@Immutable
@NonNullByDefault
public interface IVariableProvider<T extends ALangObject> extends IContractFactory<T> {
	public IVariableType getType();
}