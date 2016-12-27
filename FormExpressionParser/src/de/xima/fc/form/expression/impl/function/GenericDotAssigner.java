package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IGenericDotAssignerFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
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
	 * @param key
	 *            {@link ALangObject} The key. When not specified,
	 *            {@link NullLangObject} is assumed.
	 * @return {@link ALangObject} The object that is mapped to the given key.
	 * @NullLangObject When the map does not contain any value for the key, or
	 *                 the key is mapped to {@link NullLangObject}. Use
	 *                 {@link EDotAccessorHash#contains} to check.
	 */
	public final static IGenericDotAssignerFunction<HashLangObject> HASH = new GenericDotAssigner<HashLangObject>(ELangObjectClass.HASH,
			"genericBracketAssignerHash", false, "key") { //$NON-NLS-1$ //$NON-NLS-2$
		private static final long serialVersionUID = 1L;
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			thisContext.put(args[0].coerceString(ec), args[1]);
			return thisContext;
		}

		@Override
		public IVariableType getValueType(final IVariableType thisContext, final String property) {
			return thisContext.getGeneric(1);
		}

		@Override
		public boolean isHandlingProperty(final IVariableType thisContext, final String property) {
			return thisContext.getGeneric(0).isAssignableFrom(SimpleVariableType.STRING);
		}

		@Override
		public boolean isHandlingProperty(final String property) {
			return true;
		}

		@Override
		public ILangObjectClass getValueClass() {
			return ELangObjectClass.OBJECT;
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