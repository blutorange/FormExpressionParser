package de.xima.fc.form.expression.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.InvalidTemplateDataException;
import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.impl.embedment.EEmbedmentContractFactory;
import de.xima.fc.form.expression.util.CmnCnst;

@NonNullByDefault
public class IEmbedmentTest extends IFaceTest<IEmbedment> {

	public IEmbedmentTest(final IImplFactory<IEmbedment> implFactory) {
		super(implFactory, new IEmbedment(){
			@Override
			public void reset() {
				throw new RuntimeException("Do not test the dummy:"); //$NON-NLS-1$
			}
			@Override
			public void setCurrentEmbedment(@Nullable final String name) {
				throw new RuntimeException("Do not test the dummy:"); //$NON-NLS-1$
			}
			@Override
			public String[] getScopeListForCurrentEmbedment() {
				throw new RuntimeException("Do not test the dummy:"); //$NON-NLS-1$
			}
			@Nullable
			@Override
			public String[] getScopeList(final String embedment) {
				throw new RuntimeException("Do not test the dummy:"); //$NON-NLS-1$
			}
			@Override
			public String[] getEmbedmentList() {
				throw new RuntimeException("Do not test the dummy:"); //$NON-NLS-1$
			}
			@Override
			public void outputCode(final String data, final IEvaluationContext ec)
					throws EmbedmentOutputException, InvalidTemplateDataException {
				throw new RuntimeException("Do not test the dummy:"); //$NON-NLS-1$
			}
			@Override
			public void outputText(final String data, final IEvaluationContext ec)
					throws EmbedmentOutputException, InvalidTemplateDataException {
				throw new RuntimeException("Do not test the dummy:"); //$NON-NLS-1$
			}
		});
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
			impl.setCurrentEmbedment(CmnCnst.NonnullConstant.STRING_EMPTY);
			final String[] list = impl.getScopeList(emb);
			if (list == null)
				fail("It told me it supported the embedement " + emb); //$NON-NLS-1$
			else {
				impl.setCurrentEmbedment(emb);
				final String[] listCurr = impl.getScopeListForCurrentEmbedment();
				equalsArraysAsSet(list,listCurr);
			}
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
		return getInstancesToTest(IEmbedment.class, EEmbedmentContractFactory.FORMCYCLE_ALL.make(),
				EEmbedmentContractFactory.GENERAL.make());
	}
}
