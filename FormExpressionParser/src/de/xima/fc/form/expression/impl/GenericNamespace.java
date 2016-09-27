package de.xima.fc.form.expression.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.IMethod2Function;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

/**
 * A generic immutable {@link INamespace} that only needs to be constructed once
 * and can be used throughout the entire lifetime of the program from multiple threads.
 * <br><br>
 * Construct it with the builder:
 * <br><br>
 * <pre>
 * GenericNamespace.Builder builder = new GenericNamespace.Builder();
 * builder.addExpressionMethodBoolean(EExpressionMethodBoolean.values());
 * builder.addAttrAccessorBoolean(EAttrAccessorBoolean.values());
 * ...
 * builder.addAttrAccessorBoolean(new IFunction&lt;BooleanLangObject&gt;(){
 *   // custom attribute accessor
 * });
 * INamespace namespace = builder.build();
 * </pre>
 * @author madgaksha
 */
public class GenericNamespace implements INamespace {

	private final ImmutableMap<EMethod, IFunction<StringLangObject>> expressionMethodString;
	private final ImmutableMap<EMethod, IFunction<ArrayLangObject>> expressionMethodArray;
	private final ImmutableMap<EMethod, IFunction<NumberLangObject>> expressionMethodNumber;
	private final ImmutableMap<EMethod, IFunction<HashLangObject>> expressionMethodHash;
	private final ImmutableMap<EMethod, IFunction<ExceptionLangObject>> expressionMethodException;
	private final ImmutableMap<EMethod, IFunction<BooleanLangObject>> expressionMethodBoolean;
	private final ImmutableMap<EMethod, IFunction<FunctionLangObject>> expressionMethodFunction;

	private final ImmutableMap<String, IFunction<StringLangObject>> attrAccessorString;
	private final ImmutableMap<String, IFunction<ArrayLangObject>> attrAccessorArray;
	private final ImmutableMap<String, IFunction<BooleanLangObject>> attrAccessorBoolean;
	private final ImmutableMap<String, IFunction<NumberLangObject>> attrAccessorNumber;
	private final ImmutableMap<String, IFunction<HashLangObject>> attrAccessorHash;
	private final ImmutableMap<String, IFunction<ExceptionLangObject>> attrAccessorException;
	private final ImmutableMap<String, IFunction<FunctionLangObject>> attrAccessorFunction;

	private final IFunction<StringLangObject> genericAttrAccessorString;
	private final IFunction<NumberLangObject> genericAttrAccessorNumber;
	private final IFunction<BooleanLangObject> genericAttrAccessorBoolean;
	private final IFunction<ArrayLangObject> genericAttrAccessorArray;
	private final IFunction<HashLangObject> genericAttrAccessorHash;
	private final IFunction<FunctionLangObject> genericAttrAccessorFunction;
	private final IFunction<ExceptionLangObject> genericAttrAccessorException;

	public static class Builder {
		private final static int FIELDS_TO_FILL = 3*7;
		private final EnumMap<EMethod, IFunction<StringLangObject>> expressionMethodString = new EnumMap<>(EMethod.class);
		private final EnumMap<EMethod, IFunction<ArrayLangObject>> expressionMethodArray = new EnumMap<>(EMethod.class);
		private final EnumMap<EMethod, IFunction<BooleanLangObject>> expressionMethodBoolean = new EnumMap<>(EMethod.class);
		private final EnumMap<EMethod, IFunction<NumberLangObject>> expressionMethodNumber = new EnumMap<>(EMethod.class);
		private final EnumMap<EMethod, IFunction<HashLangObject>> expressionMethodHash = new EnumMap<>(EMethod.class);
		private final EnumMap<EMethod, IFunction<ExceptionLangObject>> expressionMethodException = new EnumMap<>(EMethod.class);
		private final EnumMap<EMethod, IFunction<FunctionLangObject>> expressionMethodFunction = new EnumMap<>(EMethod.class);
		private final Map<String, IFunction<StringLangObject>> attrAccessorString = new HashMap<>();
		private final Map<String, IFunction<ArrayLangObject>> attrAccessorArray = new HashMap<>();
		private final Map<String, IFunction<BooleanLangObject>> attrAccessorBoolean = new HashMap<>();
		private final Map<String, IFunction<NumberLangObject>> attrAccessorNumber = new HashMap<>();
		private final Map<String, IFunction<HashLangObject>> attrAccessorHash = new HashMap<>();
		private final Map<String, IFunction<ExceptionLangObject>> attrAccessorException = new HashMap<>();
		private final Map<String, IFunction<FunctionLangObject>> attrAccessorFunction = new HashMap<>();
		private IFunction<StringLangObject> genericAttrAccessorString;
		private IFunction<NumberLangObject> genericAttrAccessorNumber;
		private IFunction<BooleanLangObject> genericAttrAccessorBoolean;
		private IFunction<ArrayLangObject> genericAttrAccessorArray;
		private IFunction<HashLangObject> genericAttrAccessorHash;
		private IFunction<FunctionLangObject> genericAttrAccessorFunction;
		private IFunction<ExceptionLangObject> genericAttrAccessorException;
		private int count = 0;

