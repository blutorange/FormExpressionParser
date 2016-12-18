package de.xima.fc.form.expression.impl.function;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.ArrayIndexOutOfBoundsException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IGenericBracketAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
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
			ELangObjectClass.STRING, "genericBracketAccessorString", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
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

		@Override
		public IVariableType getPropertyType(final IVariableType thisContext) {
			return SimpleVariableType.NUMBER;
		}

		@Override
		public IVariableType getReturnType(final IVariableType thisContext) {
			return SimpleVariableType.STRING;
		}

		@Override
		public ILangObjectClass getPropertyClass() {
			return ELangObjectClass.NUMBER;
		}

		@Override
		public ILangObjectClass getReturnClass() {
			return ELangObjectClass.STRING;
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
			ELangObjectClass.ARRAY, "genericBracketAccessorArray", false, "index") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException, MathException {
			final int index = args[0].coerceNumber(ec).intValue(ec);
			final int len = thisContext.length();
			if (index >= len || index < -len)
				throw new ArrayIndexOutOfBoundsException(thisContext, index, ec);
			return thisContext.get(index < 0 ? index + len : index);
		}

		@Override
		public IVariableType getPropertyType(final IVariableType thisContext) {
			return SimpleVariableType.NUMBER;
		}

		@Override
		public IVariableType getReturnType(final IVariableType thisContext) {
			return thisContext.getGeneric(0);
		}

		@Override
		public ILangObjectClass getPropertyClass() {
			return ELangObjectClass.NUMBER;
		}

		@Override
		public ILangObjectClass getReturnClass() {
			return ELangObjectClass.OBJECT;
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
			ELangObjectClass.HASH, "genericBracketAccessorHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			return thisContext.get(args[0]);
		}

		@Override
		public IVariableType getPropertyType(final IVariableType thisContext) {
			return thisContext.getGeneric(0);
		}

		@Override
		public IVariableType getReturnType(final IVariableType thisContext) {
			return thisContext.getGeneric(1);
		}

		@Override
		public ILangObjectClass getPropertyClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ILangObjectClass getReturnClass() {
			return ELangObjectClass.OBJECT;
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