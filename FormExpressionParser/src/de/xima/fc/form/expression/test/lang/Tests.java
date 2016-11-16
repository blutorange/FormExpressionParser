package de.xima.fc.form.expression.test.lang;

import org.junit.Test;

import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;

public class Tests {
	final static NumberLangObject N0 = NumberLangObject.create(0);
	final static NumberLangObject N42 = NumberLangObject.create(42);
	public static final BooleanLangObject TRUE = BooleanLangObject.getTrueInstance();
	public static final BooleanLangObject FALSE = BooleanLangObject.getFalseInstance();

	@Test
	public void testSemanticsSuccess() {
		TestUtil.test(SemanticsSuccess.class);
	}

	@Test
	public void testSemanticsFailure() {
		TestUtil.test(SemanticsFailure.class);
	}

	@SuppressWarnings("static-method")
	@Test
	public final void testSyntaxSuccess() {
		TestUtil.test(SyntaxSuccess.class);
	}

	@SuppressWarnings("static-method")
	@Test
	public final void testSyntaxFailure() {
		TestUtil.test(SyntaxFailure.class);
	}
}
