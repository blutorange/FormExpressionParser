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
	PLUS(EMethod.PLUS, Impl.PLUS),
	STAR(EMethod.STAR, Impl.STAR),
	DOUBLE_EQUAL(EMethod.DOUBLE_EQUAL, Impl.DOUBLE_EQUAL),
	TRIPLE_EQUAL(EMethod.TRIPLE_EQUAL, Impl.TRIPLE_EQUAL),
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
		/**
		 * @param summand {@link NumberLangObject} The number to be added to this number.
		 * @return {@link NumberLangObject}. The arithmetic sum of this number and the argument.
		 */
		PLUS("summand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.add(args[0].coerceNumber(ec));
			}
		},
		/**
		 * @param multiplicand {@link NumberLangObject}. The number to be multiplied to this number.
		 * @return The arithmetic product of this number and the argument.
		 */
		STAR("multiplicand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.multiply(args[0].coerceNumber(ec));
			}
		},
		/**
		 * @param comparand {@link ALangObject}. Object to compare this object to.
		 * @return {@link BooleanLangObject}. True iff this object is of the same {@link Type} as the argument and is logically equivalent.
		 */
		DOUBLE_EQUAL("comparand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.equals(args[0]));
			}},
		/**
		 * @param comparand {@link ALangObject}. Object to compare this object to.
		 * @return {@link BooleanLangObject}. True iff this object is the same object as the argument.
		 */
		TRIPLE_EQUAL("comparand"){
				@Override
				public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
						throws EvaluationException {
					return BooleanLangObject.create(thisContext == args[0]);
				}},
		;

		private final String[] argList;

		private Impl(final String... argList) {
			this.argList = argList;
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
