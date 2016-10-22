package de.xima.fc.form.expression.grammar.html;

import de.xima.fc.form.expression.impl.contextcommand.EDocumentCommandPack;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.writer.SystemOutWriter;

public class HtmlParserTest {

	public static void main(String[] args) {
		FormcycleExternalContext fec = new FormcycleExternalContext(SystemOutWriter.getInstance());
		fec.beginWriting();
		fec.write("<html><p>This is a <");
		fec.process(EDocumentCommandPack.REMOVE_ENCLOSING_PARAGRAPH, null);
		fec.write("p>paragraph</p> test.</p></html>");
		fec.finishWriting();
	}
}