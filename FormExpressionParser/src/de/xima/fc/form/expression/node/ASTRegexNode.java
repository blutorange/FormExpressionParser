//package de.xima.fc.form.expression.node;
//
//import java.util.regex.Pattern;
//
//import de.xima.fc.form.expression.enums.EMethod;
//import de.xima.fc.form.expression.exception.EvaluationException;
//import de.xima.fc.form.expression.grammar.ParseException;
//import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;
//
//public class ASTRegexNode extends SimpleNode {
//
//	private Pattern pattern;
//
//	public ASTRegexNode(final int nodeId) {
//		super(nodeId);
//	}
//
//	public void init(final EMethod method, final String escaped, final String flagString) throws ParseException {
//		assertChildrenExactly(0);
//		siblingMethod = method;
//		pattern = Pattern.compile(unescape(escaped), getFlags(flagString));
//	}
//
//	@Override
//	public String toString() {
//		return "Regex(" + siblingMethod + "," + pattern.pattern() + "," + pattern.flags() + ")";
//	}
//
//	private int getFlags(final String flagString) {
//		final int len = flagString.length();
//		int flags = 0;
//		for (int i = 0; i!= len; ++i) {
//			switch (flagString.charAt(i)) {
//			case 's':
//				flags |= Pattern.DOTALL;
//				break;
//			case 'm':
//				flags |= Pattern.MULTILINE;
//				break;
//			case 'i':
//				flags |= Pattern.CASE_INSENSITIVE;
//				break;
//			}
//		}
//		return flags;
//	}
//
//	private String unescape(final String string) {
//		final StringBuilder sb = new StringBuilder();
//		final char[] chars = string.toCharArray();
//		for (int i = 1; i < chars.length-1; ++i) {
//			if (chars[i] == '\\' && i < chars.length-1 && chars[i+1] == '/') ++i;
//			sb.append(chars[i]);
//		}
//		return sb.toString();
//	}
//
//	@Override
//	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
//		return visitor.visit(this, data);
//	}
//
//	public Pattern getPattern() {
//		return pattern;
//	}
//
//}
