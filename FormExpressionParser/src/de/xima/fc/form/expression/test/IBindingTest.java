package de.xima.fc.form.expression.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.parse.CannotUnnestGlobalNestingException;
import de.xima.fc.form.expression.exception.parse.NestingLevelTooDeepException;
import de.xima.fc.form.expression.iface.context.IBinding;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.impl.ReadScopedEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NumberLangObject;

@SuppressWarnings("null")
@RunWith(Parameterized.class)
public class IBindingTest {

	private static interface IBindingFactory {
		public IBinding makeBinding();
	}

	private final IBindingFactory bindingFactory;
	private IBinding binding;
	private static IEvaluationContext ec;

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
					final Class<IBinding> cb = (Class<IBinding>)c;
					System.out.println(cb.getCanonicalName());
					list.add(new IBindingFactory[]{new IBindingFactory() {
						@Override
						public IBinding makeBinding() {
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
		ec = ReadScopedEvaluationContext.getNewBasicEvaluationContext();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ec.reset();
	}

	@Before
	public void setUp() throws Exception {
		assertNotNull("Binding factory must not be null", bindingFactory);
		assertNotNull("Ec must not be null", ec);
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
			binding.unnest(ec);
		}
		catch (final EvaluationException e) {
			return;
		}
		fail("Failed to throw an error when attempting to unnest global scope");
	}

	@Test
	public final void testSettingAndGettingVariableWithoutScoping() throws EvaluationException {
		assertTrue(binding.isGlobal());
		binding.setVariable("foo", NumberLangObject.create(42));
		final ALangObject foo = binding.getVariable("foo");
		assertNotNull(foo);
		assertTrue(foo.equals(NumberLangObject.create(42)));
	}

	@Test
	public final void testNestingLocallyDoesNotBlockGlobalVariable() throws EvaluationException {
		assertTrue(binding.isGlobal());
		binding.setVariable("foo", NumberLangObject.create(0));
		binding.nestLocal(ec);
		final ALangObject foo = binding.getVariable("foo");
		assertNotNull(foo);
		assertTrue(foo.equals(NumberLangObject.create(0)));
	}

	@Test
	public final void testNestingAndUnnestingSwitchBetweenLevels() throws NestingLevelTooDeepException, CannotUnnestGlobalNestingException {
		assertTrue(binding.isGlobal());
		binding.nest(ec);
		assertFalse(binding.isGlobal());
		binding.nestLocal(ec);
		assertFalse(binding.isGlobal());
		binding.unnest(ec);
		assertFalse(binding.isGlobal());
		binding.unnest(ec);
		assertTrue(binding.isGlobal());
	}

	@Test
	public final void testUnnestingDoesNotRememberVariables() throws EvaluationException {
		assertTrue(binding.isGlobal());
		binding.nest(ec);
		binding.setVariable("foo", NumberLangObject.create(1));
		binding.nest(ec);
		binding.setVariable("foo", NumberLangObject.create(2));
		binding.setVariable("bar", NumberLangObject.create(2));
		binding.unnest(ec);
		assertNull(binding.getVariable("bar"));
		assertVariableEquals("foo", NumberLangObject.create(1));
		binding.nest(ec);
		assertNull(binding.getVariable("bar"));
		assertVariableEquals("foo", NumberLangObject.create(1));
	}

	@Test
	public final void testScopingShadowsVariable() throws EvaluationException {
		assertTrue(binding.isGlobal());
		binding.setVariable("foo", NumberLangObject.create(0));
		binding.nest(ec);
		assertFalse(binding.isGlobal());
		binding.setVariable("foo", NumberLangObject.create(1));
		assertVariableEquals("foo", NumberLangObject.create(1));
	}

	@Test
	public final void testFallsBackToHigherNestingLevel() throws EvaluationException {
		assertTrue(binding.isGlobal());
		binding.nest(ec);
		binding.setVariable("foo", NumberLangObject.create(0));
		binding.nest(ec);
		assertVariableEquals("foo", NumberLangObject.create(0));
	}

	@Test
	public final void testDoesNotThrowWhenVariableHasNotBeenSet() throws EvaluationException {
		assertTrue(binding.isGlobal());
		final ALangObject foo = binding.getVariable("foo");
		assertNull(foo);
	}

	@Test
	public final void testDoesNotFallBackAfterNestingLocally() throws EvaluationException {
		assertTrue(binding.isGlobal());
		binding.nest(ec);
		binding.setVariable("foo", NumberLangObject.create(0));
		binding.nestLocal(ec);
		assertNull(binding.getVariable("foo"));
	}

	@Test
	public final void testNestingDepthLimit() throws NestingLevelTooDeepException {
		assertTrue(binding.isGlobal());
		final int limit = binding.getNestingLimit();
		if (limit >= 0) {
			for (int i = limit; i --> 0;) {
				binding.nest(ec);
			}
			assertTrue(binding.isAtMaximumNestingLimit());
			try {
				binding.nest(ec);
			}
			catch (final NestingLevelTooDeepException e) {
				return;
			}
			fail("Failed to throw an error when attempting to nest after the limit was reached.");
		}
		else {
			// We cannot test no limit, but we can try a few times.
			for (int i = 500; i--> 0;)
				binding.nest(ec);
		}
	}

	private void assertVariableEquals(final String name, final ALangObject object) throws EvaluationException {
		final ALangObject res = binding.getVariable(name);
		assertNotNull(res);
		assertTrue(res.equals(object));
	}

}
