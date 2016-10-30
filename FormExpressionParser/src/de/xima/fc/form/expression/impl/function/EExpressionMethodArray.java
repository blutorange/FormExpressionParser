package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.IMethod2Function;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;

public enum EExpressionMethodArray implements IMethod2Function<ArrayLangObject> {
	PLUS(EMethod.PLUS, Impl.UNION),
	DASH(EMethod.DASH, Impl.DIFFERENCE),
	;
	private final EMethod method;
	private final IFunction<ArrayLangObject> function;

	private EExpressionMethodArray(final EMethod method, final IFunction<ArrayLangObject> function) {
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

	private static enum Impl implements IFunction<ArrayLangObject> {
		/**
		 * @param elementsToAdd {@link ALangObject}. Element(s) to be added to this array. When an array, all of the array's elements are added at the end of this array. Otherwise, the object itself is added to the end of this array.
		 * @return <code>this</code>, with the elements specified by the argument added.
		 */
		UNION(null, "elementsToAdd"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				if (args[0].getType() == Type.ARRAY) thisContext.addAll(args[0].coerceArray(ec));
				else thisContext.add(args[0]);
				return thisContext;
			}
		},
		/**
		 * @param elementsToRemove {@link ArrayLangObject}. Element(s) to be removed from this array. When an array, all of the array's elements are removed from this array. Otherwise, all occurences of the object are removed from this array.
		 * @return <code>this</code>, with the elements specified by the argument removed.
		 */
		DIFFERENCE(null, "elementsToRemove"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				if (args[0].getType() == Type.ARRAY) thisContext.removeAll(args[0].coerceArray(ec));
				else thisContext.remove(args[0]);
				return thisContext;
			}
		},
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
			return Type.ARRAY;
		}

		@Override
		public Node getNode() {
			return null;
		}
	}
}
