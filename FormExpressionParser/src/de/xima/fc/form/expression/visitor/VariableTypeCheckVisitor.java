package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.exception.variabletype.FunctionMissingReturnTypeException;
import de.xima.fc.form.expression.exception.variabletype.IllegalVariableTypeException;
import de.xima.fc.form.expression.exception.variabletype.IncompatbileIfReturnType;
import de.xima.fc.form.expression.exception.variabletype.IncompatbileStatementListReturnType;
import de.xima.fc.form.expression.exception.variabletype.IncompatibleFunctionReturnTypeException;
import de.xima.fc.form.expression.exception.variabletype.InhomogenousArrayTypesException;
import de.xima.fc.form.expression.exception.variabletype.InhomogenousHashKeyTypesException;
import de.xima.fc.form.expression.exception.variabletype.InhomogenousHashValueTypesException;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTComparisonExpressionNode;
import de.xima.fc.form.expression.node.ASTCompoundVariableTypeDeclarationNode;
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
import de.xima.fc.form.expression.node.ASTVariableTypeDeclarationNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.type.ArrayType;
import de.xima.fc.form.expression.type.BooleanType;
import de.xima.fc.form.expression.type.ExceptionType;
import de.xima.fc.form.expression.type.FunctionType;
import de.xima.fc.form.expression.type.HashType;
import de.xima.fc.form.expression.type.IVariableType;
import de.xima.fc.form.expression.type.IndeterminatedType;
import de.xima.fc.form.expression.type.NumberType;
import de.xima.fc.form.expression.type.RegexType;
import de.xima.fc.form.expression.type.StringType;
import de.xima.fc.form.expression.type.VoidType;

public class VariableTypeCheckVisitor implements IFormExpressionParserVisitor<IVariableType, Boolean, IllegalVariableTypeException> {

	private boolean mustJump;
	private EJump jumpType;
	private String jumpLabel;

	private IVariableType buildCompoundType(final ASTCompoundVariableTypeDeclarationNode node, final int endIndex) throws IllegalVariableTypeException {
		if (endIndex < 1)
			throw new IllegalVariableTypeException(node,
					String.format(
							"Illegal compound type with first node %s. This is likely a bug with the parser, contact support.",
							node.jjtGetChild(0)));
		final ASTVariableTypeDeclarationNode lastNode = node.getNthChildAsOrNull(endIndex-1, ASTVariableTypeDeclarationNode.class);
		if (lastNode == null)
			throw new IllegalVariableTypeException(node,
					String.format(
							"Node not of the correct type: %s. This is likely a bug with the parser, contact support.",
							node.jjtGetChild(endIndex - 1)));
		switch (lastNode.getType()) {
		case FUNCTION:
			final IVariableType returnType = buildCompoundType(node, endIndex-1);
			final IVariableType[] argType = new IVariableType[lastNode.jjtGetNumChildren()];
			for (int i = 0; i < argType.length; ++i)
				argType[i] = lastNode.jjtGetChild(i).jjtAccept(this, Boolean.FALSE);
			return new FunctionType(returnType, argType);
		case ARRAY:
			return new ArrayType(buildCompoundType(node, endIndex-1));
		case HASH:
		case BOOLEAN:
		case EXCEPTION:
		case NULL:
		case NUMBER:
		case REGEX:
		case STRING:
			if (endIndex == 1)
				return lastNode.jjtAccept(this, Boolean.FALSE);
		default:
			throw new IllegalVariableTypeException(node, String.format("Unknown enum %s. This is likely a bug with the parser, contact support.", lastNode.getType()));
		}
	}

	@Override
	public IVariableType visit(final ASTExpressionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTAssignmentExpressionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTNumberNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		return NumberType.INSTANCE;
	}

	@Override
	public IVariableType visit(final ASTArrayNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		IVariableType homogenousType = null;
		for (int i = 0; i != node.jjtGetNumChildren(); ++i) {
			final IVariableType childType = node.jjtGetChild(i).jjtAccept(this, returnTypeNeeded);
			if (homogenousType != null && !childType.compatible(homogenousType))
				throw new InhomogenousArrayTypesException(i, homogenousType, childType, node);
			homogenousType = childType;
		}
		return new ArrayType(homogenousType);
	}

	@Override
	public IVariableType visit(final ASTHashNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		IVariableType homogenousKeyType = null;
		IVariableType homogenousValueType = null;
		for (int i = 0; i != node.jjtGetNumChildren(); i+=2) {
			final IVariableType childKeyType = node.jjtGetChild(i).jjtAccept(this, returnTypeNeeded);
			final IVariableType childValueType = node.jjtGetChild(i+1).jjtAccept(this, returnTypeNeeded);
			if (homogenousKeyType != null && !homogenousKeyType.compatible(childKeyType))
				throw new InhomogenousHashKeyTypesException(i, homogenousKeyType, childKeyType, node);
			if (homogenousValueType != null && !homogenousValueType.compatible(childValueType))
				throw new InhomogenousHashValueTypesException(i, homogenousValueType, childValueType, node);
			homogenousKeyType = childKeyType;
			homogenousValueType = childValueType;
		}
		return new HashType(homogenousKeyType, homogenousValueType);
	}

	@Override
	public IVariableType visit(final ASTNullNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		return VoidType.INSTANCE;
	}

	@Override
	public IVariableType visit(final ASTBooleanNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		return BooleanType.INSTANCE;
	}

	@Override
	public IVariableType visit(final ASTVariableNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		//TODO retrieveVariable, if it exists, and check type.
		return node.jjtGetNumChildren() == 0 ? IndeterminatedType.INSTANCE : node.jjtGetChild(0).jjtAccept(this, returnTypeNeeded);
	}

