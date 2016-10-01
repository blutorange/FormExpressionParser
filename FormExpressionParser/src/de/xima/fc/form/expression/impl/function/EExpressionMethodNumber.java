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
	 * @param summand {@link NumberLangObject} The number to be added to this number.
	 * @return {@link NumberLangObject}. The arithmetic sum of this number and the argument.
	 */
	PLUS(EMethod.PLUS, Impl.PLUS),
	/**
	 * @param summand {@link NumberLangObject} The number to be subtracted to this number.
	 * @return {@link NumberLangObject}. The arithmetic difference of this number and the argument.
	 */
	DASH(EMethod.DASH, Impl.DASH),
	/**
	 * @param multiplicand {@link NumberLangObject}. The number to be multiplied to this number.
	 * @return The arithmetic product of this number and the argument.
	 */
	STAR(EMethod.STAR, Impl.STAR),
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this object is of the same {@link Type} as the argument and is logically equivalent.
	 */
	DOUBLE_EQUAL(EMethod.DOUBLE_EQUAL, Impl.DOUBLE_EQUAL),
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this number is numerically equivalent to the argument.
	 */
	TRIPLE_EQUAL(EMethod.TRIPLE_EQUAL, Impl.DOUBLE_EQUAL),
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
		PLUS(null, "summand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.add(args[0].coerceNumber(ec));
			}
		},
		DASH(null, "subtrahend") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.subtract(args[0].coerceNumber(ec));
			}
		},
		STAR(null, "multiplicand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.multiply(args[0].coerceNumber(ec));
			}
		},
		DOUBLE_EQUAL(null, "comparand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.equals(args[0]));
			}},
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
