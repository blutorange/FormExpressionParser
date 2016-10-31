package de.xima.fc.form.expression.node;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTRegexNode extends SimpleNode {
	private static final long serialVersionUID = 1L;

	private Pattern pattern;

	public ASTRegexNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public void init(final EMethod method, final String regex) throws ParseException {
		assertChildrenExactly(0);
		if (regex == null)
			throw new ParseException(CmnCnst.Error.NODE_NULL_REGEX);
		if (regex.length() < 2)
			throw new ParseException(String.format(CmnCnst.Error.NODE_IMPROPER_REGEX_TERMINATION, regex));
		super.init(method);
		final int lastHash = regex.lastIndexOf('#');
		try {
			pattern = Pattern.compile(regex.substring(1, lastHash), flags(regex, lastHash+1));
		}
		catch (final PatternSyntaxException e) {
			throw new ParseException(String.format(CmnCnst.Error.NODE_INVALID_REGEX,
					new Integer(getStartLine()), new Integer(getStartColumn()), e.getMessage()));
		}
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(pattern.pattern()).append(',').append(pattern.flags()).append(',');
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public Pattern getPattern() {
		return pattern;
	}

	private static int flags(final String flagString, final int beginIndex) {
		final int len = flagString.length();
		int flags = 0;
		for (int i = beginIndex; i < len; ++i) {
			switch (flagString.charAt(i)) {
			case 's':
				flags |= Pattern.DOTALL;
				break;
			case 'm':
				flags |= Pattern.MULTILINE;
				break;
			case 'i':
				flags |= Pattern.CASE_INSENSITIVE;
				break;
			}
		}
		return flags;
	}
}
