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
public final class FixedArrayVariableProvider<V extends ALangObject>
extends AVariableProvider<ArrayLangObject> {
	private static final long serialVersionUID = 1L;
	private final IVariableProvider<V>[] items;
	@SafeVarargs
	private FixedArrayVariableProvider(final IVariableProvider<V>... items) {
		super(getInnerType(items));
		this.items = items;
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
		return ArrayLangObject.create(items, 0);
	}
}