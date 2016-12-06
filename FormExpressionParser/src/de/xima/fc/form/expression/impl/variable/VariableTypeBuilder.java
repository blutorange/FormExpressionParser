package de.xima.fc.form.expression.impl.variable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
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

	@Override
	public VariableTypeBuilder append(@Nonnull final ELangObjectType type) throws IllegalVariableTypeException {
		return append(forSimpleType(type));
	}

	@Nonnull
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
	
	@Nonnull
	public static IVariableType forSimpleType(@Nonnull final ELangObjectType type) throws IllegalVariableTypeException {
		switch (type) {
		case BOOLEAN:
			return SingletonType.BOOLEAN;
		case EXCEPTION:
			return SingletonType.EXCEPTION;
		case NULL:
			return SingletonType.NULL;
		case NUMBER:
			return SingletonType.NUMBER;
		case REGEX:
			return SingletonType.REGEX;
		case STRING:
			return SingletonType.STRING;
		default:
			return new SimpleVariableType(type);
		}
	}
	
	private static enum SingletonType implements IVariableType {
		NULL(ELangObjectType.NULL),
		BOOLEAN(ELangObjectType.BOOLEAN),
		NUMBER(ELangObjectType.NUMBER),
		STRING(ELangObjectType.STRING),
		REGEX(ELangObjectType.REGEX),
		EXCEPTION(ELangObjectType.EXCEPTION),
		;
		@Nonnull private final IVariableType impl;
		private SingletonType(@Nonnull final ELangObjectType type) {
			impl = new SimpleVariableType(type);
		}
		@Override
		public boolean equalsType(final IVariableType other) {
			return impl.equalsType(other);
		}
		@Override
		public ELangObjectType getBasicLangType() {
			return impl.getBasicLangType();
		}
		@Override
		public int getGenericCount() {
			return impl.getGenericCount();
		}
		@Override
		public IVariableType getGeneric(final int i) throws ArrayIndexOutOfBoundsException {
			return impl.getGeneric(i);
		}
		@Override
		public IVariableType union(final IVariableType type) throws IllegalVariableTypeException {
			return impl.union(type);
		}
	}
}