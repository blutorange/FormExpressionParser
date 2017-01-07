package de.xima.fc.form.expression.impl.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.evaluation.ArrayIndexOutOfBoundsException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IDotAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
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
@NonNullByDefault
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
	 * @param comparator <code>method<number, T, T></code> A comparator that must takes two argument and
	 * return a negative number when the first argument is to be sorted before the second
	 * argument, a positive number when it is to be sorted after the second argument, and
	 * zero when both arguments are equal.
	 * @return array&lt;T&gt; This array for chaining.
	 */
	sortBy(Impl.sortBy),
	map(Impl.map),
//	mapString(Impl.mapString),
//	mapNumber(Impl.mapNumber),
	/**
	 * Resizes the array to the given length and fills it with the
	 * given object. All array entries then point to the same object.
	 * @param fillerValue <code>T</code> Object to fill the array with.
	 * @param newLength <code>number</code> The new length of the array.
	 */
	fill(Impl.fill),
	/**
	 * Resizes the array to the given length and fills it with the objects
	 * returned by the given producer. The producer is a function that is given
	 * the current index and returns the new element to be set.
	 *
	 * @param producer
	 *            <code>method&lt;T, number&gt;</code> A function that takes the
	 *            current index and returns the element to be set.
	 * @param newLength
	 *            <code>boolean</code> The new length.
	 * @return this <code>array&lt;T&gt;</code> For chaining.
	 */
	fillWith(Impl.fillWith),
	copy(Impl.copy),
	deepCopy(Impl.deepCopy),
	;

	@Nullable private FunctionLangObject func;
	private final Impl impl;

	private EDotAccessorArray(final Impl impl) {
		this.impl = impl;
		func = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs ? null : FunctionLangObject.createWithoutClosure(impl);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
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
		return ELangObjectClass.ARRAY;
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
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return GenericVariableType.forSimpleFunction(thisContext.getGeneric(0), SimpleVariableType.NUMBER);
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.FUNCTION;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		push(true, "anObjectToAdd", "moreObjectsToAdd") { //$NON-NLS-1$ //$NON-NLS-2$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				for (final ALangObject arg : args)
					thisContext.add(arg);
				return thisContext;
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				// array<number>.push(number, number) => array<number>
				// function<array<number>, number, array<number>)
				return GenericVariableType.forVarArgFunction(thisContext, thisContext.getGeneric(0), thisContext.getGeneric(0));
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.FUNCTION;
			}
			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		length(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return NumberLangObject.create(thisContext.length());
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
		sort(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				thisContext.sort();
				return thisContext;
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return thisContext;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.ARRAY;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		sortBy(false, "comparator") { //$NON-NLS-1$
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
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				// array<string>.sort(comparator) => array<string>
				// function<array<string>, function<number, string, string>>
				return GenericVariableType.forSimpleFunction(thisContext.getGeneric(0), GenericVariableType
						.forSimpleFunction(SimpleVariableType.NUMBER, thisContext.getGeneric(0), thisContext.getGeneric(0)));
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.FUNCTION;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		map(false, "mapper") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return map(thisContext, args[0], ec);
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				IVariableType type = dotGenerics.length > 0 ? dotGenerics[0] : null;
				if (type == null)
					type = thisContext.getGeneric(0);
				return GenericVariableType.forSimpleFunction(GenericVariableType.forArray(type),
						GenericVariableType.forSimpleFunction(type, thisContext.getGeneric(0)));
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.FUNCTION;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length < 2;
			}
		},
		fill(false, "fillerValue", "newLength") { //$NON-NLS-1$ //$NON-NLS-2$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				final ALangObject filler = args.length > 0 ? args[0] : NullLangObject.getInstance();
				final int newLength = args.length > 1 ? args[1].coerceNumber(ec).intValue(ec) : thisContext.length();
				if (newLength > thisContext.length()) {
					Collections.fill(thisContext.listValue(), filler);
					thisContext.setLength(newLength, filler);
				}
				else {
					thisContext.setLength(newLength, filler);
					Collections.fill(thisContext.listValue(), filler);
				}
				return thisContext;
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return GenericVariableType.forSimpleFunction(thisContext, thisContext.getGeneric(0), SimpleVariableType.NUMBER);
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.FUNCTION;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		fillWith(false, "producer", "newLength") { //$NON-NLS-1$ //$NON-NLS-2$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				final FunctionLangObject filler = args.length > 0 ? args[0].coerceFunction(ec) : FunctionLangObject.getNoOpNull();
				final int newLength = args.length > 1 ? args[1].coerceNumber(ec).intValue(ec) : thisContext.length();
				thisContext.setLength(newLength, filler);
				final ListIterator<ALangObject> it = thisContext.listValue().listIterator();
				int i = -1;
				while (it.hasNext()) {
					it.next();
					it.set(filler.evaluate(ec, NumberLangObject.create(++i)));
				}
				return thisContext;
			}
			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return GenericVariableType.forSimpleFunction(thisContext,
						GenericVariableType.forSimpleFunction(thisContext.getGeneric(0), SimpleVariableType.NUMBER),
						SimpleVariableType.NUMBER);
			}
			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.FUNCTION;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		copy(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.shallowClone();
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return GenericVariableType.forArray(thisContext.getGeneric(0));
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.ARRAY;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		deepCopy(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return thisContext.deepClone();
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return GenericVariableType.forArray(thisContext.getGeneric(0));
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.ARRAY;
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
			return ELangObjectClass.ARRAY;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}

	private static class Comp implements Comparator<ALangObject> {
		private final FunctionLangObject comparator;
		private final IEvaluationContext ec;

		protected Comp(final FunctionLangObject comparator, final IEvaluationContext ec) {
			this.comparator = comparator;
			this.ec = ec;
		}
		@Override
		public int compare(@Nullable final ALangObject o1, @Nullable final ALangObject o2) {
			try {
				return comparator.evaluate(ec, o1, o2).coerceNumber(ec)
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

	protected static ALangObject map(final ArrayLangObject list, @Nullable final ALangObject mapper, final IEvaluationContext ec)
			throws EvaluationException {
		if (mapper == null)
			return list.shallowClone();
		final FunctionLangObject mapperFunc = mapper.coerceFunction(ec);
		final List<ALangObject> mapped = new ArrayList<>(list.length());
		for (final ALangObject item : list.listValue())
			mapped.add(mapperFunc.evaluate(ec, item));
		return ArrayLangObject.create(mapped);
	}
}