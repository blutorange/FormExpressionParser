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
public enum EExpressionMethodObject implements IMethod2Function<ALangObject> {
	/**
	 * Objects are coerced to <code>boolean</code>s before evaluation.
	 * @param orOperand <code>var</code>. Argument for the OR.
	 * @return <code>boolean</code>. The result of the logical OR disjunction between this object and the argument.
	 */
	DOUBLE_BAR(EMethod.DOUBLE_BAR, Impl.OR),
	/**
	 * Objects are coerced to <code>boolean</code>s before evaluation.
	 * @param andOperand <code>var</code>. Argument for the AND.
	 * @return <code>boolean</code>. The result of the logical AND conjunction between this object and the argument.
	 */
	DOUBLE_AMPERSAND(EMethod.DOUBLE_AMPERSAND, Impl.AND),
	;
	private final EMethod method;
	private final IExpressionFunction<ALangObject> function;
	private EExpressionMethodObject(final EMethod method, final IExpressionFunction<ALangObject> function) {
		this.method = method;
		this.function = function;
	}
	@Override
	public EMethod getMethod() {
		return method;
	}
	@Override
	public IExpressionFunction<ALangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IExpressionFunction<ALangObject> {
		OR(false, "orOperand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				final BooleanLangObject lhs = thisContext.coerceBoolean(ec);
				return lhs.booleanValue() ? lhs : args[0].coerceBoolean(ec);
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.BOOLEAN;
			}
			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.OBJECT;
			}
			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.OBJECT;
			}
		},
		AND(false, "andOperand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				final BooleanLangObject lhs = thisContext.coerceBoolean(ec);
				return !lhs.booleanValue() ? lhs : args[0].coerceBoolean(ec);
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.BOOLEAN;
			}
			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.OBJECT;
			}
			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.OBJECT;
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
			return ELangObjectClass.OBJECT;
		}
	}
}
