package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IAttrAccessorFunction;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NullUtil;

public enum EAttrAccessorArray implements IAttrAccessorFunction<ArrayLangObject> {
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

	@Nonnull private final FunctionLangObject func;
	@Nonnull private final Impl impl;
	private final boolean deferEvaluation;

	private EAttrAccessorArray(@Nonnull final Impl impl) {
		this.func = FunctionLangObject.create(impl);
		this.impl = impl;
		deferEvaluation = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
			throws EvaluationException {
		return deferEvaluation ? func : func.functionValue().evaluate(ec, thisContext, args);
	}

	@SuppressWarnings("null")
	@Override
	public String getDeclaredName() {
		return toString();
	}

	@SuppressWarnings("null")
	@Override
	public String getDeclaredArgument(final int i) {
		return impl.argList[i];
	}

	@Override
	public int getDeclaredArgumentCount() {
		return impl.argList.length;
	}

	@Override
	public ILangObjectClass getThisContextType() {
		return ELangObjectType.ARRAY;
	}

	@Override
	public boolean hasVarArgs() {
		return impl.hasVarArgs;
	}

	private static enum Impl implements IAttrAccessorFunction<ArrayLangObject> {
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
			NullUtil.checkItemsNotNull(argList);
			this.argList = argList;
			this.hasVarArgs = hasVarArgs;
		}

		@Override
		public boolean hasVarArgs() {
			return hasVarArgs;
		}

		@SuppressWarnings("null")
		@Override
		public String getDeclaredArgument(final int i) {
			return argList[i];
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
		public ILangObjectClass getThisContextType() {
			return ELangObjectType.ARRAY;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}