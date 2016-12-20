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
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EExpressionMethodString implements IMethod2Function<StringLangObject> {
	/**
	 * @param stringToJoin <code>string</code> The string to be concatenated to this string.
	 * @return <code>string</code> The concatenation between this string and the argument.
	 */
	PLUS(EMethod.PLUS, Impl.CONCATENATE),
	/**
	 * @param patternToCheck <code>regex</code> The regex to check.
	 * @return <code>boolean</code> Whether this string matches the regex.
	 */
	EQUAL_TILDE(EMethod.EQUAL_TILDE, Impl.MATCHES)
	;
	private final EMethod method;
	private final IExpressionFunction<StringLangObject> function;
	private EExpressionMethodString(final EMethod method, final IExpressionFunction<StringLangObject> function) {
		this.method = method;
		this.function = function;
	}
	@Override
	public EMethod getMethod() {
		return method;
	}
	@Override
	public IExpressionFunction<StringLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IExpressionFunction<StringLangObject> {
		CONCATENATE(false, "stringToJoin") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.concat(args[0].coerceString(ec));
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.STRING;
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.STRING;
			}

			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.STRING;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.STRING;
			}
		},
		MATCHES(false, "patternToCheck") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(args[0].coerceRegex(ec).patternValue()
						.matcher(thisContext.stringValue()).matches());
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.BOOLEAN;
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.REGEX;
			}

			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.REGEX;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.BOOLEAN;
			}
		}
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
			return ELangObjectClass.STRING;
		}
	}
}
