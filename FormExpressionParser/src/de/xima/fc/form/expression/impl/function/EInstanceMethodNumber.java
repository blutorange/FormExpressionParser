package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.IMethod2Function;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NamedFunctionUtils;

public enum EInstanceMethodNumber implements IMethod2Function<NumberLangObject> {
	PLUS(EMethod.PLUS, Func.PLUS),
	STAR(EMethod.STAR, Func.STAR);
	private final EMethod method;
	private final IFunction<NumberLangObject> function;
	private EInstanceMethodNumber(final EMethod method, final IFunction<NumberLangObject> function) {
		this.method = method;
		this.function = function;
	}
	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IFunction<NumberLangObject> getFunction() {
		return function;
	}

	private static enum Func implements IFunction<NumberLangObject> {
		PLUS("summand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final NumberLangObject operand = NamedFunctionUtils.getCoercedArg(Type.NUMBER, NumberLangObject.class,
						0, args, this, ec);
				return thisContext.add(operand);
			}
		},
		STAR("multiplicand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final NumberLangObject operand = NamedFunctionUtils.getCoercedArg(Type.NUMBER, NumberLangObject.class,
						0, args, this, ec);
				return thisContext.multiply(operand);
			}
		};

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
			return Type.NUMBER;
		}

		@Override
		public Node getNode() {
			return null;
		}
	}
}
