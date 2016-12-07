package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.enums.ELangObjectType.ARRAY;
import static de.xima.fc.form.expression.enums.ELangObjectType.BOOLEAN;
import static de.xima.fc.form.expression.enums.ELangObjectType.EXCEPTION;
import static de.xima.fc.form.expression.enums.ELangObjectType.NULL;
import static de.xima.fc.form.expression.enums.ELangObjectType.NUMBER;
import static de.xima.fc.form.expression.enums.ELangObjectType.REGEX;
import static de.xima.fc.form.expression.enums.ELangObjectType.STRING;
import static de.xima.fc.form.expression.util.CmnCnst.NonnullConstant.BOOLEAN_FALSE;
import static de.xima.fc.form.expression.util.CmnCnst.NonnullConstant.BOOLEAN_TRUE;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleArrayItemException;
import de.xima.fc.form.expression.exception.parse.IncompatibleBranchConditionTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleBranchSplitTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleHashEntryException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.VariableTypeBuilder;
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
import de.xima.fc.form.expression.node.ASTFunctionArgumentNode;
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
import de.xima.fc.form.expression.node.ASTVariableTypeNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;

public class VariableTypeCheckVisitor implements IFormExpressionReturnDataVisitor<IVariableType, Boolean, SemanticsException> {

	private boolean mustJump;
	private final IVariableType[] table;
	
	private VariableTypeCheckVisitor(final IVariableType[] table) {
		this.mustJump = false;
		this.table = table;
	}
	
	@Override
	public IVariableType visit(final ASTExpressionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTAssignmentExpressionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTNumberNode node, final Boolean typeNeeded) throws SemanticsException {
		return VariableTypeBuilder.forSimpleType(NUMBER);
	}

