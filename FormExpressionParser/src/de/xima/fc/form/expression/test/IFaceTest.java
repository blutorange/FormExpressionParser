package de.xima.fc.form.expression.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import de.xima.fc.form.expression.iface.IReset;
import de.xima.fc.form.expression.util.NullUtil;

@RunWith(Parameterized.class)
@NonNullByDefault
public abstract class IFaceTest<T> {

	protected final IImplFactory<T> implFactory;
	protected T impl;

	public IFaceTest(final IImplFactory<T> implFactory, final T dummy) {
		this.implFactory = implFactory;
		impl = dummy;
	}

	@Before
	public final void setUp() throws Exception {
		assertNotNull("Implementation factory must not be null", implFactory); //$NON-NLS-1$
		impl = implFactory.makeImpl();
		assertNotNull("Implementation must not be null", impl); //$NON-NLS-1$
		System.out.println("Testing impl" + impl.getClass().getCanonicalName() + " " + NullUtil.toString(impl)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@After
	public final void tearDown() throws Exception {
		if (impl instanceof IReset)
			((IReset) impl).reset();
	}

	protected final static <T> Collection<Object[]> getInstancesToTest(final Class<T> iface, @Nullable @SuppressWarnings("unchecked") final T... moreTestInstances) {
		final Reflections r = new Reflections("de.xima.fc.form.expression", new TypeAnnotationsScanner(), //$NON-NLS-1$
				new SubTypesScanner());
		final List<Object[]> list = new ArrayList<>();
		if (moreTestInstances != null)
			for (final T t : moreTestInstances) {
				final T t2 = NullUtil.checkNotNull(t);
				list.add(new IImplFactory[]{new IImplFactory<T>(){
					@Override
					public T makeImpl() {
						return t2;
					}
				}});
			}
		if (!iface.isInterface())
			fail(iface + " is not an interface."); //$NON-NLS-1$
		for (final Class<?> c : r.getSubTypesOf(iface)) {
			if ((c.getModifiers() & Modifier.ABSTRACT) == 0 && (c.getModifiers() & Modifier.INTERFACE) == 0) {
				if (iface.isAssignableFrom(c)) {
					@SuppressWarnings("unchecked")
					final Class<T> cb = (Class<T>) c;
					Constructor<T> noArgConstr = null;
					try {
						noArgConstr = cb.getConstructor();
					}
					catch (@SuppressWarnings("unused") final Exception e) {
						// We only want to know whether a no-arg constructor exists.
						// It's normal if it does not.
					}
					if (cb.isEnum()) {
						for (final T e: cb.getEnumConstants()) {
								final T e2 = NullUtil.checkNotNull(e);
								System.out.println("Loading " + iface.getSimpleName() + " implementation enum " + cb.getCanonicalName() + "." + e2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								list.add(new IImplFactory[]{ new IImplFactory<T>(){
									@Override
									public T makeImpl() {
										return e2;
									}
								}});
						}
					}
					else if (noArgConstr != null){
						System.out.println("Loading " + iface.getSimpleName() + " implementation " + cb.getCanonicalName()); //$NON-NLS-1$ //$NON-NLS-2$
						final Constructor<T> fNoArgConstr = noArgConstr;
						list.add(new IImplFactory[] { new IImplFactory<T>() {
							@Override
							public T makeImpl() {
								try {
									return NullUtil.checkNotNull(fNoArgConstr.newInstance());
								}
								catch (final InstantiationException e) {
									fail("Failed to instantiate implementation" + e.getMessage()); //$NON-NLS-1$
								}
								catch (final IllegalAccessException e) {
									fail("Failed to instantiate implementation. " + e.getMessage()); //$NON-NLS-1$
								}
								catch (final InvocationTargetException e) {
									fail("Failed to instantiate implementation. " + e.getMessage()); //$NON-NLS-1$
								}
								fail();
								throw new RuntimeException();
							}
						}});
					}
				}
				else
					fail(c.getCanonicalName() + " is not a subtype of " + iface.getCanonicalName()); //$NON-NLS-1$
			}
		}
		return list;
	}

	protected static interface IImplFactory<S> {
		public S makeImpl();
	}
}
