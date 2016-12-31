package de.xima.fc.form.expression.visitor;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.parse.FunctionIdNotResolvedException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.exception.parse.UnhandledEnumException;
import de.xima.fc.form.expression.exception.parse.VariableNotResolvableException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.iface.parse.IVariableResolutionResult;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionArgumentNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class ClosureConvertVisitor extends FormExpressionVoidDataVisitorAdapter<Integer, SemanticsException> {
	private final IVariableResolutionResult resolutionResult;

	private ClosureConvertVisitor(final IVariableResolutionResult resolutionResult) {
		this.resolutionResult = resolutionResult;
	}

	@Override
	public void visit(final ASTFunctionNode node, final Integer functionId) throws SemanticsException {
		if (!node.isFunctionIdResolved())
			throw new FunctionIdNotResolvedException(node);
		node.resolveClosureTableSize(resolutionResult.getClosureSize(node.getFunctionId()));
		visitChildren(node, node.getFunctionId());
	}

	@Override
	public void visit(final ASTFunctionClauseNode node, final Integer functionId) throws SemanticsException {
		if (!node.isFunctionIdResolved())
			throw new FunctionIdNotResolvedException(node);
		if (!node.isClosureTableSizeResolved())
			node.resolveClosureTableSize(resolutionResult.getClosureSize(node.getFunctionId()));
		if (!node.isClosureSourceResolved())
			visitSourceResolvable(functionId, node, node);
		for (int i = 0; i < node.getArgumentCount(); ++i)
			node.getArgumentNode(i).jjtAccept(this, node.getFunctionId());
		node.getBodyNode().jjtAccept(this, node.getFunctionId());
	}

	@Override
	public void visit(final ASTVariableNode node, final Integer functionId) throws SemanticsException {
		visitSourceResolvable(functionId, node, node);
		visitChildren(node, functionId);
	}

	@Override
	public void visit(final ASTFunctionArgumentNode node, final Integer functionId) throws SemanticsException {
		visitSourceResolvable(functionId, node, node);
		visitChildren(node, functionId);
	}

	@Override
	public void visit(final ASTVariableDeclarationClauseNode node, final Integer functionId) throws SemanticsException {
		visitSourceResolvable(functionId, node, node);
		visitChildren(node, functionId);
	}

	@Override
	public void visit(final ASTTryClauseNode node, final Integer functionId) throws SemanticsException {
		visitSourceResolvable(functionId, node, node);
		visitChildren(node, functionId);
	}

	@Override
	public void visit(final ASTForLoopNode node, final Integer functionId) throws SemanticsException {
		if (node.isEnhancedLoop())
			visitSourceResolvable(functionId, node, node);
		visitChildren(node, functionId);
	}

	private void visitSourceResolvable(final Integer functionId, final ISourceResolvable resolvable, final Node node)
			throws SemanticsException {
		final Integer source = Integer.valueOf(resolvable.getBasicSource());
		switch (resolvable.getSourceType()) {
		case CLOSURE:
		case ENVIRONMENTAL:
			// Remap to consecutive ID.
			Integer newSource = resolutionResult.getMappedEnvironmental(source);
			if (newSource == null) {
				newSource = resolutionResult.getMappedClosure(functionId, source);
				if (newSource == null)
					throw new SemanticsException(NullUtil.messageFormat(CmnCnst.Error.NO_MAPPING_FOUND,
							resolvable.getVariableName(), source), node);
				resolvable.convertEnvironmentalToClosure();
			}
			resolvable.resolveClosureSource(newSource.intValue());
			break;
		case EXTERNAL_CONTEXT:
		case LIBRARY:
			break;
		case UNRESOLVED:
			throw new VariableNotResolvableException(resolvable, node);
		default:
			throw new UnhandledEnumException(resolvable.getSourceType(), node);
		}
	}

	private void resolveScopeDefs(final IScopeDefinitions scopeDefs) throws SemanticsException {
		// Global.
		for (final IHeaderNode header : scopeDefs.getGlobal()) {
			visitSourceResolvable(Integer.valueOf(-1), header, header.getHeaderValueNode());
			header.getHeaderValueNode().jjtAccept(this, Integer.valueOf(-1));
		}
		// Manual scopes.
		for (final Collection<IHeaderNode> coll : scopeDefs.getManual().values()) {
			for (final IHeaderNode header : coll) {
				visitSourceResolvable(Integer.valueOf(-1), header, header.getHeaderValueNode());
				header.getHeaderValueNode().jjtAccept(this, Integer.valueOf(-1));
			}
		}
	}

	public static int convert(final Node node, final IVariableResolutionResult resolutionResult,
			final IScopeDefinitions scopeDefs) throws SemanticsException {
		final ClosureConvertVisitor v = new ClosureConvertVisitor(resolutionResult);
		v.resolveScopeDefs(scopeDefs);
		node.jjtAccept(v, Integer.valueOf(-1));
		return resolutionResult.getEnvironmentalSize();
	}
}