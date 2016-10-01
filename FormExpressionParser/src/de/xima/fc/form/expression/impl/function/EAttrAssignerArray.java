package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;

public enum EAttrAssignerArray implements IFunction<ArrayLangObject> {
	/**
	 * @param newLength The new length of the array. Padded with {@link NullLangObject} as necessary.
	 */
	length(Impl.length),
	;

	private final FunctionLangObject impl;
	private final boolean evalImmediately;
	private final String[] argList;
	private final String varArgsName;
	private EAttrAssignerArray(final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		varArgsName = impl.getVarArgsName();
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

	@Override
	public String getVarArgsName() {
		return varArgsName;
	}

	private static enum Impl implements IFunction<ArrayLangObject> {
		length(null, "newLength") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				final int len = args[2].coerceNumber(ec).intValue(ec);
				thisContext.setLength(len);
				return thisContext;
			}
		}
		;

		private String[] argList;
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