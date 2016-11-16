package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.iface.context.IMethod2Function;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ExceptionLangObject;

public enum EExpressionMethodException implements IMethod2Function<ExceptionLangObject> {
	;
	@Nonnull private final EMethod method;
	@Nonnull private final IFunction<ExceptionLangObject> function;

	private EExpressionMethodException(@Nonnull final EMethod method, @Nonnull final IFunction<ExceptionLangObject> function) {
		this.method = method;
		this.function = function;
	}

	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IFunction<ExceptionLangObject> getFunction() {
		return function;
	}

	@SuppressWarnings("unused")
	private static enum Impl implements IFunction<ExceptionLangObject> {
		// A dummy because I haven't implemented any methods yet.
		@Deprecated
		DUMMY(null, "comparand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext, final ALangObject... args)
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
			return Type.EXCEPTION;
		}

		@Override
		public Node getNode() {
			return null;
		}
	}
}
