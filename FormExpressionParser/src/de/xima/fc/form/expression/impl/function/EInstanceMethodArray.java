package de.xima.fc.form.expression.impl.function;

import org.apache.commons.lang3.NotImplementedException;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.IMethod2Function;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;

public enum EInstanceMethodArray implements IMethod2Function<ArrayLangObject> {
	;
	private final EMethod method;
	private final IFunction<ArrayLangObject> function;

	private EInstanceMethodArray(final EMethod method, final IFunction<ArrayLangObject> function) {
		this.method = method;
		this.function = function;
	}

	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IFunction<ArrayLangObject> getFunction() {
		return function;
	}

	private static enum Func implements IFunction<ArrayLangObject> {
		;

		private final String[] argList;

		private Func(final String... argList) {
			this.argList = argList;
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
			return Type.ARRAY;
		}

		@Override
		public Node getNode() {
			return null;
		}

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new NotImplementedException("cannot happen");
		}
	}
}
