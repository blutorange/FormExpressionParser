package de.xima.fc.form.expression.visitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;

public class DumpVisitor extends FormExpressionReturnDataVisitorAdapter<String, String, IOException> {
	private static class InstanceHolder {
		@Nonnull public final static DumpVisitor SYSTEM_OUT_DUMPER = new DumpVisitor(System.out);
		@Nonnull public final static DumpVisitor SYSTEM_ERR_DUMPER = new DumpVisitor(System.err);
	}

	/**
	 * Factory method for a dumper to {@link System#out}, with the default charset and line separator.
	 * @return A dumper to stdout.
	 */
	@Nonnull
	public static DumpVisitor getSystemOutDumper() {
		return InstanceHolder.SYSTEM_OUT_DUMPER;
	}
	/**
	 * Factory method for a dumper to {@link System#err}, with the default charset and line separator.
	 * @return A dumper to stderr.
	 */
	@Nonnull
	public static DumpVisitor getSystemErrDumper() {
		return InstanceHolder.SYSTEM_ERR_DUMPER;
	}

	/**
	 * Creates a new visitor and passes it a ByteArrayOutputStream, and returns the result as a String.
	 * @param node Node to visit.
	 * @param data Data, ie. the prefix.
	 * @return The result of visiting the given node, as a string.
	 * @throws EvaluationException
	 */
	public static String toString(@Nonnull final de.xima.fc.form.expression.grammar.Node node, @Nonnull final String data, final String lineSeparator) throws IOException {
		DumpVisitor v = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			final Charset charset = Charset.defaultCharset();
			v = new DumpVisitor(baos, charset, lineSeparator);
			node.jjtAccept(v, data);
			return new String(baos.toByteArray(), charset);
		}
	}

	private final OutputStream outputStream;
	private final byte[] lineSeparator;
	private final Charset charset;

	public DumpVisitor() {
		this(System.out, Charset.defaultCharset(), System.lineSeparator());
	}

	public DumpVisitor(final OutputStream outputStream) {
		this(outputStream, Charset.defaultCharset(), System.lineSeparator());
	}

	public DumpVisitor(final OutputStream outputStream, final String charset) {
		this(outputStream, charset, System.lineSeparator());
	}

	public DumpVisitor(final OutputStream outputStream, final Charset charset) {
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
	public DumpVisitor(final OutputStream outputStream, final String charset, final String lineSeparator) {
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
	public DumpVisitor(final OutputStream outputStream, final Charset charset, final String lineSeparator) {
		this.outputStream = outputStream;
		this.charset = charset;
		this.lineSeparator = lineSeparator.getBytes(charset);
	}

	@Override
	protected String map(final String prefix) {
		return prefix + StringUtils.SPACE;
	}

	@Override
	protected String visitNode(final WrappedNode node, final String prefix) throws IOException {
		outputStream.write(prefix.getBytes(charset));
		outputStream.write(node.toString().getBytes(charset));
		outputStream.write(lineSeparator);
		return prefix;
	}
}
