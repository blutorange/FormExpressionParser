package de.xima.fc.form.expression.node;

import java.util.regex.Pattern;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTRegexNode extends SimpleNode {

	private Pattern pattern;

	public ASTRegexNode(final int nodeId) {
		super(nodeId);
	}

	public init(final EMethod method, final String escaped) {
		assertChildrenExactly(1);
		siblingMethod = method;
		pattern = Pattern.compile(unescape(escaped, '\\'), flags);
	}

	private String unescape(final String string, final char magic) {
		final StringBuilder sb = new StringBuilder();
		final char[] chars = string.toCharArray();
		for (int i = 0; i != chars.length; ++i) {
			if (chars[i] == magic) ++i;
			sb.append(chars[i]);
		}
		return sb.toString();
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		visitor.visit(this, data);
	}

	public Pattern getPattern() {
		return pattern;
	}

}
