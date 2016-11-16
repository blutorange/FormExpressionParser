package de.xima.fc.form.expression.iface.context;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.util.IReset;

public interface IEmbedment extends IReset {
	/**
	 * Sets the name of the current embedment. For code templates, code is embedded
	 * in blocks such as <code>[%$ %]</code>. During evaluation, this function is called
	 * at the beginning of such a block with the name of the embedment block, eg. <code>[%$</code>.
	 * This information may then be used by {@link #outputCode(String, IEvaluationContext)} and
	 * {@link #outputText(String, IEvaluationContext)}, the current embedment may silence any output.
	 * @param name <code>null</code> means no embedment.
	 */
	public void setCurrentEmbedment(@Nullable String name);

	/** @return List of scopes this embedment defines. Must not return any <code>null</code> elements. */
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
	public void outputCode(@Nonnull String data, @Nonnull IEvaluationContext ec) throws EmbedmentOutputException;

	/**
	 * Writes the output of a plain text block (between code blocks) with the
	 * current type to the output document, file, or stream etc.
	 *
	 * @param data
	 *            Data to output.
	 * @throws EmbedmentOutputException
	 *             When an error occurred and data could not be written.
	 */
	public void outputText(@Nonnull String data, @Nonnull IEvaluationContext ec) throws EmbedmentOutputException;
}
