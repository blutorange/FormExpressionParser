package de.xima.fc.form.expression.impl.variable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.iface.parse.IVariableTypeBuilder;
import de.xima.fc.form.expression.util.CmnCnst;

@ParametersAreNonnullByDefault
public class VariableTypeBuilder implements IVariableTypeBuilder {
	@Nullable
	private ILangObjectClass type;
	@Nullable
	private List<IVariableType> children;

	public VariableTypeBuilder() {
	}

	@Override
	public VariableTypeBuilder append(@Nonnull final IVariableType type) {
		getChildren().add(type);
		return this;
	}

	@Override
	public VariableTypeBuilder append(@Nonnull final ILangObjectClass type) throws IllegalVariableTypeException {
		return append(forSimpleType(type));
	}

	private List<IVariableType> getChildren() {
		if (children != null)
			return children;
		return children = new ArrayList<>();
	}

	@Override
	@Nonnull
	public IVariableType build() throws IllegalVariableTypeException {
		final List<IVariableType> c = children;
		final ILangObjectClass t = type;
		if (t == null)
			throw new IllegalVariableTypeException(CmnCnst.Error.NULL_LANG_OBJECT_TYPE);
		if (c == null || c.isEmpty())
			return VariableTypeBuilder.forSimpleType(t);
		return new GenericVariableType(t, c);
	}

	@Override
	public IVariableTypeBuilder setBasicType(@Nonnull final ILangObjectClass type) {
		this.type = type;
		return this;
	}

	private static IVariableType forSimpleType(final ILangObjectClass type) throws IllegalVariableTypeException {
		return type.getSimpleVariableType();
	}
}