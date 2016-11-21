package de.xima.fc.form.expression.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import de.xima.fc.form.expression.exception.parse.CannotUnnestGlobalNestingException;
import de.xima.fc.form.expression.exception.parse.NestingLevelException;
import de.xima.fc.form.expression.exception.parse.NestingLevelTooDeepException;
import de.xima.fc.form.expression.iface.context.IBinding;

@SuppressWarnings("null")
@RunWith(Parameterized.class)
public class IBindingTest {

	private final static Object OBJECT_1 = new Object();
	private final static Object OBJECT_2 = new Object();
	private final static Object OBJECT_3 = new Object();
	
	private static interface IBindingFactory {
		public IBinding<Object> makeBinding();
	}

	private final IBindingFactory bindingFactory;
	private IBinding<Object> binding;

	public IBindingTest(final IBindingFactory bindingFactory) {
		this.bindingFactory = bindingFactory;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> instancesToTest() {
		final Reflections r = new Reflections("de.xima.fc.form.expression", new TypeAnnotationsScanner(),
				new SubTypesScanner());
		final List<Object[]> list = new ArrayList<>();
		for (final Class<?> c : r.getSubTypesOf(IBinding.class)) {
			if ((c.getModifiers() & Modifier.ABSTRACT) == 0 && (c.getModifiers() & Modifier.INTERFACE) == 0) {
				if (IBinding.class.isAssignableFrom(c)) {
					@SuppressWarnings("unchecked")
					final Class<IBinding<Object>> cb = (Class<IBinding<Object>>)c;
					System.out.println("Loading IBinding implementation " + cb.getCanonicalName());
					list.add(new IBindingFactory[]{new IBindingFactory() {
						@Override
						public IBinding<Object> makeBinding() {
							try {
								return cb.newInstance();
							}
							catch (final InstantiationException e) {
								fail("Failed to instantiate binding. " + e.getMessage());
							}
							catch (final IllegalAccessException e) {
								fail("Failed to instantiate binding. " + e.getMessage());
							}
							return null;
						}
					}});
				}
				else
					fail("Is not a subtype of IBinding: " + c.getCanonicalName());
			}
		}
		return list;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		assertNotNull("Binding factory must not be null", bindingFactory);
		binding = bindingFactory.makeBinding();
		assertNotNull("Binding must not be null", binding);
	}

	@After
	public void tearDown() throws Exception {
		binding.reset();
	}

	@Test
	public final void testUnnestingGlobalScopeThrows() {
		assertTrue(binding.isGlobal());
		try {
			binding.unnest();
		}
		catch (final CannotUnnestGlobalNestingException e) {
			return;
		}
		fail("Failed to throw an error when attempting to unnest global scope");
	}

	@Test
	public final void testDefiningVariable() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.defineVariable("foo", OBJECT_1);
		assertVariableEquals("foo", OBJECT_1);
	}

