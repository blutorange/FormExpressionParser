package de.xima.fc.form.expression.impl.function;

import java.util.Arrays;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.IllegalThisContextException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAccessorFunction implements IFunction<FunctionLangObject> {
	/**
	 * @return {@link StringLangObject}. The declared name of this function. The
	 *         empty string when an anonymous function.
	 */
	name(Impl.name),
	/**
	 * @param thisContext {@link ALangObject}. This context for the function.
	 * @param args... {@link ALangObject}. Arguments for the function.
	 * @return {@link ALangObject}. Return value of the function.
	 * @NullLangObject When the function returns <code>null</code>, or executes an empty <code>return;</code> clause.
	 * @see EAttrAccessorFunction#apply
	 */
	call(Impl.call),
	/**
	 * @param thisContext {@link ALangObject}. This context for the function.
	 * @param argsArray {@link ArrayLangObject}. Array of arguments for the function.
	 * @return {@link ALangObject}. Return value of the function.
	 * @NullLangObject When the function returns <code>null</code>, or executes an empty <code>return;</code> clause.
	 * @see EAttrAccessorFunction#call
	 */
	apply(Impl.apply),;

	@Nonnull private final FunctionLangObject impl;
	private final boolean evalImmediately;
	@Nonnull private final String[] argList;
	private final boolean hasVarArgs;

	private EAttrAccessorFunction(@Nonnull final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		hasVarArgs = impl.hasVarArgs();
		evalImmediately = argList.length == 0 && !hasVarArgs;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
			@Nonnull final ALangObject... args) throws EvaluationException {
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
		name(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return StringLangObject.create(thisContext.functionValue().getDeclaredName());
			}
		},
		apply(false, "thisContext", "argsArray") { //$NON-NLS-1$ //$NON-NLS-2$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final ALangObject thiz = args.length > 0 ? args[0] : NullLangObject.getInstance();
				if (thisContext.functionValue().getThisContextType() != thiz.getType())
					throw new IllegalThisContextException(thiz, thisContext.functionValue().getThisContextType(),
							thisContext.functionValue(), ec);
				if (args.length > 1)
					return thisContext.functionValue().evaluate(ec, thiz, args[1].coerceArray(ec).toArray());
				return thisContext.functionValue().evaluate(ec, thiz);
			}
		},
		call(true, "thisContext", "args") { //$NON-NLS-1$ //$NON-NLS-2$
			@SuppressWarnings("null")
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final ALangObject thiz = args.length > 0 ? args[0] : NullLangObject.getInstance();
				if (thisContext.functionValue().getThisContextType() != thiz.getType())
					throw new IllegalThisContextException(thiz, thisContext.functionValue().getThisContextType(),
							thisContext.functionValue(), ec);
				if (args.length > 1)
					return thisContext.functionValue().evaluate(ec, thiz, Arrays.copyOfRange(args, 1, args.length));
				return thisContext.functionValue().evaluate(ec, thiz);
			}
		};

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
		public abstract ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}