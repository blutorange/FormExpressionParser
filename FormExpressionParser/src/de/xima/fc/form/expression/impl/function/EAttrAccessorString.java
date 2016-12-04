package de.xima.fc.form.expression.impl.function;

import java.util.Locale;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAccessorString implements IFunction<StringLangObject> {
	/**
     * Uses the the English-like {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The upper-case version of the string.
	 */
	toUpperCase(Impl.toUpperCase),
	/**
	 * @param locale
	 *            {@link StringLangObject} (optional). The (IETF BCP 47) name of
	 *            the locale to be used for the conversion. When the argument
	 *            is {@link NullLangObject}, defaults to the English-like
	 *            {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The upper-case version of the string.
	 */
	toLocaleUpperCase(Impl.toLocaleUpperCase),
	/**
	 * @param locale Uses the English-like {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The lower-case version of the string.
	 */
	toLowerCase(Impl.toLowerCase),
	/**
	 * @param locale
	 *            {@link StringLangObject} (optional). The (IETF BCP 47) name of
	 *            the locale to be used for the conversion. When the argument
	 *            is {@link NullLangObject}, defaults to the English-like
	 *            {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The lower-case version of the string.
	 */
	toLocaleLowerCase(Impl.toLocaleLowerCase),
	/**
	 * @return {@link NumberLangObject}. The length of this string, >=0.
	 */
	length(Impl.length),
	;

	@Nonnull private final FunctionLangObject impl;
	private final boolean evalImmediately;
	@Nonnull private final String[] argList;
	private final boolean hasVarArgs;

	private EAttrAccessorString(@Nonnull final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		hasVarArgs = impl.hasVarArgs();
		evalImmediately = argList.length == 0 && !hasVarArgs;
	}

	@Override
	public ALangObject evaluate(@Nonnull final IEvaluationContext ec, @Nonnull final StringLangObject thisContext,
			@Nonnull final ALangObject... args) throws EvaluationException {
		if (!evalImmediately)
			return impl;
		return impl.functionValue().evaluate(ec, thisContext, args);
	}

	@SuppressWarnings("null")
	@Override
	public String getDeclaredName() {
		return toString();
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
	public ELangObjectType getThisContextType() {
		return ELangObjectType.STRING;
	}

	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}

	@Override
	public Node getNode() {
		return null;
	}

	private static enum Impl implements IFunction<StringLangObject> {
		toLocaleUpperCase(false, "locale") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.toUpperCase(args[0].isNull() ? Locale.ROOT : Locale.forLanguageTag(args[0].coerceString(ec).stringValue()));
			}
		},
		toLocaleLowerCase(false, "locale") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.toLowerCase(args[0].isNull() ? Locale.ROOT : Locale.forLanguageTag(args[0].coerceString(ec).stringValue()));
			}
		},
		toUpperCase(false) { 
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.toUpperCase(Locale.ROOT);
			}
		},
		toLowerCase(false) { 
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.toLowerCase(Locale.ROOT);
			}
		},
		length(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return NumberLangObject.create(thisContext.stringValue().length());
			}
		},
		;

		@Nonnull private String[] argList;
		private boolean hasVarArgs;

		private Impl(final boolean hasVarArgs, @Nonnull final String... argList) {
			this.argList = argList;
			this.hasVarArgs = hasVarArgs;
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

		@SuppressWarnings("null")
		@Override
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public ELangObjectType getThisContextType() {
			return ELangObjectType.STRING;
		}

		@Override
		public Node getNode() {
			return null;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}
