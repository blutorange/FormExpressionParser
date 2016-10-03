package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.exception.EmbedmentOutputException;

public interface IEmbedment {
	/**
	 * Must not throw when the name does not exist, but simply do nothing. The
	 * name always begins with <code>[%</code>, followed by any number of the
	 * following characters: <code>%</code>, <code>$</code>, <code>@</code>,
	 * <code>=</code>
	 *
	 * @param name
	 *            Name of the embedment type, eg. <code>[%%</code> or
	 *            <code>[%%@</code>.
	 */
	public void beginEmbedment(String name);

	/**
	 * Called once for each call to {@link #beginEmbedment(String)}.
	 */
	public void endEmbedment();

	/**
	 * Writes the given data with the current type to the output document, file, or stream etc.
	 * @param data Data to output.
	 * @throws EmbedmentOutputException When an error occured and data could not be written.
	 */
	public void output(String data, IEvaluationContext ec) throws EmbedmentOutputException;
}
