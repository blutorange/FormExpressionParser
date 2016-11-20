package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.iface.context.IMethod2Function;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.RegexLangObject;

public enum EExpressionMethodRegex implements IMethod2Function<RegexLangObject> {
	;
	@Nonnull private final EMethod method;
	@Nonnull private final IFunction<RegexLangObject> function;

	private EExpressionMethodRegex(@Nonnull final EMethod method, @Nonnull final IFunction<RegexLangObject> function) {
		this.method = method;
		this.function = function;
	}

	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IFunction<RegexLangObject> getFunction() {
		return function;
	}

	@SuppressWarnings("unused")
	private static enum Impl implements IFunction<RegexLangObject> {
		// Dummy because there are not methods yet.
		@Deprecated
		DUMMY(null, "comparand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				throw new UncatchableEvaluationException(ec);
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
			return Type.REGEX;
		}

		@Override
		public Node getNode() {
			return null;
		}
	}
}
