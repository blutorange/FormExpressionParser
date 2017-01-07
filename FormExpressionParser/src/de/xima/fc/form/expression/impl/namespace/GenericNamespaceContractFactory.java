package de.xima.fc.form.expression.impl.namespace;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.jdt.annotation.NonNullByDefault;

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
@NonNullByDefault
public final class GenericNamespaceContractFactory implements INamespaceContractFactory, INamespace {
	private static final long serialVersionUID = 1L;

	private final EnumMap<EMethod, IExpressionFunction<?>>[] expressionMethodMap;

	private final Map<String, IDotAccessorFunction<?>>[] dotAccessorMap;
	private final Map<String, IDotAssignerFunction<?>>[] dotAssignerMap;

	private final IGenericDotAccessorFunction<?>[] genericDotAccessorMap;
	private final IGenericDotAssignerFunction<?>[] genericDotAssignerMap;

	/** genericBracketAccessorMap[classId][index] */
	private final IGenericBracketAccessorFunction<?>[] genericBracketAccessorMap;

	/** genericBracketAssignerMap[classId] */
	private final IGenericBracketAssignerFunction<?>[] genericBracketAssignerMap;

	@SuppressWarnings("unchecked")
	protected GenericNamespaceContractFactory(
			final Map<Integer, EnumMap<EMethod, IExpressionFunction<?>>> expressionMethodMap,
			final Map<Integer, Map<String, IDotAccessorFunction<?>>> dotAccessorMap,
			final Map<Integer, Map<String, IDotAssignerFunction<?>>> dotAssignerMap,
			final Map<Integer, IGenericDotAccessorFunction<?>> genericDotAccessorMap,
			final Map<Integer, IGenericDotAssignerFunction<?>> genericDotAssignerMap,
			final Map<Integer, IGenericBracketAccessorFunction<?>> genericBracketAccessorMap,
			final Map<Integer, IGenericBracketAssignerFunction<?>> genericBracketAssignerMap) {
		this.expressionMethodMap = toArray(expressionMethodMap, new EnumMap[0]);
		this.dotAccessorMap = toArray(dotAccessorMap, new Map[0]);
		this.dotAssignerMap = toArray(dotAssignerMap, new Map[0]);
		this.genericDotAccessorMap = toArray(genericDotAccessorMap, new IGenericDotAccessorFunction<?>[0]);
		this.genericDotAssignerMap = toArray(genericDotAssignerMap, new IGenericDotAssignerFunction<?>[0]);
		this.genericBracketAccessorMap = toArray(genericBracketAccessorMap, new IGenericBracketAccessorFunction<?>[0]);
		this.genericBracketAssignerMap = toArray(genericBracketAssignerMap, new IGenericBracketAssignerFunction<?>[0]);
	}

	private static <T> T[] toArray(final Map<Integer, T> map, final T[] array) {
		int max = 0;
		for (final Integer i : map.keySet())
			if (i.intValue() > max)
				max = i.intValue();
		final T[] arr = NullUtil.checkNotNull(Arrays.copyOf(array, max + 1));
		for (final Entry<Integer, T> entry : map.entrySet())
			arr[entry.getKey().intValue()] = entry.getValue();
		return arr;
	}

