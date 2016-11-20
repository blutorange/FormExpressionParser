package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAssignerString implements IFunction<StringLangObject> {
	;

	@Nonnull private final FunctionLangObject impl;
	private final boolean evalImmediately;
	@Nonnull private final String[] argList;
	private final String varArgsName;

	private EAttrAssignerString(@Nonnull final Impl impl) {
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

		@Nonnull private String[] argList;
		private String optionalArgumentsName;

		private Impl(final String optArg, @Nonnull final String... argList) {
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

		@SuppressWarnings("null")
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
			throw new UncatchableEvaluationException(ec, "Method called on non-existing enum. This is most likely a problem with the parser. Contact support."); //$NON-NLS-1$
		}
	}
}
