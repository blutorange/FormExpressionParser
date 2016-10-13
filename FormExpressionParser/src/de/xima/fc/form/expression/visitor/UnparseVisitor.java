package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTARRAYNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTASSIGNMENTEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTBOOLEANNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTBREAKCLAUSENODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTCOMPARISONEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTCONTINUECLAUSENODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTEQUALEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTEXCEPTIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTFUNCTIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTHASHNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTIDENTIFIERNAMENODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTIFCLAUSENODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTLOGNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTNULLNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTNUMBERNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTPARENTHESISEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTPOSTUNARYEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTREGEXNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTRETURNCLAUSENODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTSTRINGNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTTERNARYEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTTHROWCLAUSENODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTUNARYEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTVARIABLENODE;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.impl.externalcontext.StringBuilderWriter;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTComparisonExpressionNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTEmptyNode;
import de.xima.fc.form.expression.node.ASTEqualExpressionNode;
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
	private final UnparseVisitorConfig config;
	private final List<Token> comments;
	private Token commentToken;
	private int commentPos;

	public static void unparse(final Writer writer, final Node node)
			throws IOException {
		unparse(writer, node, UnparseVisitorConfig.getDefaultConfig());
	}

	public static String unparse(final Node node) throws IOException {
		return unparse(node, UnparseVisitorConfig.getDefaultConfig());
	}

	public static String unparse(final Node node, final UnparseVisitorConfig config) throws IOException {
		try (final Writer writer = new StringBuilderWriter()) {
			unparse(writer, node, config);
			return writer.toString();
		}
	}

	public static void unparse(final Writer writer, final Node node, final UnparseVisitorConfig config) throws IOException {
		final UnparseVisitor unparser = new UnparseVisitor(writer, node.getComments(), config);
		unparser.blockOrClause(node, StringUtils.EMPTY);
		unparser.writeRemainingComments();
		writer.flush();
	}
	
	private UnparseVisitor(final Writer writer, List<Token> comments, UnparseVisitorConfig config) {
		if (writer == null) throw new IllegalArgumentException("writer must not be null");
		this.config = config;
		this.writer = writer;
		this.comments = comments;
		this.commentPos = 0;
		this.commentToken = (comments != null && commentPos < comments.size()) ? comments.get(commentPos) : null;
	}

	private void writeComment(final String prefix, final boolean isBlock) throws IOException {
		if (!config.keepComments)
			return;
		// Write the comment
		writer.write(commentToken.image);
		if (commentToken.image.charAt(1) == '/') {
			// Single line comment ends with a newline (unless it is the last
			// line)
			// Add indentation.
			if (commentToken.image.charAt(commentToken.image.length() - 1) == '\n')
				writer.write(prefix);
		} else {
			// When a multiline comment appears before a block or
			// statement, we add newline.
			if (isBlock) {
				writer.write(config.linefeed);
				writer.write(prefix);
			}
			// Otherwise, it is an inline comment and we just add a space.
			else {
				writer.write(config.optionalSpace);
			}
		}
		// Get the next comment
		++commentPos;
		commentToken = commentPos < comments.size() ? comments.get(commentPos) : null;
	}

	private void writeRemainingComments() throws IOException {
		writer.write(config.linefeed);
		while (commentToken != null) writeComment(StringUtils.EMPTY, true);
	}
	
	private void writeCommentForNode(final Node node, final String prefix, final boolean isBlock) throws IOException {
		if (!config.keepComments) return;
		// We write the comment iff the node to be processed lies after the comment. 
		// We check check for >= beginColumn to be safe, but the = case cannot happen
		// as a comment token  cannot be at the same position as a non-comment node.
		while (commentToken != null
				&& (node.getStartLine() > commentToken.beginLine || node.getStartLine() == commentToken.beginLine
						&& node.getStartColumn() >= commentToken.beginColumn)) {
			writeComment(prefix, isBlock);
		}
	}
	
	private Void expression(final Node node, final String prefix) throws IOException {
		writeCommentForNode(node, prefix, false);
		return node.jjtAccept(this, prefix);
	}

	private Void blockOrClause(final Node node, final String prefix) throws IOException {
		writeCommentForNode(node, prefix, true);
		node.jjtAccept(this, prefix);
		switch (node.jjtGetNodeId()) {
		case JJTARRAYNODE:
		case JJTASSIGNMENTEXPRESSIONNODE:
		case JJTBOOLEANNODE:
		case JJTBREAKCLAUSENODE:
		case JJTCOMPARISONEXPRESSIONNODE:
		case JJTCONTINUECLAUSENODE:
		case JJTEQUALEXPRESSIONNODE:
		case JJTEXCEPTIONNODE:
		case JJTEXPRESSIONNODE:
		case JJTFUNCTIONNODE:
		case JJTHASHNODE:
		case JJTIDENTIFIERNAMENODE:
		case JJTLOGNODE:
		case JJTNULLNODE:
		case JJTNUMBERNODE:
		case JJTPARENTHESISEXPRESSIONNODE:
		case JJTPOSTUNARYEXPRESSIONNODE:
		case JJTPROPERTYEXPRESSIONNODE:
		case JJTREGEXNODE:
		case JJTRETURNCLAUSENODE:
		case JJTSTRINGNODE:
		case JJTTERNARYEXPRESSIONNODE:
		case JJTTHROWCLAUSENODE:
		case JJTUNARYEXPRESSIONNODE:
		case JJTVARIABLENODE:
			writer.write(CmnCnst.SYNTAX_SEMI_COLON);
			break;
		}
		return null;
	}

	private Void expressionNode(final Node node, final String prefix) throws IOException {
		expression(node.jjtGetChild(0), prefix);
		for (int i = 1; i < node.jjtGetNumChildren(); ++i) {
			writer.write(config.optionalSpace);
			writer.write(node.jjtGetChild(i).getSiblingMethod().methodName);
			writer.write(config.optionalSpace);
			expression(node.jjtGetChild(i), prefix);
		}
		return null;
	}

	private void forHeaderExpression(final Node node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		for (int i = 0; i < len; ++i) {
			expression(node.jjtGetChild(0), prefix);
			if (len != 1 && i < len -1) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(config.optionalSpace);
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
	public Void visit(final ASTEqualExpressionNode node, final String prefix) throws IOException {
		return expressionNode(node, prefix);
	}
	
	@Override
	public Void visit(ASTComparisonExpressionNode node, String prefix) throws IOException {
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
			expression(node.jjtGetChild(i), prefix);
			if (len != 1 && i < len - 1) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(config.optionalSpace);
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
			if (node.jjtGetChild(i).jjtGetNodeId() == JJTIDENTIFIERNAMENODE) {
				StringLangObject.toExpression(((ASTIdentifierNameNode)node.jjtGetChild(i)).getName(), writer);
			}
			else expression(node.jjtGetChild(i), prefix);
			writer.write(CmnCnst.SYNTAX_COLON);
			writer.write(config.optionalSpace);
			expression(node.jjtGetChild(i+1), prefix);
			if (len != 1 && i < len - 2) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(config.optionalSpace);
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
				writer.write(config.linefeed);
				writer.write(prefix);
			}
		}
		return null;
	}

	@Override
	public Void visit(final ASTIfClauseNode node, final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		// if-header
		writer.write(CmnCnst.SYNTAX_IF);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		expression(node.jjtGetChild(0), prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(config.linefeed);
		// if-body
		writer.write(next);
		blockOrClause(node.jjtGetChild(1), next);
		writer.write(config.linefeed);
		// if-footer
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		// else-header
		if (node.jjtGetNumChildren() > 2) {
			final boolean joinedIf = node.jjtGetChild(2).jjtGetNodeId() == JJTIFCLAUSENODE;
			writer.write(config.optionalSpace);
			writer.write(CmnCnst.SYNTAX_ELSE);
			if (!joinedIf) {
				writer.write(config.optionalSpace);
				writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
				writer.write(config.linefeed);
				writer.write(next);
			}
			else {
				writer.write(config.requiredSpace);
			}
			// else-body
			blockOrClause(node.jjtGetChild(2), next);
			writer.write(config.linefeed);
			// else-footer
			writer.write(prefix);
			writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		}
		return null;
	}

	@Override
	public Void visit(final ASTForLoopNode node, final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		writer.write(CmnCnst.SYNTAX_FOR);
		if (node.getLabel() != null) {
			writer.write(CmnCnst.SYNTAX_ANGLE_OPEN);
			writer.write(node.getLabel());
			writer.write(CmnCnst.SYNTAX_ANGLE_CLOSE);
		}
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		if (node.getIteratingLoopVariable() == null) {
			// plain loop
			// header for (i = 0; i != 10; ++i) {
			forHeaderExpression(node.jjtGetChild(0), prefix);
			writer.write(CmnCnst.SYNTAX_SEMI_COLON);
			writer.write(config.optionalSpace);
			forHeaderExpression(node.jjtGetChild(1), prefix);
			writer.write(CmnCnst.SYNTAX_SEMI_COLON);
			writer.write(config.optionalSpace);
			forHeaderExpression(node.jjtGetChild(2), prefix);
			writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
			writer.write(config.optionalSpace);
			writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
			writer.write(config.linefeed);
			// body
			writer.write(next);
			blockOrClause(node.jjtGetChild(3), next);
			writer.write(config.linefeed);
			// footer
			writer.write(prefix);
			writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		}
		else {
			// enhanced loop
			// header  for (i : 10) {
			writer.write(node.getIteratingLoopVariable());
			writer.write(config.optionalSpace);
			writer.write(CmnCnst.SYNTAX_ENHANCED_FOR_LOOP_SEPARATOR);
			writer.write(config.optionalSpace);
			expression(node.jjtGetChild(0), prefix);
			writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
			writer.write(config.optionalSpace);
			writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
			writer.write(config.linefeed);
			// body
			writer.write(next);
			blockOrClause(node.jjtGetChild(1), next);
			writer.write(config.linefeed);
			// footer
			writer.write(prefix);
			writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		}
		return null;
	}

	@Override
	public Void visit(final ASTWhileLoopNode node, final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		// header while(foobar) {
		writer.write(CmnCnst.SYNTAX_WHILE);
		if (node.getLabel() != null) {
			writer.write(CmnCnst.SYNTAX_ANGLE_OPEN);
			writer.write(node.getLabel());
			writer.write(CmnCnst.SYNTAX_ANGLE_CLOSE);
		}
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		expression(node.jjtGetChild(0), prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(config.linefeed);
		// body
		writer.write(next);
		blockOrClause(node.jjtGetChild(1), next);
		writer.write(config.linefeed);
		// footer
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTTryClauseNode node, final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		// try
		writer.write(CmnCnst.SYNTAX_TRY);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(config.linefeed);
		writer.write(next);
		blockOrClause(node.jjtGetChild(0), next);
		writer.write(config.linefeed);
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		writer.write(config.linefeed);
		// catch
		writer.write(CmnCnst.SYNTAX_CATCH);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		writer.write(node.getErrorVariableName());
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(config.linefeed);
		writer.write(next);
		blockOrClause(node.jjtGetChild(1), next);
		writer.write(config.linefeed);
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTSwitchClauseNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + config.indentPrefix;
		final String next2 = next + config.indentPrefix;
		// header switch(foobar) {
		if (node.getLabel() != null) {
			writer.write(node.getLabel());
			writer.write(CmnCnst.SYNTAX_COLON);
		}
		writer.write(CmnCnst.SYNTAX_SWITCH);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		expression(node.getFirstChild(), prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(config.linefeed);
		// cases
		for (int i = 1; i < len; ++i) {
			switch (node.jjtGetChild(i).getSiblingMethod()) {
			case SWITCHCASE:
				writer.write(next);
				writer.write(CmnCnst.SYNTAX_CASE);
				writer.write(config.requiredSpace);
				expression(node.jjtGetChild(i), next);
				writer.write(CmnCnst.SYNTAX_COLON);
				writer.write(config.linefeed);
				break;
			case SWITCHCLAUSE:
				writer.write(next2);
				blockOrClause(node.jjtGetChild(i), next2);
				writer.write(config.linefeed);
				break;
			case SWITCHDEFAULT:
				writer.write(next);
				writer.write(CmnCnst.SYNTAX_DEFAULT);
				writer.write(CmnCnst.SYNTAX_COLON);
				writer.write(config.linefeed);
				writer.write(next2);
				blockOrClause(node.jjtGetChild(i), next2);
				writer.write(config.linefeed);
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
		final String next = prefix + config.indentPrefix;
		// header do {
		writer.write(CmnCnst.SYNTAX_DO);
		if (node.getLabel() != null) {
			writer.write(CmnCnst.SYNTAX_ANGLE_OPEN);
			writer.write(node.getLabel());
			writer.write(CmnCnst.SYNTAX_ANGLE_CLOSE);
		}
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(config.linefeed);
		// body
		writer.write(next);
		blockOrClause(node.jjtGetChild(0), next);
		writer.write(config.linefeed);
		// footer } while(foobar)
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_WHILE);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		expression(node.jjtGetChild(1), prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(CmnCnst.SYNTAX_SEMI_COLON);
		return null;
	}

	@Override
	public Void visit(final ASTExceptionNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_EXCEPTION);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		expression(node.jjtGetChild(0), prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTThrowClauseNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_THROW);
		writer.write(config.requiredSpace);
		expression(node.jjtGetChild(0), prefix);
		return null;
	}

	@Override
	public Void visit(final ASTBreakClauseNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_BREAK);
		if (node.getLabel() != null) {
			writer.write(config.requiredSpace);
			writer.write(node.getLabel());
		}
		return null;
	}

	@Override
	public Void visit(final ASTContinueClauseNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_CONTINUE);
		if (node.getLabel() != null) {
			writer.write(config.requiredSpace);
			writer.write(node.getLabel());
		}
		return null;
	}

	@Override
	public Void visit(final ASTReturnClauseNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_RETURN);
		if (node.jjtGetNumChildren()>0) {
			writer.write(config.requiredSpace);
			expression(node.jjtGetChild(0), prefix);
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
		expression(node.jjtGetChild(0), prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTFunctionNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + config.indentPrefix;
		// Function arrow ->
		writer.write(CmnCnst.SYNTAX_LAMBDA_ARROW);
		// Function argument (foo, bar)
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		for (int i = 0; i < len - 1; ++i) {
			expression(node.jjtGetChild(i), prefix);
			if (len != 2 && i < len - 2) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(config.optionalSpace);
			}
		}
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(config.optionalSpace);
		// Opening brace {
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(config.linefeed);
		// Function body ...
		writer.write(next);
		blockOrClause(node.getLastChild(), next);
		writer.write(config.linefeed);
		// Closing brace }
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTUnaryExpressionNode node, final String prefix) throws IOException {
		writer.write(node.getUnaryMethod().methodName);
		return expression(node.jjtGetChild(0), prefix);
	}

	@Override
	public Void visit(final ASTPostUnaryExpressionNode node, final String prefix) throws IOException {
		writer.write(node.getUnaryMethod().methodName);
		return expression(node.jjtGetChild(0), prefix);
	}
	
	@Override
	public Void visit(final ASTPropertyExpressionNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		expression(node.jjtGetChild(0), prefix);
		for (int i = 1; i < len; ++i) {
			final Node n = node.jjtGetChild(i);
			switch (n.getSiblingMethod()) {
			case DOT:
				writer.write(CmnCnst.SYNTAX_DOT);
				expression(n, prefix);
				break;
			case BRACKET:
				writer.write(CmnCnst.SYNTAX_BRACKET_OPEN);
				expression(n, prefix);
				writer.write(CmnCnst.SYNTAX_BRACKET_CLOSE);
				break;
			case PARENTHESIS:
				final int len2 = n.jjtGetNumChildren();
				writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
				for (int j = 0; j < len2; ++j) {
					expression(n.jjtGetChild(j), prefix);
					if (len2 != 1 && j < len2 -1) {
						writer.write(CmnCnst.SYNTAX_COMMA);
						writer.write(config.optionalSpace);
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
		final String next = prefix + config.indentPrefix;
		// header with(foo,bar) {
		writer.write(CmnCnst.SYNTAX_WITH);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		for (int i = 0; i < len - 1; ++i) {
			expression(node.jjtGetChild(i), prefix);
			if (len != 2 && i < len - 2) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(config.optionalSpace);
			}
		}
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(config.linefeed);
		// body
		writer.write(next);
		blockOrClause(node.getLastChild(), next);
		writer.write(config.linefeed);
		// footer
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		return null;
	}

	@Override
	public Void visit(final ASTFunctionClauseNode node, final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + config.indentPrefix;
		// header function foo(bar) {
		writer.write(CmnCnst.SYNTAX_FUNCTION);
		writer.write(config.requiredSpace);
		expression(node.getFirstChild(), prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		for (int i = 1; i < len - 1; ++i) {
			expression(node.jjtGetChild(i), prefix);
			if (len != 2 && i < len - 2) {
				writer.write(CmnCnst.SYNTAX_COMMA);
				writer.write(config.optionalSpace);
			}
		}
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_BRACE_OPEN);
		writer.write(config.linefeed);
		// body
		writer.write(next);
		blockOrClause(node.getLastChild(), next);
		writer.write(config.linefeed);
		// footer
		writer.write(prefix);
		writer.write(CmnCnst.SYNTAX_BRACE_CLOSE);
		writer.write(config.linefeed);
		return null;

	}

	@Override
	public Void visit(final ASTEmptyNode node, final String prefix) throws IOException {
		return null;
	}

	@Override
	public Void visit(final ASTLosNode node, final String prefix) throws IOException {
		if (node.isHasClose()) {
			writer.write(config.optionalSpace);
			writer.write(CmnCnst.SYNTAX_LOS_CLOSE);
		}
		if (node.isHasText()) {
			writer.write(node.getText());
		}
		if (node.isHasOpen()) {
			writer.write(node.getOpen());
			writer.write(config.optionalSpace);
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
		expression(node.jjtGetChild(0), prefix);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_QUESTION_MARK);
		writer.write(config.optionalSpace);
		expression(node.jjtGetChild(1), prefix);
		writer.write(config.optionalSpace);
		writer.write(CmnCnst.SYNTAX_COLON);
		writer.write(config.optionalSpace);
		expression(node.jjtGetChild(2), prefix);
		return null;
	}

	@Override
	public Void visit(final ASTParenthesisExpressionNode node, final String prefix) throws IOException {
		writer.write(CmnCnst.SYNTAX_PAREN_OPEN);
		expression(node.getFirstChild(), prefix);
		writer.write(CmnCnst.SYNTAX_PAREN_CLOSE);
		return null;
	}
}