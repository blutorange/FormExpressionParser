package de.xima.fc.form.expression.impl.function;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExpressionFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IMethod2Function;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * Expression methods for <code>hash&lt;K,V&gt;</code>.
 * @author madgaksha
 */
@ParametersAreNonnullByDefault
public enum EExpressionMethodHash implements IMethod2Function<HashLangObject> {
	/**
	 * Merges the values from the right hash into the left hash.
	 * @param hashToMerge <code>hash&lt;K,V&gt;</code> Hash with values to merge.
	 * @return this <code>hash&lt;K,V&gt;</code> The merged hashed for chaining.
	 */
	PLUS(EMethod.PLUS, Impl.UNION),
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

	private static enum Impl implements IExpressionFunction<HashLangObject> {
		UNION(false, "hashToMerge"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				thisContext.putAll(args[0].coerceHash(ec));
				return thisContext;
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return GenericVariableType.forHash(thisContext.getGeneric(0), thisContext.getGeneric(1));
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return GenericVariableType.forHash(thisContext.getGeneric(0), thisContext.getGeneric(1));
			}

			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.HASH;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.HASH;
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
			return ELangObjectClass.HASH;
		}
	}
}