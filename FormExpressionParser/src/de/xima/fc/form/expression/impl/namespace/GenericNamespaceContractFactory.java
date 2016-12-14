package de.xima.fc.form.expression.impl.namespace;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.iface.evaluate.IAttrAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IAttrAssignerFunction;
import de.xima.fc.form.expression.iface.evaluate.IExpressionFunction;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IMethod2Function;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;

/**
 * A generic immutable {@link INamespace} that only needs to be constructed once
 * and can be used throughout the entire lifetime of the program from multiple
 * threads.
 * </p>
 * <p>
 * Construct it with the builder; or use {@link #getGenericNamespace()} which
 * makes use of the methods from
 * {@link de.xima.fc.form.expression.impl.function}.
 * </p>
 * <p>
 *
 * <pre>
 * GenericNamespace.Builder builder = new GenericNamespace.Builder();
 * builder.addExpressionMethodBoolean(EExpressionMethodBoolean.values());
 * builder.addAttrAccessorBoolean(EAttrAccessorBoolean.values());
 * ...
 * builder.addAttrAccessorBoolean(new ISerializableFunction&lt;BooleanLangObject&gt;(){
 *   // custom attribute accessor
 * });
 * INamespace namespace = builder.build();
 * </pre>
 * </p>
 * <p>
 * Note that this make extensive use of casting. Correctness of this relies on
 * whether the information supplied by {@link ILangObjectClass#getClassId()}
 * {@link ILangObjectClass#getSuperType()}, and {@link ALangObject#getType()} is
 * correct. The class corresponding to the type.getSuperType() must actually be
 * a superclass of the class corresponding to the type.
 * </p>
 *
 * @author madgaksha
 */
@Immutable
@ParametersAreNonnullByDefault
public class GenericNamespaceContractFactory implements INamespaceContractFactory, INamespace {
	private static final long serialVersionUID = 1L;

	private final Map<String, IAttrAccessorFunction<?>>[] attrAccessorMap;
	private final Map<String, IAttrAssignerFunction<?>>[] attrAssignerMap;
	private final EnumMap<EMethod, IExpressionFunction<?>>[] expressionMethodMap;
	private final IAttrAccessorFunction<?>[] genericAttrAccessorMap;
	private final IAttrAssignerFunction<?>[] genericAttrAssignerMap;

	@SuppressWarnings("unchecked")
	private GenericNamespaceContractFactory(final Map<Integer, Map<String, IAttrAccessorFunction<?>>> attrAccessorMap,
			final Map<Integer, Map<String, IAttrAssignerFunction<?>>> attrAssignerMap,
			final Map<Integer, EnumMap<EMethod, IExpressionFunction<?>>> expressionMethodMap,
			final Map<Integer, IAttrAccessorFunction<?>> genericAttrAccessorMap,
			final Map<Integer, IAttrAssignerFunction<?>> genericAttrAssignerMap) {
		this.attrAccessorMap = toArray(attrAccessorMap, new Map[0]);
		this.attrAssignerMap = toArray(attrAssignerMap, new Map[0]);
		this.expressionMethodMap = toArray(expressionMethodMap, new EnumMap[0]);
		this.genericAttrAccessorMap = toArray(genericAttrAccessorMap, new IAttrAccessorFunction[0]);
		this.genericAttrAssignerMap = toArray(genericAttrAssignerMap, new IAttrAssignerFunction[0]);
	}

