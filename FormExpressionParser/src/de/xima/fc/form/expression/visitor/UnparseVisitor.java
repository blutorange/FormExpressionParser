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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IComment;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;
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
import de.xima.fc.form.expression.node.ASTScopeExternalNode;
import de.xima.fc.form.expression.node.ASTScopeGlobalNode;
import de.xima.fc.form.expression.node.ASTScopeManualNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTernaryExpressionNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.Void;

public class UnparseVisitor implements IFormExpressionReturnDataVisitor<Void, String, IOException> {
	private final static Logger LOG = LoggerFactory.getLogger(UnparseVisitor.class);

	private final Writer writer;
	private final UnparseVisitorConfig config;
	private final ImmutableList<IComment> comments;
	private IComment commentToken;
	private int commentPos;

	public static void unparse(@Nonnull final Writer writer, @Nonnull final Node node,
			@Nonnull final ImmutableList<IComment> comments) throws IOException {
		unparse(writer, node, comments, UnparseVisitorConfig.getDefaultConfig());
	}

	public static void unparse(@Nonnull final Writer writer, @Nonnull final Node node,
			@Nonnull final ImmutableList<IComment> comments, @Nonnull final UnparseVisitorConfig config)
					throws IOException {
		Preconditions.checkNotNull(writer, CmnCnst.Error.NULL_WRITER);
		Preconditions.checkNotNull(node, CmnCnst.Error.NULL_NODE);
		Preconditions.checkNotNull(config, CmnCnst.Error.NULL_PARSER_CONFIG);
		final UnparseVisitor unparser = new UnparseVisitor(writer, comments, config);
		unparser.blockOrClause(node, CmnCnst.NonnullConstant.EMPTY_STRING);
		unparser.writeRemainingComments();
		writer.flush();
	}

	@Nonnull
	public static String unparse(@Nonnull final Node node) {
		return unparse(node, ImmutableList.<IComment>of());
	}

	@Nonnull
	public static String unparse(@Nonnull final Node node, @Nonnull final ImmutableList<IComment> comments) {
		return unparse(node, comments, UnparseVisitorConfig.getDefaultConfig());
	}

	@Nonnull
	public static String unparse(@Nonnull final Node node, @Nonnull final UnparseVisitorConfig config) {
		return unparse(node, ImmutableList.<IComment>of(), config);
	}

	@Nonnull
	public static String unparse(@Nonnull final Node node, @Nonnull final ImmutableList<IComment> comments,
			@Nonnull final UnparseVisitorConfig config) {
		try (final Writer writer = new StringBuilderWriter()) {
			unparse(writer, node, comments, config);
			final String s = writer.toString();
			return s != null ? s : CmnCnst.NonnullConstant.EMPTY_STRING;
		}
		// StringBuilderWriter does not throw.
		catch (final IOException e) {
			return CmnCnst.NonnullConstant.EMPTY_STRING;
		}
	}

	private UnparseVisitor(@Nonnull final Writer writer, @Nonnull final ImmutableList<IComment> comments,
			@Nonnull final UnparseVisitorConfig config) {
		this.config = config;
		this.writer = writer;
		this.comments = comments;
		this.commentPos = 0;
		this.commentToken = (commentPos < comments.size()) ? comments.get(commentPos) : null;
	}

	private void writeComment(final String prefix, final boolean isBlock) throws IOException {
		if (!config.keepComments)
			return;
		// Write the comment
		switch (commentToken.getCommentType()) {
		case MULTI_LINE:
			writer.write(Syntax.MULTI_LINE_COMMENT_START);
			writer.write(commentToken.getText());
			writer.write(Syntax.MULTI_LINE_COMMENT_END);
			// When a multi-line comment appears before a block or
			// a statement, we add a newline.
			if (isBlock) {
				writer.write(config.linefeed);
				writer.write(prefix);
			}
			// Otherwise, it is an inline comment and we just add a space.
			else {
				writer.write(config.optionalSpace);
			}
			break;
		case SINGLE_LINE:
			writer.write(Syntax.SINGLE_LINE_COMMENT_START);
			writer.write(commentToken.getText());
			// Single line comment ends with a newline (unless it is the last
			// line). We need to add the proper indentation.
			if (commentToken.getText().charAt(commentToken.getText().length() - 1) == '\n')
				writer.write(prefix);
			break;
		default:
			LOG.error("Unknown enum: " + commentToken.getCommentType());
			break;
		}
		// Get the next comment
		++commentPos;
		commentToken = commentPos < comments.size() ? comments.get(commentPos) : null;
	}

