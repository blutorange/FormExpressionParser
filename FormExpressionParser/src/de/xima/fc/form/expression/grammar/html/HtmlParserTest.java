package de.xima.fc.form.expression.grammar.html;

import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand;
import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand.EDocumentCommandType;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.writer.SystemOutWriter;

public class HtmlParserTest {

	public static void main(String[] args) {
		FormcycleExternalContext fec = new FormcycleExternalContext(SystemOutWriter.getInstance());
		fec.beginWriting();
		fec.write("<html><p>This is a test");
		fec.process(new DocumentCommand(EDocumentCommandType.INSERT_LINK, "http://www.xi<p\">ma.de"), null);
		fec.process(new DocumentCommand(EDocumentCommandType.REMOVE_NEXT_TAG, "a"), null);
		fec.write(" for you.</p></html>");
		fec.finishWriting();
	}
}