	@Override
	public IVariableType visit(final ASTStringNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		return StringType.INSTANCE;
	}

	/**
	 * A list of statements that are executed one after another.
	 */
	@Override
	public IVariableType visit(final ASTStatementListNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		final int len = node.jjtGetNumChildren();
		IVariableType homogenousType = null;
		for (int i = 0; i != len; ++i) {
			final IVariableType tmp = node.jjtGetChild(i).jjtAccept(this,
					returnTypeNeeded.booleanValue() && i == len - 1 ? Boolean.TRUE : Boolean.FALSE);
			if (mustJump && returnTypeNeeded.booleanValue() && homogenousType != null && !homogenousType.compatible(tmp))
				throw new IncompatbileStatementListReturnType(i, homogenousType, tmp, node);
			homogenousType = tmp;
		}
		return homogenousType;
	}

	@Override
	public IVariableType visit(final ASTIfClauseNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		node.jjtGetChild(0).jjtAccept(this, Boolean.FALSE);
		final IVariableType ifType = node.jjtGetChild(1).jjtAccept(this, returnTypeNeeded);
		if (node.jjtGetNumChildren() > 2) {
			final IVariableType elseType = node.jjtGetChild(2).jjtAccept(this, returnTypeNeeded);
			if (returnTypeNeeded.booleanValue() && !ifType.compatible(elseType))
				throw new IncompatbileIfReturnType(node, ifType, elseType);
		}
		return ifType;
	}

	@Override
	public IVariableType visit(final ASTForLoopNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTWhileLoopNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTTryClauseNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTSwitchClauseNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTDoWhileLoopNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTExceptionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		return ExceptionType.INSTANCE;
	}

	@Override
	public IVariableType visit(final ASTThrowClauseNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTBreakClauseNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		jumpLabel = node.getLabel();
		jumpType = EJump.BREAK;
		mustJump = true;
		return VoidType.INSTANCE;
	}

	@Override
	public IVariableType visit(final ASTContinueClauseNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		jumpLabel = node.getLabel();
		jumpType = EJump.CONTINUE;
		mustJump = true;
		return VoidType.INSTANCE;
	}

	@Override
	public IVariableType visit(final ASTReturnClauseNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		jumpLabel = null;
		jumpType = EJump.RETURN;
		mustJump = true;
		return node.jjtGetNumChildren() == 0 ? VoidType.INSTANCE : node.jjtGetChild(0).jjtAccept(this, Boolean.TRUE);
	}

	@Override
	public IVariableType visit(final ASTLogNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTFunctionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		if (!node.isHasReturnTypeDeclaration())
			throw new FunctionMissingReturnTypeException(node);
		final int len = node.jjtGetNumChildren();
		// Build function type.
		final IVariableType returnType = node.jjtGetChild(0).jjtAccept(this, returnTypeNeeded);
		final IVariableType[] argType = new IVariableType[len-2];
		for (int i = 1; i < len - 1; ++i)
			argType[i-1] = node.jjtGetChild(i).jjtAccept(this, returnTypeNeeded);
		// Check return type of function body.
		final IVariableType blockType = node.getLastChild().jjtAccept(this, returnTypeNeeded);
		if (!blockType.compatible(returnType))
			throw new IncompatibleFunctionReturnTypeException(node, returnType, blockType);
		return new FunctionType(returnType, argType);
	}

	@Override
	public IVariableType visit(final ASTUnaryExpressionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTPropertyExpressionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTIdentifierNameNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTWithClauseNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTFunctionClauseNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		return node.jjtGetChild(0).jjtAccept(this, returnTypeNeeded);
	}

	@Override
	public IVariableType visit(final ASTEmptyNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTLosNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTRegexNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		return RegexType.INSTANCE;
	}

	@Override
	public IVariableType visit(final ASTTernaryExpressionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		node.jjtGetChild(0).jjtAccept(this, Boolean.FALSE);
		final IVariableType ifType = node.jjtGetChild(1).jjtAccept(this, returnTypeNeeded);
		final IVariableType elseType = node.jjtGetChild(2).jjtAccept(this, returnTypeNeeded);
		if (returnTypeNeeded.booleanValue() && !ifType.compatible(elseType))
			throw new IncompatbileIfReturnType(node, ifType, elseType);
		return ifType;
	}

	@Override
	public IVariableType visit(final ASTParenthesisExpressionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTEqualExpressionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTPostUnaryExpressionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTComparisonExpressionNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public IVariableType visit(final ASTVariableTypeDeclarationNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		switch (node.getType()) {
		case BOOLEAN:
			return BooleanType.INSTANCE;
		case EXCEPTION:
			return ExceptionType.INSTANCE;
		case NUMBER:
			return NumberType.INSTANCE;
		case REGEX:
			return RegexType.INSTANCE;
		case STRING:
			return StringType.INSTANCE;
		case HASH:
			final IVariableType keyType = node.jjtGetChild(0).jjtAccept(this, returnTypeNeeded);
			final IVariableType valueType = node.jjtGetChild(1).jjtAccept(this, returnTypeNeeded);
			return new HashType(keyType, valueType);
		case ARRAY:
		case FUNCTION:
		case NULL:
		default:
			throw new IllegalVariableTypeException(node, String.format("Illegal enum %s. This is likely an error with the parser, contact support.", node.getType()));
		}
	}

	@Override
	public IVariableType visit(final ASTCompoundVariableTypeDeclarationNode node, final Boolean returnTypeNeeded) throws IllegalVariableTypeException {
		return buildCompoundType(node, node.jjtGetNumChildren());
	}
}
