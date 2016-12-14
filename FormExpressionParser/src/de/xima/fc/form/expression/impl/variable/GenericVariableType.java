package de.xima.fc.form.expression.impl.variable;

import static de.xima.fc.form.expression.impl.variable.ELangObjectType.NULL;
import static de.xima.fc.form.expression.impl.variable.ELangObjectType.OBJECT;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.iface.parse.IVariableTypeBuilder;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@Immutable
public class GenericVariableType implements IVariableType {
	private static final long serialVersionUID = 1L;
	@Nonnull
	private final ILangObjectClass clazz;
	@Nonnull
	private final IVariableType[] list;

	public GenericVariableType(@Nonnull final ILangObjectClass type, @Nonnull final List<IVariableType> list) {
		if (!type.allowsGenericsCount(list.size()))
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_COMPOUND_TYPE, type));
		this.clazz = type;
		this.list = NullUtil.checkNotNull(list.toArray(new IVariableType[list.size()]));
	}

	public GenericVariableType(@Nonnull final ILangObjectClass type, @Nonnull final IVariableType... list) {
		if (!type.allowsGenericsCount(list.length))
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_COMPOUND_TYPE, type));
		this.clazz = type;
		this.list = NullUtil.checkNotNull(Arrays.copyOf(list, list.length));
	}

	@Override
	public boolean equalsType(final IVariableType other) {
		if (clazz != other.getBasicLangClass())
			return false;
		if (list.length != other.getGenericCount())
			return false;
		for (int i = list.length; i-- > 0;)
			if (!list[i].equalsType(other.getGeneric(i)))
				return false;
		return true;
	}

	@Override
	public ILangObjectClass getBasicLangClass() {
		return clazz;
	}

	@Override
	public int getGenericCount() {
		return list.length;
	}

	@SuppressWarnings("null")
	@Override
	public IVariableType getGeneric(final int i) throws ArrayIndexOutOfBoundsException {
		return list[i];
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(clazz.toString());
		sb.append('<');
		for (int i = 0; i < list.length; ++i) {
			sb.append(list[i].toString());
			if (list.length != 1 && i < list.length - 1)
				sb.append(',');
		}
		sb.append('>');
		return sb.toString();
	}

	@Override
	public IVariableType union(final IVariableType otherType) {
		// optimize for the most common case
		if (equalsType(otherType))
			return this;
		if (otherType.getBasicLangClass() == NULL || clazz == OBJECT)
			return this;
		if (clazz == ELangObjectType.NULL || otherType.getBasicLangClass() == OBJECT)
			return otherType;
		if (otherType.getGenericCount() != list.length || clazz != otherType.getBasicLangClass())
			return SimpleVariableType.OBJECT;
		final IVariableTypeBuilder builder = new VariableTypeBuilder();
		builder.setBasicType(clazz);
		for (int i = list.length; i-- > 0;)
			builder.append(list[i].union(otherType.getGeneric(i)));
		return builder.build();
	}

	@Override
	public boolean isIterable() {
		return getBasicLangClass().isIterable();
	}

	@Override
	public boolean isAssignableFrom(final IVariableType otherType) {
		// @formatter:off
		// this    null object string     ...
		// null     o      x     x     x  ...
		// object   o      o     o     o  ...
		// string   o      x     o     x   x
		//   .      o      x     x     o   x
		//   .      o      x     x     x   o
		//   .      o      x     x     x   x
		// @formatter:on
		if (otherType.getBasicLangClass() == ELangObjectType.NULL || clazz == ELangObjectType.OBJECT)
			return true;
		if (clazz != otherType.getBasicLangClass() || list.length != otherType.getGenericCount())
			return false;
		for (int i = list.length; i-- > 0;)
			if (!list[i].isAssignableFrom(otherType.getGeneric(i)))
				return false;
		return true;
	}

	@Override
	public IVariableType getIterableItemType() {
		return clazz.getIterableItemType(list);
	}
}