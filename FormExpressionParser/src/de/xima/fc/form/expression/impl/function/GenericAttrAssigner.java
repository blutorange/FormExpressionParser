package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.ArrayIndexOutOfBoundsException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAccessorException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAssignerException;
import de.xima.fc.form.expression.iface.evaluate.IAttrAssignerFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
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
 * A set of generic attribute assigners for object. Each object has got a
 * predefined set of named attributes. When none of those match, these generic
 * functions will be called. For example, they can be used to assign to an entry
 * of a {@link HashLangObject} or an {@link ArrayLangObject}.
 *
 * <br>
 * <br>
 *
 * The argument array is guaranteed to contain exactly three entries, the
 * {@link ALangObject} <code>property<code>, the {@link BooleanLangObject}
 * <code>accessedViaDot</dot>, and the <code>value</code> {@link ALangObject}
 * to be assigned.
 *
 * @author madgaksha
 *
 * @param <T>
 */
public abstract class GenericAttrAssigner<T extends ALangObject> implements IAttrAssignerFunction<T> {
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
	public final static IAttrAssignerFunction<StringLangObject> STRING = new GenericAttrAssigner<StringLangObject>(ELangObjectType.STRING,
			"genericAttrAssignerString", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
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
	public final static IAttrAssignerFunction<ArrayLangObject> ARRAY = new GenericAttrAssigner<ArrayLangObject>(ELangObjectType.ARRAY,
			"genericAttrAssignerArray", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
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
			thisContext.set(index < 0 ? index + len : index, args[2]);
			return thisContext;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IAttrAssignerFunction<BooleanLangObject> BOOLEAN = new GenericAttrAssigner<BooleanLangObject>(ELangObjectType.BOOLEAN,
			"genericAttrAssignerBoolean", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IAttrAssignerFunction<ExceptionLangObject> EXCEPTION = new GenericAttrAssigner<ExceptionLangObject>(
			ELangObjectType.EXCEPTION, "genericAttrAssignerException", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IAttrAssignerFunction<RegexLangObject> REGEX = new GenericAttrAssigner<RegexLangObject>(ELangObjectType.REGEX,
			"genericAttrAssignerRegex", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IAttrAssignerFunction<FunctionLangObject> FUNCTION = new GenericAttrAssigner<FunctionLangObject>(
			ELangObjectType.FUNCTION, "genericAttrAssignerFunction", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IAttrAssignerFunction<NumberLangObject> NUMBER = new GenericAttrAssigner<NumberLangObject>(ELangObjectType.NUMBER,
			"genericAttrAssignerFunction", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
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
	public final static IAttrAssignerFunction<HashLangObject> HASH = new GenericAttrAssigner<HashLangObject>(ELangObjectType.HASH,
			"genericAttrAssignerHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			thisContext.put(args[0], args[2]);
			return thisContext;
		}
	};

	private GenericAttrAssigner(@Nonnull final ILangObjectClass type, @Nonnull final String name,
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
