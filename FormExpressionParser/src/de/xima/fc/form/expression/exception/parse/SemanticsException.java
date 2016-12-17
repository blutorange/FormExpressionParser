package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class SemanticsException extends ParseException {
	private static final long serialVersionUID = 1L;

	public SemanticsException(final String message, final Node node) {
		this(message, node.getStartLine(), node.getStartColumn(), node.getEndLine(), node.getEndColumn());
	}

	public SemanticsException(final String message, final int beginLine, final int beginColumn, final int endLine,
			final int endColumn) {
		super(NullUtil.messageFormat(CmnCnst.Error.SEMANTIC_PARSE_EXCEPTION, beginLine, beginColumn, message));
		this.beginColumn = beginColumn;
		this.beginLine = beginLine;
		this.endLine = endLine;
		this.endColumn = endColumn;
	}

	public final int beginLine, beginColumn, endLine, endColumn;
}