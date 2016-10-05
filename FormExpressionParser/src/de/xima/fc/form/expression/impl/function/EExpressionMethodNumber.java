package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.IMethod2Function;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.BooleanLangObject;
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
	 * @param subtrahend {@link NumberLangObject} The number to be subtracted to this number.
	 * @return {@link NumberLangObject}. The arithmetic difference of this number and the argument.
	 */
	DASH(EMethod.DASH, Impl.SUBTRACT),
	/**
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
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this object is of the same {@link Type} as the argument and is logically equivalent.
	 */
	DOUBLE_EQUAL(EMethod.DOUBLE_EQUAL, Impl.EQUALITY),
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this number is numerically equivalent to the argument.
	 */
	TRIPLE_EQUAL(EMethod.TRIPLE_EQUAL, Impl.EQUALITY),
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. False iff this object is of the same {@link Type} as the argument and is logically equivalent.
	 */
	EXCLAMATION_EQUAL(EMethod.EXCLAMATION_EQUAL, Impl.INEQUALITY),
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. False iff this object is of the same {@link Type} as the argument and is logically equivalent.
	 */
	EXCLAMATION_DOUBLE_EQUAL(EMethod.EXCLAMATION_DOUBLE_EQUAL, Impl.INEQUALITY),
	/**
	 * @param comparand {@link NumberLangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this number is smaller than the argument number.
	 */
	ANGLE_OPEN(EMethod.ANGLE_OPEN, Impl.SMALLER),
	/**
	 * @param comparand {@link NumberLangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this number is smaller than or equal to the argument number.
	 */
	ANGLE_OPEN_EQUAL(EMethod.ANGLE_OPEN_EQUAL, Impl.SMALLER_OR_EQUAL),
	/**
	 * @param comparand {@link NumberLangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this number is greater than the argument number.
	 */
	ANGLE_CLOSE(EMethod.ANGLE_CLOSE, Impl.GREATER),
	/**
	 * @param comparand {@link NumberLangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this number is greater than or equal to the argument number.
	 */
	ANGLE_CLOSE_EQUAL(EMethod.ANGLE_CLOSE_EQUAL, Impl.GREATER_OR_EQUAL),

	;
	private final EMethod method;
	private final IFunction<NumberLangObject> function;
	private EExpressionMethodNumber(final EMethod method, final IFunction<NumberLangObject> function) {
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
		IDENTITY(null) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext;
			}
		},
		ADD(null, "summand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.add(args[0].coerceNumber(ec));
			}
		},
		SUBTRACT(null, "subtrahend") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.subtract(args[0].coerceNumber(ec));
			}
		},
		NEGATE(null) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.negate();
			}
		},
		MULTIPLY(null, "multiplicand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.multiply(args[0].coerceNumber(ec));
			}
		},
		DIVIDE(null, "dividend") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.divide(args[0].coerceNumber(ec));
			}
		},
		MODULO(null,"operand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.modulo(args[0].coerceNumber(ec));
			}
		},
		EQUALITY(null, "comparand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.equals(args[0]));
			}
		},
		INEQUALITY(null, "comparand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(!thisContext.equals(args[0]));
			}
		},
		SMALLER(null, "comparand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.smaller(args[0].coerceNumber(ec)));
			}
		},
		SMALLER_OR_EQUAL(null, "comparand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.smallerOrEqual(args[0].coerceNumber(ec)));
			}
		},
		GREATER(null, "comparand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.greater(args[0].coerceNumber(ec)));
			}
		},
		GREATER_OR_EQUAL(null, "comparand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.greaterOrEqual(args[0].coerceNumber(ec)));
			}
		},
		;

		private final String[] argList;
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
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public String[] getDeclaredArgumentList() {
			return argList;
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
