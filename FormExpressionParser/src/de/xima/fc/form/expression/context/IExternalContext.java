package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.InvalidTemplateDataException;
import de.xima.fc.form.expression.object.ALangObject;

public interface IExternalContext {
	/**
	 * Called once before evaluation begins.
	 * @throws EmbedmentOutputException When there is an I/O error.
	 */
	public void beginWriting() throws EmbedmentOutputException;
	
	/**
	 * Called during evaluation. Writes the given data. Data needs to be written
	 * when evaluating template files.
	 * @param data The data to be written.
	 * @throws EmbedmentOutputException When an I/O error occurs.
	 * @throw InvalidTemplateDataException When the data is syntactically or semantically invalid.
	 */
	public void write(String data) throws EmbedmentOutputException, InvalidTemplateDataException;
		
	/**
	 * Called once after evaluation is finished.
	 * @throws EmbedmentOutputException When an I/O error occurs.
	 * @throw InvalidTemplateDataException When the data is syntactically or semantically invalid.
	 */
	public void finishWriting() throws EmbedmentOutputException, InvalidTemplateDataException;

	/**
	 * Processes an isused command. For example, a function may issue
	 * the command {@link DocumentCommand#removeParagraph}. When the external
	 * context supports it, it takes that command and removes the current
	 * paragraph (eg. the current &lt;p&gt; tag) from the output.
	 * @param command The command to process.
	 */
	public void process(IExternalContextCommand command, IEvaluationContext ec);
	
	public ALangObject fetchScopedVariable(String scope, String name, IEvaluationContext ec);
}
