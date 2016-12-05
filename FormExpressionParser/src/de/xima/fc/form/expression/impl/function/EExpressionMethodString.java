package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.IMethod2Function;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EExpressionMethodString implements IMethod2Function<StringLangObject> {
	/**
	 * @param stringToJoin {@link StringLangObject} The string to be concatenated to this string.
	 * @return {@link StringLangObject} The concatenation between this string and the argument.
	 */
	PLUS(EMethod.PLUS, Impl.CONCATENATE),
	;
	@Nonnull private final EMethod method;
	@Nonnull private final IFunction<StringLangObject> function;
	private EExpressionMethodString(@Nonnull final EMethod method, @Nonnull final IFunction<StringLangObject> function) {
		this.method = method;
		this.function = function;
	}
	@Override
	public EMethod getMethod() {
		return method;
	}
	@Override
	public IFunction<StringLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IFunction<StringLangObject> {
		CONCATENATE(false, "stringToJoin") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.concat(args[0].coerceString(ec));
			}
		},
		;

		@Nonnull private final String[] argList;
		private boolean hasVarArgs;

		private Impl(final boolean hasVarArgs, @Nonnull final String... argList) {
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

		@Override
		public String[] getDeclaredArgumentList() {
			return argList;
		}

		@Override
		public int getDeclaredArgumentCount() {
			return argList.length;
		}

		@Override
		public ELangObjectType getThisContextType() {
			return ELangObjectType.STRING;
		}
		@Override
		public Node getNode() {
			return null;
		}
	}
}
