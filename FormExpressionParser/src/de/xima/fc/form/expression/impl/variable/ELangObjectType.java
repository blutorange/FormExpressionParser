package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
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
public enum ELangObjectType implements ILangObjectClass {
	OBJECT(0, false, ALangObject.class, null, CmnCnst.Syntax.OBJECT, false) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) throws IllegalVariableTypeException {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.OBJECT;
		}
	},
	NULL(1, true, NullLangObject.class, null, CmnCnst.Syntax.VAR, false) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.NULL;
		}
	},
	BOOLEAN(2, true, BooleanLangObject.class, ELangObjectType.OBJECT, CmnCnst.Syntax.BOOLEAN, false) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.BOOLEAN;
		}

		//		@Nullable
		//		@Override
		//		public IFunction<BooleanLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot,
		//				final IEvaluationContext ec) throws EvaluationException {
		//			return ec.getNamespace().attrAccessorBoolean(object, accessedViaDot);
		//		}
		//
		//		@Nullable
		//		@Override
		//		public IFunction<BooleanLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot,
		//				final IEvaluationContext ec) throws EvaluationException {
		//			return ec.getNamespace().attrAssignerBoolean(name, accessedViaDot);
		//		}
	},
	NUMBER(3, true, NumberLangObject.class, ELangObjectType.OBJECT, CmnCnst.Syntax.NUMBER, true) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
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
	},
	STRING(4, true, StringLangObject.class, ELangObjectType.OBJECT, CmnCnst.Syntax.STRING, true) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
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
	},
	REGEX(5, true, RegexLangObject.class, ELangObjectType.OBJECT, CmnCnst.Syntax.REGEX, false) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.REGEX;
		}
	},
	FUNCTION(6, true, FunctionLangObject.class, ELangObjectType.OBJECT, CmnCnst.Syntax.METHOD, false) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i > 0;
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_SIMPLE_TYPE, this));
		}
	},
	EXCEPTION(7, true, ExceptionLangObject.class, ELangObjectType.OBJECT, CmnCnst.Syntax.ERROR, false) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}

		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new IllegalVariableTypeException();
		}

		@Override
		public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
			return SimpleVariableType.EXCEPTION;
		}
	},
	ARRAY(8, false, ArrayLangObject.class, ELangObjectType.OBJECT, CmnCnst.Syntax.ARRAY, true) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 1;
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
	},
	HASH(9, false, HashLangObject.class, ELangObjectType.OBJECT, CmnCnst.Syntax.HASH, true) {
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 2;
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
	};

	private final boolean isImmutable;
	private final Integer id;
	private final boolean isIterable;
	private final Class<? extends ALangObject> clazz;
	private final String syntacticalTypeName;
	@Nullable
	private final ILangObjectClass superType;

	private ELangObjectType(final Integer id, final boolean isImmutable, final Class<? extends ALangObject> clazz,
			@Nullable final ILangObjectClass superType, final String syntacticalTypeName, final boolean isIterable) {
		this.id = id;
		this.superType = superType;
		this.clazz = clazz;
		this.syntacticalTypeName = syntacticalTypeName;
		this.isIterable = isIterable;
		this.isImmutable = isImmutable;
	}

	@Override
	public final Integer getClassId() {
		return id;
	}

	@Nullable
	@Override
	public final ILangObjectClass getSuperType() {
		return superType;
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
	public abstract boolean allowsGenericsCount(int i);

	@Override
	public abstract IVariableType getIterableItemType(IVariableType[] generics)
			throws IllegalVariableTypeException, ArrayIndexOutOfBoundsException;

	@Override
	public boolean isImmutable() {
		return isImmutable;
	}

	//	@Nullable
	//	public abstract IFunction<BooleanLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot,
	//			final IEvaluationContext ec) throws EvaluationException;
	//
	//	@Nullable
	//	public abstract IFunction<BooleanLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot,
	//			final IEvaluationContext ec) throws EvaluationException;

}