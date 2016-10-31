package de.xima.fc.form.expression.impl.function;

import java.util.Arrays;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.IllegalThisContextException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
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
	private final String varArgsName;

	private EAttrAccessorFunction(@Nonnull final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		varArgsName = impl.getVarArgsName();
		evalImmediately = argList.length == 0;
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
	public Type getThisContextType() {
		return Type.FUNCTION;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public String getVarArgsName() {
		return varArgsName;
	}

	private static enum Impl implements IFunction<FunctionLangObject> {
		name(null) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return StringLangObject.create(thisContext.functionValue().getDeclaredName());
			}
		},
		apply(null, "thisContext", "argsArray") { //$NON-NLS-1$ //$NON-NLS-2$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final ALangObject thiz = args.length > 0 ? args[0] : NullLangObject.getInstance();
				if (thisContext.functionValue().getThisContextType() != thiz.getType())
					throw new IllegalThisContextException(thiz, thisContext.functionValue().getThisContextType(),
							thisContext.functionValue(), ec);
				ec.getBinding().nestLocal(ec);
				try {
					if (args.length > 1)
						return thisContext.functionValue().evaluate(ec, thiz, args[1].coerceArray(ec).toArray());
					return thisContext.functionValue().evaluate(ec, thiz);
				}
				finally {
					ec.getBinding().unnest(ec);
				}
			}
		},
		call("args", "thisContext") { //$NON-NLS-1$ //$NON-NLS-2$
			@SuppressWarnings("null")
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final ALangObject thiz = args.length > 0 ? args[0] : NullLangObject.getInstance();
				if (thisContext.functionValue().getThisContextType() != thiz.getType())
					throw new IllegalThisContextException(thiz, thisContext.functionValue().getThisContextType(),
							thisContext.functionValue(), ec);
				ec.getBinding().nestLocal(ec);
				try {
					if (args.length > 1)
						return thisContext.functionValue().evaluate(ec, thiz, Arrays.copyOfRange(args, 1, args.length));
					return thisContext.functionValue().evaluate(ec, thiz);
				}
				finally {
					ec.getBinding().unnest(ec);
				}
			}
		};

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
			return Type.FUNCTION;
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