package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.ArrayIndexOutOfBoundsException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAssignerException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IGenericBracketAssignerFunction;
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
 * A set of generic attribute assigners for object. This is used
 * for assigning properties via bracket notation, eg.
 * <code>array[0] = 5</code>.
 * </p><p>
 * The argument array is guaranteed to contain exactly two entries, the
 * {@link ALangObject} <code>property<code> and the<code>value</code>
 * {@link ALangObject} to be assigned.
 * </p>
 *
 * @author madgaksha
 *
 * @param <T> Type of the language object for the bracket accessor.
 */
public abstract class GenericBracketAssigner<T extends ALangObject> implements IGenericBracketAssignerFunction<T> {
	private static final long serialVersionUID = 1L;

	@Nonnull
	private final String name;

	@Nonnull
	private final String[] argList;

	@Nonnull
	private final ILangObjectClass type;

	private final boolean hasVarArgs;

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericBracketAssignerFunction<StringLangObject> STRING = new GenericBracketAssigner<StringLangObject>(ELangObjectType.STRING,
			"genericBracketAssignerString", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
				final IVariableType value) {
			return false;
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
	 *             an <code>int</code>.
	 */
	public final static IGenericBracketAssignerFunction<ArrayLangObject> ARRAY = new GenericBracketAssigner<ArrayLangObject>(ELangObjectType.ARRAY,
			"genericBracketAssignerArray", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			final int index = args[0].coerceNumber(ec).intValue(ec);
			final int len = thisContext.length();
			if (index >= len || index < -len)
				throw new ArrayIndexOutOfBoundsException(thisContext, index, ec);
			thisContext.set(index < 0 ? index + len : index, args[1]);
			return thisContext;
		}

		@Override
		public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
				final IVariableType value) {
			// Accessor must be a numeric index, eg. array[0] but not array[true]
			if (!SimpleVariableType.NUMBER.isAssignableFrom(property))
				return false;
			// Object to be assigned must be of the correct type, eg. a number for array<object>
			return thisContext.getGeneric(0).isAssignableFrom(value);
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericBracketAssignerFunction<BooleanLangObject> BOOLEAN = new GenericBracketAssigner<BooleanLangObject>(ELangObjectType.BOOLEAN,
			"genericBracketAssignerBoolean", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericBracketAssignerFunction<ExceptionLangObject> EXCEPTION = new GenericBracketAssigner<ExceptionLangObject>(
			ELangObjectType.EXCEPTION, "genericBracketAssignerException", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericBracketAssignerFunction<RegexLangObject> REGEX = new GenericBracketAssigner<RegexLangObject>(ELangObjectType.REGEX,
			"genericBracketAssignerRegex", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericBracketAssignerFunction<FunctionLangObject> FUNCTION = new GenericBracketAssigner<FunctionLangObject>(
			ELangObjectType.FUNCTION, "genericBracketAssignerFunction", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericBracketAssignerFunction<NumberLangObject> NUMBER = new GenericBracketAssigner<NumberLangObject>(ELangObjectType.NUMBER,
			"genericBracketAssignerFunction", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @param key
	 *            {@link ALangObject} The key. When not specified,
	 *            {@link NullLangObject} is assumed.
	 * @return {@link ALangObject} The object that is mapped to the given key.
	 * @NullLangObject When the map does not contain any value for the key, or
	 *                 the key is mapped to {@link NullLangObject}. Use
	 *                 {@link EDotAccessorHash#contains} to check.
	 */
	public final static IGenericBracketAssignerFunction<HashLangObject> HASH = new GenericBracketAssigner<HashLangObject>(ELangObjectType.HASH,
			"genericBracketAssignerHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			thisContext.put(args[0], args[1]);
			return thisContext;
		}

		@Override
		public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
				final IVariableType value) {
			// Property must be compatible with the first generics argument,
			// eg. number for hash<object, string>
			if (!thisContext.getGeneric(0).isAssignableFrom(property))
				return false;
			// Value to be assigned must be compatible with the second
			// generics argument, eg. number for hash<string, object>
			return thisContext.getGeneric(1).isAssignableFrom(value);
		}
	};

	private GenericBracketAssigner(@Nonnull final ILangObjectClass type, @Nonnull final String name,
			final boolean hasVarArgs, @Nonnull final String... argList) {
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
