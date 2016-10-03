package de.xima.fc.form.expression.test;

import org.junit.Test;

import de.xima.fc.form.expression.object.NumberLangObject;

public class Tests {
	final static NumberLangObject N42 = NumberLangObject.create(42);

	@Test
	public void testSemanticsSuccess() {
		TestUtil.test(SemanticsSuccess.class);
	}

	@Test
	public void testSemanticsFailure() {
		TestUtil.test(SemanticsFailure.class);
	}

	@Test
	public final void testSyntaxSuccess() {
		TestUtil.test(SyntaxSuccess.class);
	}

	@Test
	public final void testSyntaxFail() {
		TestUtil.test(SyntaxFailure.class);
	}
}
