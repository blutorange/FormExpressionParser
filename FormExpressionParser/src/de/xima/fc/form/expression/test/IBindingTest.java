package de.xima.fc.form.expression.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.xima.fc.form.expression.exception.parse.CannotUnnestGlobalNestingException;
import de.xima.fc.form.expression.exception.parse.NestingLevelException;
import de.xima.fc.form.expression.exception.parse.NestingLevelTooDeepException;
import de.xima.fc.form.expression.iface.evaluate.IBinding;

@RunWith(Parameterized.class)
@NonNullByDefault
public class IBindingTest extends IFaceTest<IBinding<Object>> {

	private final static Object OBJECT_1 = new Object();
	private final static Object OBJECT_2 = new Object();
	private final static Object OBJECT_3 = new Object();

	public IBindingTest(final IImplFactory<IBinding<Object>> factory) {
		super(factory, new IBinding<Object>() {
			@Override
			public void reset() {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Nullable
			@Override
			public Object getVariable(final String name) {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public boolean hasVariableAtCurrentLevel(final String name) {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public void defineVariable(final String name, final Object object) {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public void nest() throws NestingLevelTooDeepException {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public void nestLocal() throws NestingLevelTooDeepException {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public void unnest() throws CannotUnnestGlobalNestingException {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public int getNestingLimit() {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public boolean isAtMaximumNestingLimit() {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public boolean isGlobal() {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public int getBookmark() {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
			@Override
			public void gotoBookmark(final int bookmark) throws NestingLevelException {
				throw new RuntimeException("Do not test the dummy."); //$NON-NLS-1$
			}
		});
	}

	@Parameterized.Parameters
	public static Collection<Object[]> instancesToTest() {
		return getInstancesToTest(IBinding.class);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public final void testUnnestingGlobalScopeThrows() {
		assertTrue(impl.isGlobal());
		try {
			impl.unnest();
		}
		catch (@SuppressWarnings("unused") final CannotUnnestGlobalNestingException e) {
			return;
		}
		fail("Failed to throw an error when attempting to unnest global scope"); //$NON-NLS-1$
	}

	@Test
	public final void testDefiningVariable() {
		assertTrue(impl.isGlobal());
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		assertVariableEquals("foo", OBJECT_1); //$NON-NLS-1$
	}

	@Test
	public final void testNestingLocallyDoesNotLookupInHigherLevels() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		impl.nest();
		impl.defineVariable("foo", OBJECT_2); //$NON-NLS-1$
		impl.nest();
		impl.defineVariable("foo", OBJECT_3); //$NON-NLS-1$
		impl.nestLocal();
		assertNull(impl.getVariable("foo")); //$NON-NLS-1$
	}

	@Test
	public final void testHasVariableAtCurrentLevel() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		assertTrue(impl.hasVariableAtCurrentLevel("foo")); //$NON-NLS-1$
		impl.nest();
		assertFalse(impl.hasVariableAtCurrentLevel("foo")); //$NON-NLS-1$
		impl.unnest();
		assertTrue(impl.hasVariableAtCurrentLevel("foo")); //$NON-NLS-1$
		impl.nestLocal();
		assertFalse(impl.hasVariableAtCurrentLevel("foo")); //$NON-NLS-1$
		impl.unnest();
		assertTrue(impl.hasVariableAtCurrentLevel("foo")); //$NON-NLS-1$
		impl.nest();
		impl.nest();
		impl.nest();
		impl.defineVariable("bar", OBJECT_2); //$NON-NLS-1$
		assertTrue(impl.hasVariableAtCurrentLevel("bar")); //$NON-NLS-1$
		impl.unnest();
		assertFalse(impl.hasVariableAtCurrentLevel("bar"));		 //$NON-NLS-1$
	}

	@Test
	public final void testNestingAndUnnestingSwitchBetweenLevels() throws NestingLevelTooDeepException, CannotUnnestGlobalNestingException {
		assertTrue(impl.isGlobal());
		impl.nest();
		assertFalse(impl.isGlobal());
		impl.nestLocal();
		assertFalse(impl.isGlobal());
		impl.unnest();
		assertFalse(impl.isGlobal());
		impl.unnest();
		assertTrue(impl.isGlobal());
	}

	@Test
	public final void testUnnestingDoesNotRememberVariables() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.nest();
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		impl.nest();
		impl.defineVariable("foo", OBJECT_2); //$NON-NLS-1$
		impl.defineVariable("bar", OBJECT_2); //$NON-NLS-1$
		impl.unnest();
		assertNull(impl.getVariable("bar")); //$NON-NLS-1$
		assertVariableEquals("foo", OBJECT_1); //$NON-NLS-1$
		impl.nest();
		assertNull(impl.getVariable("bar")); //$NON-NLS-1$
		assertVariableEquals("foo", OBJECT_1); //$NON-NLS-1$
	}

	@Test
	public final void testScopingShadowsVariable() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		impl.nest();
		assertFalse(impl.isGlobal());
		impl.defineVariable("foo", OBJECT_2); //$NON-NLS-1$
		assertVariableEquals("foo", OBJECT_2); //$NON-NLS-1$
	}

	@Test
	public final void testFallsBackToHigherNestingLevel() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.nest();
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		impl.nest();
		assertVariableEquals("foo", OBJECT_1); //$NON-NLS-1$
	}

	@Test
	public final void testDoesNotThrowWhenVariableHasNotBeenSet() {
		assertTrue(impl.isGlobal());
		assertNull(impl.getVariable("foo")); //$NON-NLS-1$
	}

	@Test
	public final void testDoesNotFallBackAfterNestingLocally() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.nest();
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		impl.nestLocal();
		assertNull(impl.getVariable("foo")); //$NON-NLS-1$
	}

	@Test
	public final void testBookmarkingDownwardsOne() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.nest();
		impl.nest();
		impl.nest();
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		final int bookmark = impl.getBookmark();
		impl.nest();
		impl.defineVariable("bar", OBJECT_2); //$NON-NLS-1$
		impl.gotoBookmark(bookmark);
		assertEquals(bookmark, impl.getBookmark());
		assertVariableEquals("foo", OBJECT_1); //$NON-NLS-1$
		assertNull(impl.getVariable("bar")); //$NON-NLS-1$
	}

	@Test
	public final void testBookmarkingUpwardsOne() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.nest();
		impl.nest();
		impl.nest();
		impl.nest();
		impl.nest();
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		impl.nest();
		final int bookmark = impl.getBookmark();
		impl.unnest();
		impl.gotoBookmark(bookmark);
		impl.defineVariable("foo", OBJECT_2); //$NON-NLS-1$
		assertEquals(bookmark, impl.getBookmark());
		assertVariableEquals("foo", OBJECT_2); //$NON-NLS-1$
		impl.unnest();
		assertVariableEquals("foo", OBJECT_1); //$NON-NLS-1$
	}


	@Test
	public final void testBookmarkingUpwardsMany() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.nest();
		impl.nest();
		impl.nest();
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		impl.nest();
		impl.nest();
		impl.nest();
		final int bookmark = impl.getBookmark();
		impl.unnest();
		impl.unnest();
		impl.unnest();
		impl.gotoBookmark(bookmark);
		impl.defineVariable("foo", OBJECT_2); //$NON-NLS-1$
		assertEquals(bookmark, impl.getBookmark());
		assertVariableEquals("foo", OBJECT_2); //$NON-NLS-1$
		impl.unnest();
		impl.unnest();
		impl.unnest();
		assertVariableEquals("foo", OBJECT_1); //$NON-NLS-1$
	}

	@Test
	public final void testBookmarkingDownwardsMultiple() throws NestingLevelException {
		assertTrue(impl.isGlobal());
		impl.nest();
		impl.nest();
		impl.nest();
		impl.defineVariable("foo", OBJECT_1); //$NON-NLS-1$
		final int bookmark = impl.getBookmark();
		impl.nest();
		impl.defineVariable("bar", OBJECT_2); //$NON-NLS-1$
		impl.nest();
		impl.nest();
		impl.nest();
		impl.nest();
		impl.nest();
		impl.gotoBookmark(bookmark);
		assertEquals(bookmark, impl.getBookmark());
		assertVariableEquals("foo", OBJECT_1); //$NON-NLS-1$
		assertNull(impl.getVariable("bar")); //$NON-NLS-1$
	}

	@Test
	public final void testNestingDepthLimit() throws NestingLevelTooDeepException {
		assertTrue(impl.isGlobal());
		final int limit = impl.getNestingLimit();
		if (limit >= 0) {
			for (int i = limit; i --> 0;) {
				impl.nest();
			}
			assertTrue(impl.isAtMaximumNestingLimit());
			try {
				impl.nest();
			}
			catch (@SuppressWarnings("unused") final NestingLevelTooDeepException e) {
				return;
			}
			fail("Failed to throw an error when attempting to nest after the limit was reached."); //$NON-NLS-1$
		}
		else {
			// We cannot test no limit, but we can try a few times.
			for (int i = 500; i--> 0;)
				impl.nest();
		}
	}

	private void assertVariableEquals(final String name, final Object object) {
		final Object res = impl.getVariable(name);
		assertNotNull(res);
		assertSame(res, object);
	}
}