package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAssignerException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IGenericDotAssignerFunction;
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
 * A set of generic attribute assigners for object. Each object has got a
 * predefined set of named attributes. When none of those match, these generic
 * functions will be called. For example, they can be used to assign to an key
 * of a {@link HashLangObject}, <code>hash.key = value</code>.
 * </p><p>
 * The argument array is guaranteed to contain exactly two entries, the
 * {@link StringLangObject} <code>property<code> and the<code>value</code>
 * {@link ALangObject} to be assigned.
 * </p>
 *
 * @author madgaksha
 *
 * @param <T> Type of the language object for the bracket accessor.
 */
public abstract class GenericDotAssigner<T extends ALangObject> implements IGenericDotAssignerFunction<T> {
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
	public final static IGenericDotAssignerFunction<StringLangObject> STRING = new GenericDotAssigner<StringLangObject>(ELangObjectType.STRING,
			"genericDotAssignerString", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericDotAssignerFunction<ArrayLangObject> ARRAY = new GenericDotAssigner<ArrayLangObject>(ELangObjectType.ARRAY,
			"genericBracketAssignerArray", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericDotAssignerFunction<BooleanLangObject> BOOLEAN = new GenericDotAssigner<BooleanLangObject>(ELangObjectType.BOOLEAN,
			"genericBracketAssignerBoolean", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericDotAssignerFunction<ExceptionLangObject> EXCEPTION = new GenericDotAssigner<ExceptionLangObject>(
			ELangObjectType.EXCEPTION, "genericBracketAssignerException", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericDotAssignerFunction<RegexLangObject> REGEX = new GenericDotAssigner<RegexLangObject>(ELangObjectType.REGEX,
			"genericBracketAssignerRegex", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericDotAssignerFunction<FunctionLangObject> FUNCTION = new GenericDotAssigner<FunctionLangObject>(
			ELangObjectType.FUNCTION, "genericBracketAssignerFunction", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
				final IVariableType value) {
			return false;
		}
	};

	/**
	 * @throws NoSuchAttrAssignerException.
	 *             No generic attribute assigners.
	 */
	public final static IGenericDotAssignerFunction<NumberLangObject> NUMBER = new GenericDotAssigner<NumberLangObject>(ELangObjectType.NUMBER,
			"genericBracketAssignerFunction", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAssignerException(args[0].inspect(), thisContext,
					args[1].coerceBoolean(ec).booleanValue(), ec);
		}

		@Override
		public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
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
	public final static IGenericDotAssignerFunction<HashLangObject> HASH = new GenericDotAssigner<HashLangObject>(ELangObjectType.HASH,
			"genericBracketAssignerHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			thisContext.put(args[0].coerceString(ec), args[1]);
			return thisContext;
		}

		@Override
		public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
				final IVariableType value) {
			// Property must be compatible with the first generics argument,
			// ie. a hash<string, ?>
			if (!thisContext.getGeneric(0).isAssignableFrom(SimpleVariableType.STRING))
				return false;
			// Value to be assigned must be compatible with the second
			// generics argument, eg. number for hash<string, object>
			return thisContext.getGeneric(1).isAssignableFrom(value);
		}
	};

	private GenericDotAssigner(@Nonnull final ILangObjectClass type, @Nonnull final String name,
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
