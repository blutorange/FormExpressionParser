package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.ArrayIndexOutOfBoundsException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAccessorException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IGenericBracketAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
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
import de.xima.fc.form.expression.util.NullUtil;

/**
 * <p>
 * A set of generic attribute accessors for elements. This is used
 * for accessing properties via bracket notation, eg. <code>array[0]</code>
 * </p><p>
 * The argument array is guaranteed to contain exactly one entry, the
 * {@link ALangObject} <code>property<code>.
 * </p>
 *
 * @author madgaksha
 *
 * @param <T> Type of the language object for the bracket accessor.
 */
@ParametersAreNonnullByDefault
public abstract class GenericBracketAccessor<T extends ALangObject> implements IGenericBracketAccessorFunction<T> {
	private static final long serialVersionUID = 1L;
	/**
	 * @param index
	 *            {@link NumberLangObject} The index of the character to get
	 *            from the string. May be negative to address elements from the
	 *            end of the array, eg. <code>-1</code> for the last element.
	 * @return {@link StringLangObject}. The character at the given position.
	 * @NullLangObject When there is no element at the given position, ie.
	 *                 either when <code>index&gt;=string.length</code> or
	 *                 <code>index &lt; -string.length</code>.
	 * @throws MathException
	 *             When the index is too large or too small to be represented as
	 *             a 4 byte signed int.
	 */
	public final static IGenericBracketAccessorFunction<StringLangObject> STRING = new GenericBracketAccessor<StringLangObject>(
			ELangObjectType.STRING, "genericBracketAccessorString", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			final int index = args[0].coerceNumber(ec).intValue(ec);
			final int len = thisContext.length();
			if (index >= len || index < -len)
				return NullLangObject.getInstance();
			return StringLangObject.create(thisContext.stringValue().charAt(index < 0 ? index + len : index));
		}

		@Nullable
		@Override
		public IVariableType getBracketAccessorReturnType(final IVariableType thisContext,
				final IVariableType property) {
			return SimpleVariableType.NUMBER.isAssignableFrom(property) ? SimpleVariableType.STRING : null;
		}
	};

	/**
	 * @param index
	 *            {@link NumberLangObject} The index of the character to get
	 *            from the array. May be negative to address elements from the
	 *            end of the array, eg. <code>-1</code> for the last element.
	 * @return {@link ALangObject}. The element at the given position.
	 * @NullLangObject When there is no element at the given position, ie.
	 *                 either when <code>index&gt;=string.length</code> or
	 *                 <code>index &lt; -string.length</code>.
	 * @throws MathException
	 *             When the index is too large or too small to be represented as
	 *             a 4 byte signed int.
	 */
	public final static IGenericBracketAccessorFunction<ArrayLangObject> ARRAY = new GenericBracketAccessor<ArrayLangObject>(
			ELangObjectType.ARRAY, "genericBracketAccessorArray", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			if (((BooleanLangObject) args[1]).booleanValue())
				throw new NoSuchAttrAccessorException(NullUtil.toString(args[0]), true, ec);
			final int index = args[0].coerceNumber(ec).intValue(ec);
			final int len = thisContext.length();
			if (index >= len || index < -len)
				throw new ArrayIndexOutOfBoundsException(thisContext, index, ec);
			return thisContext.get(index < 0 ? index + len : index);
		}

		@Nullable
		@Override
		public IVariableType getBracketAccessorReturnType(final IVariableType thisContext,
				final IVariableType property) {
			// Property must be a numeric index, eg. array[0], but not array[true].
			if (!SimpleVariableType.NUMBER.isAssignableFrom(property))
				return null;
			// Return type is the first generics argument, eg number for array<number>
			return thisContext.getGeneric(0);
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericBracketAccessorFunction<BooleanLangObject> BOOLEAN = new GenericBracketAccessor<BooleanLangObject>(
			ELangObjectType.BOOLEAN, "genericBracketAccessorBoolean", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getBracketAccessorReturnType(final IVariableType thisContext,
				final IVariableType property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericBracketAccessorFunction<ExceptionLangObject> EXCEPTION = new GenericBracketAccessor<ExceptionLangObject>(
			ELangObjectType.EXCEPTION, "genericBracketAccessorException", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getBracketAccessorReturnType(final IVariableType thisContext,
				final IVariableType property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericBracketAccessorFunction<RegexLangObject> REGEX = new GenericBracketAccessor<RegexLangObject>(
			ELangObjectType.REGEX, "genericBracketAccessorRegex", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getBracketAccessorReturnType(final IVariableType thisContext,
				final IVariableType property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericBracketAccessorFunction<FunctionLangObject> FUNCTION = new GenericBracketAccessor<FunctionLangObject>(
			ELangObjectType.FUNCTION, "genericBracketAccessorFunction", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getBracketAccessorReturnType(final IVariableType thisContext,
				final IVariableType property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericBracketAccessorFunction<NumberLangObject> NUMBER = new GenericBracketAccessor<NumberLangObject>(
			ELangObjectType.NUMBER, "genericBracketAccessorNumber", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getBracketAccessorReturnType(final IVariableType thisContext,
				final IVariableType property) {
			return null;
		}
	};

	/**
	 * @param key
	 *            {@link ALangObject} The key. Must be of the variable type as
	 *            specified by the first generics argument.
	 * @return {@link ALangObject} The object that is mapped to the given key.
	 *         Its type is the second generics argument.
	 * @NullLangObject When the map does not contain any value for the key, or
	 *                 the key is mapped to {@link NullLangObject}. Use
	 *                 {@link EDotAccessorHash#contains} to check.
	 */
	public final static IGenericBracketAccessorFunction<HashLangObject> HASH = new GenericBracketAccessor<HashLangObject>(
			ELangObjectType.HASH, "genericBracketAccessorHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			return thisContext.get(args[0]);
		}

		@Nullable
		@Override
		public IVariableType getBracketAccessorReturnType(final IVariableType thisContext,
				final IVariableType property) {
			// Property type must be compatible with the first generics argument,
			// eg. number for hash<object, string>
			if (!thisContext.getGeneric(0).isAssignableFrom(property))
				return null;
			return thisContext.getGeneric(1);
		}
	};

	private final String name;
	private final String[] argList;
	private final boolean hasVarArgs;
	private final ILangObjectClass type;

	private GenericBracketAccessor(final ILangObjectClass type, final String name, final boolean hasVarArgs,
			final String... argList) {
		NullUtil.checkItemsNotNull(argList);
		this.type = type;
		this.name = name;
		this.argList = argList;
		this.hasVarArgs = hasVarArgs;
	}

	@Override
	public String getDeclaredName() {
		return name;
	}

	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}

	@SuppressWarnings("null")
	@Override
	public String getDeclaredArgument(final int i) {
		return argList[i];
	}

	@Override
	public int getDeclaredArgumentCount() {
		return argList.length;
	}

	@Override
	public ILangObjectClass getThisContextType() {
		return type;
	}

	@Override
	public abstract ALangObject evaluate(IEvaluationContext ec, T thisContext, ALangObject... args)
			throws EvaluationException;
}