	private void writeRemainingComments() throws IOException {
		writer.write(config.linefeed);
		while (commentToken != null)
			writeComment(StringUtils.EMPTY, true);
	}

	private void writeCommentForNode(final Node node, @Nonnull final String prefix, final boolean isBlock)
			throws IOException {
		if (!config.keepComments)
			return;
		// We write the comment iff the node to be processed lies after the
		// comment.
		// We check check for >= beginColumn to be safe, but the = case cannot
		// happen
		// as a comment token cannot be at the same position as a non-comment
		// node.
		while (commentToken != null
				&& (node.getStartLine() > commentToken.getLine() || node.getStartLine() == commentToken.getLine()
				&& node.getStartColumn() >= commentToken.getColumn())) {
			writeComment(prefix, isBlock);
		}
	}

	private void expression(@Nullable final Node node, @Nonnull final String prefix) throws IOException {
		if (node != null) {
			writeCommentForNode(node, prefix, false);
			node.jjtAccept(this, prefix);
		}
	}

	private Void blockOrClause(final Node node, @Nonnull final String prefix) throws IOException {
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
			writer.write(Syntax.SEMI_COLON);
			break;
		}
		return null;
	}

	@Nonnull
	private Void expressionNode(@Nonnull final Node node, @Nonnull final String prefix) throws IOException {
		expression(node.jjtGetChild(0), prefix);
		for (int i = 1; i < node.jjtGetNumChildren(); ++i) {
			writer.write(config.optionalSpace);
			writer.write(node.jjtGetChild(i).getSiblingMethod().methodName);
			writer.write(config.optionalSpace);
			expression(node.jjtGetChild(i), prefix);
		}
		return Void.NULL;
	}

	private void forHeaderExpression(final Node node, @Nonnull final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		for (int i = 0; i < len; ++i) {
			expression(node.jjtGetChild(0), prefix);
			if (len != 1 && i < len - 1) {
				writer.write(Syntax.COMMA);
				writer.write(config.optionalSpace);
			}
		}
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTExpressionNode node, @Nonnull final String prefix) throws IOException {
		return expressionNode(node, prefix);
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTAssignmentExpressionNode node, @Nonnull final String prefix)
			throws IOException {
		return expressionNode(node, prefix);
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTEqualExpressionNode node, @Nonnull final String prefix) throws IOException {
		return expressionNode(node, prefix);
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTComparisonExpressionNode node, @Nonnull final String prefix)
			throws IOException {
		return expressionNode(node, prefix);
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTNumberNode node, @Nonnull final String prefix) throws IOException {
		writer.write(NumberLangObject.toExpression(node.getDoubleValue()));
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTArrayNode node, @Nonnull final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		writer.write(Syntax.BRACKET_OPEN);
		for (int i = 0; i < len; ++i) {
			expression(node.jjtGetChild(i), prefix);
			if (len != 1 && i < len - 1) {
				writer.write(Syntax.COMMA);
				writer.write(config.optionalSpace);
			}
		}
		writer.write(Syntax.BRACKET_CLOSE);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTHashNode node, @Nonnull final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		writer.write(Syntax.BRACE_OPEN);
		for (int i = 0; i < len; i += 2) {
			if (node.jjtGetChild(i).jjtGetNodeId() == JJTIDENTIFIERNAMENODE) {
				StringLangObject.toExpression(((ASTIdentifierNameNode) node.jjtGetChild(i)).getName(), writer);
			}
			else
				expression(node.jjtGetChild(i), prefix);
			writer.write(Syntax.COLON);
			writer.write(config.optionalSpace);
			expression(node.jjtGetChild(i + 1), prefix);
			if (len != 1 && i < len - 2) {
				writer.write(Syntax.COMMA);
				writer.write(config.optionalSpace);
			}
		}
		writer.write(Syntax.BRACE_CLOSE);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTNullNode node, @Nonnull final String prefix) throws IOException {
		writer.write(NullLangObject.toExpression());
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTBooleanNode node, @Nonnull final String prefix) throws IOException {
		writer.write(BooleanLangObject.toExpression(node.getBooleanValue()));
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTVariableNode node, @Nonnull final String prefix) throws IOException {
		if (node.hasScope()) {
			writer.write(node.getScope());
			writer.write(Syntax.SCOPE_SEPARATOR);
		}
		writer.write(node.getVariableName());
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTStringNode node, @Nonnull final String prefix) throws IOException {
		StringLangObject.toExpression(node.getStringValue(), writer);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTStatementListNode node, @Nonnull final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		for (int i = 0; i != len; ++i) {
			blockOrClause(node.jjtGetChild(i), prefix);
			if (len != 1 && i < len - 1
					&& ((node.jjtGetChild(i).jjtGetNodeId() != FormExpressionParserTreeConstants.JJTLOSNODE)
							|| ((ASTLosNode) node.jjtGetChild(i)).isHasOpen())) {
				writer.write(config.linefeed);
				writer.write(prefix);
			}
		}
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTIfClauseNode node, @Nonnull final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		// if-header
		writer.write(Syntax.IF);
		writer.write(config.optionalSpace);
		writer.write(Syntax.PAREN_OPEN);
		expression(node.jjtGetChild(0), prefix);
		writer.write(Syntax.PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		// if-body
		writer.write(next);
		blockOrClause(node.jjtGetChild(1), next);
		writer.write(config.linefeed);
		// if-footer
		writer.write(prefix);
		writer.write(Syntax.BRACE_CLOSE);
		// else-header
		if (node.jjtGetNumChildren() > 2) {
			final boolean joinedIf = node.jjtGetChild(2).jjtGetNodeId() == JJTIFCLAUSENODE;
			writer.write(config.optionalSpace);
			writer.write(Syntax.ELSE);
			if (!joinedIf) {
				writer.write(config.optionalSpace);
				writer.write(Syntax.BRACE_OPEN);
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
			writer.write(Syntax.BRACE_CLOSE);
		}
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTForLoopNode node, @Nonnull final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		writer.write(Syntax.FOR);
		if (node.getLabel() != null) {
			writer.write(Syntax.ANGLE_OPEN);
			writer.write(node.getLabel());
			writer.write(Syntax.ANGLE_CLOSE);
		}
		writer.write(config.optionalSpace);
		writer.write(Syntax.PAREN_OPEN);
		if (node.isPlainLoop()) {
			// plain loop
			// header for (i = 0; i != 10; ++i) {
			forHeaderExpression(node.jjtGetChild(0), prefix);
			writer.write(Syntax.SEMI_COLON);
			writer.write(config.optionalSpace);
			forHeaderExpression(node.jjtGetChild(1), prefix);
			writer.write(Syntax.SEMI_COLON);
			writer.write(config.optionalSpace);
			forHeaderExpression(node.jjtGetChild(2), prefix);
			writer.write(Syntax.PAREN_CLOSE);
			writer.write(config.optionalSpace);
			writer.write(Syntax.BRACE_OPEN);
			writer.write(config.linefeed);
			// body
			writer.write(next);
			blockOrClause(node.jjtGetChild(3), next);
			writer.write(config.linefeed);
			// footer
			writer.write(prefix);
			writer.write(Syntax.BRACE_CLOSE);
		}
		else {
			// enhanced loop
			// header for (i : 10) {
			writer.write(node.getIteratingLoopVariable());
			writer.write(config.optionalSpace);
			writer.write(Syntax.ENHANCED_FOR_LOOP_SEPARATOR);
			writer.write(config.optionalSpace);
			expression(node.jjtGetChild(0), prefix);
			writer.write(Syntax.PAREN_CLOSE);
			writer.write(config.optionalSpace);
			writer.write(Syntax.BRACE_OPEN);
			writer.write(config.linefeed);
			// body
			writer.write(next);
			blockOrClause(node.jjtGetChild(1), next);
			writer.write(config.linefeed);
			// footer
			writer.write(prefix);
			writer.write(Syntax.BRACE_CLOSE);
		}
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTWhileLoopNode node, @Nonnull final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		// header while(foobar) {
		writer.write(Syntax.WHILE);
		if (node.getLabel() != null) {
			writer.write(Syntax.ANGLE_OPEN);
			writer.write(node.getLabel());
			writer.write(Syntax.ANGLE_CLOSE);
		}
		writer.write(config.optionalSpace);
		writer.write(Syntax.PAREN_OPEN);
		expression(node.jjtGetChild(0), prefix);
		writer.write(Syntax.PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		// body
		writer.write(next);
		blockOrClause(node.jjtGetChild(1), next);
		writer.write(config.linefeed);
		// footer
		writer.write(prefix);
		writer.write(Syntax.BRACE_CLOSE);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTTryClauseNode node, @Nonnull final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		// try
		writer.write(Syntax.TRY);
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		writer.write(next);
		blockOrClause(node.jjtGetChild(0), next);
		writer.write(config.linefeed);
		writer.write(prefix);
		writer.write(Syntax.BRACE_CLOSE);
		writer.write(config.linefeed);
		// catch
		writer.write(Syntax.CATCH);
		writer.write(config.optionalSpace);
		writer.write(Syntax.PAREN_OPEN);
		writer.write(node.getErrorVariableName());
		writer.write(Syntax.PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		writer.write(next);
		blockOrClause(node.jjtGetChild(1), next);
		writer.write(config.linefeed);
		writer.write(prefix);
		writer.write(Syntax.BRACE_CLOSE);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTSwitchClauseNode node, @Nonnull final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + config.indentPrefix;
		final String next2 = next + config.indentPrefix;
		// header switch(foobar) {
		if (node.getLabel() != null) {
			writer.write(node.getLabel());
			writer.write(Syntax.COLON);
		}
		writer.write(Syntax.SWITCH);
		writer.write(config.optionalSpace);
		writer.write(Syntax.PAREN_OPEN);
		expression(node.getFirstChildOrNull(), prefix);
		writer.write(Syntax.PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		// cases
		for (int i = 1; i < len; ++i) {
			switch (node.jjtGetChild(i).getSiblingMethod()) {
			case SWITCHCASE:
				writer.write(next);
				writer.write(Syntax.CASE);
				writer.write(config.requiredSpace);
				expression(node.jjtGetChild(i), next);
				writer.write(Syntax.COLON);
				writer.write(config.linefeed);
				break;
			case SWITCHCLAUSE:
				writer.write(next2);
				blockOrClause(node.jjtGetChild(i), next2);
				writer.write(config.linefeed);
				break;
			case SWITCHDEFAULT:
				writer.write(next);
				writer.write(Syntax.DEFAULT);
				writer.write(Syntax.COLON);
				writer.write(config.linefeed);
				writer.write(next2);
				blockOrClause(node.jjtGetChild(i), next2);
				writer.write(config.linefeed);
				break;
				// $CASES-OMITTED$
			default:
				throw new RuntimeException(
						String.format(CmnCnst.Error.ILLEGAL_ENUM_SWITCH, node.jjtGetChild(i).getSiblingMethod()));
			}
		}
		// footer }
		writer.write(prefix);
		writer.write(Syntax.BRACE_CLOSE);
		// blockOrClause
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTDoWhileLoopNode node, @Nonnull final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		// header do {
		writer.write(Syntax.DO);
		if (node.getLabel() != null) {
			writer.write(Syntax.ANGLE_OPEN);
			writer.write(node.getLabel());
			writer.write(Syntax.ANGLE_CLOSE);
		}
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		// body
		writer.write(next);
		blockOrClause(node.jjtGetChild(0), next);
		writer.write(config.linefeed);
		// footer } while(foobar)
		writer.write(prefix);
		writer.write(Syntax.BRACE_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(Syntax.WHILE);
		writer.write(config.optionalSpace);
		writer.write(Syntax.PAREN_OPEN);
		expression(node.jjtGetChild(1), prefix);
		writer.write(Syntax.PAREN_CLOSE);
		writer.write(Syntax.SEMI_COLON);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTExceptionNode node, @Nonnull final String prefix) throws IOException {
		writer.write(Syntax.EXCEPTION);
		writer.write(Syntax.PAREN_OPEN);
		expression(node.jjtGetChild(0), prefix);
		writer.write(Syntax.PAREN_CLOSE);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTThrowClauseNode node, @Nonnull final String prefix) throws IOException {
		writer.write(Syntax.THROW);
		writer.write(config.requiredSpace);
		expression(node.jjtGetChild(0), prefix);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTBreakClauseNode node, @Nonnull final String prefix) throws IOException {
		writer.write(Syntax.BREAK);
		if (node.getLabel() != null) {
			writer.write(config.requiredSpace);
			writer.write(node.getLabel());
		}
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTContinueClauseNode node, @Nonnull final String prefix) throws IOException {
		writer.write(Syntax.CONTINUE);
		if (node.getLabel() != null) {
			writer.write(config.requiredSpace);
			writer.write(node.getLabel());
		}
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTReturnClauseNode node, @Nonnull final String prefix) throws IOException {
		writer.write(Syntax.RETURN);
		if (node.jjtGetNumChildren() > 0) {
			writer.write(config.requiredSpace);
			expression(node.jjtGetChild(0), prefix);
		}
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTLogNode node, @Nonnull final String prefix) throws IOException {
		switch (node.getLogLevel()) {
		case DEBUG:
			writer.write(Syntax.LOG_DEBUG);
			break;
		case ERROR:
			writer.write(Syntax.LOG_ERROR);
			break;
		case INFO:
			writer.write(Syntax.LOG_INFO);
			break;
		case WARN:
			writer.write(Syntax.LOG_WARN);
			break;
		default:
			throw new RuntimeException(String.format(CmnCnst.Error.ILLEGAL_ENUM_LOGLEVEL, node.getLogLevel()));
		}
		writer.write(Syntax.PAREN_OPEN);
		expression(node.jjtGetChild(0), prefix);
		writer.write(Syntax.PAREN_CLOSE);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTFunctionNode node, @Nonnull final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + config.indentPrefix;
		// Function arrow ->
		writer.write(Syntax.LAMBDA_ARROW);
		// Function argument (foo, bar)
		writer.write(Syntax.PAREN_OPEN);
		for (int i = 0; i < len - 1; ++i) {
			expression(node.jjtGetChild(i), prefix);
			if (len != 2 && i < len - 2) {
				writer.write(Syntax.COMMA);
				writer.write(config.optionalSpace);
			}
		}
		writer.write(Syntax.PAREN_CLOSE);
		writer.write(config.optionalSpace);
		// Opening brace {
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		// Function body ...
		writer.write(next);
		blockOrClause(node.getLastChildOrNull(), next);
		writer.write(config.linefeed);
		// Closing brace }
		writer.write(prefix);
		writer.write(Syntax.BRACE_CLOSE);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTUnaryExpressionNode node, @Nonnull final String prefix) throws IOException {
		writer.write(node.getUnaryMethod().methodName);
		expression(node.jjtGetChild(0), prefix);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTPostUnaryExpressionNode node, @Nonnull final String prefix) throws IOException {
		writer.write(node.getUnaryMethod().methodName);
		expression(node.jjtGetChild(0), prefix);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTPropertyExpressionNode node, @Nonnull final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		expression(node.jjtGetChild(0), prefix);
		for (int i = 1; i < len; ++i) {
			final Node n = node.jjtGetChild(i);
			switch (n.getSiblingMethod()) {
			case DOT:
				writer.write(Syntax.DOT);
				expression(n, prefix);
				break;
			case BRACKET:
				writer.write(Syntax.BRACKET_OPEN);
				expression(n, prefix);
				writer.write(Syntax.BRACKET_CLOSE);
				break;
			case PARENTHESIS:
				final int len2 = n.jjtGetNumChildren();
				writer.write(Syntax.PAREN_OPEN);
				for (int j = 0; j < len2; ++j) {
					expression(n.jjtGetChild(j), prefix);
					if (len2 != 1 && j < len2 - 1) {
						writer.write(Syntax.COMMA);
						writer.write(config.optionalSpace);
					}
				}
				writer.write(Syntax.PAREN_CLOSE);
				break;
				// $CASES-OMITTED$
			default:
				throw new RuntimeException(String.format(CmnCnst.Error.ILLEGAL_ENUM_PROPERTY_EXPRESSION,
						node.jjtGetChild(i).getSiblingMethod()));
			}
		}
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTIdentifierNameNode node, @Nonnull final String prefix) throws IOException {
		writer.write(node.getName());
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTWithClauseNode node, @Nonnull final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + config.indentPrefix;
		// header with(foo,bar) {
		writer.write(Syntax.WITH);
		writer.write(config.optionalSpace);
		writer.write(Syntax.PAREN_OPEN);
		for (int i = 0; i < len - 1; ++i) {
			expression(node.jjtGetChild(i), prefix);
			if (len != 2 && i < len - 2) {
				writer.write(Syntax.COMMA);
				writer.write(config.optionalSpace);
			}
		}
		writer.write(Syntax.PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		// body
		writer.write(next);
		blockOrClause(node.getLastChildOrNull(), next);
		writer.write(config.linefeed);
		// footer
		writer.write(prefix);
		writer.write(Syntax.BRACE_CLOSE);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTFunctionClauseNode node, @Nonnull final String prefix) throws IOException {
		final int len = node.jjtGetNumChildren();
		final String next = prefix + config.indentPrefix;
		// header function foo(bar) {
		writer.write(Syntax.FUNCTION);
		writer.write(config.requiredSpace);
		expression(node.getFirstChildOrNull(), prefix);
		writer.write(Syntax.PAREN_OPEN);
		for (int i = 1; i < len - 1; ++i) {
			expression(node.jjtGetChild(i), prefix);
			if (len != 2 && i < len - 2) {
				writer.write(Syntax.COMMA);
				writer.write(config.optionalSpace);
			}
		}
		writer.write(Syntax.PAREN_CLOSE);
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		// body
		writer.write(next);
		blockOrClause(node.getLastChildOrNull(), next);
		writer.write(config.linefeed);
		// footer
		writer.write(prefix);
		writer.write(Syntax.BRACE_CLOSE);
		writer.write(config.linefeed);
		return Void.NULL;

	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTEmptyNode node, @Nonnull final String prefix) throws IOException {
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTLosNode node, @Nonnull final String prefix) throws IOException {
		if (node.isHasClose()) {
			writer.write(config.optionalSpace);
			writer.write(Syntax.LOS_CLOSE);
		}
		if (node.isHasText()) {
			writer.write(node.getText());
		}
		if (node.isHasOpen()) {
			writer.write(node.getOpen());
			writer.write(config.optionalSpace);
		}
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTRegexNode node, @Nonnull final String prefix) throws IOException {
		RegexLangObject.toExpression(node.getPattern(), writer);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTTernaryExpressionNode node, @Nonnull final String prefix) throws IOException {
		expression(node.jjtGetChild(0), prefix);
		writer.write(config.optionalSpace);
		writer.write(Syntax.QUESTION_MARK);
		writer.write(config.optionalSpace);
		expression(node.jjtGetChild(1), prefix);
		writer.write(config.optionalSpace);
		writer.write(Syntax.COLON);
		writer.write(config.optionalSpace);
		expression(node.jjtGetChild(2), prefix);
		return Void.NULL;
	}

	@Override
	@Nonnull
	public Void visit(@Nonnull final ASTParenthesisExpressionNode node, @Nonnull final String prefix)
			throws IOException {
		writer.write(Syntax.PAREN_OPEN);
		expression(node.getFirstChildOrNull(), prefix);
		writer.write(Syntax.PAREN_CLOSE);
		return Void.NULL;
	}

	@Override
	public Void visit(final ASTScopeExternalNode node, final String prefix) throws IOException {
		writer.write(Syntax.REQUIRE);
		writer.write(config.requiredSpace);
		writer.write(Syntax.SCOPE);
		writer.write(config.requiredSpace);
		writer.write(node.getScopeName());
		writer.write(Syntax.SEMI_COLON);
		return Void.NULL;
	}

	@Override
	public Void visit(final ASTVariableDeclarationClauseNode node, final String prefix) throws IOException {
		writer.write(Syntax.VAR);
		writer.write(config.requiredSpace);
		writer.write(node.getVariableName());
		writer.write(config.optionalSpace);
		writer.write(Syntax.EQUAL);
		writer.write(config.optionalSpace);
		if (node.jjtGetNumChildren() == 1) {
			expression(node.getFirstChildOrNull(), prefix);
		}
		else {
			writer.write(Syntax.NULL);
		}
		writer.write(Syntax.SEMI_COLON);
		return Void.NULL;
	}

	@Override
	public Void visit(final ASTScopeManualNode node, final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		writer.write(Syntax.SCOPE);
		writer.write(config.optionalSpace);
		writer.write(node.getScopeName());
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		writer.write(prefix);
		writer.write(config.indentPrefix);
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			blockOrClause(node.jjtGetChild(i), next);
			writer.write(config.linefeed);
			if (i != node.jjtGetNumChildren()-1)
				writer.write(config.indentPrefix);
		}
		writer.write(Syntax.BRACE_CLOSE);
		return Void.NULL;
	}

	@Override
	public Void visit(final ASTScopeGlobalNode node, final String prefix) throws IOException {
		final String next = prefix + config.indentPrefix;
		writer.write(Syntax.GLOBAL);
		writer.write(config.requiredSpace);
		writer.write(Syntax.SCOPE);
		writer.write(config.optionalSpace);
		writer.write(Syntax.BRACE_OPEN);
		writer.write(config.linefeed);
		writer.write(prefix);
		writer.write(config.indentPrefix);
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			blockOrClause(node.jjtGetChild(i), next);
			writer.write(config.linefeed);
			if (i != node.jjtGetNumChildren()-1)
				writer.write(config.indentPrefix);
		}
		writer.write(config.linefeed);
		writer.write(Syntax.BRACE_CLOSE);
		return Void.NULL;
	}
}