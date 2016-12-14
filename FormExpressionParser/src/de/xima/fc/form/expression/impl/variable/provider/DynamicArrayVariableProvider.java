package de.xima.fc.form.expression.impl.variable.provider;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.parse.IVariableProvider;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;

@ParametersAreNonnullByDefault
public abstract class DynamicArrayVariableProvider<V extends ALangObject>
extends AVariableProvider<ArrayLangObject> {
	private static final long serialVersionUID = 1L;
	private final IVariableType innerType;
	protected DynamicArrayVariableProvider(final IVariableType innerType) {
		super(new GenericVariableType(ELangObjectType.ARRAY, innerType));
		this.innerType = innerType;
	}
	private static <V extends ALangObject> IVariableType getInnerType(final IVariableProvider<V>[] items) {
		IVariableType innerType = SimpleVariableType.NULL;
		for (final IVariableProvider<V> i : items) {
			if (i == null)
				throw new IllegalVariableTypeException();
			innerType = innerType.union(i.getType());
			innerType = i.getType();
		}
		return new GenericVariableType(ELangObjectType.ARRAY, innerType);
	}
	@Override
	public final ArrayLangObject make() {
		final IVariableProvider<V>[] items = getItems();
		if (getInnerType(items) != innerType)
			throw new IllegalVariableTypeException(String.format(
					"Actual inner type %s does not match declared inner type %s.", getInnerType(items), innerType));
		return ArrayLangObject.create(getItems(), 0);
	}

	protected abstract IVariableProvider<V>[] getItems();
}