package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;

public enum EAttrAccessorArray implements IFunction<ArrayLangObject> {
	/**
	 * @return {@link NumberLangObject} The length of this array.
	 */
	length(Impl.length),
	/**
	 * @param objectsToAdd {@link ALangObject} Varargs. Object(s) to add to the end of this array.
	 * @return {@link ArrayLangObject} this.
	 */
	push(Impl.push),
	/**
	 * Sorts this array according to the natural ordering of {@link ALangObject}s.
	 * @return {@link ArrayLangObject} this.
	 */
	sort(Impl.sort)
	;

	@Nonnull private final FunctionLangObject impl;
	@Nonnull private final String[] argList;
	private final boolean evalImmediately;
	private final boolean hasVarArgs;
	
	private EAttrAccessorArray(@Nonnull final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		hasVarArgs = impl.hasVarArgs();
		evalImmediately = argList.length == 0 && !hasVarArgs;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
			throws EvaluationException {
		if (!evalImmediately) return impl;
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
	public Type getThisContextType() {
		return Type.ARRAY;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}

	private static enum Impl implements IFunction<ArrayLangObject> {
		/**
		 * When you want to join two arrays <code>a</code> and <code>b</code>, use <code>a+b</code>.
		 * @param objectToAdd {@link ALangObject}*. Object(s) to be added to the end of this array.
		 * @return this. This array with the objects added at the end.
		 */
		push(true, "objectsToAdd") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				for (final ALangObject arg : args)
					thisContext.add(arg);
				return thisContext;
			}
		},
		/**
		 * @return {@link NumberLangObject}. The number of entries in this array.
		 */
		length(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return NumberLangObject.create(thisContext.length());
			}
		},
		sort(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				thisContext.sort();
				return thisContext;
			}
		}
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