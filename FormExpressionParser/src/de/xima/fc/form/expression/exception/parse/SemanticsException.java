package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.AToken;
import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class SemanticsException extends ParseException {
	private static final long serialVersionUID = 1L;

	public SemanticsException(final String message, final Node node) {
		this(message, node.getBeginLine(), node.getBeginColumn(), node.getEndLine(), node.getEndColumn());
	}

	public SemanticsException(final String message, final int beginLine, final int beginColumn, final int endLine,
			final int endColumn) {
		super(NullUtil.messageFormat(CmnCnst.Error.SEMANTIC_PARSE_EXCEPTION, beginLine, beginColumn, message),
				AToken.newToken(FormExpressionParserConstants.EOF, "", beginLine, beginColumn, endLine, endColumn));
		this.beginColumn = beginColumn;
		this.beginLine = beginLine;
		this.endLine = endLine;
		this.endColumn = endColumn;
	}

	@Override
	public boolean isPositionInformationAvailable() {
		return true;
	}

	@Override
	public int getBeginLine() {
		return beginLine;
	}

	@Override
	public int getEndLine() {
		return endLine;
	}

	@Override
	public int getBeginColumn() {
		return beginColumn;
	}

	@Override
	public int getEndColumn() {
		return endColumn;
	}

	public final int beginLine, beginColumn, endLine, endColumn;
}