	private static <T> T[] toArray(final Map<Integer, T> map, final T[] array) {
		Integer max = 0;
		for (final Integer i : map.keySet())
			if (i > max)
				max = i;
		final T[] arr = Arrays.copyOf(array, max + 1);
		if (arr == null)
			throw new FormExpressionException();
		for (final Entry<Integer, T> entry : map.entrySet())
			arr[entry.getKey()] = entry.getValue();
		return arr;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Nullable
	public <T extends ALangObject> IFunction<T> expressionMethod(final EMethod method, final T type) {
		return (IExpressionFunction<T>) expressionMethod(method, type.getType());
	}

	@Override
	@SuppressWarnings("unchecked")
	@Nullable
	public <T extends ALangObject> IFunction<T> attrAccessor(final ALangObject name, final boolean accessedViaDot,
			final T type) {
		return (IAttrAccessorFunction<T>) attrAccessor(name, accessedViaDot, type.getType());
	}

	@Override
	@SuppressWarnings("unchecked")
	@Nullable
	public <T extends ALangObject> IFunction<T> attrAssigner(final ALangObject name, final boolean accessedViaDot,
			final T type) {
		return (IAttrAssignerFunction<T>) attrAssigner(name, accessedViaDot, type.getType());
	}

	@Override
	@Nullable
	public IExpressionFunction<?> expressionMethod(final EMethod method, final ILangObjectClass type) {
		final int classId = type.getClassId();
		if (classId < expressionMethodMap.length)
			return expressionMethodMap[classId].get(method);
		final ILangObjectClass superType = type.getSuperType();
		return superType != null ? expressionMethod(method, superType) : null;
	}

	@Override
	@Nullable
	public IAttrAccessorFunction<?> attrAccessor(final ALangObject name, final boolean accessedViaDot,
			final ILangObjectClass type) {
		final int classId = type.getClassId();
		final IAttrAccessorFunction<?> func = classId < attrAccessorMap.length && name instanceof StringLangObject
				? attrAccessorMap[classId].get(((StringLangObject) name).stringValue()) : null;
		if (func != null)
			return func;
		if (classId < genericAttrAccessorMap.length)
			return genericAttrAccessorMap[classId];
		final ILangObjectClass superType = type.getSuperType();
		return superType != null ? attrAccessor(name, accessedViaDot, superType) : null;
	}

	@Override
	@Nullable
	public IAttrAssignerFunction<?> attrAssigner(final ALangObject name, final boolean accessedViaDot,
			final ILangObjectClass type) {
		final int classId = type.getClassId();
		final IAttrAssignerFunction<?> func = classId < attrAssignerMap.length && name instanceof StringLangObject
				? attrAssignerMap[classId].get(((StringLangObject) name).stringValue()) : null;
		if (func != null)
			return func;
		if (classId < genericAttrAssignerMap.length)
			return genericAttrAssignerMap[classId];
		final ILangObjectClass superType = type.getSuperType();
		return superType != null ? attrAssigner(name, accessedViaDot, superType) : null;
	}

	@Override
	public INamespace make() {
		return this;
	}

	@Nullable
	@Override
	public IVariableType getReturnOfExpressionMethod(final IVariableType lhs, final EMethod method,
			final IVariableType rhs) {
		final IExpressionFunction<?> f = expressionMethod(method, lhs.getBasicLangClass());
		return f != null ? f.getReturnTypeFor(lhs, rhs) : null;
	}

	@Override
	public boolean isBracketAttributeAssignerDefined(final IVariableType lhs, final IVariableType property, final IVariableType rhs) {
	}

	@Override
	public boolean isDotAttributeAssignerDefined(final IVariableType lhs, final IVariableType property, final IVariableType rhs) {
	}

	@NotThreadSafe
	public static class Builder {
		@Nullable
		private Map<Integer, EnumMap<EMethod, IExpressionFunction<?>>> expressionMethodMap;
		@Nullable
		private Map<Integer, Map<String, IAttrAccessorFunction<?>>> attrAccessorMap;
		@Nullable
		private Map<Integer, Map<String, IAttrAssignerFunction<?>>> attrAssignerMap;

		@Nullable
		private Map<Integer, IAttrAccessorFunction<?>> genericAttrAccessorMap;
		@Nullable
		private Map<Integer, IAttrAssignerFunction<?>> genericAttrAssignerMap;

		@Nullable
		private Map<Integer, ILangObjectClass> classPathMap;

		public Builder() {
			init();
		}

		/**
		 * @param functions
		 *            Expression methods to add.
		 * @return this for chaining.
		 * @throws FormExpressionException
		 *             When any argument is <code>null</code> or the class id of
		 *             an object type is negative.
		 */
		@SafeVarargs
		public final <T extends ALangObject> Builder addExpressionMethod(final IMethod2Function<T>... functions) {
			for (final IMethod2Function<?> f : functions) {
				if (f == null)
					throw new FormExpressionException();
				final Integer classId = f.getFunction().getThisContextType().getClassId();
				if (classId < 0)
					throw new FormExpressionException(String.format("Class id %d for %s is negative.", classId,
							f.getFunction().getThisContextType()));
				EnumMap<EMethod, IExpressionFunction<?>> map = getExpressionMethodMap().get(classId);
				if (map == null)
					getExpressionMethodMap().put(classId, map = new EnumMap<>(EMethod.class));
				getClassPathMap().put(classId, f.getFunction().getThisContextType());
				map.put(f.getMethod(), f.getFunction());
			}
			return this;
		}

		/**
		 * @param functions
		 *            Attribute accessors to add.
		 * @return this for chaining.
		 * @throws FormExpressionException
		 *             When any argument is <code>null</code> or the class id of
		 *             an object type is negative.
		 */
		@SafeVarargs
		public final <T extends ALangObject> Builder addAttrAccessor(final IAttrAccessorFunction<T>... functions) {
			for (final IAttrAccessorFunction<?> f : functions)
				putFunction2(f, getAttrAccessorMap());
			return this;
		}

		/**
		 * @param functions
		 *            Attribute assigners to add.
		 * @return this for chaining.
		 * @throws FormExpressionException
		 *             When any argument is <code>null</code> or the class id of
		 *             an object type is negative.
		 */
		@SafeVarargs
		public final <T extends ALangObject> Builder addAttrAssigner(final IAttrAssignerFunction<T>... functions) {
			for (final IAttrAssignerFunction<?> f : functions)
				putFunction2(f, getAttrAssignerMap());
			return this;
		}

		/**
		 * @param functions
		 *            Generic attribute accessors to add.
		 * @return this for chaining.
		 * @throws FormExpressionException
		 *             When any argument is <code>null</code> or the class id of
		 *             an object type is negative.
		 */
		@SafeVarargs
		public final <T extends ALangObject> Builder addGenericAttrAccessor(
				final IAttrAccessorFunction<T>... functions) {
			for (final IAttrAccessorFunction<?> f : functions)
				putFunction(f, getGenericAttrAccessorMap());
			return this;
		}

		/**
		 * @param functions
		 *            Generic attribute assigners to add.
		 * @return this for chaining.
		 * @throws FormExpressionException
		 *             When any argument is <code>null</code> or the class id of
		 *             an object type is negative.
		 */
		@SafeVarargs
		public final <T extends ALangObject> Builder addGenericAttrAssigner(
				final IAttrAssignerFunction<T>... functions) {
			for (final IAttrAssignerFunction<?> f : functions)
				putFunction(f, getGenericAttrAssignerMap());
			return this;
		}

		private <T extends IFunction<?>> void putFunction(@Nullable final T f, final Map<Integer, T> map) {
			if (f == null)
				throw new FormExpressionException();
			final Integer classId = f.getThisContextType().getClassId();
			if (classId < 0)
				throw new FormExpressionException(
						String.format("Class id %d for %s is negative.", classId, f.getThisContextType()));
			getClassPathMap().put(classId, f.getThisContextType());
			map.put(classId, f);
		}

		private <T extends IFunction<?>> void putFunction2(@Nullable final T f,
				final Map<Integer, Map<String, T>> map) {
			if (f == null)
				throw new FormExpressionException();
			final Integer classId = f.getThisContextType().getClassId();
			if (classId < 0)
				throw new FormExpressionException(
						String.format("Class id %d for %s is negative.", classId, f.getThisContextType()));
			Map<String, T> m = map.get(classId);
			if (m == null)
				map.put(classId, m = new HashMap<>());
			getClassPathMap().put(classId, f.getThisContextType());
			m.put(f.getDeclaredName(), f);
		}

		private Map<Integer, EnumMap<EMethod, IExpressionFunction<?>>> getExpressionMethodMap() {
			if (expressionMethodMap != null)
				return expressionMethodMap;
			return expressionMethodMap = new HashMap<>();
		}

		private Map<Integer, Map<String, IAttrAccessorFunction<?>>> getAttrAccessorMap() {
			if (attrAccessorMap != null)
				return attrAccessorMap;
			return attrAccessorMap = new HashMap<>();
		}

		private Map<Integer, Map<String, IAttrAssignerFunction<?>>> getAttrAssignerMap() {
			if (attrAssignerMap != null)
				return attrAssignerMap;
			return attrAssignerMap = new HashMap<>();
		}

		private Map<Integer, IAttrAccessorFunction<?>> getGenericAttrAccessorMap() {
			if (genericAttrAccessorMap != null)
				return genericAttrAccessorMap;
			return genericAttrAccessorMap = new HashMap<>();
		}

		private Map<Integer, IAttrAssignerFunction<?>> getGenericAttrAssignerMap() {
			if (genericAttrAssignerMap != null)
				return genericAttrAssignerMap;
			return genericAttrAssignerMap = new HashMap<>();
		}

		private Map<Integer, ILangObjectClass> getClassPathMap() {
			if (classPathMap != null)
				return classPathMap;
			return classPathMap = new HashMap<>();
		}

		private void init() {
			genericAttrAccessorMap = null;
			genericAttrAssignerMap = null;
			attrAccessorMap = null;
			attrAssignerMap = null;
			expressionMethodMap = null;
		}

		/**
		 * Builds the namespace factory and returns it. This also clears the
		 * builder. If you want to build another namespace factory, you need to
		 * add the desired methods again.
		 *
		 * @return The built namespace factory containing the added methods.
		 */
		public final INamespaceContractFactory build() throws FormExpressionException {
			// TODO check for sanity, "overridden" methods must be compatible,
			// see signature
			// checkInheritance();
			final INamespaceContractFactory factory = new GenericNamespaceContractFactory(getAttrAccessorMap(),
					getAttrAssignerMap(), getExpressionMethodMap(), getGenericAttrAccessorMap(),
					getGenericAttrAssignerMap());
			init();
			return factory;
		}

		private void checkInheritance() throws FormExpressionException {
			checkInheritance1(expressionMethodMap);
			checkInheritance1(attrAccessorMap);
			checkInheritance1(attrAssignerMap);
			checkInheritance2(genericAttrAccessorMap);
			checkInheritance2(genericAttrAssignerMap);
		}

		private <T, F extends IFunction<?>, S extends Map<T, F>> void checkInheritance1(
				@Nullable final Map<Integer, S> map) throws FormExpressionException {
			final Map<Integer, ILangObjectClass> classPathMap = getClassPathMap();
			if (map != null) {
				for (final Entry<Integer, S> entry : map.entrySet()) {
					final ILangObjectClass superClass = classPathMap.get(entry.getKey()).getSuperType();
					if (superClass != null) {
						final S superMap = map.get(superClass.getClassId());
						if (superMap != null) {
							for (final Entry<T, F> entry2 : entry.getValue().entrySet()) {
								final F thisFunction = entry2.getValue();
								final F superFunction = superMap.get(entry2.getKey());
								if (thisFunction != null && superFunction != null)
									checkInheritance(thisFunction, superFunction);
							}
						}
					}
				}
			}
		}

		private <T extends IFunction<?>> void checkInheritance2(@Nullable final Map<Integer, T> map)
				throws FormExpressionException {
			final Map<Integer, ILangObjectClass> classPathMap = getClassPathMap();
			if (map != null) {
				for (final Entry<Integer, T> entry : map.entrySet()) {
					final ILangObjectClass superClass = classPathMap.get(entry.getKey()).getSuperType();
					if (superClass != null) {
						final T thisFunction = entry.getValue();
						final T superFunction = map.get(superClass.getClassId());
						if (thisFunction != null && superFunction != null)
							checkInheritance(thisFunction, superFunction);
					}
				}
			}
		}

		private void checkInheritance(final IFunction<?> thisFunction, final IFunction<?> superFunction)
				throws FormExpressionException {
			throw new FormExpressionException("TODO - not yet implemented");
		}
	}
}