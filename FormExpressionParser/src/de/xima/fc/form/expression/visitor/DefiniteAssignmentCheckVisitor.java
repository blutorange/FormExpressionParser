//package de.xima.fc.form.expression.visitor;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import com.google.common.collect.Sets;
//
//import de.xima.fc.form.expression.exception.parse.SemanticsException;
//import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
//import de.xima.fc.form.expression.iface.parse.IVariableReference;
//import de.xima.fc.form.expression.node.ASTArrayNode;
//import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
//import de.xima.fc.form.expression.node.ASTBooleanNode;
//import de.xima.fc.form.expression.node.ASTBreakClauseNode;
//import de.xima.fc.form.expression.node.ASTComparisonExpressionNode;
//import de.xima.fc.form.expression.node.ASTContinueClauseNode;
//import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
//import de.xima.fc.form.expression.node.ASTEmptyNode;
//import de.xima.fc.form.expression.node.ASTEqualExpressionNode;
//import de.xima.fc.form.expression.node.ASTExceptionNode;
//import de.xima.fc.form.expression.node.ASTExpressionNode;
//import de.xima.fc.form.expression.node.ASTForLoopNode;
//import de.xima.fc.form.expression.node.ASTFunctionArgumentNode;
//import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
//import de.xima.fc.form.expression.node.ASTFunctionNode;
//import de.xima.fc.form.expression.node.ASTHashNode;
//import de.xima.fc.form.expression.node.ASTIdentifierNameNode;
//import de.xima.fc.form.expression.node.ASTIfClauseNode;
//import de.xima.fc.form.expression.node.ASTLogNode;
//import de.xima.fc.form.expression.node.ASTLosNode;
//import de.xima.fc.form.expression.node.ASTNullNode;
//import de.xima.fc.form.expression.node.ASTNumberNode;
//import de.xima.fc.form.expression.node.ASTParenthesisExpressionNode;
//import de.xima.fc.form.expression.node.ASTPostUnaryExpressionNode;
//import de.xima.fc.form.expression.node.ASTPropertyExpressionNode;
//import de.xima.fc.form.expression.node.ASTRegexNode;
//import de.xima.fc.form.expression.node.ASTReturnClauseNode;
//import de.xima.fc.form.expression.node.ASTScopeExternalNode;
//import de.xima.fc.form.expression.node.ASTScopeGlobalNode;
//import de.xima.fc.form.expression.node.ASTScopeManualNode;
//import de.xima.fc.form.expression.node.ASTStatementListNode;
//import de.xima.fc.form.expression.node.ASTStringCharactersNode;
//import de.xima.fc.form.expression.node.ASTStringNode;
//import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
//import de.xima.fc.form.expression.node.ASTTernaryExpressionNode;
//import de.xima.fc.form.expression.node.ASTThrowClauseNode;
//import de.xima.fc.form.expression.node.ASTTryClauseNode;
//import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
//import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
//import de.xima.fc.form.expression.node.ASTVariableNode;
//import de.xima.fc.form.expression.node.ASTVariableTypeNode;
//import de.xima.fc.form.expression.node.ASTWhileLoopNode;
//import de.xima.fc.form.expression.node.ASTWithClauseNode;
//
//public class DefiniteAssignmentCheckVisitor implements IFormExpressionVoidDataVisitor<Set<Integer>, SemanticsException> {
//
//	private IVariableReference[] symbolTable;
//	
//	@Override
//	public void visit(final ASTExpressionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void visit(final ASTAssignmentExpressionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTNumberNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTArrayNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTHashNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTNullNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTBooleanNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTVariableNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTStringNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTStatementListNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTIfClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		node.getConditionNode().jjtAccept(this, assigned);
//		final Set<Integer> setIf = copyOf(assigned);
//		node.getIfNode().jjtAccept(this, setIf);
//		if (node.hasElseNode()) {
//			node.getElseNode().jjtAccept(this, assigned);
//			Sets.union(setIf, assigned).copyInto(assigned);
//		}
//	}
//
//	private Set<Integer> copyOf(final Set<Integer> assigned) {
//		return new HashSet<>(assigned);
//	}
//
//	@Override
//	public void visit(final ASTForLoopNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTWhileLoopNode node, final Set<Integer> assigned) throws SemanticsException {
//		node.getWhileHeaderNode().jjtAccept(this, assigned);
//		node.getBodyNode().jjtAccept(this, copyOf(assigned));
//	}
//
//	@Override
//	public void visit(final ASTTryClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTSwitchClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTDoWhileLoopNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTExceptionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTThrowClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTBreakClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTContinueClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTReturnClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTLogNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTFunctionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTUnaryExpressionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTPropertyExpressionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTIdentifierNameNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTWithClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTFunctionClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTEmptyNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTLosNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTRegexNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTTernaryExpressionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTParenthesisExpressionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTEqualExpressionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTPostUnaryExpressionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTComparisonExpressionNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTScopeExternalNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTVariableDeclarationClauseNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTScopeManualNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTScopeGlobalNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTVariableTypeNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTFunctionArgumentNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(final ASTStringCharactersNode node, final Set<Integer> assigned) throws SemanticsException {
//		// TODO Auto-generated method stub
//		
//	}
//}