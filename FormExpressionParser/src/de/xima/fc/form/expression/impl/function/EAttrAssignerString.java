package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAssignerString implements IFunction<StringLangObject> {
	;

	private final FunctionLangObject impl;
	private final boolean evalImmediately;
	private final String[] argList;
	private final String varArgsName;

	private EAttrAssignerString(final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		varArgsName = impl.getVarArgsName();
		evalImmediately = argList.length == 0;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		if (!evalImmediately)
			return impl;
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
		return Type.STRING;
	}

	@Override
	public String getVarArgsName() {
		return varArgsName;
	}

	@Override
	public Node getNode() {
		return null;
	}

	private static enum Impl implements IFunction<StringLangObject> {
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
			return Type.STRING;
		}

		@Override
		public Node getNode() {
			return null;
		}

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new UncatchableEvaluationException(ec, "Method called on non-existing enum. This is most likely a problem with the parser. Contact support.");
		}
	}
}