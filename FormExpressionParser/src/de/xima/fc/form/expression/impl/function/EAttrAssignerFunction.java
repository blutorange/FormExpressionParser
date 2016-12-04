package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;

public enum EAttrAssignerFunction implements IFunction<FunctionLangObject> {
	;

	@Nonnull private final FunctionLangObject impl;
	@Nonnull private final String[] argList;
	private final boolean hasVarArgs;
	@Nonnull
	private EAttrAssignerFunction(@Nonnull final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		hasVarArgs = impl.hasVarArgs();
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
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
	public int getDeclaredArgumentCount() {
		return argList.length;
	}

	@Override
	public ELangObjectType getThisContextType() {
		return ELangObjectType.FUNCTION;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}

	private static enum Impl implements IFunction<FunctionLangObject> {
		;

		@Nonnull private String[] argList;
		private boolean hasVarArgs;

		private Impl(final boolean hasVarArgs, @Nonnull final String... argList) {
			this.argList = argList;
			this.hasVarArgs = hasVarArgs;
		}

		@Override
		public boolean hasVarArgs() {
			return hasVarArgs;
		}

		@Override
		public String[] getDeclaredArgumentList() {
			return argList;
		}

		@Override
		public int getDeclaredArgumentCount() {
			return argList.length;
		}

		@SuppressWarnings("null")
		@Override
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public ELangObjectType getThisContextType() {
			return ELangObjectType.FUNCTION;
		}

		@Override
		public Node getNode() {
			return null;
		}

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new UncatchableEvaluationException(ec,
					"Method called on non-existing enum. This is most likely a problem with the parser. Contact support."); //$NON-NLS-1$
		}
	}
}