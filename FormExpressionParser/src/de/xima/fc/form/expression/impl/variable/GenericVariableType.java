package de.xima.fc.form.expression.impl.variable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@Immutable
@NonNullByDefault
public class GenericVariableType implements IVariableType {
	private static final long serialVersionUID = 1L;
	private final ILangObjectClass clazz;
	private final IVariableType[] list;
	private final ImmutableCollection<EVariableTypeFlag> flags;

	public GenericVariableType(final ILangObjectClass type, final List<IVariableType> list) {
		this(type, list, null);
	}

	public GenericVariableType(final ILangObjectClass type, @Nullable final List<IVariableType> list,
			@Nullable final Collection<EVariableTypeFlag> flags) {
		final List<IVariableType> l = list != null ? list : Collections.<IVariableType> emptyList();
		this.clazz = type;
		this.list = NullUtil.checkNotNull(l.toArray(new IVariableType[l.size()]));
		this.flags = flags != null && flags.size() > 0 ? Sets.immutableEnumSet(flags) : ImmutableSet.<EVariableTypeFlag> of();
		if (!type.allowsGenericsCountAndFlags(l.size(), this.flags))
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_COMPOUND_TYPE, type));

	}

	private GenericVariableType(final ILangObjectClass type,
			@Nullable final ImmutableCollection<EVariableTypeFlag> flags, final IVariableType... list) {
		this.clazz = type;
		this.list = list;
		this.flags = flags != null ? flags : ImmutableSet.<EVariableTypeFlag>of();
		if (!type.allowsGenericsCountAndFlags(list.length, flags != null ? flags : ImmutableSet.<EVariableTypeFlag>of()))
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_COMPOUND_TYPE, type));
	}

	@Override
	public boolean equalsType(final IVariableType that) {
		return equalsType(this, that);
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
		sb.append(clazz.getSyntacticalTypeName());
		if (list.length > 0) {
			sb.append('<');
			for (int i = 0; i < list.length; ++i)
				sb.append(list[i].toString()).append(',');
			sb.setLength(sb.length()-1);
			sb.append('>');
		}
		if (!flags.isEmpty()) {
			sb.append('[');
			for (final EVariableTypeFlag flag : flags)
				sb.append(flag.toString()).append(',');
			sb.setLength(sb.length()-1);
			sb.append(']');
		}
		@Nonnull
		final String toString = sb.toString();
		return toString;
	}

	@Override
	public IVariableType union(final IVariableType that) {
		return union(this, that);
	}

	@Override
	public boolean isIterable() {
		return getBasicLangClass().isIterable();
	}

	@Override
	public boolean isAssignableFrom(final IVariableType that) {
		return isAssignableFrom(this, that);
	}

	@Override
	public IVariableType getIterableItemType() {
		return clazz.getIterableItemType(list);
	}

	@Override
	public boolean isA(final ILangObjectClass baseClass) {
		return clazz.equalsClass(baseClass);
	}

	@Override
	public boolean hasFlag(final EVariableTypeFlag flag) {
		return flags.contains(flag);
	}

	@Override
	public ImmutableCollection<EVariableTypeFlag> getFlags() {
		return flags;
	}

	@Override
	public IVariableType upconvert(final ILangObjectClass superClass) {
		return upconvert(this, superClass);
	}

	public static IVariableType forArray(final IVariableType itemType) {
		return new GenericVariableType(ELangObjectClass.ARRAY, null, itemType);
	}

	public static IVariableType forHash(final IVariableType keyType, final IVariableType valueType) {
		return new GenericVariableType(ELangObjectClass.HASH, null, keyType, valueType);
	}

	public static IVariableType forSimpleFunction(final IVariableType returnType, final IVariableType... argType) {
		final IVariableType[] arr = new IVariableType[argType.length + 1];
		arr[0] = returnType;
		System.arraycopy(argType, 0, arr, 1, argType.length);
		return new GenericVariableType(ELangObjectClass.FUNCTION, null, arr);
	}

	public static IVariableType forVarArgFunction(final IVariableType returnType, final IVariableType varArgType,
			final IVariableType... argType) {
		final IVariableType[] arr = new IVariableType[argType.length + 2];
		arr[0] = returnType;
		System.arraycopy(argType, 0, arr, 1, argType.length);
		arr[arr.length-1] = varArgType;
		return new GenericVariableType(ELangObjectClass.FUNCTION, Sets.immutableEnumSet(EVariableTypeFlag.VARARG), arr);
	}

	// @formatter:off
	// this    null object string     ...
	// null     o      x     x     x  ...
	// object   o      o     o     o  ...
	// string   o      x     o     x   x
	//   .      o      x     x     o   x
	//   .      o      x     x     x   o
	//   .      o      x     x     x   x
	// @formatter:on
	public static boolean isAssignableFrom(final IVariableType thisType, final IVariableType thatType) {
		if (thisType.isA(VoidClass.INSTANCE) || thatType.isA(VoidClass.INSTANCE))
			return false;
		if (thatType.isA(ELangObjectClass.NULL) || thisType.isA(ELangObjectClass.OBJECT) || thisType.equalsType(thatType))
			return true;
		if (thisType.isA(ELangObjectClass.NULL) || thatType.isA(ELangObjectClass.OBJECT))
			return false;
		for (IVariableType i = thatType; i != null; i = i.getBasicLangClass().getSuperType(i)) {
			if (thisType.getBasicLangClass().getClassId().equals(i.getBasicLangClass().getClassId())
					&& thisType.getGenericCount() == i.getGenericCount() && thisType.getFlags().equals(i.getFlags())) {
				if (genericsAllNull(i))
					return true;
				for (int j = 0; j < i.getGenericCount(); ++j)
					if (!thisType.getGeneric(j).equalsType(i.getGeneric(j)))
						return false;
				return true;
			}
		}
		return false;
	}

	public static IVariableType union(final IVariableType thisType, final IVariableType thatType) {
		if (thisType.isA(VoidClass.INSTANCE) || thatType.isA(VoidClass.INSTANCE))
			return VoidType.INSTANCE;
		if (thisType.equalsType(thatType) || thatType.isA(ELangObjectClass.NULL) || thisType.isA(ELangObjectClass.OBJECT))
			return thisType;
		if (thisType.isA(ELangObjectClass.NULL) || thatType.isA(ELangObjectClass.OBJECT))
			return thatType;
		// Traverse the class tree upwards and look for the closest common
		// superclass.
		for (IVariableType i = thisType; i != null; i = i.getBasicLangClass().getSuperType(i)) {
			for (IVariableType j = thatType; j != null; j = j.getBasicLangClass().getSuperType(j)) {
				if (i.equals(j) || i.getGenericCount() == j.getGenericCount() && i.isA(j.getBasicLangClass())
						&& thisType.getFlags().equals(thatType.getFlags()) && (genericsAllNull(i) || genericsAllNull(j)))
					return i;
			}
		}
		return SimpleVariableType.OBJECT;
	}

	public static boolean equalsType(final IVariableType thisType, final IVariableType thatType) {
		if (!thisType.isA(thatType.getBasicLangClass()))
			return false;
		if (thisType.getGenericCount() != thatType.getGenericCount())
			return false;
		if (!thisType.getFlags().equals(thatType.getFlags()))
			return false;
		for (int i = thisType.getGenericCount(); i-- > 0;)
			if (!thisType.getGeneric(i).equalsType(thatType.getGeneric(i)))
				return false;
		return true;
	}

	private static boolean genericsAllNull(final IVariableType type) {
		for (int i = type.getGenericCount(); i-- > 0;)
			if (!type.getGeneric(i).isA(ELangObjectClass.NULL))
				return false;
		return true;
	}

	public static IVariableType upconvert(final IVariableType subType, final ILangObjectClass superClass) {
		IVariableType type = subType;
		do {
			if (type.isA(superClass))
				return type;
			type = type.getBasicLangClass().getSuperType(type);
		} while (type != null);
		if (superClass.isSuperClassOf(subType.getBasicLangClass()))
			throw new FormExpressionException(NullUtil.messageFormat(CmnCnst.Error.INCONSISTENT_CLASS_HIERARCHY,
				subType.getBasicLangClass(), superClass));
		return SimpleVariableType.OBJECT;
	}
}