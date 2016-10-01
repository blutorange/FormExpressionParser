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
import de.xima.fc.form.expression.impl.function.EAttrAccessorArray;
import de.xima.fc.form.expression.impl.function.EAttrAccessorBoolean;
import de.xima.fc.form.expression.impl.function.EAttrAccessorException;
import de.xima.fc.form.expression.impl.function.EAttrAccessorFunction;
import de.xima.fc.form.expression.impl.function.EAttrAccessorHash;
import de.xima.fc.form.expression.impl.function.EAttrAccessorNumber;
import de.xima.fc.form.expression.impl.function.EAttrAccessorString;
import de.xima.fc.form.expression.impl.function.EAttrAssignerArray;
import de.xima.fc.form.expression.impl.function.EAttrAssignerBoolean;
import de.xima.fc.form.expression.impl.function.EAttrAssignerException;
import de.xima.fc.form.expression.impl.function.EAttrAssignerFunction;
import de.xima.fc.form.expression.impl.function.EAttrAssignerHash;
import de.xima.fc.form.expression.impl.function.EAttrAssignerNumber;
import de.xima.fc.form.expression.impl.function.EAttrAssignerString;
import de.xima.fc.form.expression.impl.function.EExpressionMethodArray;
import de.xima.fc.form.expression.impl.function.EExpressionMethodBoolean;
import de.xima.fc.form.expression.impl.function.EExpressionMethodException;
import de.xima.fc.form.expression.impl.function.EExpressionMethodFunction;
import de.xima.fc.form.expression.impl.function.EExpressionMethodHash;
import de.xima.fc.form.expression.impl.function.EExpressionMethodNumber;
import de.xima.fc.form.expression.impl.function.EExpressionMethodString;
import de.xima.fc.form.expression.impl.function.GenericAttrAccessor;
import de.xima.fc.form.expression.impl.function.GenericAttrAssigner;
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
 * Construct it with the builder; or use {@link #getGenericNamespaceInstance()} which
 * makes use of the methods from {@link de.xima.fc.form.expression.impl.function}.
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

	private final ImmutableMap<StringLangObject, IFunction<StringLangObject>> attrAccessorString;
	private final ImmutableMap<StringLangObject, IFunction<ArrayLangObject>> attrAccessorArray;
	private final ImmutableMap<StringLangObject, IFunction<BooleanLangObject>> attrAccessorBoolean;
	private final ImmutableMap<StringLangObject, IFunction<NumberLangObject>> attrAccessorNumber;
	private final ImmutableMap<StringLangObject, IFunction<HashLangObject>> attrAccessorHash;
	private final ImmutableMap<StringLangObject, IFunction<ExceptionLangObject>> attrAccessorException;
	private final ImmutableMap<StringLangObject, IFunction<FunctionLangObject>> attrAccessorFunction;

	private final ImmutableMap<StringLangObject, IFunction<StringLangObject>> attrAssignerString;
	private final ImmutableMap<StringLangObject, IFunction<ArrayLangObject>> attrAssignerArray;
	private final ImmutableMap<StringLangObject, IFunction<BooleanLangObject>> attrAssignerBoolean;
	private final ImmutableMap<StringLangObject, IFunction<NumberLangObject>> attrAssignerNumber;
	private final ImmutableMap<StringLangObject, IFunction<HashLangObject>> attrAssignerHash;
	private final ImmutableMap<StringLangObject, IFunction<ExceptionLangObject>> attrAssignerException;
	private final ImmutableMap<StringLangObject, IFunction<FunctionLangObject>> attrAssignerFunction;

	private final IFunction<StringLangObject> genericAttrAccessorString;
	private final IFunction<NumberLangObject> genericAttrAccessorNumber;
	private final IFunction<BooleanLangObject> genericAttrAccessorBoolean;
	private final IFunction<ArrayLangObject> genericAttrAccessorArray;
	private final IFunction<HashLangObject> genericAttrAccessorHash;
	private final IFunction<FunctionLangObject> genericAttrAccessorFunction;
	private final IFunction<ExceptionLangObject> genericAttrAccessorException;

	private final IFunction<StringLangObject> genericAttrAssignerString;
	private final IFunction<NumberLangObject> genericAttrAssignerNumber;
	private final IFunction<BooleanLangObject> genericAttrAssignerBoolean;
	private final IFunction<ArrayLangObject> genericAttrAssignerArray;
	private final IFunction<HashLangObject> genericAttrAssignerHash;
	private final IFunction<FunctionLangObject> genericAttrAssignerFunction;
	private final IFunction<ExceptionLangObject> genericAttrAssignerException;

	public static class Builder {
		private final static int FIELDS_TO_FILL = 5*7;
		private EnumMap<EMethod, IFunction<StringLangObject>> expressionMethodString;
		private EnumMap<EMethod, IFunction<ArrayLangObject>> expressionMethodArray;
		private EnumMap<EMethod, IFunction<BooleanLangObject>> expressionMethodBoolean;
		private EnumMap<EMethod, IFunction<NumberLangObject>> expressionMethodNumber;
		private EnumMap<EMethod, IFunction<HashLangObject>> expressionMethodHash;
		private EnumMap<EMethod, IFunction<ExceptionLangObject>> expressionMethodException;
		private EnumMap<EMethod, IFunction<FunctionLangObject>> expressionMethodFunction;

		private Map<StringLangObject, IFunction<StringLangObject>> attrAccessorString;
		private Map<StringLangObject, IFunction<ArrayLangObject>> attrAccessorArray;
		private Map<StringLangObject, IFunction<BooleanLangObject>> attrAccessorBoolean;
		private Map<StringLangObject, IFunction<NumberLangObject>> attrAccessorNumber;
		private Map<StringLangObject, IFunction<HashLangObject>> attrAccessorHash;
		private Map<StringLangObject, IFunction<ExceptionLangObject>> attrAccessorException;
		private Map<StringLangObject, IFunction<FunctionLangObject>> attrAccessorFunction;

		private Map<StringLangObject, IFunction<StringLangObject>> attrAssignerString;
		private Map<StringLangObject, IFunction<ArrayLangObject>> attrAssignerArray;
		private Map<StringLangObject, IFunction<BooleanLangObject>> attrAssignerBoolean;
		private Map<StringLangObject, IFunction<NumberLangObject>> attrAssignerNumber;
		private Map<StringLangObject, IFunction<HashLangObject>> attrAssignerHash;
		private Map<StringLangObject, IFunction<ExceptionLangObject>> attrAssignerException;
		private Map<StringLangObject, IFunction<FunctionLangObject>> attrAssignerFunction;

		private IFunction<StringLangObject> genericAttrAccessorString;
		private IFunction<NumberLangObject> genericAttrAccessorNumber;
		private IFunction<BooleanLangObject> genericAttrAccessorBoolean;
		private IFunction<ArrayLangObject> genericAttrAccessorArray;
		private IFunction<HashLangObject> genericAttrAccessorHash;
		private IFunction<FunctionLangObject> genericAttrAccessorFunction;
		private IFunction<ExceptionLangObject> genericAttrAccessorException;

		private IFunction<StringLangObject> genericAttrAssignerString;
		private IFunction<NumberLangObject> genericAttrAssignerNumber;
		private IFunction<BooleanLangObject> genericAttrAssignerBoolean;
		private IFunction<ArrayLangObject> genericAttrAssignerArray;
		private IFunction<HashLangObject> genericAttrAssignerHash;
		private IFunction<FunctionLangObject> genericAttrAssignerFunction;
		private IFunction<ExceptionLangObject> genericAttrAssignerException;

		private int count = 0;

		public Builder() {
			init();
		}

		private final <T extends ALangObject> Map<StringLangObject, IFunction<T>> addAttrAccessor(final IFunction<T>[] m, Map<StringLangObject, IFunction<T>> map) {
			if (map == null) {
				++count;
				map = new HashMap<>();
			}
			for (final IFunction<T> f : m)
				map.put(StringLangObject.create(f.getDeclaredName()), f);
			return map;
		}

		private final <T extends ALangObject> Map<StringLangObject, IFunction<T>> addAttrAssigner(final IFunction<T>[] m, Map<StringLangObject, IFunction<T>> map) {
			if (map == null) {
				++count;
				map = new HashMap<>();
			}
			for (final IFunction<T> f : m)
				map.put(StringLangObject.create(f.getDeclaredName()), f);
			return map;
		}

		private final <T extends ALangObject> EnumMap<EMethod, IFunction<T>> addExpressionMethod(final IMethod2Function<T>[] m, EnumMap<EMethod, IFunction<T>> map) {
			if (map == null) {
				++count;
				map = new EnumMap<>(EMethod.class);
			}
			for (final IMethod2Function<T> f : m)
				map.put(f.getMethod(), f.getFunction());
			return map;
		}

		public final Builder addExpressionMethodString(final IMethod2Function<StringLangObject>[] m) {
			expressionMethodString = addExpressionMethod(m, expressionMethodString);
			return this;
		}
		public final Builder addExpressionMethodBoolean(final IMethod2Function<BooleanLangObject>[] m) {
			expressionMethodBoolean = addExpressionMethod(m, expressionMethodBoolean);
			return this;
		}
		public final Builder addExpressionMethodNumber(final IMethod2Function<NumberLangObject>[] m) {
			expressionMethodNumber = addExpressionMethod(m, expressionMethodNumber);
			return this;
		}
		public final Builder addExpressionMethodHash(final IMethod2Function<HashLangObject>[] m) {
			expressionMethodHash = addExpressionMethod(m, expressionMethodHash);
			return this;
		}
		public final Builder addExpressionMethodArray(final IMethod2Function<ArrayLangObject>[] m) {
			expressionMethodArray = addExpressionMethod(m, expressionMethodArray);
			return this;
		}
		public final Builder addExpressionMethodException(final IMethod2Function<ExceptionLangObject>[] m) {
			expressionMethodException = addExpressionMethod(m, expressionMethodException);
			return this;
		}
		public final Builder addExpressionMethodFunction(final IMethod2Function<FunctionLangObject>[] m) {
			expressionMethodFunction = addExpressionMethod(m, expressionMethodFunction);
			return this;
		}

		public final Builder addAttrAccessorString(final IFunction<StringLangObject>[] m) {
			attrAccessorString = addAttrAccessor(m, attrAccessorString);
			return this;
		}
		public final Builder addAttrAccessorFunction(final IFunction<FunctionLangObject>[] m) {
			attrAccessorFunction = addAttrAccessor(m, attrAccessorFunction);
			return this;
		}
		public final Builder addAttrAccessorBoolean(final IFunction<BooleanLangObject>[] m) {
			attrAccessorBoolean = addAttrAccessor(m, attrAccessorBoolean);
			return this;
		}
		public final Builder addAttrAccessorNumber(final IFunction<NumberLangObject>[] m) {
			attrAccessorNumber = addAttrAccessor(m, attrAccessorNumber);
			return this;
		}
		public final Builder addAttrAccessorHash(final IFunction<HashLangObject>[] m) {
			attrAccessorHash = addAttrAccessor(m, attrAccessorHash);
			return this;
		}
		public final Builder addAttrAccessorArray(final IFunction<ArrayLangObject>[] m) {
			attrAccessorArray = addAttrAccessor(m, attrAccessorArray);
			return this;
		}
		public final Builder addAttrAccessorException(final IFunction<ExceptionLangObject>[] m) {
			attrAccessorException = addAttrAccessor(m, attrAccessorException);
			return this;
		}

		public final Builder addAttrAssignerString(final IFunction<StringLangObject>[] m) {
			attrAssignerString = addAttrAssigner(m, attrAssignerString);
			return this;
		}
		public final Builder addAttrAssignerFunction(final IFunction<FunctionLangObject>[] m) {
			attrAssignerFunction = addAttrAssigner(m, attrAssignerFunction);
			return this;
		}
		public final Builder addAttrAssignerBoolean(final IFunction<BooleanLangObject>[] m) {
			attrAssignerBoolean = addAttrAssigner(m, attrAssignerBoolean);
			return this;
		}
		public final Builder addAttrAssignerNumber(final IFunction<NumberLangObject>[] m) {
			attrAssignerNumber = addAttrAssigner(m, attrAssignerNumber);
			return this;
		}
		public final Builder addAttrAssignerHash(final IFunction<HashLangObject>[] m) {
			attrAssignerHash = addAttrAssigner(m, attrAssignerHash);
			return this;
		}
		public final Builder addAttrAssignerArray(final IFunction<ArrayLangObject>[] m) {
			attrAssignerArray = addAttrAssigner(m, attrAssignerArray);
			return this;
		}
		public final Builder addAttrAssignerException(final IFunction<ExceptionLangObject>[] m) {
			attrAssignerException = addAttrAssigner(m, attrAssignerException);
			return this;
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

		public final Builder setGenericAttrAssignerString(final IFunction<StringLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerString == null) ++ count;
			genericAttrAssignerString = func;
			return this;
		}
		public final Builder setGenericAttrAssignerNumber(final IFunction<NumberLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerNumber == null) ++ count;
			genericAttrAssignerNumber = func;
			return this;
		}
		public final Builder setGenericAttrAssignerBoolean(final IFunction<BooleanLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerBoolean == null) ++ count;
			genericAttrAssignerBoolean = func;
			return this;
		}
		public final Builder setGenericAttrAssignerHash(final IFunction<HashLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerHash == null) ++ count;
			genericAttrAssignerHash = func;
			return this;
		}
		public final Builder setGenericAttrAssignerArray(final IFunction<ArrayLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerArray == null) ++ count;
			genericAttrAssignerArray = func;
			return this;
		}
		public final Builder setGenericAttrAssignerException(final IFunction<ExceptionLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerException == null) ++ count;
			genericAttrAssignerException = func;
			return this;
		}
		public final Builder setGenericAttrAssignerFunction(final IFunction<FunctionLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerFunction == null) ++ count;
			genericAttrAssignerFunction = func;
			return this;
		}

		private void init() {
			count = 0;
			genericAttrAccessorArray = null;
			genericAttrAccessorBoolean = null;
			genericAttrAccessorException = null;
			genericAttrAccessorFunction = null;
			genericAttrAccessorHash = null;
			genericAttrAccessorNumber = null;
			genericAttrAccessorString = null;
			genericAttrAssignerArray = null;
			genericAttrAssignerBoolean = null;
			genericAttrAssignerException = null;
			genericAttrAssignerFunction = null;
			genericAttrAssignerHash = null;
			genericAttrAssignerNumber = null;
			genericAttrAssignerString = null;
			expressionMethodArray = null;
			expressionMethodBoolean = null;
			expressionMethodException = null;
			expressionMethodFunction = null;
			expressionMethodHash = null;
			expressionMethodNumber = null;
			expressionMethodString = null;
			attrAccessorArray = null;
			attrAccessorBoolean = null;
			attrAccessorException = null;
			attrAccessorFunction = null;
			attrAccessorHash = null;
			attrAccessorNumber = null;
			attrAccessorString = null;
			attrAssignerArray = null;
			attrAssignerBoolean = null;
			attrAssignerException = null;
			attrAssignerFunction = null;
			attrAssignerHash = null;
			attrAssignerNumber = null;
			attrAssignerString = null;
		}

		public final INamespace build() throws IllegalStateException {
			if (count != FIELDS_TO_FILL)
				throw new IllegalStateException(
						"All expression method, attribute accessors, and attribute assigners must be set.");
			final GenericNamespace retVal = new GenericNamespace(
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

					attrAssignerBoolean,
					attrAssignerNumber,
					attrAssignerString,
					attrAssignerArray,
					attrAssignerHash,
					attrAssignerException,
					attrAssignerFunction,

					genericAttrAccessorBoolean,
					genericAttrAccessorNumber,
					genericAttrAccessorString,
					genericAttrAccessorArray,
					genericAttrAccessorHash,
					genericAttrAccessorException,
					genericAttrAccessorFunction,

					genericAttrAssignerBoolean,
					genericAttrAssignerNumber,
					genericAttrAssignerString,
					genericAttrAssignerArray,
					genericAttrAssignerHash,
					genericAttrAssignerException,
					genericAttrAssignerFunction);
			init();
			return retVal;
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

			final Map<StringLangObject, IFunction<BooleanLangObject>> attrAccessorBoolean,
			final Map<StringLangObject, IFunction<NumberLangObject>> attrAccessorNumber,
			final Map<StringLangObject, IFunction<StringLangObject>> attrAccessorString,
			final Map<StringLangObject, IFunction<ArrayLangObject>> attrAccessorArray,
			final Map<StringLangObject, IFunction<HashLangObject>> attrAccessorHash,
			final Map<StringLangObject, IFunction<ExceptionLangObject>> attrAccessorException,
			final Map<StringLangObject, IFunction<FunctionLangObject>> attrAccessorFunction,

			final Map<StringLangObject, IFunction<BooleanLangObject>> attrAssignerBoolean,
			final Map<StringLangObject, IFunction<NumberLangObject>> attrAssignerNumber,
			final Map<StringLangObject, IFunction<StringLangObject>> attrAssignerString,
			final Map<StringLangObject, IFunction<ArrayLangObject>> attrAssignerArray,
			final Map<StringLangObject, IFunction<HashLangObject>> attrAssignerHash,
			final Map<StringLangObject, IFunction<ExceptionLangObject>> attrAssignerException,
			final Map<StringLangObject, IFunction<FunctionLangObject>> attrAssignerFunction,

			final IFunction<BooleanLangObject> genericAttrAccessorBoolean,
			final IFunction<NumberLangObject> genericAttrAccessorNumber,
			final IFunction<StringLangObject> genericAttrAccessorString,
			final IFunction<ArrayLangObject> genericAttrAccessorArray,
			final IFunction<HashLangObject> genericAttrAccessorHash,
			final IFunction<ExceptionLangObject> genericAttrAccessorExcepton,
			final IFunction<FunctionLangObject> genericAttrAccessorFunction,

			final IFunction<BooleanLangObject> genericAttrAssignerBoolean,
			final IFunction<NumberLangObject> genericAttrAssignerNumber,
			final IFunction<StringLangObject> genericAttrAssignerString,
			final IFunction<ArrayLangObject> genericAttrAssignerArray,
			final IFunction<HashLangObject> genericAttrAssignerHash,
			final IFunction<ExceptionLangObject> genericAttrAssignerExcepton,
			final IFunction<FunctionLangObject> genericAttrAssignerFunction) {

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

		this.attrAssignerBoolean = ImmutableMap.copyOf(attrAssignerBoolean);
		this.attrAssignerNumber = ImmutableMap.copyOf(attrAssignerNumber);
		this.attrAssignerString = ImmutableMap.copyOf(attrAssignerString);
		this.attrAssignerArray = ImmutableMap.copyOf(attrAssignerArray);
		this.attrAssignerHash = ImmutableMap.copyOf(attrAssignerHash);
		this.attrAssignerException = ImmutableMap.copyOf(attrAssignerException);
		this.attrAssignerFunction = ImmutableMap.copyOf(attrAssignerFunction);

		this.genericAttrAccessorArray = genericAttrAccessorArray;
		this.genericAttrAccessorBoolean = genericAttrAccessorBoolean;
		this.genericAttrAccessorException = genericAttrAccessorExcepton;
		this.genericAttrAccessorFunction = genericAttrAccessorFunction;
		this.genericAttrAccessorHash = genericAttrAccessorHash;
		this.genericAttrAccessorNumber = genericAttrAccessorNumber;
		this.genericAttrAccessorString = genericAttrAccessorString;

		this.genericAttrAssignerArray = genericAttrAssignerArray;
		this.genericAttrAssignerBoolean = genericAttrAssignerBoolean;
		this.genericAttrAssignerException = genericAttrAssignerExcepton;
		this.genericAttrAssignerFunction = genericAttrAssignerFunction;
		this.genericAttrAssignerHash = genericAttrAssignerHash;
		this.genericAttrAssignerNumber = genericAttrAssignerNumber;
		this.genericAttrAssignerString = genericAttrAssignerString;
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

	@Override
	public IFunction<StringLangObject> attrAssignerString(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<StringLangObject> func = attrAssignerString.get(name);
		return func != null ? func : genericAttrAssignerString;
	}
	@Override
	public IFunction<NumberLangObject> attrAssignerNumber(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<NumberLangObject> func = attrAssignerNumber.get(name);
		return func != null ? func : genericAttrAssignerNumber;
	}
	@Override
	public IFunction<ArrayLangObject> attrAssignerArray(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<ArrayLangObject> func = attrAssignerArray.get(name);
		return func != null ? func : genericAttrAssignerArray;

	}
	@Override
	public IFunction<HashLangObject> attrAssignerHash(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<HashLangObject> func = attrAssignerHash.get(name);
		return func != null ? func : genericAttrAssignerHash;

	}
	@Override
	public IFunction<BooleanLangObject> attrAssignerBoolean(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<BooleanLangObject> func = attrAssignerBoolean.get(name);
		return func != null ? func : genericAttrAssignerBoolean;
	}
	@Override
	public IFunction<ExceptionLangObject> attrAssignerException(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<ExceptionLangObject> func = attrAssignerException.get(name);
		return func != null ? func : genericAttrAssignerException;
	}
	@Override
	public IFunction<FunctionLangObject> attrAssignerFunction(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<FunctionLangObject> func = attrAssignerFunction.get(name);
		return func != null ? func : genericAttrAssignerFunction;
	}

	public static INamespace getGenericNamespaceInstance() {
		return InstanceHolder.GENERIC;
	}

	private final static class InstanceHolder {
		public final static INamespace GENERIC;
		static {
			final GenericNamespace.Builder builder = new GenericNamespace.Builder();

			builder.addExpressionMethodBoolean(EExpressionMethodBoolean.values());
			builder.addExpressionMethodNumber(EExpressionMethodNumber.values());
			builder.addExpressionMethodString(EExpressionMethodString.values());
			builder.addExpressionMethodArray(EExpressionMethodArray.values());
			builder.addExpressionMethodHash(EExpressionMethodHash.values());
			builder.addExpressionMethodException(EExpressionMethodException.values());
			builder.addExpressionMethodFunction(EExpressionMethodFunction.values());

			builder.addAttrAccessorBoolean(EAttrAccessorBoolean.values());
			builder.addAttrAccessorNumber(EAttrAccessorNumber.values());
			builder.addAttrAccessorString(EAttrAccessorString.values());
			builder.addAttrAccessorArray(EAttrAccessorArray.values());
			builder.addAttrAccessorHash(EAttrAccessorHash.values());
			builder.addAttrAccessorException(EAttrAccessorException.values());
			builder.addAttrAccessorFunction(EAttrAccessorFunction.values());

			builder.addAttrAssignerBoolean(EAttrAssignerBoolean.values());
			builder.addAttrAssignerNumber(EAttrAssignerNumber.values());
			builder.addAttrAssignerString(EAttrAssignerString.values());
			builder.addAttrAssignerArray(EAttrAssignerArray.values());
			builder.addAttrAssignerHash(EAttrAssignerHash.values());
			builder.addAttrAssignerException(EAttrAssignerException.values());
			builder.addAttrAssignerFunction(EAttrAssignerFunction.values());

			builder.setGenericAttrAccessorArray(GenericAttrAccessor.ARRAY);
			builder.setGenericAttrAccessorBoolean(GenericAttrAccessor.BOOLEAN);
			builder.setGenericAttrAccessorException(GenericAttrAccessor.EXCEPTION);
			builder.setGenericAttrAccessorFunction(GenericAttrAccessor.FUNCTION);
			builder.setGenericAttrAccessorHash(GenericAttrAccessor.HASH);
			builder.setGenericAttrAccessorNumber(GenericAttrAccessor.NUMBER);
			builder.setGenericAttrAccessorString(GenericAttrAccessor.STRING);

			builder.setGenericAttrAssignerArray(GenericAttrAssigner.ARRAY);
			builder.setGenericAttrAssignerBoolean(GenericAttrAssigner.BOOLEAN);
			builder.setGenericAttrAssignerException(GenericAttrAssigner.EXCEPTION);
			builder.setGenericAttrAssignerFunction(GenericAttrAssigner.FUNCTION);
			builder.setGenericAttrAssignerHash(GenericAttrAssigner.HASH);
			builder.setGenericAttrAssignerNumber(GenericAttrAssigner.NUMBER);
			builder.setGenericAttrAssignerString(GenericAttrAssigner.STRING);

			GENERIC = builder.build();
		}
	}
}