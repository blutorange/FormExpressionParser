package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleArrayItemException;
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
		return VariableTypeBuilder.forSimpleType(ELangObjectType.NUMBER);
	}

	@Override
	public IVariableType visit(final ASTArrayNode node, final Boolean typeNeeded) throws SemanticsException {
		IVariableType type = VariableTypeBuilder.forSimpleType(ELangObjectType.NULL);
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			final IVariableType newType = node.jjtGetChild(i).jjtAccept(this, CmnCnst.NonnullConstant.BOOLEAN_TRUE);
			try {
				type = type.union(newType);
			}
			catch (final IllegalVariableTypeException e) {
				throw new IncompatibleArrayItemException(e.getMessage(), i, newType, type, node.jjtGetChild(i));
			}
		}
		return wrapInArray(type);
	}

	@Override
	public IVariableType visit(final ASTHashNode node, final Boolean typeNeeded) throws SemanticsException {
		IVariableType typeKey = VariableTypeBuilder.forSimpleType(ELangObjectType.NULL);
		IVariableType typeValue = VariableTypeBuilder.forSimpleType(ELangObjectType.NULL);
		for (int i = 0; i < node.jjtGetNumChildren(); i += 2) {
			final IVariableType newTypeKey = node.jjtGetChild(i).jjtAccept(this, CmnCnst.NonnullConstant.BOOLEAN_TRUE);
			final IVariableType newTypeValue = node.jjtGetChild(i+1).jjtAccept(this,CmnCnst.NonnullConstant.BOOLEAN_TRUE);
			try {
				typeKey = typeKey.union(newTypeKey);
			}
			catch (final IllegalVariableTypeException e) {
				throw new IncompatibleHashEntryException(e.getMessage(), true, newTypeKey, typeKey, node.jjtGetChild(i));
			}
			try {
				typeValue = typeValue.union(newTypeValue);
			}
			catch (final IllegalVariableTypeException e) {
				throw new IncompatibleHashEntryException(e.getMessage(), false, newTypeValue, typeValue, node.jjtGetChild(i+1));
			}
		}
		return wrapInHash(typeKey, typeValue);
	}

	@Override
	public IVariableType visit(final ASTNullNode node, final Boolean typeNeeded) throws SemanticsException {
		return VariableTypeBuilder.forSimpleType(ELangObjectType.NULL);
	}

	@Override
	public IVariableType visit(final ASTBooleanNode node, final Boolean typeNeeded) throws SemanticsException {
		return VariableTypeBuilder.forSimpleType(ELangObjectType.BOOLEAN);
	}

	@Override
	public IVariableType visit(final ASTVariableNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTStringNode node, final Boolean typeNeeded) throws SemanticsException {
		return VariableTypeBuilder.forSimpleType(ELangObjectType.STRING);
	}

	@Override
	public IVariableType visit(final ASTStatementListNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableType visit(final ASTIfClauseNode node, final Boolean typeNeeded) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
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
		checkChildren(node, CmnCnst.NonnullConstant.BOOLEAN_FALSE);
		return VariableTypeBuilder.forSimpleType(ELangObjectType.EXCEPTION);
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
		// TODO Auto-generated method stub
		return null;
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
		return VariableTypeBuilder.forSimpleType(ELangObjectType.REGEX);
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
		return new VariableTypeBuilder().setBasicType(ELangObjectType.ARRAY).append(type).build();
	}

	@Nonnull
	private static IVariableType wrapInHash(@Nonnull final IVariableType typeKey, @Nonnull final IVariableType typeValue) {
		return new VariableTypeBuilder().setBasicType(ELangObjectType.ARRAY).append(typeKey).append(typeValue).build();
	}
}