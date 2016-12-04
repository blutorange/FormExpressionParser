package de.xima.fc.form.expression.impl.function;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.iface.context.IMethod2Function;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;

public enum EExpressionMethodArray implements IMethod2Function<ArrayLangObject> {
	PLUS(EMethod.PLUS, Impl.UNION),
	DASH(EMethod.DASH, Impl.DIFFERENCE),
	;
	@Nonnull private final EMethod method;
	@Nonnull private final IFunction<ArrayLangObject> function;

	private EExpressionMethodArray(@Nonnull final EMethod method, @Nonnull final IFunction<ArrayLangObject> function) {
		this.method = method;
		this.function = function;
	}

	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IFunction<ArrayLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IFunction<ArrayLangObject> {
		/**
		 * @param elementsToAdd {@link ALangObject}. Element(s) to be added to this array. When an array, all of the array's elements are added at the end of this array. Otherwise, the object itself is added to the end of this array.
		 * @return <code>this</code>, with the elements specified by the argument added.
		 */
		UNION(false, "elementsToAdd"){ //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				if (args[0].getType() == ELangObjectType.ARRAY) thisContext.addAll(args[0].coerceArray(ec));
				else thisContext.add(args[0]);
				return thisContext;
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
				if (args[0].getType() == ELangObjectType.ARRAY) thisContext.removeAll(args[0].coerceArray(ec));
				else thisContext.remove(args[0]);
				return thisContext;
			}
		},
		;

		@Nonnull private final String[] argList;
		private boolean hasVarArgs;

		private Impl(final boolean hasVarArgs, @Nonnull final String... argList) {
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

		@Override
		public String[] getDeclaredArgumentList() {
			return argList;
		}

		@Override
		public int getDeclaredArgumentCount() {
			return argList.length;
		}

		@Override
		public ELangObjectType getThisContextType() {
			return ELangObjectType.ARRAY;
		}

		@Override
		public Node getNode() {
			return null;
		}
	}
}
