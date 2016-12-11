package de.xima.fc.form.expression.impl.variable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.iface.parse.IVariableTypeBuilder;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
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

	@Override
	public VariableTypeBuilder append(@Nonnull final ELangObjectType type) throws IllegalVariableTypeException {
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
		final ELangObjectType t = type;
		if (t == null)
			throw new IllegalVariableTypeException(CmnCnst.Error.NULL_LANG_OBJECT_TYPE);
		if (c == null || c.isEmpty())
			return VariableTypeBuilder.forSimpleType(t);
		return new GenericVariableType(t, c);
	}

	@Override
	public IVariableTypeBuilder setBasicType(@Nonnull final ELangObjectType type) {
		this.type = type;
		return this;
	}

	private static IVariableType forSimpleType(final ELangObjectType type) throws IllegalVariableTypeException {
		switch (type) {
		case OBJECT:
			return SimpleVariableType.OBJECT;
		case BOOLEAN:
			return SimpleVariableType.BOOLEAN;
		case EXCEPTION:
			return SimpleVariableType.EXCEPTION;
		case NULL:
			return SimpleVariableType.NULL;
		case NUMBER:
			return SimpleVariableType.NUMBER;
		case REGEX:
			return SimpleVariableType.REGEX;
		case STRING:
			return SimpleVariableType.STRING;
		case ARRAY:
		case FUNCTION:
		case HASH:
		default:
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_SIMPLE_TYPE, type));
		}
	}
}