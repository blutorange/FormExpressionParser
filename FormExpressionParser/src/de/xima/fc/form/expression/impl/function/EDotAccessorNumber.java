package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IDotAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
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

	@Nullable private FunctionLangObject func;
	private final Impl impl;

	private EDotAccessorNumber(final Impl impl) {
		this.impl = impl;
		func = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs ? null : FunctionLangObject.createWithoutClosure(impl);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		if (func != null)
			return func.bind(thisContext, ec).evaluate(ec);
		return FunctionLangObject.createWithoutClosure(impl).bind(thisContext, ec);
	}

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
		return ELangObjectClass.NUMBER;
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
	public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
		return impl.getReturnType(thisContext, dotGenerics);
	}

	@Override
	public boolean supportsGenerics(final IVariableType[] dotGenerics) {
		return impl.supportsGenerics(dotGenerics);
	}

	private static enum Impl implements IDotAccessorFunction<NumberLangObject> {
		sin(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.sin();
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		nan(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.isNaN());
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		infinite(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.isInfinite());
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.BOOLEAN;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.BOOLEAN;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		finite(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.isFinite());
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType dotGenerics[]) {
				return SimpleVariableType.BOOLEAN;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.BOOLEAN;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		;

		protected String[] argList;
		protected boolean hasVarArgs;

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

		@Override
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public ILangObjectClass getThisContextType() {
			return ELangObjectClass.NUMBER;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}
