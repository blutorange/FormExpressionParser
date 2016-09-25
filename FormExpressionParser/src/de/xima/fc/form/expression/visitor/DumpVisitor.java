package de.xima.fc.form.expression.visitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.VisitorException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTDotExpressionNode;
import de.xima.fc.form.expression.node.ASTExceptionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTLogNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;

public class DumpVisitor implements IFormExpressionParserVisitor<Void, String> {
	private static class InstanceHolder {
		public final static DumpVisitor SYSTEM_OUT_DUMPER = new DumpVisitor(System.out);
		public final static DumpVisitor SYSTEM_ERR_DUMPER = new DumpVisitor(System.err);
	}

	/**
	 * Factory method for a dumper to {@link System#out}, with the default charset and line separator.
	 * @return A dumper to stdout.
	 */
	public static DumpVisitor getSystemOutDumper() {
		return InstanceHolder.SYSTEM_OUT_DUMPER;
	}
	/**
	 * Factory method for a dumper to {@link System#err}, with the default charset and line separator.
	 * @return A dumper to stderr.
	 */
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
	public static String toString(final Node node, final String data, final String lineSeparator) throws EvaluationException {
		DumpVisitor v = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			final Charset charset = Charset.defaultCharset();
			v = new DumpVisitor(baos, charset, lineSeparator);
			node.jjtAccept(v, data);
			return new String(baos.toByteArray(), charset);
		} catch (final IOException e) {
			throw new VisitorException(v, node, data, e);
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

	private Void dump(final Node node, final String prefix) throws EvaluationException {
		try {
			outputStream.write(prefix.getBytes(charset));
			outputStream.write(node.toString().getBytes(charset));
			outputStream.write(lineSeparator);
		} catch (final IOException e) {
			throw new VisitorException(this, node, prefix, e);
		}
		final Node[] children = node.getChildArray();
		for (int i = 0; i != children.length; ++i)
			children[i].jjtAccept(this, prefix + "  ");
		return null;
	}

	@Override
	public Void visit(final ASTExpressionNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}

	@Override
	public Void visit(final ASTDotExpressionNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}

	@Override
	public Void visit(final ASTNumberNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}

	@Override
	public Void visit(final ASTStringNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}

	@Override
	public Void visit(final ASTArrayNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}

	@Override
	public Void visit(final ASTHashNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}

	@Override
	public Void visit(final ASTNullNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}

	@Override
	public Void visit(final ASTBooleanNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}

	@Override
	public Void visit(final ASTVariableNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}

	@Override
	public Void visit(final ASTStatementListNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTIfClauseNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTForLoopNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTWhileLoopNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTTryClauseNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTSwitchClauseNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTDoWhileLoopNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTAssignmentExpressionNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTExceptionNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTThrowClauseNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTBreakClauseNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(final ASTContinueClauseNode node, final String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(ASTReturnClauseNode node, String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(ASTLogNode node, String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(ASTFunctionNode node, String data) throws EvaluationException {
		return dump(node, data);
	}
	@Override
	public Void visit(ASTUnaryExpressionNode node, String data) throws EvaluationException {
		return dump(node, data);
	}
}