		public Builder() {
		}

		private final <T extends ALangObject> Builder addAttrAccessor(final IFunction<T>[] m, final Map<String, IFunction<T>> map) {
			if (m.length == 0) return this;
			if (map.isEmpty()) {
				++count;
			}
			for (final IFunction<T> f : m)
				map.put(f.getDeclaredName(), f);
			return this;
		}

		private final <T extends ALangObject> Builder addExpressionMethod(final IMethod2Function<T>[] m, final EnumMap<EMethod, IFunction<T>> map) {
			if (m.length == 0) return this;
			if (map.isEmpty()) {
				++count;
			}
			for (final IMethod2Function<T> f : m)
				map.put(f.getMethod(), f.getFunction());
			return this;
		}

		public final Builder addExpressionMethodString(final IMethod2Function<StringLangObject>[] m) {
			return addExpressionMethod(m, expressionMethodString);
		}

		public final Builder addExpressionMethodBoolean(final IMethod2Function<BooleanLangObject>[] m) {
			return addExpressionMethod(m, expressionMethodBoolean);
		}

		public final Builder addExpressionMethodNumber(final IMethod2Function<NumberLangObject>[] m) {
			return addExpressionMethod(m, expressionMethodNumber);
		}

		public final Builder addExpressionMethodHash(final IMethod2Function<HashLangObject>[] m) {
			return addExpressionMethod(m, expressionMethodHash);
		}

		public final Builder addExpressionMethodArray(final IMethod2Function<ArrayLangObject>[] m) {
			return addExpressionMethod(m, expressionMethodArray);
		}

		public final Builder addExpressionMethodException(final IMethod2Function<ExceptionLangObject>[] m) {
			return addExpressionMethod(m, expressionMethodException);
		}

		public final Builder addExpressionMethodFunction(final IMethod2Function<FunctionLangObject>[] m) {
			return addExpressionMethod(m, expressionMethodFunction);
		}

		public final Builder addAttrAccessorString(final IFunction<StringLangObject>[] m) {
			return addAttrAccessor(m, attrAccessorString);
		}

		public final Builder addAttrAccessorFunction(final IFunction<FunctionLangObject>[] m) {
			return addAttrAccessor(m, attrAccessorFunction);
		}

		public final Builder addAttrAccessorBoolean(final IFunction<BooleanLangObject>[] m) {
			return addAttrAccessor(m, attrAccessorBoolean);
		}

		public final Builder addAttrAccessorNumber(final IFunction<NumberLangObject>[] m) {
			return addAttrAccessor(m, attrAccessorNumber);
		}

		public final Builder addAttrAccessorHash(final IFunction<HashLangObject>[] m) {
			return addAttrAccessor(m, attrAccessorHash);
		}

		public final Builder addAttrAccessorArray(final IFunction<ArrayLangObject>[] m) {
			return addAttrAccessor(m, attrAccessorArray);
		}

		public final Builder addAttrAccessorException(final IFunction<ExceptionLangObject>[] m) {
			return addAttrAccessor(m, attrAccessorException);
		}