	@Test
	public final void testNestingLocallyDoesNotLookupInHigherLevels() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.defineVariable("foo", OBJECT_1);
		binding.nest();
		binding.defineVariable("foo", OBJECT_2);
		binding.nest();
		binding.defineVariable("foo", OBJECT_3);
		binding.nestLocal();
		assertNull(binding.getVariable("foo"));
	}
	
	@Test
	public final void testHasVariableAtCurrentLevel() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.defineVariable("foo", OBJECT_1);
		assertTrue(binding.hasVariableAtCurrentLevel("foo"));
		binding.nest();
		assertFalse(binding.hasVariableAtCurrentLevel("foo"));
		binding.unnest();
		assertTrue(binding.hasVariableAtCurrentLevel("foo"));
		binding.nestLocal();
		assertFalse(binding.hasVariableAtCurrentLevel("foo"));
		binding.unnest();
		assertTrue(binding.hasVariableAtCurrentLevel("foo"));
		binding.nest();
		binding.nest();
		binding.nest();
		binding.defineVariable("bar", OBJECT_2);
		assertTrue(binding.hasVariableAtCurrentLevel("bar"));
		binding.unnest();
		assertFalse(binding.hasVariableAtCurrentLevel("bar"));		
	}

	@Test
	public final void testNestingAndUnnestingSwitchBetweenLevels() throws NestingLevelTooDeepException, CannotUnnestGlobalNestingException {
		assertTrue(binding.isGlobal());
		binding.nest();
		assertFalse(binding.isGlobal());
		binding.nestLocal();
		assertFalse(binding.isGlobal());
		binding.unnest();
		assertFalse(binding.isGlobal());
		binding.unnest();
		assertTrue(binding.isGlobal());
	}

	@Test
	public final void testUnnestingDoesNotRememberVariables() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.nest();
		binding.defineVariable("foo", OBJECT_1);
		binding.nest();
		binding.defineVariable("foo", OBJECT_2);
		binding.defineVariable("bar", OBJECT_2);
		binding.unnest();
		assertNull(binding.getVariable("bar"));
		assertVariableEquals("foo", OBJECT_1);
		binding.nest();
		assertNull(binding.getVariable("bar"));
		assertVariableEquals("foo", OBJECT_1);
	}

	@Test
	public final void testScopingShadowsVariable() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.defineVariable("foo", OBJECT_1);
		binding.nest();
		assertFalse(binding.isGlobal());
		binding.defineVariable("foo", OBJECT_2);
		assertVariableEquals("foo", OBJECT_2);
	}

	@Test
	public final void testFallsBackToHigherNestingLevel() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.nest();
		binding.defineVariable("foo", OBJECT_1);
		binding.nest();
		assertVariableEquals("foo", OBJECT_1);
	}

	@Test
	public final void testDoesNotThrowWhenVariableHasNotBeenSet() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		assertNull(binding.getVariable("foo"));
	}

	@Test
	public final void testDoesNotFallBackAfterNestingLocally() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.nest();
		binding.defineVariable("foo", OBJECT_1);
		binding.nestLocal();
		assertNull(binding.getVariable("foo"));
	}

	@Test
	public final void testBookmarkingDownwardsOne() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.nest();
		binding.nest();
		binding.nest();
		binding.defineVariable("foo", OBJECT_1);
		final int bookmark = binding.getBookmark();
		binding.nest();
		binding.defineVariable("bar", OBJECT_2);
		binding.gotoBookmark(bookmark);
		assertEquals(bookmark, binding.getBookmark());
		assertVariableEquals("foo", OBJECT_1);
		assertNull(binding.getVariable("bar"));
	}

	@Test
	public final void testBookmarkingUpwardsOne() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.nest();
		binding.nest();
		binding.nest();
		binding.nest();
		binding.nest();
		binding.defineVariable("foo", OBJECT_1);
		binding.nest();
		final int bookmark = binding.getBookmark();
		binding.unnest();
		binding.gotoBookmark(bookmark);
		binding.defineVariable("foo", OBJECT_2);
		assertEquals(bookmark, binding.getBookmark());
		assertVariableEquals("foo", OBJECT_2);
		binding.unnest();
		assertVariableEquals("foo", OBJECT_1);
	}


	@Test
	public final void testBookmarkingUpwardsMany() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.nest();
		binding.nest();
		binding.nest();
		binding.defineVariable("foo", OBJECT_1);
		binding.nest();
		binding.nest();
		binding.nest();
		final int bookmark = binding.getBookmark();
		binding.unnest();
		binding.unnest();
		binding.unnest();
		binding.gotoBookmark(bookmark);
		binding.defineVariable("foo", OBJECT_2);
		assertEquals(bookmark, binding.getBookmark());
		assertVariableEquals("foo", OBJECT_2);
		binding.unnest();
		binding.unnest();
		binding.unnest();
		assertVariableEquals("foo", OBJECT_1);
	}

	@Test
	public final void testBookmarkingDownwardsMultiple() throws NestingLevelException {
		assertTrue(binding.isGlobal());
		binding.nest();
		binding.nest();
		binding.nest();
		binding.defineVariable("foo", OBJECT_1);
		final int bookmark = binding.getBookmark();
		binding.nest();
		binding.defineVariable("bar", OBJECT_2);
		binding.nest();
		binding.nest();
		binding.nest();
		binding.nest();
		binding.nest();
		binding.gotoBookmark(bookmark);
		assertEquals(bookmark, binding.getBookmark());
		assertVariableEquals("foo", OBJECT_1);
		assertNull(binding.getVariable("bar"));
	}

	@Test
	public final void testNestingDepthLimit() throws NestingLevelTooDeepException {
		assertTrue(binding.isGlobal());
		final int limit = binding.getNestingLimit();
		if (limit >= 0) {
			for (int i = limit; i --> 0;) {
				binding.nest();
			}
			assertTrue(binding.isAtMaximumNestingLimit());
			try {
				binding.nest();
			}
			catch (final NestingLevelTooDeepException e) {
				return;
			}
			fail("Failed to throw an error when attempting to nest after the limit was reached.");
		}
		else {
			// We cannot test no limit, but we can try a few times.
			for (int i = 500; i--> 0;)
				binding.nest();
		}
	}

	private void assertVariableEquals(final String name, final Object object) throws NestingLevelException {
		final Object res = binding.getVariable(name);
		assertNotNull(res);
		assertSame(res, object);
	}
}
