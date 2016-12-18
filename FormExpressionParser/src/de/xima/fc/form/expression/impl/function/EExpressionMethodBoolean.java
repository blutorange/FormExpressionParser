package de.xima.fc.form.expression.impl.function;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExpressionFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IMethod2Function;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EExpressionMethodBoolean implements IMethod2Function<BooleanLangObject> {
	DOUBLE_BAR(EMethod.DOUBLE_BAR, Impl.OR),
	DOUBLE_AMPERSAND(EMethod.DOUBLE_AMPERSAND, Impl.AND),
	CIRCUMFLEX(EMethod.CIRCUMFLEX, Impl.XOR),
	EXCLAMATION(EMethod.EXCLAMATION, Impl.NOT),
	;
	private final EMethod method;
	private final IExpressionFunction<BooleanLangObject> function;

	private EExpressionMethodBoolean(final EMethod method, final IExpressionFunction<BooleanLangObject> function) {
		this.method = method;
		this.function = function;
	}

	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IExpressionFunction<BooleanLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IExpressionFunction<BooleanLangObject> {
		/**
		 * @param orOperand {@link BooleanLangObject}. Argument for the OR.
		 * @return {@link BooleanLangObject}. The result of the logical OR disjunction between this boolean and the argument.
		 */
		OR(false, "orOperand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.or(args[0].coerceBoolean(ec));
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public IVariableType getRhsType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public ILangObjectClass getRhsClass() {
				return ELangObjectClass.BOOLEAN;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.BOOLEAN;
			}
		},
		/**
		 * @param andOperand {@link BooleanLangObject}. Argument for the AND.
		 * @return {@link BooleanLangObject}. The result of the logical AND conjunction between this boolean and the argument.
		 */
		AND(false, "andOperand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.and(args[0].coerceBoolean(ec));
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public IVariableType getRhsType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public ILangObjectClass getRhsClass() {
				return ELangObjectClass.BOOLEAN;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.BOOLEAN;
			}
		},
		/**
		 * @param xorOperand {@link BooleanLangObject}. Argument for the XOR.
		 * @return {@link BooleanLangObject}. The result of the logical XOR exclusive disjunction between this boolean and the argument.
		 */
		XOR(false, "xorOperand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.xor(args[0].coerceBoolean(ec));
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public IVariableType getRhsType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public ILangObjectClass getRhsClass() {
				return ELangObjectClass.BOOLEAN;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.BOOLEAN;
			}
		},
		/**
		 * @return {@link BooleanLangObject}. The logical negation of this boolean.
		 */
		NOT(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.not();
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public IVariableType getRhsType(final IVariableType thisContext) {
				return SimpleVariableType.OBJECT;
			}
			@Override
			public ILangObjectClass getRhsClass() {
				return ELangObjectClass.OBJECT;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.BOOLEAN;
			}
		},
		;

		private final String[] argList;
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
		public String getDeclaredName() {
			return toString();
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
		public ILangObjectClass getThisContextType() {
			return ELangObjectClass.BOOLEAN;
		}
	}
}
