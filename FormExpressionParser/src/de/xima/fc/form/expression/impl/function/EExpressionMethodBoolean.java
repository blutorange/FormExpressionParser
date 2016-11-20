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
import de.xima.fc.form.expression.object.BooleanLangObject;

public enum EExpressionMethodBoolean implements IMethod2Function<BooleanLangObject> {
	DOUBLE_BAR(EMethod.DOUBLE_BAR, Impl.OR),
	DOUBLE_AMPERSAND(EMethod.DOUBLE_AMPERSAND, Impl.AND),
	CIRCUMFLEX(EMethod.CIRCUMFLEX, Impl.XOR),
	EXCLAMATION(EMethod.EXCLAMATION, Impl.NOT),
	;
	@Nonnull private final EMethod method;
	@Nonnull private final IFunction<BooleanLangObject> function;

	private EExpressionMethodBoolean(@Nonnull final EMethod method, @Nonnull final IFunction<BooleanLangObject> function) {
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
		OR(null, "orOperand"){ //$NON-NLS-1$
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
		AND(null, "andOperand"){ //$NON-NLS-1$
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
		XOR(null, "xorOperand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.xor(args[0].coerceBoolean(ec));
			}
		},
		/**
		 * @return {@link BooleanLangObject}. The logical negation of this boolean.
		 */
		NOT(null){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.not();
			}
		},
		;

		@Nonnull private final String[] argList;
		private String optionalArgumentsName;

		private Impl(final String optArg, @Nonnull final String... argList) {
			this.argList = argList;
			this.optionalArgumentsName = optArg;
		}

		@Override
		public String getVarArgsName() {
			return optionalArgumentsName;
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
		public Type getThisContextType() {
			return Type.BOOLEAN;
		}

		@Override
		public Node getNode() {
			return null;
		}
	}
}
