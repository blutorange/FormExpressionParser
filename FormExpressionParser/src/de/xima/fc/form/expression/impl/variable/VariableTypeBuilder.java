package de.xima.fc.form.expression.impl.variable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.iface.parse.IVariableTypeBuilder;
import de.xima.fc.form.expression.util.CmnCnst;

public class VariableTypeBuilder implements IVariableTypeBuilder {
	@Nullable
	private ELangObjectType type;
	@Nullable
	private List<IVariableType> children;

	public VariableTypeBuilder() {
	}

	@Override
	public VariableTypeBuilder append(@Nonnull final IVariableType type) {
		getChildren().add(type);
		return this;
	}

	@Nonnull
	private List<IVariableType> getChildren() {
		if (children != null)
			return children;
		return children = new ArrayList<>();
	}

	@Override
	@Nonnull
	public IVariableType build() throws IllegalStateException {
		final List<IVariableType> c = children;
		final ELangObjectType t = type;
		if (t == null)
			throw new IllegalStateException(CmnCnst.Error.NULL_LANG_OBJECT_TYPE);
		if (c == null || c.isEmpty())
			return new SimpleVariableType(t);
		return new GenericVariableType(t, c);
	}

	@Override
	public void setBasicType(@Nonnull final ELangObjectType type) {
		this.type = type;
	}
}