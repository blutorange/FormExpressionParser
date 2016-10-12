package de.xima.fc.form.expression.visitor;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.impl.externalcontext.StringBuilderWriter;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTComparisonExpressionNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTEmptyNode;
import de.xima.fc.form.expression.node.ASTExceptionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIdentifierNameNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTLogNode;
import de.xima.fc.form.expression.node.ASTLosNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTParenthesisExpressionNode;
import de.xima.fc.form.expression.node.ASTPostUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTPropertyExpressionNode;
import de.xima.fc.form.expression.node.ASTRegexNode;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTernaryExpressionNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public class UnparseVisitor implements IFormExpressionParserVisitor<Void, String, IOException>{

	private final Writer writer;
	private final String indentPrefix;
	private final String linefeed;
	private final String optionalSpace;
	private final String requiredSpace = " ";


	public static void unparse(final Writer writer, final Node node, final String indentPrefix, final String linefeed) throws IOException {
		unparse(writer, node, indentPrefix, linefeed, 1);
	}
	public static String unparse(final Node node, final String indentPrefix, final String linefeed) throws IOException {
		return unparse(node, indentPrefix, linefeed, 1);
	}

	public static void unparse(final Writer writer, final Node node, final String indentPrefix, final String linefeed, final int numberOfOptionalSpaces) throws IOException {
		final UnparseVisitor unparser = new UnparseVisitor(writer, indentPrefix, linefeed, numberOfOptionalSpaces);
		unparser.blockOrClause(node, StringUtils.EMPTY);
		writer.flush();
	}

	public static String unparse(final Node node, final String indentPrefix, final String linefeed, final int numberOfOptionalSpaces) throws IOException {
		final Writer writer = new StringBuilderWriter();
		final UnparseVisitor unparser = new UnparseVisitor(writer, indentPrefix, linefeed, numberOfOptionalSpaces);
		unparser.blockOrClause(node, StringUtils.EMPTY);
		writer.close();
		return writer.toString();
	}

	private UnparseVisitor(final Writer writer, String indentPrefix, String linefeed, final int numberOfOptionalSpaces) {
		if (writer == null) throw new IllegalArgumentException("writer must not be null");
		if (indentPrefix == null) indentPrefix = StringUtils.SPACE;
		if (linefeed == null) linefeed = StringUtils.LF;
		optionalSpace = StringUtils.repeat(' ', numberOfOptionalSpaces < 0 ? 0 : numberOfOptionalSpaces);
		this.indentPrefix = indentPrefix;
		this.writer = writer;
		this.linefeed = linefeed;
	}

	private Void blockOrClause(final Node node, final String prefix) throws IOException {
		node.jjtAccept(this, prefix);
		switch (node.jjtGetNodeId()) {
		case FormExpressionParserTreeConstants.JJTARRAYNODE:
		case FormExpressionParserTreeConstants.JJTASSIGNMENTEXPRESSIONNODE:
		case FormExpressionParserTreeConstants.JJTBOOLEANNODE:
		case FormExpressionParserTreeConstants.JJTBREAKCLAUSENODE:
		case FormExpressionParserTreeConstants.JJTCOMPARISONEXPRESSIONNODE:
		case FormExpressionParserTreeConstants.JJTCONTINUECLAUSENODE:
		case FormExpressionParserTreeConstants.JJTEXCEPTIONNODE:
		case FormExpressionParserTreeConstants.JJTEXPRESSIONNODE:
		case FormExpressionParserTreeConstants.JJTFUNCTIONNODE:
		case FormExpressionParserTreeConstants.JJTHASHNODE:
		case FormExpressionParserTreeConstants.JJTIDENTIFIERNAMENODE:
		case FormExpressionParserTreeConstants.JJTLOGNODE:
		case FormExpressionParserTreeConstants.JJTNULLNODE:
		case FormExpressionParserTreeConstants.JJTNUMBERNODE:
		case FormExpressionParserTreeConstants.JJTPARENTHESISEXPRESSIONNODE:
		case FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE:
		case FormExpressionParserTreeConstants.JJTREGEXNODE:
		case FormExpressionParserTreeConstants.JJTRETURNCLAUSENODE:
		case FormExpressionParserTreeConstants.JJTSTRINGNODE:
		case FormExpressionParserTreeConstants.JJTTERNARYEXPRESSIONNODE:
		case FormExpressionParserTreeConstants.JJTTHROWCLAUSENODE:
		case FormExpressionParserTreeConstants.JJTUNARYEXPRESSIONNODE:
		case FormExpressionParserTreeConstants.JJTVARIABLENODE:
			writer.write(CmnCnst.SYNTAX_SEMI_COLON);
			break;
		}
		return null;
	}

	private Void expressionNode(final Node node, final String prefix) throws IOException {
		node.jjtGetChild(0).jjtAccept(this, prefix);
		for (int i = 1; i < node.jjtGetNumChildren(); ++i) {
			writer.write(optionalSpace);
			writer.write(node.jjtGetChild(i).getSiblingMethod().methodName);
			writer.write(optionalSpace);
			node.jjtGetChild(i).jjtAccept(this, prefix);
		}
		return null;
	}

	private void forHeaderExpression(final Node node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		for (int i = 0; i < len; ++i) {
			node.jjtGetChild(0).jjtAccept(this, prefix);
			if (len != 1 && i < len -1) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(optionalSpace);
			}
		}
	}

	@Override
	public Void visit(final ASTExpressionNode node, final String prefix) throws IOException {
		return expressionNode(node, prefix);
	}

	@Override
	public Void visit(final ASTAssignmentExpressionNode node, final String prefix) throws IOException {
		return expressionNode(node, prefix);
	}

	@Override
	public Void visit(final ASTComparisonExpressionNode node, final String prefix) throws IOException {
		return expressionNode(node, prefix);
	}

	@Override
	public Void visit(final ASTNumberNode node, final String prefix) throws IOException {
		writer.write(NumberLangObject.toExpression(node.getDoubleValue()));
		return null;
	}

	@Override
	public Void visit(final ASTArrayNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		writer.write(CmnCnst.SYNTAX_BRACKET_OPEN);
		for (int i = 0; i < len; ++i) {
			node.jjtGetChild(i).jjtAccept(this, prefix);
			if (len != 1 && i < len - 1) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(optionalSpace);
			}
		}
		writer.write(CmnCnst.SYNTAX_BRACKET_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTHashNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		for (int i = 0; i < len; i+=2) {
			if (node.jjtGetChild(i).jjtGetNodeId() == FormExpressionParserTreeConstants.JJTIDENTIFIERNAMENODE) {
				StringLangObject.toExpression(((ASTIdentifierNameNode)node.jjtGetChild(i)).getName(), writer);
			}
			else node.jjtGetChild(i).jjtAccept(this, prefix);
			writer.write(CmnCnst.SYNTAX_COLON);
			writer.write(optionalSpace);
			node.jjtGetChild(i+1).jjtAccept(this, prefix);
			if (len != 1 && i < len - 2) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(optionalSpace);
			}
		}
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTNullNode node, final String prefix) throws IOException {
		writer.write(NullLangObject.toExpression());
		return null;
	}

	@Override
	public Void visit(final ASTBooleanNode node, final String prefix) throws IOException {
		writer.write(BooleanLangObject.toExpression(node.getBooleanValue()));
		return null;
	}

	@Override
	public Void visit(final ASTVariableNode node, final String prefix) throws IOException {
		if (node.hasScope()) {
			writer.write(node.getScope());
			writer.write(CmnCnst.SYNTAX_SCOPE_SEPARATOR);
		}
		writer.write(node.getName());
		return null;
	}

	@Override
	public Void visit(final ASTStringNode node, final String prefix) throws IOException {
		StringLangObject.toExpression(node.getStringValue(), writer);
		return null;
	}

	@Override
	public Void visit(final ASTStatementListNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		for (int i = 0; i != len; ++i) {
			blockOrClause(node.jjtGetChild(i), prefix);
			if (len != 1 && i < len - 1) {
				writer.write(linefeed);
				writer.write(prefix);
			}
		}
		return null;
	}

	@Override
	public Void visit(final ASTIfClauseNode node, final String prefix) throws IOException {
		final String next = prefix + indentPrefix;
		// if-header
		writer.write(CmnCnst.SYNTAX_IF);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		node.jjtGetChild(0).jjtAccept(this, prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(linefeed);
		// if-body
		writer.write(next);
		blockOrClause(node.jjtGetChild(1), next);
		writer.write(linefeed);
		// if-footer
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		// else-header
		if (node.jjtGetNumChildren() > 2) {
			final boolean joinedIf = node.jjtGetChild(2).jjtGetNodeId() == FormExpressionParserTreeConstants.JJTIFCLAUSENODE;
			writer.write(optionalSpace);
			writer.write(CmnCnst.SYNTAX_ELSE);
			if (!joinedIf) {
				writer.write(optionalSpace);
				writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
				writer.write(linefeed);
				writer.write(next);
			}
			else {
				writer.write(requiredSpace);
			}
			// else-body
			blockOrClause(node.jjtGetChild(2), next);
			writer.write(linefeed);
			// else-footer
			writer.write(prefix);
			writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		}
		return null;
	}

	@Override
	public Void visit(final ASTForLoopNode node, final String prefix) throws IOException {
		final String next = prefix + indentPrefix;
		writer.write(CmnCnst.SYNTAX_FOR);
		if (node.getLabel() != null) {
			writer.write(CmnCnst.SYNTAX_ANGLE_OPEN);
			writer.write(node.getLabel());
			writer.write(CmnCnst.SYNTAX_ANGLE_CLOSE);
		}
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		if (node.getIteratingLoopVariable() == null) {
			// plain loop
			// header for (i = 0; i != 10; ++i) {
			forHeaderExpression(node.jjtGetChild(0), prefix);
			writer.write(CmnCnst.SYNTAX_SEMI_COLON);
			writer.write(optionalSpace);
			forHeaderExpression(node.jjtGetChild(1), prefix);
			writer.write(CmnCnst.SYNTAX_SEMI_COLON);
			writer.write(optionalSpace);
			forHeaderExpression(node.jjtGetChild(2), prefix);
			writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
			writer.write(optionalSpace);
			writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
			writer.write(linefeed);
			// body
			writer.write(next);
			blockOrClause(node.jjtGetChild(3), next);
			writer.write(linefeed);
			// footer
			writer.write(prefix);
			writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		}
		else {
			// enhanced loop
			// header  for (i : 10) {
			writer.write(node.getIteratingLoopVariable());
			writer.write(optionalSpace);
			writer.write(CmnCnst.SYNTAX_ENHANCED_FOR_LOOP_SEPARATOR);
			writer.write(optionalSpace);
			node.jjtGetChild(0).jjtAccept(this, prefix);
			writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
			writer.write(optionalSpace);
			writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
			writer.write(linefeed);
			// body
			writer.write(next);
			blockOrClause(node.jjtGetChild(1), next);
			writer.write(linefeed);
			// footer
			writer.write(prefix);
			writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		}
		return null;
	}

	@Override
	public Void visit(final ASTWhileLoopNode node, final String prefix) throws IOException {
		final String next = prefix + indentPrefix;
		// header while(foobar) {
		writer.write(CmnCnst.SYNTAX_WHILE);
		if (node.getLabel() != null) {
			writer.write(CmnCnst.SYNTAX_ANGLE_OPEN);
			writer.write(node.getLabel());
			writer.write(CmnCnst.SYNTAX_ANGLE_CLOSE);
		}
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		node.jjtGetChild(0).jjtAccept(this, prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(linefeed);
		// body
		writer.write(next);
		blockOrClause(node.jjtGetChild(1), next);
		writer.write(linefeed);
		// footer
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTTryClauseNode node, final String prefix) throws IOException {
		final String next = prefix + indentPrefix;
		// try
		writer.write(CmnCnst.SYNTAX_TRY);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(linefeed);
		writer.write(next);
		blockOrClause(node.jjtGetChild(0), next);
		writer.write(linefeed);
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		writer.write(linefeed);
		// catch
		writer.write(CmnCnst.SYNTAX_CATCH);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		writer.write(node.getErrorVariableName());
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(linefeed);
		writer.write(next);
		blockOrClause(node.jjtGetChild(1), next);
		writer.write(linefeed);
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTSwitchClauseNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + indentPrefix;
		final String next2 = next + indentPrefix;
		// header switch(foobar) {
		if (node.getLabel() != null) {
			writer.write(node.getLabel());
			writer.write(CmnCnst.SYNTAX_COLON);
		}
		writer.write(CmnCnst.SYNTAX_SWITCH);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		node.getFirstChild().jjtAccept(this, prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(linefeed);
		// cases
		for (int i = 1; i < len; ++i) {
			switch (node.jjtGetChild(i).getSiblingMethod()) {
			case SWITCHCASE:
				writer.write(next);
				writer.write(CmnCnst.SYNTAX_CASE);
				writer.write(requiredSpace);
				node.jjtGetChild(i).jjtAccept(this, next);
				writer.write(CmnCnst.SYNTAX_COLON);
				writer.write(linefeed);
				break;
			case SWITCHCLAUSE:
				writer.write(next2);
				blockOrClause(node.jjtGetChild(i), next2);
				writer.write(linefeed);
				break;
			case SWITCHDEFAULT:
				writer.write(next);
				writer.write(CmnCnst.SYNTAX_DEFAULT);
				writer.write(CmnCnst.SYNTAX_COLON);
				writer.write(linefeed);
				writer.write(next2);
				blockOrClause(node.jjtGetChild(i), next2);
				writer.write(linefeed);
				break;
				//$CASES-OMITTED$
			default:
				throw new RuntimeException(String.format("Illegal enum %s. This is likely an error with the parser. Contact support.", node.jjtGetChild(i).getSiblingMethod()));
			}
		}
		// footer }
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		//blockOrClause
		return null;
	}

	@Override
	public Void visit(final ASTDoWhileLoopNode node, final String prefix) throws IOException {
		final String next = prefix + indentPrefix;
		// header do {
		writer.write(CmnCnst.SYNTAX_DO);
		if (node.getLabel() != null) {
			writer.write(CmnCnst.SYNTAX_ANGLE_OPEN);
			writer.write(node.getLabel());
			writer.write(CmnCnst.SYNTAX_ANGLE_CLOSE);
		}
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(linefeed);
		// body
		writer.write(next);
		blockOrClause(node.jjtGetChild(0), next);
		writer.write(linefeed);
		// footer } while(foobar)
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_WHILE);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		node.jjtGetChild(1).jjtAccept(this, prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(CmnCnst.SYNTAX_SEMI_COLON);
		return null;
	}

	@Override
	public Void visit(final ASTExceptionNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_EXCEPTION);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		node.jjtGetChild(0).jjtAccept(this, prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTThrowClauseNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_THROW);
		writer.write(requiredSpace);
		node.jjtGetChild(0).jjtAccept(this, prefix);
		return null;
	}

	@Override
	public Void visit(final ASTBreakClauseNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_BREAK);
		if (node.getLabel() != null) {
			writer.write(requiredSpace);
			writer.write(node.getLabel());
		}
		return null;
	}

	@Override
	public Void visit(final ASTContinueClauseNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_CONTINUE);
		if (node.getLabel() != null) {
			writer.write(requiredSpace);
			writer.write(node.getLabel());
		}
		return null;
	}

	@Override
	public Void visit(final ASTReturnClauseNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_RETURN);
		if (node.jjtGetNumChildren()>0) {
			writer.write(requiredSpace);
			node.jjtGetChild(0).jjtAccept(this, prefix);
		}
		return null;
	}

	@Override
	public Void visit(final ASTLogNode node, final String prefix) throws IOException {
		switch (node.getLogLevel()) {
		case DEBUG:
			writer.write(CmnCnst.SYNTAX_LOG_DEBUG);
			break;
		case ERROR:
			writer.write(CmnCnst.SYNTAX_LOG_ERROR);
			break;
		case INFO:
			writer.write(CmnCnst.SYNTAX_LOG_INFO);
			break;
		case WARN:
			writer.write(CmnCnst.SYNTAX_LOG_WARN);
			break;
		default:
			throw new RuntimeException("Unknown enum: " + node.getLogLevel());
		}
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		node.jjtGetChild(0).jjtAccept(this, prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTFunctionNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + indentPrefix;
		// Function arrow ->
		writer.write(CmnCnst.SYNTAX_LAMBDA_ARROW);
		// Function argument (foo, bar)
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		for (int i = 0; i < len - 1; ++i) {
			node.jjtGetChild(i).jjtAccept(this, prefix);
			if (len != 2 && i < len - 2) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(optionalSpace);
			}
		}
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(optionalSpace);
		// Opening brace {
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(linefeed);
		// Function body ...
		writer.write(next);
		blockOrClause(node.getLastChild(), next);
		writer.write(linefeed);
		// Closing brace }
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTUnaryExpressionNode node, final String prefix) throws IOException {
		writer.write(node.getUnaryMethod().methodName);
		return node.jjtGetChild(0).jjtAccept(this, prefix);
	}

	@Override
	public Void visit(final ASTPostUnaryExpressionNode node, final String prefix) throws IOException {
		writer.write(node.getUnaryMethod().methodName);
		return node.jjtGetChild(0).jjtAccept(this, prefix);
	}
	
	@Override
	public Void visit(final ASTPropertyExpressionNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		node.jjtGetChild(0).jjtAccept(this, prefix);
		for (int i = 1; i < len; ++i) {
			final Node n = node.jjtGetChild(i);
			switch (n.getSiblingMethod()) {
			case DOT:
				writer.write(CmnCnst.SYNTAX_DOT);
				n.jjtAccept(this, prefix);
				break;
			case BRACKET:
				writer.write(CmnCnst.SYNTAX_BRACKET_OPEN);
				n.jjtAccept(this, prefix);
				writer.write(CmnCnst.SYNTAX_BRACKET_CLOSE);
				break;
			case PARENTHESIS:
				final int len2 = n.jjtGetNumChildren();
				writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
				for (int j = 0; j < len2; ++j) {
					n.jjtGetChild(j).jjtAccept(this, prefix);
					if (len2 != 1 && j < len2 -1) {
						writer.write(CmnCnst.SYNTAX_COMMA);
						writer.write(optionalSpace);
					}
				}
				writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
				break;
				//$CASES-OMITTED$
			default:
				throw new RuntimeException(String.format("Unexpected enum %s. This is likely an error with the parser. Contact support.", node.jjtGetChild(i).getSiblingMethod()));
			}
		}
		return null;
	}

	@Override
	public Void visit(final ASTIdentifierNameNode node, final String prefix) throws IOException {
		writer.write(node.getName());
		return null;
	}

	@Override
	public Void visit(final ASTWithClauseNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + indentPrefix;
		// header with(foo,bar) {
		writer.write(CmnCnst.SYNTAX_WITH);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		for (int i = 0; i < len - 1; ++i) {
			node.jjtGetChild(i).jjtAccept(this, prefix);
			if (len != 2 && i < len - 2) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(optionalSpace);
			}
		}
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(linefeed);
		// body
		writer.write(next);
		blockOrClause(node.getLastChild(), prefix);
		writer.write(linefeed);
		// footer
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTFunctionClauseNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix+indentPrefix;
		// header function foo(bar) {
		writer.write(CmnCnst.SYNTAX_FUNCTION);
		writer.write(requiredSpace);
		node.getFirstChild().jjtAccept(this, prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		for (int i = 1; i < len - 1; ++i) {
			node.jjtGetChild(i).jjtAccept(this, prefix);
			if (len != 2 && i < len - 2) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(optionalSpace);
			}
		}
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(linefeed);
		// body
		writer.write(next);
		blockOrClause(node.getLastChild(), next);
		writer.write(linefeed);
		// footer
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		writer.write(linefeed);
		return null;

	}

	@Override
	public Void visit(final ASTEmptyNode node, final String prefix) throws IOException {
		return null;
	}

	@Override
	public Void visit(final ASTLosNode node, final String prefix) throws IOException {
		if (node.isHasClose()) {
			writer.write(optionalSpace);
			writer.write(CmnCnst.SYNTAX_LOS_CLOSE);
		}
		if (node.isHasText()) {
			writer.write(node.getText());
		}
		if (node.isHasOpen()) {
			writer.write(node.getOpen());
			writer.write(optionalSpace);
		}
		return null;
	}

	@Override
	public Void visit(final ASTRegexNode node, final String prefix) throws IOException {
		RegexLangObject.toExpression(node.getPattern(), writer);
		return null;
	}

	@Override
	public Void visit(final ASTTernaryExpressionNode node, final String prefix) throws IOException {
		node.jjtGetChild(0).jjtAccept(this, prefix);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_QUESTION_MARK);
		writer.write(optionalSpace);
		node.jjtGetChild(1).jjtAccept(this, prefix);
		writer.write(optionalSpace);
		writer.write(CmnCnst.SYNTAX_COLON);
		writer.write(optionalSpace);
		node.jjtGetChild(2).jjtAccept(this, prefix);
		return null;
	}

	@Override
	public Void visit(final ASTParenthesisExpressionNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		node.getFirstChild().jjtAccept(this, prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		return null;
	}

}
