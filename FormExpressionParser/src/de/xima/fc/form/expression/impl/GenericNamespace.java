package de.xima.fc.form.expression.impl;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public class GenericNamespace implements INamespace {

	private final Map<String, INamedFunction<NullLangObject>> globalMethod;
	private final Map<String, INamedFunction<StringLangObject>> instanceMethodString;
	private final Map<String, INamedFunction<ArrayLangObject>> instanceMethodArray;
	private final Map<String, INamedFunction<NumberLangObject>> instanceMethodNumber;
	private final Map<String, INamedFunction<HashLangObject>> instanceMethodHash;
	private final Map<String, INamedFunction<BooleanLangObject>> instanceMethodBoolean;
	private final Map<String, INamedFunction<StringLangObject>> attrAccessorString;
	private final Map<String, INamedFunction<ArrayLangObject>> attrAccessorArray;
	private final Map<String, INamedFunction<BooleanLangObject>> attrAccessorBoolean;
	private final Map<String, INamedFunction<NumberLangObject>> attrAccessorNumber;
	private final Map<String, INamedFunction<HashLangObject>> attrAccessorHash;

	public static class Builder {
		private Map<String, INamedFunction<NullLangObject>> globalMethod;
		private Map<String, INamedFunction<StringLangObject>> instanceMethodString;
		private Map<String, INamedFunction<ArrayLangObject>> instanceMethodArray;
		private Map<String, INamedFunction<BooleanLangObject>> instanceMethodBoolean;
		private Map<String, INamedFunction<NumberLangObject>> instanceMethodNumber;
		private Map<String, INamedFunction<HashLangObject>> instanceMethodHash;
		private Map<String, INamedFunction<StringLangObject>> attrAccessorString;
		private Map<String, INamedFunction<ArrayLangObject>> attrAccessorArray;
		private Map<String, INamedFunction<BooleanLangObject>> attrAccessorBoolean;
		private Map<String, INamedFunction<NumberLangObject>> attrAccessorNumber;
		private Map<String, INamedFunction<HashLangObject>> attrAccessorHash;
		private int count = 0;

		public Builder() {
		}

		/** @param map Map that may be filled with additional custom methods. */
		protected void customGlobalMethod(final Map<String, INamedFunction<NullLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customInstanceMethodString(final Map<String, INamedFunction<StringLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customInstanceMethodArray(final Map<String, INamedFunction<ArrayLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customInstanceMethodBoolean(final Map<String, INamedFunction<BooleanLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customInstanceMethodNumber(final Map<String, INamedFunction<NumberLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customInstanceMethodHash(final Map<String, INamedFunction<HashLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorString(final Map<String, INamedFunction<StringLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorArray(final Map<String, INamedFunction<ArrayLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorBoolean(final Map<String, INamedFunction<BooleanLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorNumber(final Map<String, INamedFunction<NumberLangObject>> map) {
		}
		/** @param map Map that may be filled with additional custom methods. */
		protected void customAttrAccessorHash(final Map<String, INamedFunction<HashLangObject>> map) {
		}

		public final Builder setGlobalMethod(final INamedFunction<NullLangObject>[] m) {
			if (globalMethod == null) {
				++count;
				globalMethod = new HashMap<>(m.length);
				for (final INamedFunction<NullLangObject> f : m)
					globalMethod.put(f.getName(), f);
			}
			customGlobalMethod(globalMethod);
			return this;
		}

		public final Builder setInstanceMethodString(final INamedFunction<StringLangObject>[] m) {
			if (instanceMethodString == null) {
				++count;
				instanceMethodString = new HashMap<>(m.length);
				for (final INamedFunction<StringLangObject> f : m)
					instanceMethodString.put(f.getName(), f);
			}
			customInstanceMethodString(instanceMethodString);
			return this;
		}

		public final Builder setInstanceMethodBoolean(final INamedFunction<BooleanLangObject>[] m) {
			if (instanceMethodBoolean == null) {
				++count;
				instanceMethodBoolean = new HashMap<>(m.length);
				for (final INamedFunction<BooleanLangObject> f : m)
					instanceMethodBoolean.put(f.getName(), f);
			}
			customInstanceMethodBoolean(instanceMethodBoolean);
			return this;
		}

		public final Builder setInstanceMethodNumber(final INamedFunction<NumberLangObject>[] m) {
			if (instanceMethodNumber == null) {
				++count;
				instanceMethodNumber = new HashMap<>(m.length);
				for (final INamedFunction<NumberLangObject> f : m)
					instanceMethodNumber.put(f.getName(), f);
			}
			customInstanceMethodNumber(instanceMethodNumber);
			return this;
		}

		public final Builder setInstanceMethodHash(final INamedFunction<HashLangObject>[] m) {
			if (instanceMethodHash == null) {
				++count;
				instanceMethodHash = new HashMap<>(m.length);
				for (final INamedFunction<HashLangObject> f : m)
					instanceMethodHash.put(f.getName(), f);
			}
			customInstanceMethodHash(instanceMethodHash);
			return this;
		}

		public final Builder setInstanceMethodArray(final INamedFunction<ArrayLangObject>[] m) {
			if (instanceMethodArray == null) {
				++count;
				instanceMethodArray = new HashMap<>(m.length);
				for (final INamedFunction<ArrayLangObject> f : m)
					instanceMethodArray.put(f.getName(), f);
			}
			customInstanceMethodArray(instanceMethodArray);
			return this;
		}

		public final Builder setAttrAccessorString(final INamedFunction<StringLangObject>[] m) {
			if (attrAccessorString == null) {
				++count;
				attrAccessorString = new HashMap<>(m.length);
				for (final INamedFunction<StringLangObject> f : m)
					attrAccessorString.put(f.getName(), f);
			}
			customAttrAccessorString(attrAccessorString);
			return this;
		}

		public final Builder setAttrAccessorBoolean(final INamedFunction<BooleanLangObject>[] m) {
			if (attrAccessorBoolean == null) {
				++count;
				attrAccessorBoolean = new HashMap<>(m.length);
				for (final INamedFunction<BooleanLangObject> f : m)
					attrAccessorBoolean.put(f.getName(), f);
			}
			customAttrAccessorBoolean(attrAccessorBoolean);
			return this;
		}

		public final Builder setAttrAccessorNumber(final INamedFunction<NumberLangObject>[] m) {
			if (attrAccessorNumber == null) {
				++count;
				attrAccessorNumber = new HashMap<>(m.length);
				for (final INamedFunction<NumberLangObject> f : m)
					attrAccessorNumber.put(f.getName(), f);
			}
			customAttrAccessorNumber(attrAccessorNumber);
			return this;
		}

		public final Builder setAttrAccessorHash(final INamedFunction<HashLangObject>[] m) {
			if (attrAccessorHash == null) {
				++count;
				attrAccessorHash = new HashMap<>(m.length);
				for (final INamedFunction<HashLangObject> f : m)
					attrAccessorHash.put(f.getName(), f);
			}
			customAttrAccessorHash(attrAccessorHash);
			return this;
		}

		public final Builder setAttrAccessorArray(final INamedFunction<ArrayLangObject>[] m) {
			if (attrAccessorArray == null) {
				++count;
				attrAccessorArray = new HashMap<>(m.length);
				for (final INamedFunction<ArrayLangObject> f : m)
					attrAccessorArray.put(f.getName(), f);
			}
			customAttrAccessorArray(attrAccessorArray);
			return this;
		}

		public final INamespace build() throws IllegalStateException {
			if (count != 11)
				throw new IllegalStateException("all instance and attribute accessor methods must be set");
			return new GenericNamespace(globalMethod, instanceMethodBoolean, instanceMethodNumber,
					instanceMethodString, instanceMethodArray, instanceMethodHash, attrAccessorBoolean,
					attrAccessorNumber, attrAccessorString, attrAccessorArray, attrAccessorHash);
		}
	}

	private GenericNamespace(final Map<String, INamedFunction<NullLangObject>> globalMethod,
			final Map<String, INamedFunction<BooleanLangObject>> instanceMethodBoolean,
			final Map<String, INamedFunction<NumberLangObject>> instanceMethodNumber,
			final Map<String, INamedFunction<StringLangObject>> instanceMethodString,
			final Map<String, INamedFunction<ArrayLangObject>> instanceMethodArray,
			final Map<String, INamedFunction<HashLangObject>> instanceMethodHash,
			final Map<String, INamedFunction<BooleanLangObject>> attrAccessorBoolean,
			final Map<String, INamedFunction<NumberLangObject>> attrAccessorNumber,
			final Map<String, INamedFunction<StringLangObject>> attrAccessorString,
			final Map<String, INamedFunction<ArrayLangObject>> attrAccessorArray,
			final Map<String, INamedFunction<HashLangObject>> attrAccessorHash) {
		this.globalMethod = globalMethod;
		this.instanceMethodBoolean = instanceMethodBoolean;
		this.instanceMethodNumber = instanceMethodNumber;
		this.instanceMethodString = instanceMethodString;
		this.instanceMethodArray = instanceMethodArray;
		this.instanceMethodHash = instanceMethodHash;
		this.attrAccessorBoolean = attrAccessorBoolean;
		this.attrAccessorNumber = attrAccessorNumber;
		this.attrAccessorString = attrAccessorString;
		this.attrAccessorArray = attrAccessorArray;
		this.attrAccessorHash = attrAccessorHash;
	}

	@Override
	public INamedFunction<NullLangObject> globalMethod(final String name) throws EvaluationException {
		return globalMethod.get(name);
	}

	@Override
	public INamedFunction<StringLangObject> instanceMethodString(final String name) throws EvaluationException {
		return instanceMethodString.get(name);
	}

	@Override
	public INamedFunction<NumberLangObject> instanceMethodNumber(final String name) throws EvaluationException {
		return instanceMethodNumber.get(name);
	}

	@Override
	public INamedFunction<ArrayLangObject> instanceMethodArray(final String name) throws EvaluationException {
		return instanceMethodArray.get(name);
	}

	@Override
	public INamedFunction<HashLangObject> instanceMethodHash(final String name) throws EvaluationException {
		return instanceMethodHash.get(name);
	}

	@Override
	public INamedFunction<BooleanLangObject> instanceMethodBoolean(final String name) throws EvaluationException {
		return instanceMethodBoolean.get(name);
	}

	@Override
	public INamedFunction<StringLangObject> attrAccessorString(final String name) throws EvaluationException {
		return attrAccessorString.get(name);
	}

	@Override
	public INamedFunction<NumberLangObject> attrAccessorNumber(final String name) throws EvaluationException {
		return attrAccessorNumber.get(name);
	}

	@Override
	public INamedFunction<ArrayLangObject> attrAccessorArray(final String name) throws EvaluationException {
		return attrAccessorArray.get(name);
	}

	@Override
	public INamedFunction<HashLangObject> attrAccessorHash(final String name) throws EvaluationException {
		return attrAccessorHash.get(name);
	}

	@Override
	public INamedFunction<BooleanLangObject> attrAccessorBoolean(final String name) throws EvaluationException {
		return attrAccessorBoolean.get(name);
	}
}
