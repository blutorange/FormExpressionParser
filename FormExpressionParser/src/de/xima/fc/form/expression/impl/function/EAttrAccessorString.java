package de.xima.fc.form.expression.impl.function;

import java.util.Locale;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAccessorString implements IFunction<StringLangObject> {
	/**
	 * @param locale
	 *            {@link StringLangObject} (optional). The (IETF BCP 47) name of
	 *            the locale to be used for the conversion. Defaults to the
	 *            English-like {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The upper-case version of the string.
	 */
	toUpperCase(Impl.toUpperCase),
	/**
	 * @param locale
	 *            {@link StringLangObject} (optional). The (IETF BCP 47) name of
	 *            the locale to be used for the conversion. Defaults to the
	 *            English-like {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The lower-case version of the string.
	 */
	toLowerCase(Impl.toLowerCase),
	/**
	 * @return {@link NumberLangObject}. The length of this string, >=0.
	 */
	length(Impl.length),
	;

	@Nonnull private final FunctionLangObject impl;
	private final boolean evalImmediately;
	@Nonnull private final String[] argList;
	private final String varArgsName;

	private EAttrAccessorString(@Nonnull final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		varArgsName = impl.getVarArgsName();
		evalImmediately = argList.length == 0;
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
	public Type getThisContextType() {
		return Type.STRING;
	}

	@Override
	public String getVarArgsName() {
		return varArgsName;
	}

	@Override
	public Node getNode() {
		return null;
	}

	private static enum Impl implements IFunction<StringLangObject> {
		toUpperCase(null, "locale") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.toUpperCase(
						args.length == 0 ? Locale.ROOT : Locale.forLanguageTag(args[0].coerceString(ec).stringValue()));
			}
		},
		toLowerCase(null, "locale") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.toLowerCase(
						args.length == 0 ? Locale.ROOT : Locale.forLanguageTag(args[0].coerceString(ec).stringValue()));
			}
		},
		length(null) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return NumberLangObject.create(thisContext.stringValue().length());
			}
		},
		;

		@Nonnull private String[] argList;
		private String optionalArgumentsName;

		private Impl(final String optArg, @Nonnull final String... argList) {
			this.argList = argList;
			this.optionalArgumentsName = optArg;
		}

		@Override
		public String getVarArgsName() {
			return optionalArgumentsName;
		}

		@Override
		public String[] getDeclaredArgumentList() {
			return argList;
		}

		@SuppressWarnings("null")
		@Override
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public Type getThisContextType() {
			return Type.STRING;
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
