package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
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
	private final boolean hasVarArgs;

	private EAttrAccessorRegex(@Nonnull final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		hasVarArgs = impl.hasVarArgs();
		evalImmediately = argList.length == 0 && !hasVarArgs;
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
	public int getDeclaredArgumentCount() {
		return argList.length;
	}

	@Override
	public ELangObjectType getThisContextType() {
		return ELangObjectType.REGEX;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}

	private static enum Impl implements IFunction<RegexLangObject> {
		matches(false, "string") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final String input = args.length == 0 ? StringUtils.EMPTY : args[0].coerceString(ec).stringValue();
				return BooleanLangObject.create(thisContext.patternValue().matcher(input).matches());
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
			return ELangObjectType.REGEX;
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