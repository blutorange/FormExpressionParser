package de.xima.fc.form.expression.node;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTRegexNode extends SimpleNode {

	private Pattern pattern;

	public ASTRegexNode(final int nodeId) {
		super(nodeId);
	}

	public void init(final EMethod method, final String regex) throws ParseException {
		assertChildrenExactly(0);
		if (regex == null)
			throw new ParseException("Regex is null. This is likely an error with the parser, contact support.");
		if (regex.length() < 2)
			throw new ParseException(String.format("Regex <%s> not terminated properly.  This is likely an error with the parser, contact support.", regex));
		siblingMethod = method;
		final int lastHash = regex.lastIndexOf('#');
		try {
			pattern = Pattern.compile(regex.substring(1, lastHash), flags(regex, lastHash+1));
		}
		catch (PatternSyntaxException e) {
			throw new ParseException(String.format("Encountered invalid regex at line %d, column %d: %s",
					new Integer(getStartLine()), new Integer(getStartColumn()), e.getMessage()));
		}
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(pattern.pattern()).append(',').append(pattern.flags()).append(',');
	}

	private int flags(final String flagString, final int beginIndex) {
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

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public Pattern getPattern() {
		return pattern;
	}
}
