package de.xima.fc.form.expression.test;

import static org.junit.Assert.fail;

import javax.annotation.Nonnull;

import org.junit.Test;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.iface.factory.IEmbedmentContractFactory;
import de.xima.fc.form.expression.iface.factory.IExternalContextContractFactory;
import de.xima.fc.form.expression.iface.factory.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.factory.ILoggerContractFactory;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand;
import de.xima.fc.form.expression.impl.embedment.EEmbedmentContractFactory;
import de.xima.fc.form.expression.impl.externalcontext.AHtmlExternalContext;
import de.xima.fc.form.expression.impl.formexpression.EvaluationContextImpl;
import de.xima.fc.form.expression.impl.library.ELibraryContractFactory;
import de.xima.fc.form.expression.impl.logger.ELoggerContractFactory;
import de.xima.fc.form.expression.impl.namespace.ENamespaceContractFactory;
import de.xima.fc.form.expression.impl.tracer.DummyTracer;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.NullUtil;

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

		PREV001("<html><p id=2></p></html>", "<html><p id=1></p><p id=2>", DocumentCommand.newRemovePreviousTag("p"),
				"</p></html>"),
		PREV002("<html><p id=1><p id=2></p></p></html>", "<html><p id=1><p id=2>",
				DocumentCommand.newRemovePreviousTag("p"), "</p></p></html>"),

		ENCL001("<html></html>", "<html><p><p a></p>", DocumentCommand.newRemoveEnclosingTag("p"),
				"<p b></p></p></html>"),
		ENCL002("<html><p><p a></p></p></html>", "<html><p><p a></p><", DocumentCommand.newRemoveEnclosingTag("p"),
				"p b></p></p></html>"),

		NEXT001("<html><p></p></html>", "<html><p>", DocumentCommand.newRemoveNextTag("p"), "</p><p foo></p></html>"),

		LINK001("<html><a href=\"a\" target=\"c\">b</a></html>", "<html>", DocumentCommand.newInsertLink("a", "b", "c"),
				"</html>"),
		LINK002("<html><a href=\"a\"></a></html>", "<html>", DocumentCommand.newInsertLink("a", null, null), "</html>"),
		LINK003("<html><a href=\"&lt;&quot;&gt;\"></a></html>", "<html>",
				DocumentCommand.newInsertLink("<\">", null, null), "</html>"),;
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
			System.out.println("Running test " + test);
			String result = null;
			try {
				result = process(test.stuffToWrite);
			}
			catch (final Exception e) {
				e.printStackTrace(System.err);
				fail(String.format("Test %s threw an exception.", test));
			}
			if (!test.expectedResult.equals(result))
				fail(String.format("Test %s did not return the expected result: %s", test, result));
		}
	}

	private static class DummyHtmlExternalContext extends AHtmlExternalContext {
		private final StringBuilder sb;
		public DummyHtmlExternalContext(final StringBuilder sb) {
			this.sb = sb;
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
		@Override
		public ALangObject fetchScopedVariable(final String scope, final String name, final IEvaluationContext ec)
				throws EvaluationException {
			throw new VariableNotDefinedException(scope, name, ec);
		}
		@Override
		public void reset() {
		}
	}
	private static enum DummyHtmlExternalContextContractFactory implements IExternalContextContractFactory<StringBuilder> {
		INSTANCE;
		@Override
		public DummyHtmlExternalContext make(final StringBuilder sb) {
			return new DummyHtmlExternalContext(sb);
		}
		@Override
		public boolean isProvidingScope(final String scope) {
			return false;
		}
		@Override
		public ILibraryScopeContractFactory<StringBuilder> getScopeFactory(final String scope) {
			return null;
		}
	}
	private static enum DummyContractFactory implements IEvaluationContextContract<StringBuilder> {
		INSTANCE;
		@Override
		public IEmbedmentContractFactory getEmbedmentFactory() {
			return EEmbedmentContractFactory.EMPTY;
		}
		@Override
		public ITracer<Node> makeTracer() {
			return DummyTracer.INSTANCE;
		}
		@Override
		public ILoggerContractFactory getLoggerFactory() {
			return ELoggerContractFactory.DUMMY;
		}
		@Override
		public INamespaceContractFactory getNamespaceFactory() {
			return ENamespaceContractFactory.EMPTY;
		}
		@Override
		public ILibraryContractFactory getLibraryFactory() {
			return ELibraryContractFactory.EMPTY;
		}
		@Override
		public DummyHtmlExternalContextContractFactory getExternalFactory() {
			return DummyHtmlExternalContextContractFactory.INSTANCE;
		}
	}

	private static String process(final Object... stuffToWrite) throws Exception {
		final StringBuilder sb = new StringBuilder();
		final DummyHtmlExternalContext hec = DummyContractFactory.INSTANCE.getExternalFactory().make(sb);
		final IEvaluationContext ec = new EvaluationContextImpl(DummyContractFactory.INSTANCE, "", null);
		hec.beginWriting();
		try {
			for (final Object stuff : stuffToWrite) {
				if (stuff instanceof IExternalContextCommand)
					hec.process((IExternalContextCommand) stuff, ec);
				else {
					@Nonnull
					final String s = NullUtil.toString(stuff);
					hec.write(s);
				}
			}
		}
		finally {
			hec.finishWriting();
		}
		return hec.toString();
	}
}