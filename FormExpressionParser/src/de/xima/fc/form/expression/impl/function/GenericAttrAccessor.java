package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.ArrayIndexOutOfBoundsException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAccessorException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IAttrAccessorFunction;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
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
 * A set of generic attribute accessors for elements. Each object has got a
 * predefined set of named attributes, eg. <code>length</code> for
 * {@link ArrayLangObject}s or <code>message</code> for
 * {@link ExceptionLangObject}s. When none of those match, these generic
 * functions will be called. For example, they can be used to access an entry of
 * a {@link HashLangObject}, or the character at a certain index of a
 * {@link StringLangObject}.
 *
 * <br>
 * <br>
 *
 * The argument array is guaranteed to contain exactly two entries, the
 * {@link ALangObject} <code>property<code> and the {@link BooleanLangObject}
 * <code>accessedViaDot</dot>.
 *
 * @author madgaksha
 *
 * @param <T>
 */
public abstract class GenericAttrAccessor<T extends ALangObject> implements IAttrAccessorFunction<T> {
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
	 *             an <code>int</code>.
	 */
	public final static IAttrAccessorFunction<StringLangObject> STRING = new GenericAttrAccessor<StringLangObject>(
			ELangObjectType.STRING, "genericAttrAccessorString", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			if (((BooleanLangObject) args[1]).booleanValue())
				throw new NoSuchAttrAccessorException(NullUtil.toString(args[0]), true, ec);
			final int index = args[0].coerceNumber(ec).intValue(ec);
			final int len = thisContext.length();
			if (index >= len || index < -len)
				return NullLangObject.getInstance();
			return StringLangObject.create(thisContext.stringValue().charAt(index < 0 ? index + len : index));
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
	public final static IAttrAccessorFunction<ArrayLangObject> ARRAY = new GenericAttrAccessor<ArrayLangObject>(
			ELangObjectType.ARRAY, "genericAttrAccessorArray", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
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
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IAttrAccessorFunction<BooleanLangObject> BOOLEAN = new GenericAttrAccessor<BooleanLangObject>(
			ELangObjectType.BOOLEAN, "genericAttrAccessorBoolean", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IAttrAccessorFunction<ExceptionLangObject> EXCEPTION = new GenericAttrAccessor<ExceptionLangObject>(
			ELangObjectType.EXCEPTION, "genericAttrAccessorException", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IAttrAccessorFunction<RegexLangObject> REGEX = new GenericAttrAccessor<RegexLangObject>(
			ELangObjectType.REGEX, "genericAttrAccessorRegex", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IAttrAccessorFunction<FunctionLangObject> FUNCTION = new GenericAttrAccessor<FunctionLangObject>(
			ELangObjectType.FUNCTION, "genericAttrAccessorFunction", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IAttrAccessorFunction<NumberLangObject> NUMBER = new GenericAttrAccessor<NumberLangObject>(
			ELangObjectType.NUMBER, "genericAttrAccessorNumber", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}
	};

	/**
	 * @param key
	 *            {@link ALangObject} The key. When not specified,
	 *            {@link NullLangObject} is assumed.
	 * @return {@link ALangObject} The object that is mapped to the given key.
	 * @NullLangObject When the map does not contain any value for the key, or
	 *                 the key is mapped to {@link NullLangObject}. Use
	 *                 {@link EAttrAccessorHash#contains} to check.
	 */
	public final static IAttrAccessorFunction<HashLangObject> HASH = new GenericAttrAccessor<HashLangObject>(
			ELangObjectType.HASH, "genericAttrAccessorHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			return thisContext.get(args[0]);
		}
	};

	@Nonnull
	private final String name;
	@Nonnull
	private final String[] argList;
	private final boolean hasVarArgs;
	@Nonnull
	private final ILangObjectClass type;

	private GenericAttrAccessor(@Nonnull final ILangObjectClass type, @Nonnull final String name,
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
