package de.xima.fc.form.expression.impl.function;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IDotAssignerFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EDotAssignerBoolean implements IDotAssignerFunction<BooleanLangObject> {
	;

	private final FunctionLangObject func;
	private final Impl impl;
	private final boolean hasVarArgs;

	private EDotAssignerBoolean(final Impl impl) {
		this.func = FunctionLangObject.create(impl);
		this.impl = impl;
		hasVarArgs = impl.hasVarArgs();
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
			throws EvaluationException {
		return func.functionValue().evaluate(ec, thisContext, args);
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
		return ELangObjectType.BOOLEAN;
	}

	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}

	@Override
	public boolean isDotAssignerDefined(final IVariableType thisContext, final IVariableType value) {
		return impl.isDotAssignerDefined(thisContext, value);
	}

	private static enum Impl implements IDotAssignerFunction<BooleanLangObject> {
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
			return ELangObjectType.BOOLEAN;
		}

		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			throw new UncatchableEvaluationException(ec,
					"Method called on non-existing enum. This is most likely a problem with the parser. Contact support."); //$NON-NLS-1$
		}

		@Override
		public boolean isDotAssignerDefined(final IVariableType thisContext, final IVariableType value) {
			return false;
		}
	}
}
