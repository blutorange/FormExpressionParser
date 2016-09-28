package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;

public enum EAttrAccessorArray implements IFunction<ArrayLangObject> {
	length(Impl.length),
	push(Impl.push),
	;

	private final FunctionLangObject impl;
	private final boolean evalImmediately;
	private final String[] argList;
	private EAttrAccessorArray(final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		evalImmediately = argList.length == 0;
	}
	
	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
			throws EvaluationException {
		if (!evalImmediately) return impl;
		return impl.functionValue().evaluate(ec, thisContext, args);
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
	
	private static enum Impl implements IFunction<ArrayLangObject> {
		/**
		 * When you want to join two arrays <code>a</code> and <code>b</code>, use <code>a+b</code>.
		 * @param objectToAdd {@link ALangObject}*. Object(s) to be added to the end of this array.
		 * @return this. This array with the objects added at the end.
		 */
		push("objectToAdd") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				for (ALangObject arg : args)
					thisContext.add(arg);
				return thisContext;
			}
		},
		/**
		 * @return {@link NumberLangObject}. The number of entries in this array.
		 */
		length() {
			@Override
			public ALangObject evaluate(IEvaluationContext ec, ArrayLangObject thisContext, ALangObject... args)
					throws EvaluationException {
				return NumberLangObject.create(thisContext.length());
			}			
		}
		;
		
		private String[] argList;
		
		private Impl(String... argList) {
			this.argList = argList;
		}
		
		@Override
		public String[] getDeclaredArgumentList() {
			return argList;
		}

		@Override
		public String getDeclaredName() {
			return toString();
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
		public abstract ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}