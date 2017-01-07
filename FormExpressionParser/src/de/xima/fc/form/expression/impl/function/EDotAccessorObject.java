package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IDotAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public enum EDotAccessorObject implements IDotAccessorFunction<ALangObject> {
	/**
	 * @return <code>number</code>. <code>0</code>, when this is false, <code>1</code> when this is true.
	 */
	toString(Impl.toString),
	/**
	 * @return <code>number</code> The id of this object. This may change between consecutive executions of the same program.
	 */
	id(Impl.id),
	copy(Impl.copy),
	deepCopy(Impl.deepCopy),
	;

	@Nullable private FunctionLangObject func;
	private final Impl impl;

	private EDotAccessorObject(final Impl impl) {
		this.impl = impl;
		func = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs ? null : FunctionLangObject.createWithoutClosure(impl);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		if (func != null)
			return func.bind(thisContext, ec).evaluate(ec);
		return FunctionLangObject.createWithoutClosure(impl).bind(thisContext, ec);
	}

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
		return ELangObjectClass.OBJECT;
	}

	@Override
	public boolean hasVarArgs() {
		return impl.hasVarArgs;
	}

	@Override
	public ILangObjectClass getReturnClass() {
		return impl.getReturnClass();
	}

	@Override
	public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
		return impl.getReturnType(thisContext, dotGenerics);
	}

	@Override
	public boolean supportsGenerics(final IVariableType[] dotGenerics) {
		return impl.supportsGenerics(dotGenerics);
	}

	private static enum Impl implements IDotAccessorFunction<ALangObject> {
		toString(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.coerceString(ec);
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.STRING;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.STRING;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		id(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return NumberLangObject.create(thisContext.getId());
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		copy(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.shallowClone();
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.OBJECT;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.OBJECT;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		deepCopy(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.deepClone();
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.OBJECT;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.OBJECT;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		}
		;

		protected String[] argList;
		protected boolean hasVarArgs;

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

		@Override
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public ILangObjectClass getThisContextType() {
			return ELangObjectClass.OBJECT;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}