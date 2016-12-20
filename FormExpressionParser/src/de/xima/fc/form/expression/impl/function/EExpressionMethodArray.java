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
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EExpressionMethodArray implements IMethod2Function<ArrayLangObject> {
	PLUS(EMethod.PLUS, Impl.UNION),
	DASH(EMethod.DASH, Impl.DIFFERENCE),
	;
	private final EMethod method;
	private final IExpressionFunction<ArrayLangObject> function;

	private EExpressionMethodArray(final EMethod method, final IExpressionFunction<ArrayLangObject> function) {
		this.method = method;
		this.function = function;
	}

	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IExpressionFunction<ArrayLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IExpressionFunction<ArrayLangObject> {
		/**
		 * @param elementsToAdd {@link ALangObject}. Element(s) to be added to this array. When an array, all of the array's elements are added at the end of this array. Otherwise, the object itself is added to the end of this array.
		 * @return <code>this</code>, with the elements specified by the argument added.
		 */
		UNION(false, "elementsToAdd"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				if (args[0].getObjectClass() == ELangObjectClass.ARRAY) thisContext.addAll(args[0].coerceArray(ec));
				else thisContext.add(args[0]);
				return thisContext;
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return GenericVariableType.forArray(thisContext.getGeneric(0));
			}

			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return GenericVariableType.forArray(thisContext.getGeneric(0));
			}
			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.ARRAY;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.ARRAY;
			}
		},
		/**
		 * @param elementsToRemove {@link ArrayLangObject}. Element(s) to be removed from this array. When an array, all of the array's elements are removed from this array. Otherwise, all occurences of the object are removed from this array.
		 * @return <code>this</code>, with the elements specified by the argument removed.
		 */
		DIFFERENCE(false, "elementsToRemove"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				if (args[0].getObjectClass() == ELangObjectClass.ARRAY) thisContext.removeAll(args[0].coerceArray(ec));
				else thisContext.remove(args[0]);
				return thisContext;
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return GenericVariableType.forArray(thisContext.getGeneric(0));
			}
			@Override
			public IVariableType getValueType(final IVariableType thisContext) {
				return GenericVariableType.forArray(thisContext.getGeneric(0));
			}
			@Override
			public ILangObjectClass getValueClass() {
				return ELangObjectClass.ARRAY;
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.ARRAY;
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
			return ELangObjectClass.ARRAY;
		}
	}
}
