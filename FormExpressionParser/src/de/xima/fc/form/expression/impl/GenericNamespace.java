package de.xima.fc.form.expression.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.IMethod2Function;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public class GenericNamespace implements INamespace {

	private final EnumMap<EMethod, IFunction<StringLangObject>> expressionMethodString;
	private final EnumMap<EMethod, IFunction<ArrayLangObject>> expressionMethodArray;
	private final EnumMap<EMethod, IFunction<NumberLangObject>> expressionMethodNumber;
	private final EnumMap<EMethod, IFunction<HashLangObject>> expressionMethodHash;
	private final EnumMap<EMethod, IFunction<ExceptionLangObject>> expressionMethodException;
	private final EnumMap<EMethod, IFunction<BooleanLangObject>> expressionMethodBoolean;
	private final EnumMap<EMethod, IFunction<FunctionLangObject>> expressionMethodFunction;

	private final Map<String, IFunction<StringLangObject>> attrAccessorString;
	private final Map<String, IFunction<ArrayLangObject>> attrAccessorArray;
	private final Map<String, IFunction<BooleanLangObject>> attrAccessorBoolean;
	private final Map<String, IFunction<NumberLangObject>> attrAccessorNumber;
	private final Map<String, IFunction<HashLangObject>> attrAccessorHash;
	private final Map<String, IFunction<ExceptionLangObject>> attrAccessorException;
	private final Map<String, IFunction<FunctionLangObject>> attrAccessorFunction;

	public static class Builder {
		private EnumMap<EMethod, IFunction<StringLangObject>> expressionMethodString;
		private EnumMap<EMethod, IFunction<ArrayLangObject>> expressionMethodArray;
		private EnumMap<EMethod, IFunction<BooleanLangObject>> expressionMethodBoolean;
		private EnumMap<EMethod, IFunction<NumberLangObject>> expressionMethodNumber;
		private EnumMap<EMethod, IFunction<HashLangObject>> expressionMethodHash;
		private EnumMap<EMethod, IFunction<ExceptionLangObject>> expressionMethodException;
		private EnumMap<EMethod, IFunction<FunctionLangObject>> expressionMethodFunction;
		private Map<String, IFunction<StringLangObject>> attrAccessorString;
		private Map<String, IFunction<ArrayLangObject>> attrAccessorArray;
		private Map<String, IFunction<BooleanLangObject>> attrAccessorBoolean;
		private Map<String, IFunction<NumberLangObject>> attrAccessorNumber;
		private Map<String, IFunction<HashLangObject>> attrAccessorHash;
		private Map<String, IFunction<ExceptionLangObject>> attrAccessorException;
		private Map<String, IFunction<FunctionLangObject>> attrAccessorFunction;
		private int count = 0;

		public Builder() {
		}

		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorString(final Map<String, IFunction<StringLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorArray(final Map<String, IFunction<ArrayLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorBoolean(final Map<String, IFunction<BooleanLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorNumber(final Map<String, IFunction<NumberLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorHash(final Map<String, IFunction<HashLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorException(final Map<String, IFunction<ExceptionLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorFunction(final Map<String, IFunction<FunctionLangObject>> map) {
		}

		public final Builder setExpressionMethodString(final IMethod2Function<StringLangObject>[] m) {
			if (expressionMethodString == null) {
				++count;
				expressionMethodString = new EnumMap<>(EMethod.class);
				for (final IMethod2Function<StringLangObject> f : m)
					expressionMethodString.put(f.getMethod(), f.getFunction());
			}
			return this;
		}

		public final Builder setExpressionMethodBoolean(final IMethod2Function<BooleanLangObject>[] m) {
			if (expressionMethodBoolean == null) {
				++count;
				expressionMethodBoolean = new EnumMap<>(EMethod.class);
				for (final IMethod2Function<BooleanLangObject> f : m)
					expressionMethodBoolean.put(f.getMethod(), f.getFunction());
			}
			return this;
		}

		public final Builder setExpressionMethodNumber(final IMethod2Function<NumberLangObject>[] m) {
			if (expressionMethodNumber == null) {
				++count;
				expressionMethodNumber = new EnumMap<>(EMethod.class);
				for (final IMethod2Function<NumberLangObject> f : m)
					expressionMethodNumber.put(f.getMethod(), f.getFunction());
			}
			return this;
		}

		public final Builder setExpressionMethodHash(final IMethod2Function<HashLangObject>[] m) {
			if (expressionMethodHash == null) {
				++count;
				expressionMethodHash = new EnumMap<>(EMethod.class);
				for (final IMethod2Function<HashLangObject> f : m)
					expressionMethodHash.put(f.getMethod(), f.getFunction());
			}
			return this;
		}

		public final Builder setExpressionMethodArray(final IMethod2Function<ArrayLangObject>[] m) {
			if (expressionMethodArray == null) {
				++count;
				expressionMethodArray = new EnumMap<>(EMethod.class);
				for (final IMethod2Function<ArrayLangObject> f : m)
					expressionMethodArray.put(f.getMethod(), f.getFunction());
			}
			return this;
		}

		public final Builder setExpressionMethodException(final IMethod2Function<ExceptionLangObject>[] m) {
			if (expressionMethodException == null) {
				++count;
				expressionMethodException = new EnumMap<>(EMethod.class);
				for (final IMethod2Function<ExceptionLangObject> f : m)
					expressionMethodException.put(f.getMethod(), f.getFunction());
			}
			return this;
		}


		public final Builder setAttrAccessorString(final IFunction<StringLangObject>[] m) {
			if (attrAccessorString == null) {
				++count;
				attrAccessorString = new HashMap<>(m.length);
				for (final IFunction<StringLangObject> f : m)
					attrAccessorString.put(f.getDeclaredName(), f);
			}
			customAttrAccessorString(attrAccessorString);
			return this;
		}

		public final Builder setAttrAccessorBoolean(final IFunction<BooleanLangObject>[] m) {
			if (attrAccessorBoolean == null) {
				++count;
				attrAccessorBoolean = new HashMap<>(m.length);
				for (final IFunction<BooleanLangObject> f : m)
					attrAccessorBoolean.put(f.getDeclaredName(), f);
			}
			customAttrAccessorBoolean(attrAccessorBoolean);
			return this;
		}

		public final Builder setAttrAccessorNumber(final IFunction<NumberLangObject>[] m) {
			if (attrAccessorNumber == null) {
				++count;
				attrAccessorNumber = new HashMap<>(m.length);
				for (final IFunction<NumberLangObject> f : m)
					attrAccessorNumber.put(f.getDeclaredName(), f);
			}
			customAttrAccessorNumber(attrAccessorNumber);
			return this;
		}

		public final Builder setAttrAccessorHash(final IFunction<HashLangObject>[] m) {
			if (attrAccessorHash == null) {
				++count;
				attrAccessorHash = new HashMap<>(m.length);
				for (final IFunction<HashLangObject> f : m)
					attrAccessorHash.put(f.getDeclaredName(), f);
			}
			customAttrAccessorHash(attrAccessorHash);
			return this;
		}

		public final Builder setAttrAccessorArray(final IFunction<ArrayLangObject>[] m) {
			if (attrAccessorArray == null) {
				++count;
				attrAccessorArray = new HashMap<>(m.length);
				for (final IFunction<ArrayLangObject> f : m)
					attrAccessorArray.put(f.getDeclaredName(), f);
			}
			customAttrAccessorArray(attrAccessorArray);
			return this;
		}

		public final Builder setAttrAccessorException(final IFunction<ExceptionLangObject>[] m) {
			if (attrAccessorException == null) {
				++count;
				attrAccessorException = new HashMap<>(m.length);
				for (final IFunction<ExceptionLangObject> f : m)
					attrAccessorException.put(f.getDeclaredName(), f);
			}
			customAttrAccessorException(attrAccessorException);
			return this;
		}

		public final INamespace build() throws IllegalStateException {
			if (count != 14)
				throw new IllegalStateException("All instance and attribute accessor methods must be set");
			return new GenericNamespace(expressionMethodBoolean, expressionMethodNumber, expressionMethodString,
					expressionMethodArray, expressionMethodHash, expressionMethodException, expressionMethodFunction, attrAccessorBoolean,
					attrAccessorNumber, attrAccessorString, attrAccessorArray, attrAccessorHash, attrAccessorException, attrAccessorFunction);
		}
	}

	private GenericNamespace(final EnumMap<EMethod, IFunction<BooleanLangObject>> expressionMethodBoolean,
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
			final Map<String, IFunction<FunctionLangObject>> attrAccessorFunction) {
		this.expressionMethodBoolean = expressionMethodBoolean;
		this.expressionMethodNumber = expressionMethodNumber;
		this.expressionMethodString = expressionMethodString;
		this.expressionMethodArray = expressionMethodArray;
		this.expressionMethodHash = expressionMethodHash;
		this.expressionMethodException = expressionMethodException;
		this.expressionMethodFunction = expressionMethodFunction;
		this.attrAccessorBoolean = attrAccessorBoolean;
		this.attrAccessorNumber = attrAccessorNumber;
		this.attrAccessorString = attrAccessorString;
		this.attrAccessorArray = attrAccessorArray;
		this.attrAccessorHash = attrAccessorHash;
		this.attrAccessorException = attrAccessorException;
		this.attrAccessorFunction = attrAccessorFunction;
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
	public IFunction<StringLangObject> attrAccessorString(final String name) throws EvaluationException {
		return attrAccessorString.get(name);
	}

	@Override
	public IFunction<NumberLangObject> attrAccessorNumber(final String name) throws EvaluationException {
		return attrAccessorNumber.get(name);
	}

	@Override
	public IFunction<ArrayLangObject> attrAccessorArray(final String name) throws EvaluationException {
		return attrAccessorArray.get(name);
	}

	@Override
	public IFunction<HashLangObject> attrAccessorHash(final String name) throws EvaluationException {
		return attrAccessorHash.get(name);
	}

	@Override
	public IFunction<BooleanLangObject> attrAccessorBoolean(final String name) throws EvaluationException {
		return attrAccessorBoolean.get(name);
	}

	@Override
	public IFunction<ExceptionLangObject> attrAccessorException(final String name) throws EvaluationException {
		return attrAccessorException.get(name);
	}

	@Override
	public IFunction<FunctionLangObject> attrAccessorFunction(final String name) throws EvaluationException {
		return attrAccessorFunction.get(name);
	}
}