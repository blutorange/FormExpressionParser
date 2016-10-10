package de.xima.fc.form.expression.impl.function;

import java.util.Locale;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
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
	length(Impl.length),;

	private final FunctionLangObject impl;
	private final boolean evalImmediately;
	private final String[] argList;
	private final String varArgsName;

	private EAttrAccessorString(final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		varArgsName = impl.getVarArgsName();
		evalImmediately = argList.length == 0;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		if (!evalImmediately)
			return impl;
		return impl.functionValue().evaluate(ec, thisContext, args);
	}

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
		toUpperCase(null, "locale") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.toUpperCase(
						args.length == 0 ? Locale.ROOT : Locale.forLanguageTag(args[0].coerceString(ec).stringValue()));
			}
		},
		toLowerCase(null, "locale") {
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
		};

		private String[] argList;
		private String optionalArgumentsName;

		private Impl(final String optArg, final String... argList) {
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
