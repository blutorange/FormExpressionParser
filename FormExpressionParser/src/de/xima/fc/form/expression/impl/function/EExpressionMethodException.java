package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.IMethod2Function;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ExceptionLangObject;

public enum EExpressionMethodException implements IMethod2Function<ExceptionLangObject> {
	;
	private final EMethod method;
	private final IFunction<ExceptionLangObject> function;

	private EExpressionMethodException(final EMethod method, final IFunction<ExceptionLangObject> function) {
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

	private static enum Impl implements IFunction<ExceptionLangObject> {
		// A dummy because I haven't implemented any methods yet.
		@Deprecated
		DUMMY(null, "comparand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				throw new UncatchableEvaluationException(ec);
			}},
		;

			private final String[] argList;
			private String optionalArgumentsName;

			private Impl(final String optArg, final String... argList) {
				this.argList = argList;
				this.optionalArgumentsName = optArg;
			}

			@Override
			public String getVarArgsName() {
				return optionalArgumentsName;
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
				return Type.EXCEPTION;
			}

			@Override
			public Node getNode() {
				return null;
			}
	}
}
