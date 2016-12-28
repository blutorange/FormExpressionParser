package de.xima.fc.form.expression.impl.variable.provider;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.parse.IVariableProvider;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public abstract class DynamicArrayVariableProvider<V extends ALangObject> extends AVariableProvider<ArrayLangObject> {
	private static final long serialVersionUID = 1L;
	private final IVariableType innerType;

	protected DynamicArrayVariableProvider(final IVariableType innerType) {
		super(GenericVariableType.forArray(innerType));
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
		return GenericVariableType.forArray(innerType);
	}

	@Override
	public final ArrayLangObject make() {
		final IVariableProvider<V>[] items = getItems();
		if (getInnerType(items) != innerType)
			throw new IllegalVariableTypeException(
					NullUtil.messageFormat(CmnCnst.Error.INNER_TYPE_NOT_MATCHING, getInnerType(items), innerType));
		return ArrayLangObject.create(getItems(), 0);
	}

	protected abstract IVariableProvider<V>[] getItems();
}