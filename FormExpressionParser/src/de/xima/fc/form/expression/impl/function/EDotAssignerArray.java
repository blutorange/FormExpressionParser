package de.xima.fc.form.expression.impl.function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IDotAssignerFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public enum EDotAssignerArray implements IDotAssignerFunction<ArrayLangObject> {
	/**
	 * @param newLength The new length of the array. Padded with {@link NullLangObject} as necessary.
	 */
	length(Impl.length),
	;

	private final FunctionLangObject func;
	private final Impl impl;
	private final boolean hasVarArgs;

	private EDotAssignerArray(final Impl impl) {
		this.func = FunctionLangObject.createWithoutClosure(impl);
		this.impl = impl;
		hasVarArgs = impl.hasVarArgs();
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
			throws EvaluationException {
		func.bind(thisContext, ec);
		return func.evaluate(ec, args[1]);
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
		return ELangObjectClass.ARRAY;
	}

	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}

	@Override
	public ILangObjectClass getValueClass() {
		return impl.getValueClass();
	}

	@Override
	public IVariableType getValueType(final IVariableType thisContext) {
		return impl.getValueType(thisContext);
	}

	private static enum Impl implements IDotAssignerFunction<ArrayLangObject> {
		length(false, "newLength") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				final int len = args[0].coerceNumber(ec).intValue(ec);
				thisContext.setLength(len);
				return thisContext;
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.NUMBER;
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
			return ELangObjectClass.ARRAY;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}