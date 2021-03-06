package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.InvalidTemplateDataException;
import de.xima.fc.form.expression.iface.IReset;

@NonNullByDefault
public interface IEmbedment extends IReset {
	/**
	 * Sets the name of the current embedment. For code templates, code is
	 * embedded in blocks such as <code>[%$ %]</code>. During evaluation, this
	 * function is called at the beginning of such a block with the name of the
	 * embedment block, eg. <code>[%$</code>. This information may then be used
	 * by {@link #outputCode(String, IEvaluationContext)} and
	 * {@link #outputText(String, IEvaluationContext)}, the current embedment
	 * may silence any output.
	 *
	 * @param name
	 *            <code>null</code> means no embedment.
	 */
	public void setCurrentEmbedment(@Nullable String name);

	/**
	 * @return List of scopes the current embedment defines. Must not return any
	 *         <code>null</code> elements.
	 */
	public String[] getScopeListForCurrentEmbedment();

	/**
	 * @param embedment Embedment to check.
	 * @return List of scopes the given embedment defines. <code>null</code> when the embedment is unsupported.
	 */
	@Nullable
	public String[] getScopeList(String embedment);

	/**
	 * Does not need to be fast, used only for testing or pre-processing.
	 *
	 * @return A list of embedments this embedment defines, ie. for which
	 *         {@link #getScopeList(String)} does not return <code>null</code>.
	 */
	public String[] getEmbedmentList();

	/**
	 * Writes the output of an embedded code block with the current type to the
	 * output document, file, or stream etc.
	 *
	 * @param data
	 *            Data to output.
	 * @throws EmbedmentOutputException
	 *             When an error occurred and data could not be written.
	 * @throws InvalidTemplateDataException
	 *             When this embedment places some restrictions on the data and
	 *             these are violated. For example an embedment requiring HTML content.
	 */
	public void outputCode(String data, IEvaluationContext ec)
			throws EmbedmentOutputException, InvalidTemplateDataException;

	/**
	 * Writes the output of a plain text block (between code blocks) with the
	 * current type to the output document, file, or stream etc.
	 *
	 * @param data
	 *            Data to output.
	 * @throws EmbedmentOutputException
	 *             When an error occurred and data could not be written.
	 * @throws InvalidTemplateDataException
	 *             When this embedment places some restrictions on the data and
	 *             these are violated. For example an embedment requiring HTML content.
	 */
	public void outputText(String data, IEvaluationContext ec)
			throws EmbedmentOutputException, InvalidTemplateDataException;
}
