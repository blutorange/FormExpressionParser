package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExpressionFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IMethod2Function;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EExpressionMethodHash implements IMethod2Function<HashLangObject> {
	;
	private final EMethod method;
	private final IExpressionFunction<HashLangObject> function;

	private EExpressionMethodHash(final EMethod method, final IExpressionFunction<HashLangObject> function) {
		this.method = method;
		this.function = function;
	}

	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IExpressionFunction<HashLangObject> getFunction() {
		return function;
	}

	@SuppressWarnings("unused")
	private static enum Impl implements IExpressionFunction<HashLangObject> {
		// A dummy because there are no methods yet.
		@Deprecated
		DUMMY(false, "comparand"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				throw new UncatchableEvaluationException(ec);
			}

			@Nullable
			@Override
			public IVariableType getReturnTypeFor(final IVariableType lhs, final IVariableType rhs) {
				return null;
			}
		},
		;

		private final String[] argList;
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
		public String getDeclaredName() {
			return toString();
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
		public ILangObjectClass getThisContextType() {
			return ELangObjectType.HASH;
		}
	}
}
