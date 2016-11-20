package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAccessorRegex implements IFunction<RegexLangObject> {
	/**
	 * @param string {@link StringLangObject} String to match. When not given, defaults to the empty string.
	 * @return {@link BooleanLangObject}. Whether this regex matches the string.
	 */
	matches(Impl.matches),
	;

	@Nonnull private final FunctionLangObject impl;
	private final boolean evalImmediately;
	@Nonnull private final String[] argList;
	private final String varArgsName;

	private EAttrAccessorRegex(@Nonnull final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		varArgsName = impl.getVarArgsName();
		evalImmediately = argList.length == 0;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
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
		return Type.REGEX;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public String getVarArgsName() {
		return varArgsName;
	}

	private static enum Impl implements IFunction<RegexLangObject> {
		matches(null, "string") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final String input = args.length == 0 ? StringUtils.EMPTY : args[0].coerceString(ec).stringValue();
				return BooleanLangObject.create(thisContext.patternValue().matcher(input).matches());
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
			return Type.REGEX;
		}

		@Override
		public Node getNode() {
			return null;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}