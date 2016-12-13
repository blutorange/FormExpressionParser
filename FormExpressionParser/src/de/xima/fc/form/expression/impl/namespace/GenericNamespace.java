package de.xima.fc.form.expression.impl.namespace;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import static com.google.common.base.Preconditions.checkNotNull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.IMethod2Function;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.impl.function.EAttrAccessorArray;
import de.xima.fc.form.expression.impl.function.EAttrAccessorBoolean;
import de.xima.fc.form.expression.impl.function.EAttrAccessorException;
import de.xima.fc.form.expression.impl.function.EAttrAccessorFunction;
import de.xima.fc.form.expression.impl.function.EAttrAccessorHash;
import de.xima.fc.form.expression.impl.function.EAttrAccessorNumber;
import de.xima.fc.form.expression.impl.function.EAttrAccessorRegex;
import de.xima.fc.form.expression.impl.function.EAttrAccessorString;
import de.xima.fc.form.expression.impl.function.EAttrAssignerArray;
import de.xima.fc.form.expression.impl.function.EAttrAssignerBoolean;
import de.xima.fc.form.expression.impl.function.EAttrAssignerException;
import de.xima.fc.form.expression.impl.function.EAttrAssignerFunction;
import de.xima.fc.form.expression.impl.function.EAttrAssignerHash;
import de.xima.fc.form.expression.impl.function.EAttrAssignerNumber;
import de.xima.fc.form.expression.impl.function.EAttrAssignerRegex;
import de.xima.fc.form.expression.impl.function.EAttrAssignerString;
import de.xima.fc.form.expression.impl.function.EExpressionMethodArray;
import de.xima.fc.form.expression.impl.function.EExpressionMethodBoolean;
import de.xima.fc.form.expression.impl.function.EExpressionMethodException;
import de.xima.fc.form.expression.impl.function.EExpressionMethodFunction;
import de.xima.fc.form.expression.impl.function.EExpressionMethodHash;
import de.xima.fc.form.expression.impl.function.EExpressionMethodNumber;
import de.xima.fc.form.expression.impl.function.EExpressionMethodRegex;
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
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

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
	private final ImmutableMap<EMethod, IFunction<RegexLangObject>> expressionMethodRegex;

	private final ImmutableMap<StringLangObject, IFunction<StringLangObject>> attrAccessorString;
	private final ImmutableMap<StringLangObject, IFunction<ArrayLangObject>> attrAccessorArray;
	private final ImmutableMap<StringLangObject, IFunction<BooleanLangObject>> attrAccessorBoolean;
	private final ImmutableMap<StringLangObject, IFunction<NumberLangObject>> attrAccessorNumber;
	private final ImmutableMap<StringLangObject, IFunction<HashLangObject>> attrAccessorHash;
	private final ImmutableMap<StringLangObject, IFunction<ExceptionLangObject>> attrAccessorException;
	private final ImmutableMap<StringLangObject, IFunction<FunctionLangObject>> attrAccessorFunction;
	private final ImmutableMap<StringLangObject, IFunction<RegexLangObject>> attrAccessorRegex;

	private final ImmutableMap<StringLangObject, IFunction<StringLangObject>> attrAssignerString;
	private final ImmutableMap<StringLangObject, IFunction<ArrayLangObject>> attrAssignerArray;
	private final ImmutableMap<StringLangObject, IFunction<BooleanLangObject>> attrAssignerBoolean;
	private final ImmutableMap<StringLangObject, IFunction<NumberLangObject>> attrAssignerNumber;
	private final ImmutableMap<StringLangObject, IFunction<HashLangObject>> attrAssignerHash;
	private final ImmutableMap<StringLangObject, IFunction<ExceptionLangObject>> attrAssignerException;
	private final ImmutableMap<StringLangObject, IFunction<FunctionLangObject>> attrAssignerFunction;
	private final ImmutableMap<StringLangObject, IFunction<RegexLangObject>> attrAssignerRegex;

	private final IFunction<StringLangObject> genericAttrAccessorString;
	private final IFunction<NumberLangObject> genericAttrAccessorNumber;
	private final IFunction<BooleanLangObject> genericAttrAccessorBoolean;
	private final IFunction<ArrayLangObject> genericAttrAccessorArray;
	private final IFunction<HashLangObject> genericAttrAccessorHash;
	private final IFunction<FunctionLangObject> genericAttrAccessorFunction;
	private final IFunction<ExceptionLangObject> genericAttrAccessorException;
	private final IFunction<RegexLangObject> genericAttrAccessorRegex;

	private final IFunction<StringLangObject> genericAttrAssignerString;
	private final IFunction<NumberLangObject> genericAttrAssignerNumber;
	private final IFunction<BooleanLangObject> genericAttrAssignerBoolean;
	private final IFunction<ArrayLangObject> genericAttrAssignerArray;
	private final IFunction<HashLangObject> genericAttrAssignerHash;
	private final IFunction<FunctionLangObject> genericAttrAssignerFunction;
	private final IFunction<ExceptionLangObject> genericAttrAssignerException;
	private final IFunction<RegexLangObject> genericAttrAssignerRegex;

	public static class Builder {
		private final static int FIELDS_TO_FILL = 5*8;
		@Nullable private EnumMap<EMethod, IFunction<StringLangObject>> expressionMethodString;
		@Nullable private EnumMap<EMethod, IFunction<ArrayLangObject>> expressionMethodArray;
		@Nullable private EnumMap<EMethod, IFunction<BooleanLangObject>> expressionMethodBoolean;
		@Nullable private EnumMap<EMethod, IFunction<NumberLangObject>> expressionMethodNumber;
		@Nullable private EnumMap<EMethod, IFunction<HashLangObject>> expressionMethodHash;
		@Nullable private EnumMap<EMethod, IFunction<ExceptionLangObject>> expressionMethodException;
		@Nullable private EnumMap<EMethod, IFunction<FunctionLangObject>> expressionMethodFunction;
		@Nullable private EnumMap<EMethod, IFunction<RegexLangObject>> expressionMethodRegex;

		@Nullable private Map<StringLangObject, IFunction<StringLangObject>> attrAccessorString;
		@Nullable private Map<StringLangObject, IFunction<ArrayLangObject>> attrAccessorArray;
		@Nullable private Map<StringLangObject, IFunction<BooleanLangObject>> attrAccessorBoolean;
		@Nullable private Map<StringLangObject, IFunction<NumberLangObject>> attrAccessorNumber;
		@Nullable private Map<StringLangObject, IFunction<HashLangObject>> attrAccessorHash;
		@Nullable private Map<StringLangObject, IFunction<ExceptionLangObject>> attrAccessorException;
		@Nullable private Map<StringLangObject, IFunction<FunctionLangObject>> attrAccessorFunction;
		@Nullable private Map<StringLangObject, IFunction<RegexLangObject>> attrAccessorRegex;

		@Nullable private Map<StringLangObject, IFunction<StringLangObject>> attrAssignerString;
		@Nullable private Map<StringLangObject, IFunction<ArrayLangObject>> attrAssignerArray;
		@Nullable private Map<StringLangObject, IFunction<BooleanLangObject>> attrAssignerBoolean;
		@Nullable private Map<StringLangObject, IFunction<NumberLangObject>> attrAssignerNumber;
		@Nullable private Map<StringLangObject, IFunction<HashLangObject>> attrAssignerHash;
		@Nullable private Map<StringLangObject, IFunction<ExceptionLangObject>> attrAssignerException;
		@Nullable private Map<StringLangObject, IFunction<FunctionLangObject>> attrAssignerFunction;
		@Nullable private Map<StringLangObject, IFunction<RegexLangObject>> attrAssignerRegex;

		@Nullable private IFunction<StringLangObject> genericAttrAccessorString;
		@Nullable private IFunction<NumberLangObject> genericAttrAccessorNumber;
		@Nullable private IFunction<BooleanLangObject> genericAttrAccessorBoolean;
		@Nullable private IFunction<ArrayLangObject> genericAttrAccessorArray;
		@Nullable private IFunction<HashLangObject> genericAttrAccessorHash;
		@Nullable private IFunction<FunctionLangObject> genericAttrAccessorFunction;
		@Nullable private IFunction<ExceptionLangObject> genericAttrAccessorException;
		@Nullable private IFunction<RegexLangObject> genericAttrAccessorRegex;

		@Nullable private IFunction<StringLangObject> genericAttrAssignerString;
		@Nullable private IFunction<NumberLangObject> genericAttrAssignerNumber;
		@Nullable private IFunction<BooleanLangObject> genericAttrAssignerBoolean;
		@Nullable private IFunction<ArrayLangObject> genericAttrAssignerArray;
		@Nullable private IFunction<HashLangObject> genericAttrAssignerHash;
		@Nullable private IFunction<FunctionLangObject> genericAttrAssignerFunction;
		@Nullable private IFunction<ExceptionLangObject> genericAttrAssignerException;
		@Nullable private IFunction<RegexLangObject> genericAttrAssignerRegex;

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

		@Nonnull
		public final Builder addExpressionMethodString(final IMethod2Function<StringLangObject>[] m) {
			expressionMethodString = addExpressionMethod(m, expressionMethodString);
			return this;
		}
		@Nonnull
		public final Builder addExpressionMethodBoolean(final IMethod2Function<BooleanLangObject>[] m) {
			expressionMethodBoolean = addExpressionMethod(m, expressionMethodBoolean);
			return this;
		}
		@Nonnull
		public final Builder addExpressionMethodNumber(final IMethod2Function<NumberLangObject>[] m) {
			expressionMethodNumber = addExpressionMethod(m, expressionMethodNumber);
			return this;
		}
		@Nonnull
		public final Builder addExpressionMethodHash(final IMethod2Function<HashLangObject>[] m) {
			expressionMethodHash = addExpressionMethod(m, expressionMethodHash);
			return this;
		}
		@Nonnull
		public final Builder addExpressionMethodArray(final IMethod2Function<ArrayLangObject>[] m) {
			expressionMethodArray = addExpressionMethod(m, expressionMethodArray);
			return this;
		}
		@Nonnull
		public final Builder addExpressionMethodException(final IMethod2Function<ExceptionLangObject>[] m) {
			expressionMethodException = addExpressionMethod(m, expressionMethodException);
			return this;
		}
		@Nonnull
		public final Builder addExpressionMethodFunction(final IMethod2Function<FunctionLangObject>[] m) {
			expressionMethodFunction = addExpressionMethod(m, expressionMethodFunction);
			return this;
		}
		@Nonnull
		public final Builder addExpressionMethodRegex(final IMethod2Function<RegexLangObject>[] m) {
			expressionMethodRegex = addExpressionMethod(m, expressionMethodRegex);
			return this;
		}
		@Nonnull
		public final Builder addAttrAccessorString(final IFunction<StringLangObject>[] m) {
			attrAccessorString = addAttrAccessor(m, attrAccessorString);
			return this;
		}
		@Nonnull
		public final Builder addAttrAccessorFunction(final IFunction<FunctionLangObject>[] m) {
			attrAccessorFunction = addAttrAccessor(m, attrAccessorFunction);
			return this;
		}
		@Nonnull
		public final Builder addAttrAccessorBoolean(final IFunction<BooleanLangObject>[] m) {
			attrAccessorBoolean = addAttrAccessor(m, attrAccessorBoolean);
			return this;
		}
		@Nonnull
		public final Builder addAttrAccessorNumber(final IFunction<NumberLangObject>[] m) {
			attrAccessorNumber = addAttrAccessor(m, attrAccessorNumber);
			return this;
		}
		@Nonnull
		public final Builder addAttrAccessorHash(final IFunction<HashLangObject>[] m) {
			attrAccessorHash = addAttrAccessor(m, attrAccessorHash);
			return this;
		}
		@Nonnull
		public final Builder addAttrAccessorArray(final IFunction<ArrayLangObject>[] m) {
			attrAccessorArray = addAttrAccessor(m, attrAccessorArray);
			return this;
		}
		@Nonnull
		public final Builder addAttrAccessorException(final IFunction<ExceptionLangObject>[] m) {
			attrAccessorException = addAttrAccessor(m, attrAccessorException);
			return this;
		}
		@Nonnull
		public final Builder addAttrAccessorRegex(final IFunction<RegexLangObject>[] m) {
			attrAccessorRegex = addAttrAccessor(m, attrAccessorRegex);
			return this;
		}
		@Nonnull
		public final Builder addAttrAssignerString(final IFunction<StringLangObject>[] m) {
			attrAssignerString = addAttrAssigner(m, attrAssignerString);
			return this;
		}
		@Nonnull
		public final Builder addAttrAssignerFunction(final IFunction<FunctionLangObject>[] m) {
			attrAssignerFunction = addAttrAssigner(m, attrAssignerFunction);
			return this;
		}
		@Nonnull
		public final Builder addAttrAssignerBoolean(final IFunction<BooleanLangObject>[] m) {
			attrAssignerBoolean = addAttrAssigner(m, attrAssignerBoolean);
			return this;
		}
		@Nonnull
		public final Builder addAttrAssignerNumber(final IFunction<NumberLangObject>[] m) {
			attrAssignerNumber = addAttrAssigner(m, attrAssignerNumber);
			return this;
		}
		@Nonnull
		public final Builder addAttrAssignerHash(final IFunction<HashLangObject>[] m) {
			attrAssignerHash = addAttrAssigner(m, attrAssignerHash);
			return this;
		}
		@Nonnull
		public final Builder addAttrAssignerArray(final IFunction<ArrayLangObject>[] m) {
			attrAssignerArray = addAttrAssigner(m, attrAssignerArray);
			return this;
		}
		@Nonnull
		public final Builder addAttrAssignerException(final IFunction<ExceptionLangObject>[] m) {
			attrAssignerException = addAttrAssigner(m, attrAssignerException);
			return this;
		}
		@Nonnull
		public final Builder addAttrAssignerRegex(final IFunction<RegexLangObject>[] m) {
			attrAssignerRegex = addAttrAssigner(m, attrAssignerRegex);
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAccessorString(final IFunction<StringLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorString == null) ++ count;
			genericAttrAccessorString = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAccessorNumber(final IFunction<NumberLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorNumber == null) ++ count;
			genericAttrAccessorNumber = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAccessorBoolean(final IFunction<BooleanLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorBoolean == null) ++ count;
			genericAttrAccessorBoolean = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAccessorHash(final IFunction<HashLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorHash == null) ++ count;
			genericAttrAccessorHash = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAccessorArray(final IFunction<ArrayLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorArray == null) ++ count;
			genericAttrAccessorArray = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAccessorException(final IFunction<ExceptionLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorException == null) ++ count;
			genericAttrAccessorException = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAccessorFunction(final IFunction<FunctionLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorFunction == null) ++ count;
			genericAttrAccessorFunction = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAccessorRegex(final IFunction<RegexLangObject> func) {
			if (func == null) return this;
			if (genericAttrAccessorRegex == null) ++ count;
			genericAttrAccessorRegex = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAssignerString(final IFunction<StringLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerString == null) ++ count;
			genericAttrAssignerString = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAssignerNumber(final IFunction<NumberLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerNumber == null) ++ count;
			genericAttrAssignerNumber = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAssignerBoolean(final IFunction<BooleanLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerBoolean == null) ++ count;
			genericAttrAssignerBoolean = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAssignerHash(final IFunction<HashLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerHash == null) ++ count;
			genericAttrAssignerHash = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAssignerArray(final IFunction<ArrayLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerArray == null) ++ count;
			genericAttrAssignerArray = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAssignerException(final IFunction<ExceptionLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerException == null) ++ count;
			genericAttrAssignerException = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAssignerFunction(final IFunction<FunctionLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerFunction == null) ++ count;
			genericAttrAssignerFunction = func;
			return this;
		}
		@Nonnull
		public final Builder setGenericAttrAssignerRegex(final IFunction<RegexLangObject> func) {
			if (func == null) return this;
			if (genericAttrAssignerRegex == null) ++ count;
			genericAttrAssignerRegex = func;
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
			genericAttrAccessorRegex = null;
			genericAttrAssignerArray = null;
			genericAttrAssignerBoolean = null;
			genericAttrAssignerException = null;
			genericAttrAssignerFunction = null;
			genericAttrAssignerHash = null;
			genericAttrAssignerNumber = null;
			genericAttrAssignerString = null;
			genericAttrAssignerRegex = null;
			expressionMethodArray = null;
			expressionMethodBoolean = null;
			expressionMethodException = null;
			expressionMethodFunction = null;
			expressionMethodHash = null;
			expressionMethodNumber = null;
			expressionMethodString = null;
			expressionMethodRegex = null;
			attrAccessorArray = null;
			attrAccessorBoolean = null;
			attrAccessorException = null;
			attrAccessorFunction = null;
			attrAccessorHash = null;
			attrAccessorNumber = null;
			attrAccessorString = null;
			attrAccessorRegex = null;
			attrAssignerArray = null;
			attrAssignerBoolean = null;
			attrAssignerException = null;
			attrAssignerFunction = null;
			attrAssignerHash = null;
			attrAssignerNumber = null;
			attrAssignerString = null;
			attrAssignerRegex = null;
		}

		//TODO make this return a INamespaceContractFactory
		@Nonnull
		public final INamespace build() throws IllegalStateException {
			if (count != FIELDS_TO_FILL)
				throw new IllegalStateException(CmnCnst.Error.ILLEGAL_STATE_NAMESPACE_BUILDER);
			final GenericNamespace retVal = new GenericNamespace(
					checkNotNull(expressionMethodBoolean),
					checkNotNull(expressionMethodNumber),
					checkNotNull(expressionMethodString),
					checkNotNull(expressionMethodArray),
					checkNotNull(expressionMethodHash),
					checkNotNull(expressionMethodException),
					checkNotNull(expressionMethodFunction),
					checkNotNull(expressionMethodRegex),

					checkNotNull(attrAccessorBoolean),
					checkNotNull(attrAccessorNumber),
					checkNotNull(attrAccessorString),
					checkNotNull(attrAccessorArray),
					checkNotNull(attrAccessorHash),
					checkNotNull(attrAccessorException),
					checkNotNull(attrAccessorFunction),
					checkNotNull(attrAccessorRegex),

					checkNotNull(attrAssignerBoolean),
					checkNotNull(attrAssignerNumber),
					checkNotNull(attrAssignerString),
					checkNotNull(attrAssignerArray),
					checkNotNull(attrAssignerHash),
					checkNotNull(attrAssignerException),
					checkNotNull(attrAssignerFunction),
					checkNotNull(attrAssignerRegex),

					checkNotNull(genericAttrAccessorBoolean),
					checkNotNull(genericAttrAccessorNumber),
					checkNotNull(genericAttrAccessorString),
					checkNotNull(genericAttrAccessorArray),
					checkNotNull(genericAttrAccessorHash),
					checkNotNull(genericAttrAccessorException),
					checkNotNull(genericAttrAccessorFunction),
					checkNotNull(genericAttrAccessorRegex),

					checkNotNull(genericAttrAssignerBoolean),
					checkNotNull(genericAttrAssignerNumber),
					checkNotNull(genericAttrAssignerString),
					checkNotNull(genericAttrAssignerArray),
					checkNotNull(genericAttrAssignerHash),
					checkNotNull(genericAttrAssignerException),
					checkNotNull(genericAttrAssignerFunction),
					checkNotNull(genericAttrAssignerRegex));
			init();
			return retVal;
		}
	}

	private GenericNamespace(
			@Nonnull final EnumMap<EMethod, IFunction<BooleanLangObject>> expressionMethodBoolean,
			@Nonnull final EnumMap<EMethod, IFunction<NumberLangObject>> expressionMethodNumber,
			@Nonnull final EnumMap<EMethod, IFunction<StringLangObject>> expressionMethodString,
			@Nonnull final EnumMap<EMethod, IFunction<ArrayLangObject>> expressionMethodArray,
			@Nonnull final EnumMap<EMethod, IFunction<HashLangObject>> expressionMethodHash,
			@Nonnull final EnumMap<EMethod, IFunction<ExceptionLangObject>> expressionMethodException,
			@Nonnull final EnumMap<EMethod, IFunction<FunctionLangObject>> expressionMethodFunction,
			@Nonnull final EnumMap<EMethod, IFunction<RegexLangObject>> expressionMethodRegex,

			@Nonnull final Map<StringLangObject, IFunction<BooleanLangObject>> attrAccessorBoolean,
			@Nonnull final Map<StringLangObject, IFunction<NumberLangObject>> attrAccessorNumber,
			@Nonnull final Map<StringLangObject, IFunction<StringLangObject>> attrAccessorString,
			@Nonnull final Map<StringLangObject, IFunction<ArrayLangObject>> attrAccessorArray,
			@Nonnull final Map<StringLangObject, IFunction<HashLangObject>> attrAccessorHash,
			@Nonnull final Map<StringLangObject, IFunction<ExceptionLangObject>> attrAccessorException,
			@Nonnull final Map<StringLangObject, IFunction<FunctionLangObject>> attrAccessorFunction,
			@Nonnull final Map<StringLangObject, IFunction<RegexLangObject>> attrAccessorRegex,

			@Nonnull final Map<StringLangObject, IFunction<BooleanLangObject>> attrAssignerBoolean,
			@Nonnull final Map<StringLangObject, IFunction<NumberLangObject>> attrAssignerNumber,
			@Nonnull final Map<StringLangObject, IFunction<StringLangObject>> attrAssignerString,
			@Nonnull final Map<StringLangObject, IFunction<ArrayLangObject>> attrAssignerArray,
			@Nonnull final Map<StringLangObject, IFunction<HashLangObject>> attrAssignerHash,
			@Nonnull final Map<StringLangObject, IFunction<ExceptionLangObject>> attrAssignerException,
			@Nonnull final Map<StringLangObject, IFunction<FunctionLangObject>> attrAssignerFunction,
			@Nonnull final Map<StringLangObject, IFunction<RegexLangObject>> attrAssignerRegex,

			@Nonnull final IFunction<BooleanLangObject> genericAttrAccessorBoolean,
			@Nonnull final IFunction<NumberLangObject> genericAttrAccessorNumber,
			@Nonnull final IFunction<StringLangObject> genericAttrAccessorString,
			@Nonnull final IFunction<ArrayLangObject> genericAttrAccessorArray,
			@Nonnull final IFunction<HashLangObject> genericAttrAccessorHash,
			@Nonnull final IFunction<ExceptionLangObject> genericAttrAccessorExcepton,
			@Nonnull final IFunction<FunctionLangObject> genericAttrAccessorFunction,
			@Nonnull final IFunction<RegexLangObject> genericAttrAccessorRegex,

			@Nonnull final IFunction<BooleanLangObject> genericAttrAssignerBoolean,
			@Nonnull final IFunction<NumberLangObject> genericAttrAssignerNumber,
			@Nonnull final IFunction<StringLangObject> genericAttrAssignerString,
			@Nonnull final IFunction<ArrayLangObject> genericAttrAssignerArray,
			@Nonnull final IFunction<HashLangObject> genericAttrAssignerHash,
			@Nonnull final IFunction<ExceptionLangObject> genericAttrAssignerExcepton,
			@Nonnull final IFunction<FunctionLangObject> genericAttrAssignerFunction,
			@Nonnull final IFunction<RegexLangObject> genericAttrAssignerRegex) {

		this.expressionMethodBoolean = Maps.immutableEnumMap(expressionMethodBoolean);
		this.expressionMethodNumber = Maps.immutableEnumMap(expressionMethodNumber);
		this.expressionMethodString = Maps.immutableEnumMap(expressionMethodString);
		this.expressionMethodArray = Maps.immutableEnumMap(expressionMethodArray);
		this.expressionMethodHash = Maps.immutableEnumMap(expressionMethodHash);
		this.expressionMethodException = Maps.immutableEnumMap(expressionMethodException);
		this.expressionMethodFunction = Maps.immutableEnumMap(expressionMethodFunction);
		this.expressionMethodRegex = Maps.immutableEnumMap(expressionMethodRegex);

		this.attrAccessorBoolean = ImmutableMap.copyOf(attrAccessorBoolean);
		this.attrAccessorNumber = ImmutableMap.copyOf(attrAccessorNumber);
		this.attrAccessorString = ImmutableMap.copyOf(attrAccessorString);
		this.attrAccessorArray = ImmutableMap.copyOf(attrAccessorArray);
		this.attrAccessorHash = ImmutableMap.copyOf(attrAccessorHash);
		this.attrAccessorException = ImmutableMap.copyOf(attrAccessorException);
		this.attrAccessorFunction = ImmutableMap.copyOf(attrAccessorFunction);
		this.attrAccessorRegex = ImmutableMap.copyOf(attrAccessorRegex);

		this.attrAssignerBoolean = ImmutableMap.copyOf(attrAssignerBoolean);
		this.attrAssignerNumber = ImmutableMap.copyOf(attrAssignerNumber);
		this.attrAssignerString = ImmutableMap.copyOf(attrAssignerString);
		this.attrAssignerArray = ImmutableMap.copyOf(attrAssignerArray);
		this.attrAssignerHash = ImmutableMap.copyOf(attrAssignerHash);
		this.attrAssignerException = ImmutableMap.copyOf(attrAssignerException);
		this.attrAssignerFunction = ImmutableMap.copyOf(attrAssignerFunction);
		this.attrAssignerRegex = ImmutableMap.copyOf(attrAssignerRegex);

		this.genericAttrAccessorArray = genericAttrAccessorArray;
		this.genericAttrAccessorBoolean = genericAttrAccessorBoolean;
		this.genericAttrAccessorException = genericAttrAccessorExcepton;
		this.genericAttrAccessorFunction = genericAttrAccessorFunction;
		this.genericAttrAccessorHash = genericAttrAccessorHash;
		this.genericAttrAccessorNumber = genericAttrAccessorNumber;
		this.genericAttrAccessorString = genericAttrAccessorString;
		this.genericAttrAccessorRegex = genericAttrAccessorRegex;

		this.genericAttrAssignerArray = genericAttrAssignerArray;
		this.genericAttrAssignerBoolean = genericAttrAssignerBoolean;
		this.genericAttrAssignerException = genericAttrAssignerExcepton;
		this.genericAttrAssignerFunction = genericAttrAssignerFunction;
		this.genericAttrAssignerHash = genericAttrAssignerHash;
		this.genericAttrAssignerNumber = genericAttrAssignerNumber;
		this.genericAttrAssignerString = genericAttrAssignerString;
		this.genericAttrAssignerRegex = genericAttrAssignerRegex;
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
	public IFunction<RegexLangObject> expressionMethodRegex(final EMethod method) throws EvaluationException {
		return expressionMethodRegex.get(method);
	}

	@SuppressWarnings("null")
	@Override
	public IFunction<StringLangObject> attrAccessorString(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<StringLangObject> func = attrAccessorString.get(name);
		return func != null ? func : genericAttrAccessorString;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<NumberLangObject> attrAccessorNumber(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<NumberLangObject> func = attrAccessorNumber.get(name);
		return func != null ? func : genericAttrAccessorNumber;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<ArrayLangObject> attrAccessorArray(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<ArrayLangObject> func = attrAccessorArray.get(name);
		return func != null ? func : genericAttrAccessorArray;

	}
	@SuppressWarnings("null")
	@Override
	public IFunction<HashLangObject> attrAccessorHash(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<HashLangObject> func = attrAccessorHash.get(name);
		return func != null ? func : genericAttrAccessorHash;

	}
	@SuppressWarnings("null")
	@Override
	public IFunction<BooleanLangObject> attrAccessorBoolean(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<BooleanLangObject> func = attrAccessorBoolean.get(name);
		return func != null ? func : genericAttrAccessorBoolean;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<ExceptionLangObject> attrAccessorException(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<ExceptionLangObject> func = attrAccessorException.get(name);
		return func != null ? func : genericAttrAccessorException;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<FunctionLangObject> attrAccessorFunction(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<FunctionLangObject> func = attrAccessorFunction.get(name);
		return func != null ? func : genericAttrAccessorFunction;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<RegexLangObject> attrAccessorRegex(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<RegexLangObject> func = attrAccessorRegex.get(name);
		return func != null ? func : genericAttrAccessorRegex;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<StringLangObject> attrAssignerString(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<StringLangObject> func = attrAssignerString.get(name);
		return func != null ? func : genericAttrAssignerString;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<NumberLangObject> attrAssignerNumber(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<NumberLangObject> func = attrAssignerNumber.get(name);
		return func != null ? func : genericAttrAssignerNumber;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<ArrayLangObject> attrAssignerArray(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<ArrayLangObject> func = attrAssignerArray.get(name);
		return func != null ? func : genericAttrAssignerArray;

	}
	@SuppressWarnings("null")
	@Override
	public IFunction<HashLangObject> attrAssignerHash(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<HashLangObject> func = attrAssignerHash.get(name);
		return func != null ? func : genericAttrAssignerHash;

	}
	@SuppressWarnings("null")
	@Override
	public IFunction<BooleanLangObject> attrAssignerBoolean(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<BooleanLangObject> func = attrAssignerBoolean.get(name);
		return func != null ? func : genericAttrAssignerBoolean;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<ExceptionLangObject> attrAssignerException(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<ExceptionLangObject> func = attrAssignerException.get(name);
		return func != null ? func : genericAttrAssignerException;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<FunctionLangObject> attrAssignerFunction(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<FunctionLangObject> func = attrAssignerFunction.get(name);
		return func != null ? func : genericAttrAssignerFunction;
	}
	@SuppressWarnings("null")
	@Override
	public IFunction<RegexLangObject> attrAssignerRegex(final ALangObject name, final boolean accessedViaDot)
			throws EvaluationException {
		final IFunction<RegexLangObject> func = attrAssignerRegex.get(name);
		return func != null ? func : genericAttrAssignerRegex;
	}

	@Nonnull
	public static INamespace getGenericNamespaceInstance() {
		return InstanceHolder.GENERIC;
	}

	private final static class InstanceHolder {
		@Nonnull public final static INamespace GENERIC;
		static {
			final GenericNamespace.Builder builder = new GenericNamespace.Builder();

			builder.addExpressionMethodBoolean(EExpressionMethodBoolean.values());
			builder.addExpressionMethodNumber(EExpressionMethodNumber.values());
			builder.addExpressionMethodString(EExpressionMethodString.values());
			builder.addExpressionMethodArray(EExpressionMethodArray.values());
			builder.addExpressionMethodHash(EExpressionMethodHash.values());
			builder.addExpressionMethodException(EExpressionMethodException.values());
			builder.addExpressionMethodFunction(EExpressionMethodFunction.values());
			builder.addExpressionMethodRegex(EExpressionMethodRegex.values());

			builder.addAttrAccessorBoolean(EAttrAccessorBoolean.values());
			builder.addAttrAccessorNumber(EAttrAccessorNumber.values());
			builder.addAttrAccessorString(EAttrAccessorString.values());
			builder.addAttrAccessorArray(EAttrAccessorArray.values());
			builder.addAttrAccessorHash(EAttrAccessorHash.values());
			builder.addAttrAccessorException(EAttrAccessorException.values());
			builder.addAttrAccessorFunction(EAttrAccessorFunction.values());
			builder.addAttrAccessorRegex(EAttrAccessorRegex.values());

			builder.addAttrAssignerBoolean(EAttrAssignerBoolean.values());
			builder.addAttrAssignerNumber(EAttrAssignerNumber.values());
			builder.addAttrAssignerString(EAttrAssignerString.values());
			builder.addAttrAssignerArray(EAttrAssignerArray.values());
			builder.addAttrAssignerHash(EAttrAssignerHash.values());
			builder.addAttrAssignerException(EAttrAssignerException.values());
			builder.addAttrAssignerFunction(EAttrAssignerFunction.values());
			builder.addAttrAssignerRegex(EAttrAssignerRegex.values());

			builder.setGenericAttrAccessorArray(GenericAttrAccessor.ARRAY);
			builder.setGenericAttrAccessorBoolean(GenericAttrAccessor.BOOLEAN);
			builder.setGenericAttrAccessorException(GenericAttrAccessor.EXCEPTION);
			builder.setGenericAttrAccessorFunction(GenericAttrAccessor.FUNCTION);
			builder.setGenericAttrAccessorHash(GenericAttrAccessor.HASH);
			builder.setGenericAttrAccessorNumber(GenericAttrAccessor.NUMBER);
			builder.setGenericAttrAccessorString(GenericAttrAccessor.STRING);
			builder.setGenericAttrAccessorRegex(GenericAttrAccessor.REGEX);

			builder.setGenericAttrAssignerArray(GenericAttrAssigner.ARRAY);
			builder.setGenericAttrAssignerBoolean(GenericAttrAssigner.BOOLEAN);
			builder.setGenericAttrAssignerException(GenericAttrAssigner.EXCEPTION);
			builder.setGenericAttrAssignerFunction(GenericAttrAssigner.FUNCTION);
			builder.setGenericAttrAssignerHash(GenericAttrAssigner.HASH);
			builder.setGenericAttrAssignerNumber(GenericAttrAssigner.NUMBER);
			builder.setGenericAttrAssignerString(GenericAttrAssigner.STRING);
			builder.setGenericAttrAssignerRegex(GenericAttrAssigner.REGEX);

			GENERIC = builder.build();
		}
	}
}