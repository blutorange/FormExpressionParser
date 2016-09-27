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

public enum EExpressionMethodBoolean implements IMethod2Function<BooleanLangObject> {
	DOUBLE_BAR(EMethod.DOUBLE_BAR, Impl.DOUBLE_BAR),
	DOUBLE_AMPERSAND(EMethod.DOUBLE_AMPERSAND, Impl.DOUBLE_AMPERSAND),
	CIRCUMFLEX(EMethod.CIRCUMFLEX, Impl.CIRCUMFLEX),
	EXCLAMATION(EMethod.EXCLAMATION, Impl.EXCLAMATION),
	DOUBLE_EQUAL(EMethod.DOUBLE_EQUAL, Impl.DOUBLE_EQUAL),
	TRIPLE_EQUAL(EMethod.TRIPLE_EQUAL, Impl.TRIPLE_EQUAL),
	;
	private final EMethod method;
	private final IFunction<BooleanLangObject> function;

	private EExpressionMethodBoolean(final EMethod method, final IFunction<BooleanLangObject> function) {
		this.method = method;
		this.function = function;
	}

	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IFunction<BooleanLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IFunction<BooleanLangObject> {
		/**
		 * @param orOperand {@link BooleanLangObject}. Argument for the OR.
		 * @return {@link BooleanLangObject}. The result of the logical OR disjunction between this boolean and the argument.
		 */
		DOUBLE_BAR("orOperand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.or(args[0].coerceBoolean(ec));
			}
		},
		/**
		 * @param andOperand {@link BooleanLangObject}. Argument for the AND.
		 * @return {@link BooleanLangObject}. The result of the logical AND conjunction between this boolean and the argument.
		 */
		DOUBLE_AMPERSAND("andOperand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.and(args[0].coerceBoolean(ec));
			}
		},
		/**
		 * @param xorOperand {@link BooleanLangObject}. Argument for the XOR.
		 * @return {@link BooleanLangObject}. The result of the logical XOR exclusive disjunction between this boolean and the argument.
		 */
		CIRCUMFLEX("xorOperand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.xor(args[0].coerceBoolean(ec));
			}
		},
		/**
		 * @return {@link BooleanLangObject}. The logical negation of this boolean.
		 */
		EXCLAMATION(){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.not();
			}
		},
		/**
		 * @param comparand {@link ALangObject}. Object to compare this object to.
		 * @return {@link BooleanLangObject}. True iff this object is of the same {@link Type} as the argument and is logically equivalent.
		 */
		DOUBLE_EQUAL("comparand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.equals(args[0]));
			}},
		/**
		 * @param comparand {@link ALangObject}. Object to compare this object to.
		 * @return {@link BooleanLangObject}. True iff this object is the same object as the argument.
		 */
		TRIPLE_EQUAL("comparand"){
				@Override
				public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
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
			return Type.BOOLEAN;
		}

		@Override
		public Node getNode() {
			return null;
		}
	}
}
