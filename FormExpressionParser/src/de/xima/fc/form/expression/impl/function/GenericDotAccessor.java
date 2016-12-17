package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAccessorException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IGenericDotAccessorFunction;
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
 * A set of generic attribute accessors for elements. Each object has got a
 * predefined set of named attributes, eg. <code>length</code> for
 * {@link ArrayLangObject}s or <code>message</code> for
 * {@link ExceptionLangObject}s. When none of those match, these generic
 * functions will be attempted. For example, they can be used to access an
 * entry of a {@link HashLangObject}, <code>hash.key</code>.
 * </p><p>
 * The argument array is guaranteed to contain exactly one entry, the
 * {@link StringLangObject} <code>property<code>.
 * </p>
 * @author madgaksha
 *
 * @param <T> Type of the language object for the bracket accessor.
 */
@ParametersAreNonnullByDefault
public abstract class GenericDotAccessor<T extends ALangObject> implements IGenericDotAccessorFunction<T> {
	private static final long serialVersionUID = 1L;

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericDotAccessorFunction<StringLangObject> STRING = new GenericDotAccessor<StringLangObject>(
			ELangObjectType.STRING, "genericDotAccessorString", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].inspect(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getDotAccessorReturnType(final IVariableType thisContext,
				final String property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericDotAccessorFunction<ArrayLangObject> ARRAY = new GenericDotAccessor<ArrayLangObject>(
			ELangObjectType.ARRAY, "genericBracketAccessorArray", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			throw new NoSuchAttrAccessorException(args[0].coerceString(ec).stringValue(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getDotAccessorReturnType(final IVariableType thisContext,
				final String property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericDotAccessorFunction<BooleanLangObject> BOOLEAN = new GenericDotAccessor<BooleanLangObject>(
			ELangObjectType.BOOLEAN, "genericBracketAccessorBoolean", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].coerceString(ec).stringValue(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getDotAccessorReturnType(final IVariableType thisContext,
				final String property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericDotAccessorFunction<ExceptionLangObject> EXCEPTION = new GenericDotAccessor<ExceptionLangObject>(
			ELangObjectType.EXCEPTION, "genericBracketAccessorException", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].coerceString(ec).stringValue(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getDotAccessorReturnType(final IVariableType thisContext,
				final String property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericDotAccessorFunction<RegexLangObject> REGEX = new GenericDotAccessor<RegexLangObject>(
			ELangObjectType.REGEX, "genericBracketAccessorRegex", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].coerceString(ec).stringValue(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getDotAccessorReturnType(final IVariableType thisContext,
				final String property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericDotAccessorFunction<FunctionLangObject> FUNCTION = new GenericDotAccessor<FunctionLangObject>(
			ELangObjectType.FUNCTION, "genericBracketAccessorFunction", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].coerceString(ec).stringValue(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getDotAccessorReturnType(final IVariableType thisContext,
				final String property) {
			return null;
		}
	};

	/**
	 * @throws NoSuchAttrAccessorException
	 *             No generic attribute accessors.
	 */
	public final static IGenericDotAccessorFunction<NumberLangObject> NUMBER = new GenericDotAccessor<NumberLangObject>(
			ELangObjectType.NUMBER, "genericBracketAccessorNumber", false) { //$NON-NLS-1$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NoSuchAttrAccessorException(args[0].coerceString(ec).stringValue(), thisContext, ec);
		}

		@Nullable
		@Override
		public IVariableType getDotAccessorReturnType(final IVariableType thisContext,
				final String property) {
			return null;
		}
	};

	/**
	 * @param key {@link ALangObject} The key. Hash must be compatible with string keys.
	 * @return {@link ALangObject} The object that is mapped to the given key. Its type is the second generics argument.
	 * @NullLangObject When the map does not contain any value for the key, or
	 *                 the key is mapped to {@link NullLangObject}. Use
	 *                 {@link EDotAccessorHash#contains} to check.
	 */
	public final static IGenericDotAccessorFunction<HashLangObject> HASH = new GenericDotAccessor<HashLangObject>(
			ELangObjectType.HASH, "genericBracketAccessorHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			return thisContext.get(args[0].coerceString(ec));
		}

		@Nullable
		@Override
		public IVariableType getDotAccessorReturnType(final IVariableType thisContext,
				final String property) {
			// Property type must be compatible with the first generics argument,
			// ie. it must be a hash<string, ?>
			if (!thisContext.getGeneric(0).isAssignableFrom(SimpleVariableType.STRING))
				return null;
			return thisContext.getGeneric(1);
		}
	};

	private final String name;
	private final String[] argList;
	private final boolean hasVarArgs;
	private final ILangObjectClass type;

	private GenericDotAccessor(final ILangObjectClass type, final String name, final boolean hasVarArgs,
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
