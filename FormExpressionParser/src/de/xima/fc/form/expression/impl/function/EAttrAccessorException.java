package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAccessorException implements IFunction<ExceptionLangObject> {
	/**
	 * @return {@link StringLangObject}. The message for this exception. The empty string when this exception does not contain a message.
	 */
	message(Impl.message),
	;

	@Nonnull private final FunctionLangObject impl;
	private final boolean evalImmediately;
	@Nonnull private final String[] argList;
	private final String varArgsName;

	private EAttrAccessorException(@Nonnull final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		varArgsName = impl.getVarArgsName();
		evalImmediately = argList.length == 0;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		return evalImmediately ? impl.functionValue().evaluate(ec, thisContext, args) : impl;
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

	@Override
	public String getVarArgsName() {
		return varArgsName;
	}

	private static enum Impl implements IFunction<ExceptionLangObject> {
		message(null) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return StringLangObject.create(thisContext.exceptionValue().getMessage());
			}
		}
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
			return Type.EXCEPTION;
		}

		@Override
		public Node getNode() {
			return null;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}
