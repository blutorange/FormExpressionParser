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
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
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
	private final EMethod method;
	private final IExpressionFunction<NumberLangObject> function;
	private EExpressionMethodNumber(final EMethod method, final IExpressionFunction<NumberLangObject> function) {
		this.method = method;
		this.function = function;
	}
	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IExpressionFunction<NumberLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IExpressionFunction<NumberLangObject> {
		IDENTITY(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext;
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}
			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.OBJECT;
			}
			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.OBJECT;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}
		},
		ADD(false, "summand") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.add(args[0].coerceNumber(ec));
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}
		},
		SUBTRACT(false, "subtrahend") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.subtract(args[0].coerceNumber(ec));
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return thisContext;
			}

			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}
		},
		INCREMENT(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.add(NumberLangObject.getOneInstance());
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}
			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.OBJECT;
			}
			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.OBJECT;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}
		},
		DECREMENT(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.subtract(NumberLangObject.getOneInstance());
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}
			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.OBJECT;
			}
			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.OBJECT;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}
		},
		NEGATE(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.negate();
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}
			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.OBJECT;
			}
			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.OBJECT;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}
		},
		MULTIPLY(false, "multiplicand") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.multiply(args[0].coerceNumber(ec));
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}
		},
		DIVIDE(false, "dividend") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.divide(args[0].coerceNumber(ec));
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}
		},
		MODULO(false,"operand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.modulo(args[0].coerceNumber(ec));
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
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
			return ELangObjectClass.NUMBER;
		}
	}
}
