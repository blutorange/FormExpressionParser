package de.xima.fc.form.expression.impl.namespace;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.iface.evaluate.IDotAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IDotAssignerFunction;
import de.xima.fc.form.expression.iface.evaluate.IExpressionFunction;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.IGenericBracketAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IGenericBracketAssignerFunction;
import de.xima.fc.form.expression.iface.evaluate.IGenericDotAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IGenericDotAssignerFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IMethod2Function;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.NullUtil;

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
 * Note that this class make extensive use of casting. Correctness of this relies on
 * whether the information supplied by {@link ILangObjectClass#getClassId()}
 * {@link ILangObjectClass#getSuperType()}, and {@link ALangObject#getObjectClass()} is
 * consistent. The class corresponding to the type.getSuperType() must actually be
 * a superclass of the class corresponding to the type.
 * </p>
 *
 * @author madgaksha
 */
@Immutable
@ParametersAreNonnullByDefault
public class GenericNamespaceContractFactory implements INamespaceContractFactory, INamespace {
	private static final long serialVersionUID = 1L;

	private final EnumMap<EMethod, IExpressionFunction<?>[]>[] expressionMethodMap;

	private final Map<String, IDotAccessorFunction<?>>[] dotAccessorMap;
	private final Map<String, IDotAssignerFunction<?>[]>[] dotAssignerMap;

	private final IGenericDotAccessorFunction<?>[][] genericDotAccessorMap;
	private final IGenericDotAssignerFunction<?>[][] genericDotAssignerMap;

	/**
	 * For each class, there may be several overloaded accessors / assigners.
	 * genericBracketAccessorMap[classId][index]
	 */
	private final IGenericBracketAccessorFunction<?>[][] genericBracketAccessorMap;

	/**
	 * For each class, there may be several overloaded accessors / assigners.
	 * genericBracketAssignerMap[classId][index]
	 */
	private final IGenericBracketAssignerFunction<?>[][] genericBracketAssignerMap;

	@SuppressWarnings("unchecked")
	private GenericNamespaceContractFactory(
			final Map<Integer, EnumMap<EMethod, List<IExpressionFunction<?>>>> expressionMethodMap,
			final Map<Integer, Map<String, IDotAccessorFunction<?>>> dotAccessorMap,
			final Map<Integer, Map<String, List<IDotAssignerFunction<?>>>> dotAssignerMap,
			final Map<Integer, List<IGenericDotAccessorFunction<?>>> genericDotAccessorMap,
			final Map<Integer, List<IGenericDotAssignerFunction<?>>> genericDotAssignerMap,
			final Map<Integer, List<IGenericBracketAccessorFunction<?>>> genericBracketAccessorMap,
			final Map<Integer, List<IGenericBracketAssignerFunction<?>>> genericBracketAssignerMap) {
		this.expressionMethodMap = toArrayExpressionMethod(expressionMethodMap, new IExpressionFunction<?>[0]);
		this.dotAccessorMap = toArray(dotAccessorMap, new Map[0]);
		this.dotAssignerMap = toArrayDotAssigner(dotAssignerMap, new IDotAssignerFunction<?>[0]);
		this.genericDotAccessorMap = toArrayGeneric(genericDotAccessorMap, new IGenericDotAccessorFunction<?>[0]);
		this.genericDotAssignerMap = toArrayGeneric(genericDotAssignerMap, new IGenericDotAssignerFunction<?>[0]);
		this.genericBracketAccessorMap = toArrayGeneric(genericBracketAccessorMap, new IGenericBracketAccessorFunction<?>[0]);
		this.genericBracketAssignerMap = toArrayGeneric(genericBracketAssignerMap, new IGenericBracketAssignerFunction<?>[0]);
	}

	private static <T> T[] toArray(final Map<Integer, T> map, final T[] array) {
		Integer max = 0;
		for (final Integer i : map.keySet())
			if (i > max)
				max = i;
		final T[] arr = NullUtil.checkNotNull(Arrays.copyOf(array, max + 1));
		for (final Entry<Integer, T> entry : map.entrySet())
			arr[entry.getKey()] = entry.getValue();
		return arr;
	}

	private static <T> Map<String, T[]>[] toArrayDotAssigner(final Map<Integer, Map<String, List<T>>> map, final T[] emptyArray) {
		Integer max = 0;
		for (final Integer i : map.keySet())
			if (i > max)
				max = i;
		@SuppressWarnings("unchecked")
		final Map<String, T[]>[] outer = new Map[max + 1];
		for (final Entry<Integer, Map<String, List<T>>> entry : map.entrySet()) {
			final Map<String, T[]> inner = new HashMap<>();
			for (final Entry<String, List<T>> entry2 : entry.getValue().entrySet())
				inner.put(entry2.getKey(), entry2.getValue().toArray(emptyArray));
			outer[entry.getKey()] = inner;
		}
		return outer;
	}

	private static <T> EnumMap<EMethod, T[]>[] toArrayExpressionMethod(
			final Map<Integer, EnumMap<EMethod, List<T>>> map, final T[] emptyArray) {
		Integer max = 0;
		for (final Integer i : map.keySet())
			if (i > max)
				max = i;
		@SuppressWarnings("unchecked")
		final EnumMap<EMethod, T[]>[] outer = new EnumMap[max + 1];
		for (final Entry<Integer, EnumMap<EMethod, List<T>>> entry : map.entrySet()) {
			final EnumMap<EMethod, T[]> inner = new EnumMap<>(EMethod.class);
			for (final Entry<EMethod, List<T>> entry2 : entry.getValue().entrySet())
				inner.put(entry2.getKey(), entry2.getValue().toArray(emptyArray));
			outer[entry.getKey()] = inner;
		}
		return outer;
	}

	private static <T> T[][] toArrayGeneric(final Map<Integer, List<T>> map, final T[] emptyArray) {
		Integer max = 0;
		for (final Integer i : map.keySet())
			if (i > max)
				max = i;
		@SuppressWarnings("unchecked")
		final T[][] outer = NullUtil.checkNotNull((T[][])Array.newInstance(emptyArray.getClass(), max+1));
		for (final Entry<Integer, List<T>> entry : map.entrySet())
			outer[entry.getKey().intValue()] = entry.getValue().toArray(emptyArray);
		return outer;
	}

	/* FIXME This is not working as it should. Do we need to make some type info (basic class) available during run time??
	 *
	 * global scope {
	 *  number n = null;
	 *  array<string> a = ["1","2"];
	 * }
	 * a[n];
	 *
	 * And also:
	 *
	 * global scope {
	 *  string a = null;
	 *  string b = "foo";
	 * }
	 * b+a;
	 */
	@Nullable
	@Override
	public IExpressionFunction<?> expressionMethod(final ILangObjectClass thisContext, final EMethod method,
			final ILangObjectClass rhs) {
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId();
			if (classId < expressionMethodMap.length && expressionMethodMap[classId] != null) {
				final IExpressionFunction<?>[] methodList = expressionMethodMap[classId].get(method);
				if (methodList != null)
					for (final IExpressionFunction<?> m : methodList)
						if (m.getRhsClass().isSuperClassOf(rhs))
							return m;
			}
		}
		return null;
	}

	@Nullable
	@Override
	public IGenericBracketAccessorFunction<?> bracketAccessor(final ILangObjectClass thisContext, final ILangObjectClass property) {
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId();
			if (classId < genericBracketAccessorMap.length && genericBracketAccessorMap[classId] != null)
				for (final IGenericBracketAccessorFunction<?> accessor : genericBracketAccessorMap[classId])
					if (accessor.getPropertyClass().isSuperClassOf(property))
						return accessor;
		}
		return null;
	}

	@Override
	@Nullable
	public IGenericBracketAssignerFunction<?> bracketAssigner(final ILangObjectClass thisContext, final ILangObjectClass property, final ILangObjectClass value) {
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId();
			if (classId < genericBracketAssignerMap.length && genericBracketAssignerMap[classId] != null)
				for (final IGenericBracketAssignerFunction<?> assigner : genericBracketAssignerMap[classId])
					if (assigner.getPropertyClass().isSuperClassOf(property) && assigner.getValueClass().isSuperClassOf(value))
						return assigner;
		}
		return null;
	}

	@Nullable
	@Override
	public IFunction<?> dotAccessor(final ILangObjectClass thisContext, final String property) {
		// Named accessor
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId();
			if (classId < dotAccessorMap.length && dotAccessorMap[classId] != null) {
				final IDotAccessorFunction<?> accessor = dotAccessorMap[classId].get(property);
				if (accessor != null)
					return accessor;
			}
		}
		// Generic accessor
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId();
			if (classId < genericDotAccessorMap.length && genericDotAccessorMap[classId] != null)
				for (final IGenericDotAccessorFunction<?> accessor : genericDotAccessorMap[classId])
					if (accessor.isHandlingProperty(property))
						return accessor;
		}
		return null;
	}

	@Override
	@Nullable
	public IFunction<?> dotAssigner(final ILangObjectClass thisContext, final String property, final ILangObjectClass value) {
		// Named assigner
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId();
			if (classId < dotAssignerMap.length && dotAssignerMap[classId] != null) {
				final IDotAssignerFunction<?>[] assignerList = dotAssignerMap[classId].get(property);
				if (assignerList != null)
					for (final IDotAssignerFunction<?> assigner : assignerList)
						if (assigner.getValueClass().isSuperClassOf(value))
							return assigner;
			}
		}
		// Generic assigner
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId();
			if (classId < genericDotAssignerMap.length && genericDotAssignerMap[classId] != null)
				for (final IGenericDotAssignerFunction<?> assigner : genericDotAssignerMap[classId])
					if (assigner.isHandlingProperty(property))
						return assigner;
		}
		return null;
	}

	@Override
	public INamespace make() {
		return this;
	}

	@Nullable
	@Override
	public IVariableType getExpressionMethodReturnType(final IVariableType thisContext, final EMethod method,
			final IVariableType rhs) {
		final IExpressionFunction<?> func = expressionMethod(thisContext.getBasicLangClass(), method, rhs.getBasicLangClass());
		if (func == null)
			return null;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		if (!func.getRhsType(convertedThisContext).isAssignableFrom(rhs))
			return null;
		return func.getReturnType(convertedThisContext);
	}

	@Override
	public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
			final IVariableType value) {
		final IGenericBracketAssignerFunction<?> func = bracketAssigner(thisContext.getBasicLangClass(),
				property.getBasicLangClass(), value.getBasicLangClass());
		if (func == null)
			return false;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		if (!func.getPropertyType(convertedThisContext).isAssignableFrom(property)
				|| !func.getValueType(convertedThisContext).isAssignableFrom(value))
			return false;
		return true;
	}

	@Nullable
	@Override
	public IVariableType getBracketAccessorReturnType(final IVariableType thisContext, final IVariableType property) {
		final IGenericBracketAccessorFunction<?> func = bracketAccessor(thisContext.getBasicLangClass(),
				property.getBasicLangClass());
		if (func == null)
			return null;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		if (!func.getPropertyType(convertedThisContext).isAssignableFrom(property))
			return null;
		return func.getReturnType(convertedThisContext);
	}

	@Override
	public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
			final IVariableType value) {
		final IFunction<?> func = dotAssigner(thisContext.getBasicLangClass(), property, value.getBasicLangClass());
		if (func == null)
			return false;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		// Casting is better than repeating the search for a fitting function.
		if (func instanceof IDotAssignerFunction<?>) {
			final IDotAssignerFunction<?> f = ((IDotAssignerFunction<?>) func);
			if (!f.getValueType(convertedThisContext).isAssignableFrom(value))
				return false;
			return true;
		}
		final IGenericDotAssignerFunction<?> f = ((IGenericDotAssignerFunction<?>) func);
		if (!f.isHandlingProperty(convertedThisContext, property) || !f.getValueType(convertedThisContext, property).isAssignableFrom(value))
			return false;
		return true;
	}

	@Nullable
	@Override
	public IVariableType getDotAccessorReturnType(final IVariableType thisContext, final String property) {
		final IFunction<?> func = dotAccessor(thisContext.getBasicLangClass(), property);
		if (func == null)
			return null;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		// Casting is better than repeating the search for a fitting function.
		if (func instanceof IDotAccessorFunction<?>)
			return ((IDotAccessorFunction<?>) func).getReturnType(convertedThisContext);
		final IGenericDotAccessorFunction<?> f = ((IGenericDotAccessorFunction<?>) func);
		if (!f.isHandlingProperty(convertedThisContext, property))
			return null;
		return f.getReturnType(convertedThisContext, property);
	}

	@NotThreadSafe
	public static class Builder {
		@Nullable
		private Map<Integer, EnumMap<EMethod, List<IExpressionFunction<?>>>> expressionMethodMap;

		@Nullable
		private Map<Integer, Map<String, IDotAccessorFunction<?>>> dotAccessorMap;
		@Nullable
		private Map<Integer, Map<String, List<IDotAssignerFunction<?>>>> dotAssignerMap;

		@Nullable
		private Map<Integer, List<IGenericDotAccessorFunction<?>>> genericDotAccessorMap;
		@Nullable
		private Map<Integer, List<IGenericDotAssignerFunction<?>>> genericDotAssignerMap;

		@Nullable
		private Map<Integer, List<IGenericBracketAccessorFunction<?>>> genericBracketAccessorMap;
		@Nullable
		private Map<Integer, List<IGenericBracketAssignerFunction<?>>> genericBracketAssignerMap;

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
		public final <T extends ALangObject> Builder addExpressionMethod(final IMethod2Function<T>... functions)
				throws FormExpressionException {
			for (final IMethod2Function<?> f : functions) {
				if (f == null)
					throw new FormExpressionException();
				final Integer classId = f.getFunction().getThisContextType().getClassId();
				if (classId < 0)
					throw new FormExpressionException(String.format("Class id %d for %s is negative.", classId,
							f.getFunction().getThisContextType()));
				EnumMap<EMethod, List<IExpressionFunction<?>>> map = getExpressionMethodMap().get(classId);
				if (map == null)
					getExpressionMethodMap().put(classId, map = new EnumMap<>(EMethod.class));
				List<IExpressionFunction<?>> list = map.get(f.getMethod());
				if (list == null)
					map.put(f.getMethod(), list = new ArrayList<>());
				getClassPathMap().put(classId, f.getFunction().getThisContextType());
				list.add(f.getFunction());
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
		public final <T extends ALangObject> Builder addDotAccessor(final IDotAccessorFunction<T>... functions)
				throws FormExpressionException {
			for (final IDotAccessorFunction<?> f : functions)
				putFunctionSingle(NullUtil.checkNotNull(f), getDotAccessorMap());
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
		public final <T extends ALangObject> Builder addDotAssigner(final IDotAssignerFunction<T>... functions)
				throws FormExpressionException {
			for (final IDotAssignerFunction<?> f : functions)
				putFunctionArray(NullUtil.checkNotNull(f), getDotAssignerMap());
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
		public final <T extends ALangObject> Builder addGenericDotAccessor(
				final IGenericDotAccessorFunction<T>... functions) throws FormExpressionException {
			for (final IGenericDotAccessorFunction<?> f : functions)
				putFunctionGeneric(NullUtil.checkNotNull(f), getGenericDotAccessorMap());
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
		public final <T extends ALangObject> Builder addGenericDotAssigner(
				final IGenericDotAssignerFunction<T>... functions) throws FormExpressionException {
			for (final IGenericDotAssignerFunction<?> f : functions)
				putFunctionGeneric(NullUtil.checkNotNull(f), getGenericDotAssignerMap());
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
		public final <T extends ALangObject> Builder addGenericBracketAccessor(
				final IGenericBracketAccessorFunction<T>... functions) throws FormExpressionException {
			for (final IGenericBracketAccessorFunction<?> f : functions)
				putFunctionGeneric(NullUtil.checkNotNull(f), getGenericBracketAccessorMap());
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
		public final <T extends ALangObject> Builder addGenericBracketAssigner(
				final IGenericBracketAssignerFunction<T>... functions) throws FormExpressionException {
			for (final IGenericBracketAssignerFunction<?> f : functions)
				putFunctionGeneric(NullUtil.checkNotNull(f), getGenericBracketAssignerMap());
			return this;
		}

		private <T extends IFunction<?>> void putFunctionGeneric(final T f, final Map<Integer, List<T>> map) {
			final Integer classId = f.getThisContextType().getClassId();
			if (classId < 0)
				throw new FormExpressionException(
						String.format("Class id %d for %s is negative.", classId, f.getThisContextType()));
			getClassPathMap().put(classId, f.getThisContextType());
			@Nullable List<T> list = map.get(classId);
			if (list == null)
				map.put(classId, list = new ArrayList<>());
			list.add(f);
		}

		private <T extends IFunction<?>> void putFunctionArray(final T f, final Map<Integer, Map<String, List<T>>> map) {
			final Integer classId = f.getThisContextType().getClassId();
			if (classId < 0)
				throw new FormExpressionException(
						String.format("Class id %d for %s is negative.", classId, f.getThisContextType()));
			Map<String, List<T>> m = map.get(classId);
			if (m == null)
				map.put(classId, m = new HashMap<>());
			final String property = f.getDeclaredName();
			@Nullable List<T> list = m.get(property);
			if (list == null)
				m.put(property, list = new ArrayList<>());
			getClassPathMap().put(classId, f.getThisContextType());
			list.add(f);
		}

		private <T extends IFunction<?>> void putFunctionSingle(final T f, final Map<Integer, Map<String, T>> map) {
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

		private Map<Integer, EnumMap<EMethod, List<IExpressionFunction<?>>>> getExpressionMethodMap() {
			if (expressionMethodMap != null)
				return expressionMethodMap;
			return expressionMethodMap = new HashMap<>();
		}

		private Map<Integer, Map<String, IDotAccessorFunction<?>>> getDotAccessorMap() {
			if (dotAccessorMap != null)
				return dotAccessorMap;
			return dotAccessorMap = new HashMap<>();
		}

		private Map<Integer, Map<String, List<IDotAssignerFunction<?>>>> getDotAssignerMap() {
			if (dotAssignerMap != null)
				return dotAssignerMap;
			return dotAssignerMap = new HashMap<>();
		}

		private Map<Integer, List<IGenericDotAccessorFunction<?>>> getGenericDotAccessorMap() {
			if (genericDotAccessorMap != null)
				return genericDotAccessorMap;
			return genericDotAccessorMap = new HashMap<>();
		}

		private Map<Integer, List<IGenericDotAssignerFunction<?>>> getGenericDotAssignerMap() {
			if (genericDotAssignerMap != null)
				return genericDotAssignerMap;
			return genericDotAssignerMap = new HashMap<>();
		}

		private Map<Integer, List<IGenericBracketAccessorFunction<?>>> getGenericBracketAccessorMap() {
			if (genericBracketAccessorMap != null)
				return genericBracketAccessorMap;
			return genericBracketAccessorMap = new HashMap<>();
		}

		private Map<Integer, List<IGenericBracketAssignerFunction<?>>> getGenericBracketAssignerMap() {
			if (genericBracketAssignerMap != null)
				return genericBracketAssignerMap;
			return genericBracketAssignerMap = new HashMap<>();
		}

		private Map<Integer, ILangObjectClass> getClassPathMap() {
			if (classPathMap != null)
				return classPathMap;
			return classPathMap = new HashMap<>();
		}

		private void init() {
			expressionMethodMap = null;
			dotAccessorMap = null;
			dotAssignerMap = null;
			genericDotAccessorMap = null;
			genericDotAssignerMap = null;
			genericBracketAccessorMap = null;
			genericBracketAssignerMap = null;
			classPathMap = null;
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
			final INamespaceContractFactory factory = new GenericNamespaceContractFactory(getExpressionMethodMap(),
					getDotAccessorMap(), getDotAssignerMap(), getGenericDotAccessorMap(), getGenericDotAssignerMap(),
					getGenericBracketAccessorMap(), getGenericBracketAssignerMap());
			init();
			return factory;
		}
	}
}