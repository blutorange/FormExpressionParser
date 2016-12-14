package de.xima.fc.form.expression.impl.function;

import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IAttrAccessorFunction;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EAttrAccessorRegex implements IAttrAccessorFunction<RegexLangObject> {
	/**
	 * @param string {@link StringLangObject} String to match. When not given, defaults to the empty string.
	 * @return {@link BooleanLangObject}. Whether this regex matches the string.
	 */
	matches(Impl.matches),
	;

	private final FunctionLangObject func;
	private final Impl impl;
	private final boolean deferEvaluation;

	private EAttrAccessorRegex(final Impl impl) {
		this.func = FunctionLangObject.create(impl);
		this.impl = impl;
		deferEvaluation = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		return deferEvaluation ? func : func.functionValue().evaluate(ec, thisContext, args);
	}

	@SuppressWarnings("null")
	@Override
	public String getDeclaredName() {
		return toString();
	}

	@SuppressWarnings("null")
	@Override
	public String getDeclaredArgument(final int i) {
		return impl.argList[i];
	}

	@Override
	public int getDeclaredArgumentCount() {
		return impl.argList.length;
	}

	@Override
	public ILangObjectClass getThisContextType() {
		return ELangObjectType.REGEX;
	}

	@Override
	public boolean hasVarArgs() {
		return impl.hasVarArgs;
	}
	private static enum Impl implements IAttrAccessorFunction<RegexLangObject> {
		matches(false, "string") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final String input = args.length == 0 ? StringUtils.EMPTY : args[0].coerceString(ec).stringValue();
				return BooleanLangObject.create(thisContext.patternValue().matcher(input).matches());
			}
		},
		;

		private String[] argList;
		private boolean hasVarArgs;

		private Impl(final boolean hasVarArgs, final String... argList) {
			NullUtil.checkItemsNotNull(argList);
			this.argList = argList;
			this.hasVarArgs = hasVarArgs;
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

		@SuppressWarnings("null")
		@Override
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public ILangObjectClass getThisContextType() {
			return ELangObjectType.REGEX;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}