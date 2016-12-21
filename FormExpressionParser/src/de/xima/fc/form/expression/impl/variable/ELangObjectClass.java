package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableCollection;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * Class providing meta information about the different types of language
 * objects and their capabilities.
 *
 * @author madgaksha
 *
 */
@ParametersAreNonnullByDefault
public enum ELangObjectClass implements ILangObjectClass {
	OBJECT(0, false, ALangObject.class, CmnCnst.Syntax.VAR, false, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			return i == 0 && flags.isEmpty();
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) throws IllegalVariableTypeException {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return null;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return null;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) {
			return object;
		}
	},
	NULL(1, true, NullLangObject.class, CmnCnst.Syntax.VAR, false, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			return i == 0 && flags.isEmpty();
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.NULL;
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) throws CoercionException {
			throw new CoercionException(NullLangObject.getInstance(), ELangObjectClass.NULL, ec);
		}
	},
	BOOLEAN(2, true, BooleanLangObject.class, CmnCnst.Syntax.BOOLEAN, false, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			return i == 0 && flags.isEmpty();
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.BOOLEAN;
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) {
			return object.coerceBoolean(ec);
		}
	},
	NUMBER(3, true, NumberLangObject.class, CmnCnst.Syntax.NUMBER, true, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			return i == 0 && flags.isEmpty();
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			// Iterates from 0 to n-1.
			return SimpleVariableType.NUMBER;
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.NUMBER;
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) throws CoercionException {
			return object.coerceNumber(ec);
		}
	},
	STRING(4, true, StringLangObject.class, CmnCnst.Syntax.STRING, true, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			return i == 0 && flags.isEmpty();
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			// Iterates over the string's characters.
			return SimpleVariableType.STRING;
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.STRING;
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) {
			return object.coerceString(ec);
		}
	},
	REGEX(5, true, RegexLangObject.class, CmnCnst.Syntax.REGEX, false, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			return i == 0 && flags.isEmpty();
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.REGEX;
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) throws CoercionException {
			return object.coerceRegex(ec);
		}
	},
	FUNCTION(6, true, FunctionLangObject.class, CmnCnst.Syntax.METHOD, false, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			for (final EVariableTypeFlag flag : flags) {
				if (flag != EVariableTypeFlag.VARARG)
					return false;
				return i >= 2;
			}
			return i >= 1;
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_SIMPLE_TYPE, this));
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) throws CoercionException {
			return object.coerceFunction(ec);
		}

	},
	EXCEPTION(7, true, ExceptionLangObject.class, CmnCnst.Syntax.ERROR, false, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			return i == 0 && flags.isEmpty();
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.EXCEPTION;
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) throws CoercionException {
			return object.coerceRegex(ec);
		}
	},
	ARRAY(8, false, ArrayLangObject.class, CmnCnst.Syntax.ARRAY, true, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			return i == 1 && flags.isEmpty();
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			// Iterates over the array items.
			return NullUtil.checkNotNull(generics[0]);
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_SIMPLE_TYPE, this));
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) throws CoercionException {
			return object.coerceArray(ec);
		}
	},
	HASH(9, false, HashLangObject.class, CmnCnst.Syntax.HASH, true, true) {
		@Override
		public boolean allowsGenericsCountAndFlags(final int i, final ImmutableCollection<EVariableTypeFlag> flags) {
			return i == 2 && flags.isEmpty();
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			// Iterates over the keys.
			return NullUtil.checkNotNull(generics[0]);
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_SIMPLE_TYPE, this));
		}

		@Nullable
		@Override
		public IVariableType getSuperType(final IVariableType type) {
			return SimpleVariableType.OBJECT;
		}

		@Nullable
		@Override
		public ILangObjectClass getSuperClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) throws CoercionException {
			return object.coerceHash(ec);
		}
	};

	private final boolean supportsCoercion;
	private final boolean isImmutable;
	private final Integer id;
	private final boolean isIterable;
	private final Class<? extends ALangObject> clazz;
	private final String syntacticalTypeName;

	private ELangObjectClass(final Integer id, final boolean isImmutable, final Class<? extends ALangObject> clazz,
			final String syntacticalTypeName, final boolean isIterable, final boolean supportsCoercion) {
		this.id = id;
		this.clazz = clazz;
		this.syntacticalTypeName = syntacticalTypeName;
		this.isIterable = isIterable;
		this.isImmutable = isImmutable;
		this.supportsCoercion = supportsCoercion;
	}

	@Override
	public final Integer getClassId() {
		return id;
	}

	@Override
	public final Class<? extends ALangObject> getLangObjectClass() {
		return clazz;
	}

	@Override
	public final String getSyntacticalTypeName() {
		return syntacticalTypeName;
	}

	@Override
	public final boolean isIterable() {
		return isIterable;
	}

	@Override
	public abstract IVariableType getIterableItemType(IVariableType[] generics)
			throws IllegalVariableTypeException, ArrayIndexOutOfBoundsException;

	@Override
	public boolean isImmutable() {
		return isImmutable;
	}

	@Override
	public final boolean equalsClass(final ILangObjectClass clazz) {
		return id.equals(clazz.getClassId());
	}

	@Override
	public final boolean supportsBasicCoercion() {
		return supportsCoercion;
	}

	@Override
	public boolean isSuperClassOf(final ILangObjectClass that) {
		for (ILangObjectClass clazz = that; clazz != null; clazz = clazz.getSuperClass())
			if (equalsClass(clazz))
				return true;
		return false;
	}
}