package de.xima.fc.form.expression.test.lang;

import static org.junit.Assert.fail;

import javax.annotation.Nonnull;

import org.junit.Test;

import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContextCommand;
import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand;
import de.xima.fc.form.expression.impl.externalcontext.AHtmlExternalContext;
import de.xima.fc.form.expression.impl.pool.GenericEcFactory;
import de.xima.fc.form.expression.object.ALangObject;

@SuppressWarnings("nls")
public class HtmlDocumentCommandTest {
	private static enum TestCase {
		TEST001("<html></html>", "<html></html>"),

		EDGE001("<html></html>", "<html><p></p>", DocumentCommand.newRemovePreviousTag("p"), "</html>"),
		EDGE002("<html><p></p></html>", "<html><p></p>", DocumentCommand.newRemoveEnclosingTag("p"), "</html>"),
		EDGE003("<html><p></p></html>", "<html><p></p>", DocumentCommand.newRemoveNextTag("p"), "</html>"),

		EDGE004("<html><p></p></html>", "<html><p></p", DocumentCommand.newRemovePreviousTag("p"), "></html>"),
		EDGE005("<html></html>", "<html><p></p", DocumentCommand.newRemoveEnclosingTag("p"), "></html>"),
		EDGE006("<html><p></p></html>", "<html><p></p", DocumentCommand.newRemoveNextTag("p"), "></html>"),

		EDGE007("<html><p></p></html>", "<html>", DocumentCommand.newRemovePreviousTag("p"), "<p></p></html>"),
		EDGE008("<html><p></p></html>", "<html>", DocumentCommand.newRemoveEnclosingTag("p"), "<p></p></html>"),
		EDGE009("<html></html>", "<html>", DocumentCommand.newRemoveNextTag("p"), "<p></p></html>"),

		EDGE010("<html><p></p></html>", "<html><", DocumentCommand.newRemovePreviousTag("p"), "p></p></html>"),
		EDGE011("<html></html>", "<html><", DocumentCommand.newRemoveEnclosingTag("p"), "p></p></html>"),
		EDGE012("<html><p></p></html>", "<html><", DocumentCommand.newRemoveNextTag("p"), "p></p></html>"),

		PREV001("<html><p id=2></p></html>", "<html><p id=1></p><p id=2>", DocumentCommand.newRemovePreviousTag("p"), "</p></html>"),
		PREV002("<html><p id=1><p id=2></p></p></html>", "<html><p id=1><p id=2>", DocumentCommand.newRemovePreviousTag("p"), "</p></p></html>"),

		ENCL001("<html></html>", "<html><p><p a></p>", DocumentCommand.newRemoveEnclosingTag("p"), "<p b></p></p></html>"),
		ENCL002("<html><p><p a></p></p></html>", "<html><p><p a></p><", DocumentCommand.newRemoveEnclosingTag("p"), "p b></p></p></html>"),

		NEXT001("<html><p></p></html>", "<html><p>", DocumentCommand.newRemoveNextTag("p"), "</p><p foo></p></html>"),

		LINK001("<html><a href=\"a\" target=\"c\">b</a></html>", "<html>", DocumentCommand.newInsertLink("a", "b", "c"), "</html>"),
		LINK002("<html><a href=\"a\"></a></html>", "<html>", DocumentCommand.newInsertLink("a", null, null), "</html>"),
		LINK003("<html><a href=\"&lt;&quot;&gt;\"></a></html>", "<html>", DocumentCommand.newInsertLink("<\">", null, null), "</html>"),
		;
		public final String expectedResult;
		public final Object[] stuffToWrite;

		private TestCase(final String expectedResult, final Object... stuffToWrite) {
			this.expectedResult = expectedResult;
			this.stuffToWrite = stuffToWrite;
		}
	}

	@SuppressWarnings("static-method")
	@Test
	public final void test() {
		for (final TestCase test : TestCase.values()) {
			System.out.println("Running test "+ test);
			String result = null;
			try {
				result = process(test.stuffToWrite);
			}
			catch (final Exception e) {
				fail(String.format("Test %s threw an exception.", test));
			}
			if (!test.expectedResult.equals(result))
				fail(String.format("Test %s did not return the expected result: %s", test, result));
		}
	}

	private static class HtmlExternalContext extends AHtmlExternalContext {
		private final StringBuilder sb = new StringBuilder();

		@Override
		public ALangObject fetchScopedVariable(final String scope, final String name, final IEvaluationContext ec) {
			return null;
		}

		@Override
		protected void output(final String html) throws EmbedmentOutputException {
			sb.append(html);
		}

		@Override
		protected void finishOutput() throws EmbedmentOutputException {
		}

		@Override
		public String toString() {
			return sb.toString();
		}
	}

	private static String process(final Object... stuffToWrite) throws Exception {
		final HtmlExternalContext hec = new HtmlExternalContext();
		@SuppressWarnings("null")
		@Nonnull
		final IEvaluationContext ec = GenericEcFactory.getPoolInstance().borrowObject();
		try {
			hec.beginWriting();
			for (final Object stuff : stuffToWrite) {
				if (stuff instanceof IExternalContextCommand)
					hec.process((IExternalContextCommand) stuff, ec);
				else {
					@SuppressWarnings("null")
					@Nonnull
					final String s = String.valueOf(stuff);
					hec.write(s);
				}
			}
			hec.finishWriting();
		}
		finally {
			GenericEcFactory.getPoolInstance().returnObject(ec);
		}
		return hec.toString();
	}

}
