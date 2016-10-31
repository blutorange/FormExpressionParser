package de.xima.fc.form.expression.visitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.util.CmnCnst;

public class GraphvizVisitor extends GenericDepthFirstVisitor<byte[], byte[], IOException> {
	private static class InstanceHolder {
		public final static GraphvizVisitor SYSTEM_OUT_GRAPHVIZ = new GraphvizVisitor(System.out);
		public final static GraphvizVisitor SYSTEM_ERR_GRAPHVIZ = new GraphvizVisitor(System.err);
	}

	/**
	 * Factory method for a dumper to {@link System#out}, with the default
	 * charset and line separator.
	 *
	 * @return A dumper to stdout.
	 */
	public static GraphvizVisitor getSystemOutGraphviz() {
		return InstanceHolder.SYSTEM_OUT_GRAPHVIZ;
	}

	/**
	 * Factory method for a dumper to {@link System#err}, with the default
	 * charset and line separator.
	 *
	 * @return A dumper to stderr.
	 */
	public static GraphvizVisitor getSystemErrGraphviz() {
		return InstanceHolder.SYSTEM_ERR_GRAPHVIZ;
	}

	/**
	 * Creates a new visitor and passes it a ByteArrayOutputStream, and returns
	 * the result as a String.
	 *
	 * @param node
	 *            Node to visit.
	 * @param lineSeparator
	 *            To separate the lines.
	 * @param headerAndFooter
	 *            Header that will be prepended and footer that will be
	 *            appended.
	 * @param footerBegin
	 *            The index for the first element of the footer.
	 * @return The result of visiting the given node, as a string.
	 * @throws EvaluationException
	 */
	public static String toString(final de.xima.fc.form.expression.grammar.Node node, final String lineSeparator, final String[] headerAndFooter,
			final int footerBegin) throws IOException {
		GraphvizVisitor v = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			final Charset charset = Charset.defaultCharset();
			// Write header
			for (int i = 0; i < footerBegin; ++i)
				baos.write(headerAndFooter[i].getBytes(charset));
			// Write body
			v = new GraphvizVisitor(baos, charset, lineSeparator);
			node.jjtAccept(v, CmnCnst.EMPTY_BYTE_ARRAY);
			// Write footer
			for (int i = footerBegin; i < headerAndFooter.length; ++i)
				baos.write(headerAndFooter[i].getBytes(charset));
			// Return result as string
			return new String(baos.toByteArray(), charset);
		}
	}

	public static String withHeaderAndFooter(final de.xima.fc.form.expression.grammar.Node node, final String title, final String lineSeparator)
			throws IOException {
		final String[] headerAndFooter = new String[11];

		headerAndFooter[0] = "digraph G {"; //$NON-NLS-1$
		headerAndFooter[1] = System.lineSeparator();
		headerAndFooter[2] = "ROOT->"; //$NON-NLS-1$
		headerAndFooter[3] = String.valueOf(node.getId());
		headerAndFooter[4] = System.lineSeparator();
		headerAndFooter[5] = "ROOT"; //$NON-NLS-1$
		headerAndFooter[6] = " [label=\""; //$NON-NLS-1$
		headerAndFooter[7] = StringEscapeUtils.escapeHtml4(title);
		headerAndFooter[8] = "\"]"; //$NON-NLS-1$
		headerAndFooter[9] = System.lineSeparator();
		headerAndFooter[10] = "}"; //$NON-NLS-1$

		return GraphvizVisitor.toString(node, lineSeparator, headerAndFooter, 10);
	}

	private final OutputStream outputStream;
	private final Charset charset;
	private final byte[] lineSeparator;
	private final byte[] bytesLabelOpen;
	private final byte[] bytesLabelClose;
	private final byte[] bytesArrow;

	public GraphvizVisitor() {
		this(System.out, Charset.defaultCharset(), System.lineSeparator());
	}

	public GraphvizVisitor(final OutputStream outputStream) {
		this(outputStream, Charset.defaultCharset(), System.lineSeparator());
	}

	public GraphvizVisitor(final OutputStream outputStream, final String charset) {
		this(outputStream, charset, System.lineSeparator());
	}

	public GraphvizVisitor(final OutputStream outputStream, final Charset charset) {
		this(outputStream, charset, System.lineSeparator());
	}

	/**
	 * @param outputStream
	 *            Output stream to write to.
	 * @param charset
	 *            Name of a {@link Charset} to use.
	 * @param lineSeparator
	 *            Separator for lines.
	 * @throws UnsupportedCharsetException
	 *             If no support for the named charset is available in this
	 *             instance of the Java virtual machine
	 * @throws IllegalCharsetNameException
	 *             If the given charset name is illegal
	 * @throws IllegalArgumentException
	 *             If the given charsetName is null
	 */
	public GraphvizVisitor(final OutputStream outputStream, final String charset, final String lineSeparator) {
		this(outputStream, Charset.forName(charset), lineSeparator);
	}

	/**
	 * @param outputStream
	 *            Output stream to write to.
	 * @param charset
	 *            {@link Charset} to use.
	 * @param lineSeparator
	 *            Separator for lines.
	 */
	public GraphvizVisitor(final OutputStream outputStream, final Charset charset, final String lineSeparator) {
		this.outputStream = outputStream;
		this.charset = charset;
		this.lineSeparator = lineSeparator.getBytes(charset);
		bytesLabelOpen = " [label=\"".getBytes(charset); //$NON-NLS-1$
		bytesLabelClose = "\"]".getBytes(charset); //$NON-NLS-1$
		bytesArrow = "->".getBytes(charset); //$NON-NLS-1$
	}

	@Override
	protected byte[] visitNode(final Node node, final byte[] parentId) throws IOException {
		final String label = StringEscapeUtils.escapeHtml4(node.toString());
		final byte[] nodeId = String.valueOf(node.getId()).getBytes(charset);
		outputStream.write(nodeId);
		outputStream.write(bytesLabelOpen);
		outputStream.write(label.getBytes(charset));
		outputStream.write(bytesLabelClose);

		outputStream.write(lineSeparator);

		if (parentId.length != 0) {
			outputStream.write(parentId);
			outputStream.write(bytesArrow);
			outputStream.write(nodeId);
			outputStream.write(lineSeparator);
		}
		return nodeId != null ? nodeId : CmnCnst.EMPTY_BYTE_ARRAY;
	}
}