	@Nullable
	@Override
	public IExpressionFunction<?> expressionMethod(final ILangObjectClass thisContext, final EMethod method) {
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId().intValue();
			if (classId < expressionMethodMap.length && expressionMethodMap[classId] != null) {
				final IExpressionFunction<?> func = expressionMethodMap[classId].get(method);
					if (func != null)
						return func;
			}
		}
		return null;
	}

	@Nullable
	@Override
	public IGenericBracketAccessorFunction<?> bracketAccessor(final ILangObjectClass thisContext) {
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId().intValue();
			if (classId < genericBracketAccessorMap.length && genericBracketAccessorMap[classId] != null)
				return genericBracketAccessorMap[classId];
		}
		return null;
	}

	@Override
	@Nullable
	public IGenericBracketAssignerFunction<?> bracketAssigner(final ILangObjectClass thisContext) {
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId().intValue();
			if (classId < genericBracketAssignerMap.length && genericBracketAssignerMap[classId] != null)
				return genericBracketAssignerMap[classId];
		}
		return null;
	}

	@Nullable
	@Override
	public IFunction<?> dotAccessor(final ILangObjectClass thisContext, final String property) {
		// Named accessor
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId().intValue();
			if (classId < dotAccessorMap.length && dotAccessorMap[classId] != null) {
				final IDotAccessorFunction<?> accessor = dotAccessorMap[classId].get(property);
				if (accessor != null)
					return accessor;
			}
		}
		// Generic accessor
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId().intValue();
			if (classId < genericDotAccessorMap.length && genericDotAccessorMap[classId] != null)
				if (genericDotAccessorMap[classId].isHandlingProperty(property))
					return genericDotAccessorMap[classId];
		}
		return null;
	}

	@Override
	@Nullable
	public IFunction<?> dotAssigner(final ILangObjectClass thisContext, final String property) {
		// Named assigner
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId().intValue();
			if (classId < dotAssignerMap.length && dotAssignerMap[classId] != null) {
				final IDotAssignerFunction<?> assigner = dotAssignerMap[classId].get(property);
				if (assigner != null)
					return assigner;
			}
		}
		// Generic assigner
		for (ILangObjectClass clazz = thisContext; clazz != null; clazz = clazz.getSuperClass()) {
			final int classId = clazz.getClassId().intValue();
			if (classId < genericDotAssignerMap.length && genericDotAssignerMap[classId] != null)
				if (genericDotAssignerMap[classId].isHandlingProperty(property))
					return genericDotAssignerMap[classId];
		}
		return null;
	}

	@Override
	public INamespace make() {
		return this;
	}

	@Override
	public void reset() {
		// nothing to reset, we are immutable
	}

	@Nullable
	@Override
	public IValueReturn getExpressionMethodInfo(final IVariableType thisContext, final EMethod method) {
		final IExpressionFunction<?> func = expressionMethod(thisContext.getBasicLangClass(), method);
		if (func == null)
			return null;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		return new ValueReturn(func.getValueType(convertedThisContext), func.getReturnType(convertedThisContext));
	}

	@Nullable
	@Override
	public IPropertyValue getBracketAssignerInfo(final IVariableType thisContext) {
		final IGenericBracketAssignerFunction<?> func = bracketAssigner(thisContext.getBasicLangClass());
		if (func == null)
			return null;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		return new PropertyValue(func.getPropertyType(convertedThisContext), func.getValueType(convertedThisContext));
	}

	@Nullable
	@Override
	public IPropertyReturn getBracketAccessorInfo(final IVariableType thisContext) {
		final IGenericBracketAccessorFunction<?> func = bracketAccessor(thisContext.getBasicLangClass());
		if (func == null)
			return null;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		return new PropertyReturn(func.getPropertyType(convertedThisContext), func.getReturnType(convertedThisContext));
	}

	@Nullable
	@Override
	public IValue getDotAssignerInfo(final IVariableType thisContext, final String property) {
		final IFunction<?> func = dotAssigner(thisContext.getBasicLangClass(), property);
		if (func == null)
			return null;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		// Casting is better than repeating the search for a fitting function.
		if (func instanceof IDotAssignerFunction<?>) {
			final IDotAssignerFunction<?> f = ((IDotAssignerFunction<?>) func);
			return new Value(f.getValueType(convertedThisContext));
		}
		final IGenericDotAssignerFunction<?> f = ((IGenericDotAssignerFunction<?>) func);
		return new Value(f.getValueType(convertedThisContext, property));
	}

	@Nullable
	@Override
	public IReturn getDotAccessorInfo(final IVariableType thisContext, final String property, final IVariableType[] dotGenerics) {
		final IFunction<?> func = dotAccessor(thisContext.getBasicLangClass(), property);
		if (func == null)
			return null;
		final IVariableType convertedThisContext = thisContext.upconvert(func.getThisContextType());
		// Casting is better than repeating the search for a fitting function.
		if (func instanceof IDotAccessorFunction<?>) {
			final IDotAccessorFunction<?> f = ((IDotAccessorFunction<?>) func);
			if (!f.supportsGenerics(dotGenerics))
				return null;
			return new Return(f.getReturnType(convertedThisContext, dotGenerics));
		}
		final IGenericDotAccessorFunction<?> f = ((IGenericDotAccessorFunction<?>) func);
		if (!f.supportsGenerics(dotGenerics))
			return null;
		return new Return(f.getReturnType(convertedThisContext, property, dotGenerics));
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
				if (classId.intValue() < 0)
					throw new FormExpressionException(NullUtil.messageFormat(CmnCnst.Error.NEGATIVE_CLASS_ID, classId,
							f.getFunction().getThisContextType()));
				EnumMap<EMethod, IExpressionFunction<?>> map = getExpressionMethodMap().get(classId);
				if (map == null)
					getExpressionMethodMap().put(classId, map = new EnumMap<>(EMethod.class));
				if (map.containsKey(f.getMethod()))
					throw new FormExpressionException(NullUtil.messageFormat(CmnCnst.Error.METHOD_ALREADY_SET,
							f.getMethod(), f.getFunction().getThisContextType()));
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
				putFunctionSingle(NullUtil.checkNotNull(f), getDotAssignerMap());
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

		private <T extends IFunction<?>> void putFunctionGeneric(final T f, final Map<Integer, T> map) {
			final Integer classId = f.getThisContextType().getClassId();
			if (classId.intValue() < 0)
				throw new FormExpressionException(
						NullUtil.messageFormat(CmnCnst.Error.NEGATIVE_CLASS_ID, classId, f.getThisContextType()));
			if (map.containsKey(classId))
				throw new FormExpressionException(
						NullUtil.messageFormat(CmnCnst.Error.METHOD_ALREADY_SET, f, f.getThisContextType()));
			getClassPathMap().put(classId, f.getThisContextType());
			map.put(classId, f);
		}

		private <T extends IFunction<?>> void putFunctionSingle(final T f, final Map<Integer, Map<String, T>> map) {
			final Integer classId = f.getThisContextType().getClassId();
			if (classId.intValue() < 0)
				throw new FormExpressionException(
						NullUtil.messageFormat(CmnCnst.Error.NEGATIVE_CLASS_ID, classId, f.getThisContextType()));
			Map<String, T> m = map.get(classId);
			if (m == null)
				map.put(classId, m = new HashMap<>());
			if (m.containsKey(f.getDeclaredName()))
				throw new FormExpressionException(
						NullUtil.messageFormat(CmnCnst.Error.METHOD_ALREADY_SET, f.getDeclaredName(), f.getThisContextType()));
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
			// methods are searched by the run-time type of the first argument only (thisContext)
			//
			final INamespaceContractFactory factory = new GenericNamespaceContractFactory(getExpressionMethodMap(),
					getDotAccessorMap(), getDotAssignerMap(), getGenericDotAccessorMap(), getGenericDotAssignerMap(),
					getGenericBracketAccessorMap(), getGenericBracketAssignerMap());
			init();
			return factory;
		}
	}
}