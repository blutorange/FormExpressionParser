package de.xima.fc.form.expression.impl.function;

import java.util.Comparator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.ArrayIndexOutOfBoundsException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IDotAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * Dot accessors for objects of type array&lt;T&gt;.
 * @author madgaksha
 */
@ParametersAreNonnullByDefault
public enum EDotAccessorArray implements IDotAccessorFunction<ArrayLangObject> {
	/**
	 * @return number The length of this array.
	 */
	length(Impl.length),
	/**
	 * Retrieves an element from the array.
	 * @param index <code>number</code> The index of the element to retrieve.
	 * @return T The element at the given index.
	 * @throws ArrayIndexOutOfBoundsException When the index is not in the range [0,length), or the element is null.
	 */
	get(Impl.get),
	/**
	 * Adds the given elements to the array.
	 *
	 * @param anElementToAdd T Element to add at the end of this array.
	 * @param moreElementsToAdd T Varargs. More elements to add at the end of this array.
	 * @return array&lt;T&gt; This for chaining.
	 */
	push(Impl.push),
	/**
	 * Sorts the items of this array in their natural order. Sorting is
	 * performed by sorting the items by their class first. This ordering is
	 * subject to change and should be relied upon. Afterwards, objects of the
	 * same type are sorted by their value, see the documentation for the
	 * available language objects.
	 *
	 * @return this array&lt;T&gt; For chaining.
	 */
	sort(Impl.sort),
	/**
	 * Sorts the items of this array according to the given order.
	 * @param comparator function<number, T, T> A comparator that must takes two argument and
	 * return a negative number when the first argument is to be sorted before the second
	 * argument, a positive number when it is to be sorted after the second argument, and
	 * zero when both arguments are equal.
	 * @return array&lt;T&gt; This array for chaining.
	 */
	sortBy(Impl.sortBy),
	;

	private final FunctionLangObject func;
	private final Impl impl;
	private final boolean deferEvaluation;

	private EDotAccessorArray(final Impl impl) {
		this.func = FunctionLangObject.create(impl);
		this.impl = impl;
		deferEvaluation = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
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
		return ELangObjectType.ARRAY;
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
	public IVariableType getReturnType(final IVariableType thisContext) {
		return impl.getReturnType(thisContext);
	}

	private static enum Impl implements IDotAccessorFunction<ArrayLangObject> {
		get(false, "index") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				if (args.length == 0)
					return NullLangObject.getInstance();
				final int index = args[0].coerceNumber(ec).intValue(ec);
				if (index < 0 || index >= thisContext.length())
					throw new ArrayIndexOutOfBoundsException(thisContext, index, ec);
				return thisContext.get(index);
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return GenericVariableType.forSimpleFunction(thisContext.getGeneric(0), SimpleVariableType.NUMBER);
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectType.FUNCTION;
			}
		},
		push(true, "objectsToAdd") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				for (final ALangObject arg : args)
					thisContext.add(arg);
				return thisContext;
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				// array<number>.push(number, number) => array<number>
				// function<array<number>, number, array<number>)
				return GenericVariableType.forVarArgFunction(thisContext, thisContext.getGeneric(0), thisContext.getGeneric(0));
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectType.FUNCTION;
			}
		},
		length(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return NumberLangObject.create(thisContext.length());
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectType.NUMBER;
			}
		},
		sort(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				thisContext.sort();
				return thisContext;
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				return thisContext;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectType.ARRAY;
			}
		},
		sortBy(false, "comparator") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				if (args.length > 0) {
					try {
						thisContext.sort(new Comp(args[0].coerceFunction(ec), ec));
					}
					catch (final EncapsulatingEvaluationException e) {
						throw e.exception;
					}
				}
				else
					thisContext.sort();
				return thisContext;
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext) {
				// array<string>.sort(comparator) => array<string>
				// function<array<string>, function<number, string, string>>
				return GenericVariableType.forSimpleFunction(thisContext, GenericVariableType
						.forSimpleFunction(SimpleVariableType.NUMBER, thisContext.getGeneric(0), thisContext.getGeneric(0)));
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectType.FUNCTION;
			}
		},;

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
			return ELangObjectType.ARRAY;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}

	private static class Comp implements Comparator<ALangObject> {
		private final FunctionLangObject comparator;
		private final IEvaluationContext ec;
		private final NullLangObject nullLangObject = NullLangObject.getInstance();

		private Comp(final FunctionLangObject comparator, final IEvaluationContext ec) {
			this.comparator = comparator;
			this.ec = ec;
		}

		@Override
		public int compare(@Nullable final ALangObject o1, @Nullable final ALangObject o2) {
			try {
				return comparator.functionValue().evaluate(ec, nullLangObject, o1, o2).coerceNumber(ec)
						.signumInt();
			}
			catch (final EvaluationException e) {
				throw new EncapsulatingEvaluationException(e);
			}
		}
	}

	private static class EncapsulatingEvaluationException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public final EvaluationException exception;

		public EncapsulatingEvaluationException(final EvaluationException exception) {
			this.exception = exception;
		}
	}
}