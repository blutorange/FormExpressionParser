package de.xima.fc.form.expression.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;

import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.impl.embedment.EEmbedmentFactory;

public class IEmbedmentTest extends IFaceTest<IEmbedment> {

	public IEmbedmentTest(final IImplFactory<IEmbedment> implFactory) {
		super(implFactory);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetScopeList() {
		// Get all embedments and check which scopes they define
		for (final String emb : impl.getEmbedmentList()) {
			assertNotNull(emb);
			impl.setCurrentEmbedment("");
			final String[] list = impl.getScopeList(emb);
			impl.setCurrentEmbedment(emb);
			final String[] listCurr = impl.getScopeListForCurrentEmbedment();
			equalsArraysAsSet(list,listCurr);
		}
	}

	private void equalsArraysAsSet(final String[] a1, final String[] a2) {
		assertNotNull(a1);
		assertNotNull(a2);
		assertTrue(a1.length == a2.length);
		final Set<String> s1 = new HashSet<>();
		final Set<String> s2 = new HashSet<>();
		for (final String s : a1) {
			assertNotNull(s);
			assertFalse(s1.contains(s));
			s1.add(s);
		}
		for (final String s : a2) {
			assertNotNull(s);
			assertFalse(s2.contains(s));
			s2.add(s);
		}
		assertTrue(s1.equals(s2));
	}

	@Parameterized.Parameters
	public static Collection<Object[]> instancesToTest() {
		return getInstancesToTest(IEmbedment.class, EEmbedmentFactory.FORMCYCLE.makeEmbedment(),
				EEmbedmentFactory.GENERAL.makeEmbedment());
	}
}
