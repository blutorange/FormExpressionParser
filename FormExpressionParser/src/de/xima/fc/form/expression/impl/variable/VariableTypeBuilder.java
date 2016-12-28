package de.xima.fc.form.expression.impl.variable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.iface.parse.IVariableTypeBuilder;
import de.xima.fc.form.expression.util.CmnCnst;

@NonNullByDefault
public class VariableTypeBuilder implements IVariableTypeBuilder {
	@Nullable
	private ILangObjectClass type;
	@Nullable
	private List<IVariableType> children;
	@Nullable
	private Collection<EVariableTypeFlag> flags;

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

	@Override
	public IVariableTypeBuilder setFlag(final EVariableTypeFlag flag) {
		getFlags().add(flag);
		return this;
	}

	@Override
	@Nonnull
	public IVariableType build() throws IllegalVariableTypeException {
		final List<IVariableType> c = children;
		final Collection<EVariableTypeFlag> f = flags;
		final ILangObjectClass t = type;
		if (t == null)
			throw new IllegalVariableTypeException(CmnCnst.Error.NULL_LANG_OBJECT_TYPE);
		if ((c == null || c.isEmpty()) && (f == null || f.isEmpty()))
			return VariableTypeBuilder.forSimpleType(t);
		return new GenericVariableType(t, c, f);
	}

	@Override
	public IVariableTypeBuilder setBasicType(@Nonnull final ILangObjectClass type) {
		this.type = type;
		return this;
	}

	private List<IVariableType> getChildren() {
		if (children != null)
			return children;
		return children = new ArrayList<>();
	}

	@SuppressWarnings("null")
	private Collection<EVariableTypeFlag> getFlags() {
		if (flags != null)
			return flags;
		return flags = EnumSet.noneOf(EVariableTypeFlag.class);
	}

	private static IVariableType forSimpleType(final ILangObjectClass type) throws IllegalVariableTypeException {
		return type.getSimpleVariableType();
	}
}