package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.ArrayIndexOutOfBoundsException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IGenericBracketAssignerFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
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
	public final static IGenericBracketAssignerFunction<ArrayLangObject> ARRAY = new GenericBracketAssigner<ArrayLangObject>(ELangObjectClass.ARRAY,
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
		public IVariableType getPropertyType(final IVariableType thisContext) {
			return SimpleVariableType.NUMBER;
		}

		@Override
		public IVariableType getValueType(final IVariableType thisContext) {
			return thisContext.getGeneric(0);
		}

		@Override
		public ILangObjectClass getPropertyClass() {
			return ELangObjectClass.NUMBER;
		}

		@Override
		public ILangObjectClass getValueClass() {
			return ELangObjectClass.OBJECT;
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
	public final static IGenericBracketAssignerFunction<HashLangObject> HASH = new GenericBracketAssigner<HashLangObject>(ELangObjectClass.HASH,
			"genericBracketAssignerHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			thisContext.put(args[0], args[1]);
			return thisContext;
		}

		@Override
		public IVariableType getPropertyType(final IVariableType thisContext) {
			return thisContext.getGeneric(0);
		}

		@Override
		public IVariableType getValueType(final IVariableType thisContext) {
			return thisContext.getGeneric(1);
		}

		@Override
		public ILangObjectClass getPropertyClass() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public ILangObjectClass getValueClass() {
			return ELangObjectClass.OBJECT;
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