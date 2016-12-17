package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IDotAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EDotAccessorNumber implements IDotAccessorFunction<NumberLangObject> {
	/**
	 * @return {@link NumberLangObject}. The sine of this number.
	 */
	sin(Impl.sin),
	/**
	 * @return {@link BooleanLangObject} True iff this number is <code>NaN</code>.
	 */
	nan(Impl.nan),
	/**
	 * @return {@link BooleanLangObject} True iff this number is <code>Infinity</code> or <code>-Infinity</code>.
	 */
	infinite(Impl.infinite),
	/**
	 * @return {@link BooleanLangObject} True iff this number is neither <code>NaN</code> nor <code>Infinity</code> or <code>-Infinity</code>.
	 */
	finite(Impl.finite),
	;

	private final FunctionLangObject func;
	private final Impl impl;
	private final boolean deferEvaluation;

	private EDotAccessorNumber(final Impl impl) {
		this.func = FunctionLangObject.create(impl);
		this.impl = impl;
		deferEvaluation = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
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
		return ELangObjectType.NUMBER;
	}

	@Override
	public boolean hasVarArgs() {
		return impl.hasVarArgs;
	}

	@Nullable
	@Override
	public IVariableType getDotAccessorReturnType(final IVariableType thisContext) {
		return impl.getDotAccessorReturnType(thisContext);
	}

	private static enum Impl implements IDotAccessorFunction<NumberLangObject> {
		sin(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.sin();
			}

			@Nullable
			@Override
			public IVariableType getDotAccessorReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}
		},
		nan(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.isNaN());
			}

			@Nullable
			@Override
			public IVariableType getDotAccessorReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}
		},
		infinite(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.isInfinite());
			}

			@Nullable
			@Override
			public IVariableType getDotAccessorReturnType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
		},
		finite(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.isFinite());
			}

			@Nullable
			@Override
			public IVariableType getDotAccessorReturnType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
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
			return ELangObjectType.NUMBER;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}
