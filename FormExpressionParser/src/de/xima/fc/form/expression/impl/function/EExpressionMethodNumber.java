package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.iface.context.IMethod2Function;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.NumberLangObject;

public enum EExpressionMethodNumber implements IMethod2Function<NumberLangObject> {
	/**
	 * @return {@link NumberLangObject} This number.
	 */
	PLUS_UNARY(EMethod.PLUS_UNARY, Impl.IDENTITY),
	/**
	 * @return {@link NumberLangObject}. The additive inverse of this number.
	 */
	DASH_UNARY(EMethod.DASH_UNARY, Impl.NEGATE),
	/**
	 * @param summand {@link NumberLangObject} The number to be added to this number.
	 * @return {@link NumberLangObject}. The arithmetic sum of this number and the argument.
	 */
	PLUS(EMethod.PLUS, Impl.ADD),
	/**
	 * @return {@link NumberLangObject}. This number increased by one.
	 */
	DOUBLE_PLUS(EMethod.DOUBLE_PLUS_PREFIX, Impl.INCREMENT),
	/**
	 * @param subtrahend {@link NumberLangObject} The number to be subtracted to this number.
	 * @return {@link NumberLangObject}. The arithmetic difference of this number and the argument.
	 */
	DASH(EMethod.DASH, Impl.SUBTRACT),
	/**
	 * @return {@link NumberLangObject}. This number decreased by one.
	 */
	DOUBLE_DASH(EMethod.DOUBLE_DASH_PREFIX, Impl.DECREMENT),	/**
	 * @param multiplicand {@link NumberLangObject}. The number to be multiplied to this number.
	 * @return The arithmetic product of this number and the argument.
	 */
	STAR(EMethod.STAR, Impl.MULTIPLY),
	/**
	 * @param dividend {@link NumberLangObject}. The number to through which to divide this number.
	 * @return The ration of this number divided by the argument.
	 */
	SLASH(EMethod.SLASH, Impl.DIVIDE),
	/**
	 * @param operand Number for the modulo division.
	 * @return The modulo of this number by the argument, eg. 5%3=2 or -3%5=2.
	 */
	PERCENT(EMethod.PERCENT, Impl.MODULO),
	;
	@Nonnull private final EMethod method;
	@Nonnull private final IFunction<NumberLangObject> function;
	private EExpressionMethodNumber(@Nonnull final EMethod method, @Nonnull final IFunction<NumberLangObject> function) {
		this.method = method;
		this.function = function;
	}
	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IFunction<NumberLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IFunction<NumberLangObject> {
		IDENTITY(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext;
			}
		},
		ADD(false, "summand") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.add(args[0].coerceNumber(ec));
			}
		},
		SUBTRACT(false, "subtrahend") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.subtract(args[0].coerceNumber(ec));
			}
		},
		INCREMENT(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.add(NumberLangObject.getOneInstance());
			}
		},
		DECREMENT(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.subtract(NumberLangObject.getOneInstance());
			}
		},
		NEGATE(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.negate();
			}
		},
		MULTIPLY(false, "multiplicand") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.multiply(args[0].coerceNumber(ec));
			}
		},
		DIVIDE(false, "dividend") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.divide(args[0].coerceNumber(ec));
			}
		},
		MODULO(false,"operand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.modulo(args[0].coerceNumber(ec));
			}
		},
		;

		@Nonnull private final String[] argList;
		private boolean hasVarArgs;

		private Impl(final boolean hasVarArgs, @Nonnull final String... argList) {
			this.argList = argList;
			this.hasVarArgs = hasVarArgs;
		}

		@Override
		public boolean hasVarArgs() {
			return hasVarArgs;
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
		public Type getThisContextType() {
			return Type.NUMBER;
		}

		@Override
		public Node getNode() {
			return null;
		}
	}
}
