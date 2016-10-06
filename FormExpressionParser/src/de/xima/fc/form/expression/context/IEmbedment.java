package de.xima.fc.form.expression.context;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.util.IReset;

public interface IEmbedment extends IReset<Void> {
	/**
	 * @param name
	 *            <code>null</code> means no embedment.
	 */
	public void setCurrentEmbedment(@Nullable String name);

	@Nonnull
	public String[] getScopeList();

	/**
	 * Writes the output of an embedded code block with the current type to the
	 * output document, file, or stream etc.
	 * 
	 * @param data
	 *            Data to output.
	 * @throws EmbedmentOutputException
	 *             When an error occurred and data could not be written.
	 */
	public void outputCode(String data, IEvaluationContext ec) throws EmbedmentOutputException;

	/**
	 * Writes the output of a plain text block (between code blocks) with the
	 * current type to the output document, file, or stream etc.
	 * 
	 * @param data
	 *            Data to output.
	 * @throws EmbedmentOutputException
	 *             When an error occurred and data could not be written.
	 */
	public void outputText(String data, IEvaluationContext ec) throws EmbedmentOutputException;
}
