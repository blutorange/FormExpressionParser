package de.xima.fc.form.expression.impl.variable.provider;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableProvider;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * <p>
 * A base implementation that can be extended, possibly as an anonymous class.
 * </p>
 * <p><b>Take care not to provide the wrong variable type when extending this class
 * directly.</b></p>
 * <p>
 * You can also use one of its subclasses, eg. {@link StringVariableProvider}.
 * </p>
 *
 * @author madgaksha
 *
 * @param <T>
 *            Class of the {@link ALangObject}.
 */
@ParametersAreNonnullByDefault
public abstract class AVariableProvider<T extends ALangObject> implements IVariableProvider<T> {
	private static final long serialVersionUID = 1L;

	private final IVariableType type;

	protected AVariableProvider(final IVariableType type) {
		this.type = type;
	}

	@Override
	public final IVariableType getType() {
		return type;
	}
}