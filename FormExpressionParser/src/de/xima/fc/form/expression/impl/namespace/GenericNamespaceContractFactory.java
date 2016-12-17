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
import de.xima.fc.form.expression.util.CmnCnst;
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

	private final EnumMap<EMethod, IExpressionFunction<?>>[] expressionMethodMap;

	private final Map<String, IDotAccessorFunction<?>>[] dotAccessorMap;
	private final Map<String, IDotAssignerFunction<?>>[] dotAssignerMap;

	private final IGenericDotAccessorFunction<?>[] genericDotAccessorMap;
	private final IGenericDotAssignerFunction<?>[] genericDotAssignerMap;

	private final IGenericBracketAccessorFunction<?>[] genericBracketAccessorMap;
	private final IGenericBracketAssignerFunction<?>[] genericBracketAssignerMap;

	@SuppressWarnings("unchecked")
	private GenericNamespaceContractFactory(
			final Map<Integer, EnumMap<EMethod, IExpressionFunction<?>>> expressionMethodMap,
			final Map<Integer, Map<String, IDotAccessorFunction<?>>> dotAccessorMap,
			final Map<Integer, Map<String, IDotAssignerFunction<?>>> dotAssignerMap,
			final Map<Integer, IGenericDotAccessorFunction<?>> genericDotAccessorMap,
			final Map<Integer, IGenericDotAssignerFunction<?>> genericDotAssignerMap,
			final Map<Integer, IGenericBracketAccessorFunction<?>> genericBracketAccessorMap,
			final Map<Integer, IGenericBracketAssignerFunction<?>> genericBracketAssignerMap) {
		this.dotAccessorMap = toArray(dotAccessorMap, new Map[0]);
		this.dotAssignerMap = toArray(dotAssignerMap, new Map[0]);
		this.genericDotAccessorMap = toArray(genericDotAccessorMap, new IGenericDotAccessorFunction[0]);
		this.genericDotAssignerMap = toArray(genericDotAssignerMap, new IGenericDotAssignerFunction[0]);
		this.genericBracketAccessorMap = toArray(genericBracketAccessorMap, new IGenericBracketAccessorFunction[0]);
		this.genericBracketAssignerMap = toArray(genericBracketAssignerMap, new IGenericBracketAssignerFunction[0]);
		this.expressionMethodMap = toArray(expressionMethodMap, new EnumMap[0]);
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

	@Nullable
	@Override
	public IExpressionFunction<?> expressionMethod(final ILangObjectClass thisContext, final EMethod method) {
		final int classId = thisContext.getClassId();
		if (classId < expressionMethodMap.length)
			return expressionMethodMap[classId].get(method);
		final ILangObjectClass superType = thisContext.getSuperClass();
		return superType != null ? expressionMethod(superType, method) : null;
	}

	@Nullable
	@Override
	public IGenericBracketAccessorFunction<?> bracketAccessor(final ILangObjectClass thisContext) {
		return bracket(thisContext, genericBracketAccessorMap);
	}

	@Override
	@Nullable
	public IGenericBracketAssignerFunction<?> bracketAssigner(final ILangObjectClass thisContext) {
		return bracket(thisContext, genericBracketAssignerMap);
	}

	@Nullable
	private static <T extends IFunction<?>> T bracket(final ILangObjectClass thisContext, final T[] map) {
		final int classId = thisContext.getClassId();
		final T func = classId < map.length ? map[classId] : null;
		if (func != null)
			return func;
		final ILangObjectClass superType = thisContext.getSuperClass();
		return superType != null ? bracket(superType, map) : null;
	}

	@Nullable
	@Override
	public IFunction<?> dotAccessor(final ILangObjectClass thisContext, final String property) {
		return dot(thisContext, property, dotAccessorMap, genericDotAccessorMap);
	}

	@Override
	@Nullable
	public IFunction<?> dotAssigner(final ILangObjectClass thisContext, final String property) {
		return dot(thisContext, property, dotAssignerMap, genericDotAssignerMap);
	}

	@Nullable
	private static <T extends IFunction<?>, S extends IFunction<?>> IFunction<?> dot(final ILangObjectClass thisContext,
			final String property, final Map<String, T>[] map, final S[] genericMap) {
		final int classId = thisContext.getClassId();
		final T func = classId < map.length && map[classId] != null ? map[classId].get(property) : null;
		if (func != null)
			return func;
		if (classId < genericMap.length && genericMap[classId] != null)
			return genericMap[classId];
		final ILangObjectClass superType = thisContext.getSuperClass();
		return superType != null ? dot(superType, property, map, genericMap) : null;
	}

	@Override
	public INamespace make() {
		return this;
	}

	@Nullable
	@Override
	public IVariableType getExpressionMethodReturnType(final IVariableType lhs, final EMethod method,
			final IVariableType rhs) {
		final IExpressionFunction<?> f = expressionMethod(lhs.getBasicLangClass(), method);
		return f != null ? f.getReturnTypeFor(genericsType(lhs, f.getThisContextType()), rhs) : null;
	}

	@Override
	public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property,
			final IVariableType value) {
		final IGenericBracketAssignerFunction<?> func = bracketAssigner(thisContext.getBasicLangClass());
		return func != null
				? func.isBracketAssignerDefined(genericsType(thisContext, func.getThisContextType()), property, value)
				: false;
	}

	@Nullable
	@Override
	public IVariableType getBracketAccessorReturnType(final IVariableType thisContext, final IVariableType property) {
		final IGenericBracketAccessorFunction<?> func = bracketAccessor(thisContext.getBasicLangClass());
		return func != null
				? func.getBracketAccessorReturnType(genericsType(thisContext, func.getThisContextType()), property)
				: null;
	}

	@Override
	public boolean isDotAssignerDefined(final IVariableType thisContext, final String property,
			final IVariableType value) {
		final IFunction<?> func = dotAssigner(thisContext.getBasicLangClass(), property);
		if (func == null)
			return false;
		// Casting is better than repeating almost the same code from
		// dotAssigner(...).
		if (func instanceof IDotAssignerFunction<?>)
			return ((IDotAssignerFunction<?>) func).isDotAssignerDefined(thisContext, value);
		return ((IGenericDotAssignerFunction<?>) func)
				.isDotAssignerDefined(genericsType(thisContext, func.getThisContextType()), property, value);
	}

	@Nullable
	@Override
	public IVariableType getDotAccessorReturnType(final IVariableType thisContext, final String property) {
		final IFunction<?> func = dotAccessor(thisContext.getBasicLangClass(), property);
		if (func == null)
			return null;
		// Casting is better than repeating almost the same code from
		// dotAssigner(...).
		if (func instanceof IDotAccessorFunction<?>)
			return ((IDotAccessorFunction<?>) func).getDotAccessorReturnType(thisContext);
		return ((IGenericDotAccessorFunction<?>) func)
				.getDotAccessorReturnType(genericsType(thisContext, func.getThisContextType()), property);
	}

	private IVariableType genericsType(IVariableType declared, final ILangObjectClass actual) {
		while (declared.getBasicLangClass().getClassId() != actual.getClassId()) {
			final IVariableType d = declared.getBasicLangClass().getSuperType(declared);
			if (d == null)
				throw new FormExpressionException(
						NullUtil.messageFormat(CmnCnst.Error.INCONSISTENT_CLASS_HIERARCHY, declared, actual));
			declared = d;
		}
		return declared;
	}

	@NotThreadSafe
	public static class Builder {
		@Nullable
		private Map<Integer, EnumMap<EMethod, IExpressionFunction<?>>> expressionMethodMap;

		@Nullable
		private Map<Integer, Map<String, IDotAccessorFunction<?>>> dotAccessorMap;
		@Nullable
		private Map<Integer, Map<String, IDotAssignerFunction<?>>> dotAssignerMap;

		@Nullable
		private Map<Integer, IGenericDotAccessorFunction<?>> genericDotAccessorMap;
		@Nullable
		private Map<Integer, IGenericDotAssignerFunction<?>> genericDotAssignerMap;

		@Nullable
		private Map<Integer, IGenericBracketAccessorFunction<?>> genericBracketAccessorMap;
		@Nullable
		private Map<Integer, IGenericBracketAssignerFunction<?>> genericBracketAssignerMap;

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
		public final <T extends ALangObject> Builder addDotAccessor(final IDotAccessorFunction<T>... functions)
				throws FormExpressionException {
			for (final IDotAccessorFunction<?> f : functions)
				putFunction(f, getDotAccessorMap());
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
				putFunction(f, getDotAssignerMap());
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
				putFunctionGeneric(f, getGenericDotAccessorMap());
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
				putFunctionGeneric(f, getGenericDotAssignerMap());
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
				putFunctionGeneric(f, getGenericBracketAccessorMap());
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
				putFunctionGeneric(f, getGenericBracketAssignerMap());
			return this;
		}

		private <T extends IFunction<?>> void putFunctionGeneric(@Nullable final T f, final Map<Integer, T> map) {
			if (f == null)
				throw new FormExpressionException();
			final Integer classId = f.getThisContextType().getClassId();
			if (classId < 0)
				throw new FormExpressionException(
						String.format("Class id %d for %s is negative.", classId, f.getThisContextType()));
			getClassPathMap().put(classId, f.getThisContextType());
			map.put(classId, f);
		}

		private <T extends IFunction<?>> void putFunction(@Nullable final T f, final Map<Integer, Map<String, T>> map) {
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

		private Map<Integer, Map<String, IDotAccessorFunction<?>>> getDotAccessorMap() {
			if (dotAccessorMap != null)
				return dotAccessorMap;
			return dotAccessorMap = new HashMap<>();
		}

		private Map<Integer, Map<String, IDotAssignerFunction<?>>> getDotAssignerMap() {
			if (dotAssignerMap != null)
				return dotAssignerMap;
			return dotAssignerMap = new HashMap<>();
		}

		private Map<Integer, IGenericDotAccessorFunction<?>> getGenericDotAccessorMap() {
			if (genericDotAccessorMap != null)
				return genericDotAccessorMap;
			return genericDotAccessorMap = new HashMap<>();
		}

		private Map<Integer, IGenericDotAssignerFunction<?>> getGenericDotAssignerMap() {
			if (genericDotAssignerMap != null)
				return genericDotAssignerMap;
			return genericDotAssignerMap = new HashMap<>();
		}

		private Map<Integer, IGenericBracketAccessorFunction<?>> getGenericBracketAccessorMap() {
			if (genericBracketAccessorMap != null)
				return genericBracketAccessorMap;
			return genericBracketAccessorMap = new HashMap<>();
		}

		private Map<Integer, IGenericBracketAssignerFunction<?>> getGenericBracketAssignerMap() {
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

		// private void checkInheritance() throws FormExpressionException {
		// checkInheritance1(expressionMethodMap);
		// checkInheritance1(attrAccessorMap);
		// checkInheritance1(attrAssignerMap);
		// checkInheritance2(genericAttrAccessorMap);
		// checkInheritance2(genericAttrAssignerMap);
		// }
		//
		// private <T, F extends IFunction<?>, S extends Map<T, F>> void
		// checkInheritance1(
		// @Nullable final Map<Integer, S> map) throws FormExpressionException {
		// final Map<Integer, ILangObjectClass> classPathMap =
		// getClassPathMap();
		// if (map != null) {
		// for (final Entry<Integer, S> entry : map.entrySet()) {
		// final ILangObjectClass superClass =
		// classPathMap.get(entry.getKey()).getSuperType();
		// if (superClass != null) {
		// final S superMap = map.get(superClass.getClassId());
		// if (superMap != null) {
		// for (final Entry<T, F> entry2 : entry.getValue().entrySet()) {
		// final F thisFunction = entry2.getValue();
		// final F superFunction = superMap.get(entry2.getKey());
		// if (thisFunction != null && superFunction != null)
		// checkInheritance(thisFunction, superFunction);
		// }
		// }
		// }
		// }
		// }
		// }
		//
		// private <T extends IFunction<?>> void checkInheritance2(@Nullable
		// final Map<Integer, T> map)
		// throws FormExpressionException {
		// final Map<Integer, ILangObjectClass> classPathMap =
		// getClassPathMap();
		// if (map != null) {
		// for (final Entry<Integer, T> entry : map.entrySet()) {
		// final ILangObjectClass superClass =
		// classPathMap.get(entry.getKey()).getSuperType();
		// if (superClass != null) {
		// final T thisFunction = entry.getValue();
		// final T superFunction = map.get(superClass.getClassId());
		// if (thisFunction != null && superFunction != null)
		// checkInheritance(thisFunction, superFunction);
		// }
		// }
		// }
		// }
		//
		// private void checkInheritance(final IFunction<?> thisFunction, final
		// IFunction<?> superFunction)
		// throws FormExpressionException {
		// throw new FormExpressionException("TODO - not yet implemented");
		// }
	}
}