		public final Builder setGenericAttrAccessorString(final IFunction<StringLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorString == null) ++ count;
			genericAttrAccessorString = func;
			return this;
		}

		public final Builder setGenericAttrAccessorNumber(final IFunction<NumberLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorNumber == null) ++ count;
			genericAttrAccessorNumber = func;
			return this;
		}

		public final Builder setGenericAttrAccessorBoolean(final IFunction<BooleanLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorBoolean == null) ++ count;
			genericAttrAccessorBoolean = func;
			return this;
		}

		public final Builder setGenericAttrAccessorHash(final IFunction<HashLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorHash == null) ++ count;
			genericAttrAccessorHash = func;
			return this;
		}

		public final Builder setGenericAttrAccessorArray(final IFunction<ArrayLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorArray == null) ++ count;
			genericAttrAccessorArray = func;
			return this;
		}

		public final Builder setGenericAttrAccessorException(final IFunction<ExceptionLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorException == null) ++ count;
			genericAttrAccessorException = func;
			return this;
		}

		public final Builder setGenericAttrAccessorFunction(final IFunction<FunctionLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorFunction == null) ++ count;
			genericAttrAccessorFunction = func;
			return this;
		}

		public final INamespace build() throws IllegalStateException {
			if (count != FIELDS_TO_FILL)
				throw new IllegalStateException("All expression method and attribute accessors must be set.");
			return new GenericNamespace(
					expressionMethodBoolean,
					expressionMethodNumber,
					expressionMethodString,
					expressionMethodArray,
					expressionMethodHash,
					expressionMethodException,
					expressionMethodFunction,
					attrAccessorBoolean,
					attrAccessorNumber,
					attrAccessorString,
					attrAccessorArray,
					attrAccessorHash,
					attrAccessorException,
					attrAccessorFunction,
					genericAttrAccessorBoolean,
					genericAttrAccessorNumber,
					genericAttrAccessorString,
					genericAttrAccessorArray,
					genericAttrAccessorHash,
					genericAttrAccessorException,
					genericAttrAccessorFunction);
		}
	}

	private GenericNamespace(
			final EnumMap<EMethod, IFunction<BooleanLangObject>> expressionMethodBoolean,
			final EnumMap<EMethod, IFunction<NumberLangObject>> expressionMethodNumber,
			final EnumMap<EMethod, IFunction<StringLangObject>> expressionMethodString,
			final EnumMap<EMethod, IFunction<ArrayLangObject>> expressionMethodArray,
			final EnumMap<EMethod, IFunction<HashLangObject>> expressionMethodHash,
			final EnumMap<EMethod, IFunction<ExceptionLangObject>> expressionMethodException,
			final EnumMap<EMethod, IFunction<FunctionLangObject>> expressionMethodFunction,
			final Map<String, IFunction<BooleanLangObject>> attrAccessorBoolean,
			final Map<String, IFunction<NumberLangObject>> attrAccessorNumber,
			final Map<String, IFunction<StringLangObject>> attrAccessorString,
			final Map<String, IFunction<ArrayLangObject>> attrAccessorArray,
			final Map<String, IFunction<HashLangObject>> attrAccessorHash,
			final Map<String, IFunction<ExceptionLangObject>> attrAccessorException,
			final Map<String, IFunction<FunctionLangObject>> attrAccessorFunction,
			final IFunction<BooleanLangObject> genericAttrAccessorBoolean,
			final IFunction<NumberLangObject> genericAttrAccessorNumber,
			final IFunction<StringLangObject> genericAttrAccessorString,
			final IFunction<ArrayLangObject> genericAttrAccessorArray,
			final IFunction<HashLangObject> genericAttrAccessorHash,
			final IFunction<ExceptionLangObject> genericAttrAccessorExcepton,
			final IFunction<FunctionLangObject> genericAttrAccessorFunction) {
		this.expressionMethodBoolean = Maps.immutableEnumMap(expressionMethodBoolean);
		this.expressionMethodNumber = Maps.immutableEnumMap(expressionMethodNumber);
		this.expressionMethodString = Maps.immutableEnumMap(expressionMethodString);
		this.expressionMethodArray = Maps.immutableEnumMap(expressionMethodArray);
		this.expressionMethodHash = Maps.immutableEnumMap(expressionMethodHash);
		this.expressionMethodException = Maps.immutableEnumMap(expressionMethodException);
		this.expressionMethodFunction = Maps.immutableEnumMap(expressionMethodFunction);
		this.attrAccessorBoolean = ImmutableMap.copyOf(attrAccessorBoolean);
		this.attrAccessorNumber = ImmutableMap.copyOf(attrAccessorNumber);
		this.attrAccessorString = ImmutableMap.copyOf(attrAccessorString);
		this.attrAccessorArray = ImmutableMap.copyOf(attrAccessorArray);
		this.attrAccessorHash = ImmutableMap.copyOf(attrAccessorHash);
		this.attrAccessorException = ImmutableMap.copyOf(attrAccessorException);
		this.attrAccessorFunction = ImmutableMap.copyOf(attrAccessorFunction);
		this.genericAttrAccessorArray = genericAttrAccessorArray;
		this.genericAttrAccessorBoolean = genericAttrAccessorBoolean;
		this.genericAttrAccessorException = genericAttrAccessorExcepton;
		this.genericAttrAccessorFunction = genericAttrAccessorFunction;
		this.genericAttrAccessorHash = genericAttrAccessorHash;
		this.genericAttrAccessorNumber = genericAttrAccessorNumber;
		this.genericAttrAccessorString = genericAttrAccessorString;
	}

	@Override
	public IFunction<StringLangObject> expressionMethodString(final EMethod method) throws EvaluationException {
		return expressionMethodString.get(method);
	}

	@Override
	public IFunction<NumberLangObject> expressionMethodNumber(final EMethod method) throws EvaluationException {
		return expressionMethodNumber.get(method);
	}

	@Override
	public IFunction<ArrayLangObject> expressionMethodArray(final EMethod method) throws EvaluationException {
		return expressionMethodArray.get(method);
	}

	@Override
	public IFunction<HashLangObject> expressionMethodHash(final EMethod method) throws EvaluationException {
		return expressionMethodHash.get(method);
	}

	@Override
	public IFunction<BooleanLangObject> expressionMethodBoolean(final EMethod method) throws EvaluationException {
		return expressionMethodBoolean.get(method);
	}

	@Override
	public IFunction<ExceptionLangObject> expressionMethodException(final EMethod method) throws EvaluationException {
		return expressionMethodException.get(method);
	}

	@Override
	public IFunction<FunctionLangObject> expressionMethodFunction(final EMethod method) throws EvaluationException {
		return expressionMethodFunction.get(method);
	}

	@Override
	public IFunction<StringLangObject> attrAccessorString(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<StringLangObject> func = attrAccessorString.get(name);
		return func != null ? func : genericAttrAccessorString;
	}

	@Override
	public IFunction<NumberLangObject> attrAccessorNumber(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<NumberLangObject> func = attrAccessorNumber.get(name);
		return func != null ? func : genericAttrAccessorNumber;
	}

	@Override
	public IFunction<ArrayLangObject> attrAccessorArray(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<ArrayLangObject> func = attrAccessorArray.get(name);
		return func != null ? func : genericAttrAccessorArray;

	}

	@Override
	public IFunction<HashLangObject> attrAccessorHash(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<HashLangObject> func = attrAccessorHash.get(name);
		return func != null ? func : genericAttrAccessorHash;

	}

	@Override
	public IFunction<BooleanLangObject> attrAccessorBoolean(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<BooleanLangObject> func = attrAccessorBoolean.get(name);
		return func != null ? func : genericAttrAccessorBoolean;
	}

	@Override
	public IFunction<ExceptionLangObject> attrAccessorException(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<ExceptionLangObject> func = attrAccessorException.get(name);
		return func != null ? func : genericAttrAccessorException;
	}

	@Override
	public IFunction<FunctionLangObject> attrAccessorFunction(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<FunctionLangObject> func = attrAccessorFunction.get(name);
		return func != null ? func : genericAttrAccessorFunction;
	}
}