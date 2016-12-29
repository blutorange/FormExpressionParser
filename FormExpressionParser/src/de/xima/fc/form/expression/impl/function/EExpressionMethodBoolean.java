package de.xima.fc.form.expression.impl.function;

import org.eclipse.jdt.annotation.NonNullByDefault;

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

@NonNullByDefault
public enum EExpressionMethodBoolean implements IMethod2Function<BooleanLangObject> {
	CIRCUMFLEX(EMethod.CIRCUMFLEX, Impl.XOR),
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
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.BOOLEAN;
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
