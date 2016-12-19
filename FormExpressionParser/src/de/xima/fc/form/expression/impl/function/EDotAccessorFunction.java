package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IDotAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EDotAccessorFunction implements IDotAccessorFunction<FunctionLangObject> {
	/**
	 * @return {@link StringLangObject}. The declared name of this function. The
	 *         empty string when an anonymous function.
	 */
	name(Impl.name),
	;

	@Nullable private FunctionLangObject func;
	private final Impl impl;

	private EDotAccessorFunction(final Impl impl) {
		this.impl = impl;
		func = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs ? null : FunctionLangObject.create(impl);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		if (func != null)
			return func.bind(thisContext, ec).evaluate(ec, args);
		return FunctionLangObject.create(impl).bind(thisContext, ec);
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
		return ELangObjectClass.FUNCTION;
	}

	@Override
	public boolean hasVarArgs() {
		return impl.hasVarArgs;
	}

	@Override
	public ILangObjectClass getReturnClass() {
		return impl.getReturnClass();
	}

	@Override
	public IVariableType getReturnType(final IVariableType thisContext) {
		return impl.getReturnType(thisContext);
	}

	private static enum Impl implements IDotAccessorFunction<FunctionLangObject> {
		name(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return StringLangObject.create(thisContext.getDeclaredName());
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.STRING;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.STRING;
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
			return ELangObjectClass.FUNCTION;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}