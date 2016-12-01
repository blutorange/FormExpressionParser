package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.ArrayIndexOutOfBoundsException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAccessorException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAssignerException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
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
 * {@link ALangObject}
 * <code>property<code>, the {@link BooleanLangObject} <code>accessedViaDot</dot>,
 * and the <code>value</code> {@link ALangObject} to be assigned.
 *
 * @author madgaksha
 *
 * @param <T>
 */
public abstract class GenericAttrAssigner<T extends ALangObject> implements IFunction<T> {
	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IFunction<StringLangObject> STRING = new GenericAttrAssigner<StringLangObject>(Type.STRING,
			"genericAttrAssignerString", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
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
	public final static IFunction<ArrayLangObject> ARRAY = new GenericAttrAssigner<ArrayLangObject>(Type.ARRAY,
			"genericAttrAssignerArray", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
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
	public final static IFunction<BooleanLangObject> BOOLEAN = new GenericAttrAssigner<BooleanLangObject>(Type.BOOLEAN,
			"genericAttrAssignerBoolean", false) { //$NON-NLS-1$
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
	public final static IFunction<ExceptionLangObject> EXCEPTION = new GenericAttrAssigner<ExceptionLangObject>(
			Type.EXCEPTION, "genericAttrAssignerException", false) { //$NON-NLS-1$
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
	public final static IFunction<RegexLangObject> REGEX = new GenericAttrAssigner<RegexLangObject>(Type.REGEX,
			"genericAttrAssignerRegex", false) { //$NON-NLS-1$
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
	public final static IFunction<FunctionLangObject> FUNCTION = new GenericAttrAssigner<FunctionLangObject>(
			Type.FUNCTION, "genericAttrAssignerFunction", false) { //$NON-NLS-1$
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
	public final static IFunction<NumberLangObject> NUMBER = new GenericAttrAssigner<NumberLangObject>(Type.NUMBER,
			"genericAttrAssignerFunction", false) { //$NON-NLS-1$
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
	public final static IFunction<HashLangObject> HASH = new GenericAttrAssigner<HashLangObject>(Type.HASH,
			"genericAttrAssignerHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			thisContext.put(args[0], args[2]);
			return thisContext;
		}
	};

	@Nonnull
	private final String name;
	@Nonnull
	private final String[] argList;
	private final boolean hasVarArgs;
	@Nonnull
	private final Type type;

	private GenericAttrAssigner(@Nonnull final Type type, @Nonnull final String name,
			final boolean hasVarArgs, @Nonnull final String... argList) {
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

	@Override
	public String[] getDeclaredArgumentList() {
		return argList;
	}

	@Override
	public int getDeclaredArgumentCount() {
		return argList.length;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public Type getThisContextType() {
		return type;
	}

	@Override
	public abstract ALangObject evaluate(IEvaluationContext ec, T thisContext, ALangObject... args)
			throws EvaluationException;
}
