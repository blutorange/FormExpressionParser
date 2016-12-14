package de.xima.fc.form.expression.impl.function;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IAttrAccessorFunction;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EAttrAccessorObject implements IAttrAccessorFunction<ALangObject> {
	/**
	 * @return {@link NumberLangObject}. <code>0</code>, when this is false, <code>1</code> when this is true.
	 */
	toString(Impl.toString),
	;

	private final FunctionLangObject func;
	private final Impl impl;
	private final boolean deferEvaluation;

	private EAttrAccessorObject(final Impl impl) {
		this.func = FunctionLangObject.create(impl);
		this.impl = impl;
		deferEvaluation = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext,
			final ALangObject... args) throws EvaluationException {
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
		return ELangObjectType.OBJECT;
	}

	@Override
	public boolean hasVarArgs() {
		return impl.hasVarArgs;
	}

	private static enum Impl implements IAttrAccessorFunction<ALangObject> {
		toString(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.coerceString(ec);
			}
		}
		;

		private String[] argList;
		private boolean hasVarArgs;

		private Impl(final boolean hasVarArgs, final String... argList) {
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
			return ELangObjectType.OBJECT;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}
