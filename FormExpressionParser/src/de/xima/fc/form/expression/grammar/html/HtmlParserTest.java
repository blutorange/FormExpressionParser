package de.xima.fc.form.expression.grammar.html;

import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.writer.SystemOutWriter;

public class HtmlParserTest {
	public static void main(final String[] args) {
		final FormcycleExternalContext fec = new FormcycleExternalContext(SystemOutWriter.getInstance());
		fec.beginWriting();
		fec.write("<html><table>...");
		fec.process(DocumentCommand.newInsertLink("http://www.<xima>.de", "xima", "_blank"), null);
		//fec.process(new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, "table"), null);
		//fec.process(new DocumentCommand(EDocumentCommandType.REMOVE_NEXT_TAG, "p"), null);
		fec.write(" for </table><p>del me</p> you.</html>");
		fec.finishWriting();
	}
}