	@Override
	public IVariableType visit(final ASTArrayNode node, final Boolean typeNeeded) throws SemanticsException {
		IVariableType type = VariableTypeBuilder.forSimpleType(NULL);
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			final IVariableType newType = node.jjtGetChild(i).jjtAccept(this, BOOLEAN_TRUE);
			try {
				type = type.union(newType);
			}
			catch (final IllegalVariableTypeException e) {
				throw new IncompatibleArrayItemException(e, i, newType, type, node.jjtGetChild(i));
			}
		}
		return wrapInArray(type);
	}

	@Override
	public IVariableType visit(final ASTHashNode node, final Boolean typeNeeded) throws SemanticsException {
		IVariableType typeKey = VariableTypeBuilder.forSimpleType(NULL);
		IVariableType typeValue = VariableTypeBuilder.forSimpleType(NULL);
		for (int i = 0; i < node.jjtGetNumChildren(); i += 2) {
			final IVariableType newTypeKey = node.jjtGetChild(i).jjtAccept(this, BOOLEAN_TRUE);
			final IVariableType newTypeValue = node.jjtGetChild(i+1).jjtAccept(this, BOOLEAN_TRUE);
			try {
				typeKey = typeKey.union(newTypeKey);
			}
			catch (final IllegalVariableTypeException e) {
				throw new IncompatibleHashEntryException(e, true, newTypeKey, typeKey, node.jjtGetChild(i));
			}
			try {
				typeValue = typeValue.union(newTypeValue);
			}
			catch (final IllegalVariableTypeException e) {
				throw new IncompatibleHashEntryException(e, false, newTypeValue, typeValue, node.jjtGetChild(i+1));
			}
		}
		return wrapInHash(typeKey, typeValue);
	}

	@Override
	public IVariableType visit(final ASTNullNode node, final Boolean typeNeeded) throws SemanticsException {
		return VariableTypeBuilder.forSimpleType(NULL);
	}

	@Override
	public IVariableType visit(final ASTBooleanNode node, final Boolean typeNeeded) throws SemanticsException {
		return VariableTypeBuilder.forSimpleType(BOOLEAN);
	}

	@Override
	public IVariableType visit(final ASTVariableNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTStringNode node, final Boolean typeNeeded) throws SemanticsException {
		return VariableTypeBuilder.forSimpleType(STRING);
	}

	/**
	 * <p>
	 * A list of statements executed sequentially. However, a statement
	 * may contain a break, continue, or return clause statement which
	 * skips the remaining statements.
	 * <pre>
	 *   while (condition) {
	 *     var i = k + 2;
	 *     if (i > 9) break;
	 *     i *= 2;
	 *   }
	 * </pre>
	 * </p><p>
	 * To determine the return type, we check wether the return types
	 * of all return points are mutually compatible; and return that
	 * type. A return point any statement at which execution of the
	 * block may end. By the language specifications, the last statement
	 * is always a return point.
	 * </p><p>
	 * 
	 * </p><p>
	 * @param node
	 * @param typeNeeded
	 * @return
	 * @throws SemanticsException
	 */
	@Override
	public IVariableType visit(final ASTStatementListNode node, final Boolean typeNeeded) throws SemanticsException {
		IVariableType type = null;
		final int endIndex = node.jjtGetNumChildren() - 1;
		for (int i = 0; i <= endIndex; ++i)
			type = node.jjtAccept(this, i == endIndex ? BOOLEAN_TRUE : BOOLEAN_FALSE);		
		return type != null ? type : VariableTypeBuilder.forSimpleType(NULL);
	}

	/**
	 * <p>
	 * An if-clause node always contains a condition an a node for when that
	 * condition is satisfied. Optionally, it may contain an else-clause node.
	 * </p>
	 * <p>
	 * The return type is the return type of the if or else-clause node. As we
	 * cannot check the condition on compile-time, their return type needs to be
	 * the same.
	 * </p>
	 * <p>
	 * When there is no else-clause and the condition is not satisfied, the
	 * return type is null. As null is compatible with every type, we can simply
	 * return the return type of the if-clause node.
	 * </p><p>
	 * Additionally, it may happen that the if-clause or else-clause may
	 * contain a return statement.
	 * </p>
	 * @param node
	 * @param typeNeeded
	 * @return
	 * @throws SemanticsException
	 *             When the if-clause and else-clause node return type are not
	 *             compatible.
	 */
	@Override
	public IVariableType visit(final ASTIfClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		asd
		final IVariableType typeCondition = node.getConditionNode().jjtAccept(this, BOOLEAN_TRUE);
		if (!typeCondition.equalsType(VariableTypeBuilder.forSimpleType(BOOLEAN)))
			throw new IncompatibleBranchConditionTypeException(typeCondition, node.getConditionNode());
		IVariableType type = node.getIfNode().jjtAccept(this, typeNeeded);
		if (node.hasElseNode()) {
			final IVariableType typeElse = node.getElseNode().jjtAccept(this, typeNeeded);
			if (typeNeeded) {
				final IVariableType typeMerged;
				try {
					typeMerged = type.union(typeElse);
				}
				catch (final IllegalVariableTypeException e) {
					throw new IncompatibleBranchSplitTypeException(e, type, typeElse, node.getElseNode());
				}
				type = typeMerged;
			}
		}
		return type;
	}

	@Override
	public IVariableType visit(final ASTForLoopNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTWhileLoopNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTTryClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTSwitchClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTDoWhileLoopNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTExceptionNode node, final Boolean typeNeeded) throws SemanticsException {
		//TODO check if it can be coerced to string? probably not necessary
		checkChildren(node, CmnCnst.NonnullConstant.BOOLEAN_FALSE);
		return VariableTypeBuilder.forSimpleType(EXCEPTION);
	}

	@Override
	public IVariableType visit(final ASTThrowClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTBreakClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTContinueClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTReturnClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		final IVariableType type = node.hasReturn() ? node.jjtAccept(this, typeNeeded)
				: VariableTypeBuilder.forSimpleType(NULL);
		mustJump = true;
		return type;
	}

	@Override
	public IVariableType visit(final ASTLogNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTFunctionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTUnaryExpressionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTPropertyExpressionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTIdentifierNameNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTWithClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTFunctionClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTEmptyNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTLosNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTRegexNode node, final Boolean typeNeeded) throws SemanticsException {
		return VariableTypeBuilder.forSimpleType(REGEX);
	}

	@Override
	public IVariableType visit(final ASTTernaryExpressionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTParenthesisExpressionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTEqualExpressionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTPostUnaryExpressionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTComparisonExpressionNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTScopeExternalNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTVariableDeclarationClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTScopeManualNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTScopeGlobalNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTVariableTypeNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTFunctionArgumentNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void checkChildren(final Node node, @Nonnull final Boolean typeNeeded) throws SemanticsException {
		for (int i = 0; i < node.jjtGetNumChildren(); ++i)
			node.jjtGetChild(i).jjtAccept(this, typeNeeded);
	}

	@Nonnull
	private static IVariableType wrapInArray(@Nonnull final IVariableType type) {
		return new VariableTypeBuilder().setBasicType(ARRAY).append(type).build();
	}

	@Nonnull
	private static IVariableType wrapInHash(@Nonnull final IVariableType typeKey, @Nonnull final IVariableType typeValue) {
		return new VariableTypeBuilder().setBasicType(ARRAY).append(typeKey).append(typeValue).build();
	}
}