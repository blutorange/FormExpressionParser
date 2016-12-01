package de.xima.fc.form.expression.visitor;

import java.util.Collection;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.impl.warning.UnusedVariableWarning;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;

public class UnusedVariableCheckVisitor extends FormExpressionVoidDataVisitorAdapter<Boolean, RuntimeException> {
	@Nonnull private final ISourceResolvable[] resolvableTable;
	@Nonnull private final Node[] nodeTable;
	@Nonnull private final boolean[] booleanTable;
	
	private UnusedVariableCheckVisitor(final int symbolTableSize) {
		resolvableTable = new ISourceResolvable[symbolTableSize];
		nodeTable = new Node[symbolTableSize];
		booleanTable = new boolean[symbolTableSize];
	}
	
	private void addToNodeTable(@Nonnull final ISourceResolvable resolvable, @Nonnull final Node node) {
		final int source = resolvable.getSource();
		if (source >= 0 && source < resolvableTable.length && nodeTable[source] == null) {
			resolvableTable[source] = resolvable;
			nodeTable[source] = node;
		}
	}

	private void addToBooleanTable(final int source) {
		if (source >= 0 && source < booleanTable.length)
			booleanTable[source] = true;		
	}

	private void addWarnings(@Nonnull final IEvaluationContext ec) {
		for (int i = resolvableTable.length; i-- > 0;) {
			if (!booleanTable[i]) {
				final ISourceResolvable resolvable = resolvableTable[i];
				final Node node = nodeTable[i];
				if (node != null && resolvable != null)
					ec.getTracer().appendWarning(new UnusedVariableWarning(resolvable, node));
			}
		}
	}

	@Override
	public void visit(final ASTVariableDeclarationClauseNode node, final Boolean assignment) {
		visitChildren(node, assignment);
		addToNodeTable(node, node);
	}
	
	@Override
	public void visit(final ASTVariableNode node, final Boolean assignment) {
		final int source = node.getSource();
		if (source >= 0) {
			if (assignment)
				addToNodeTable(node, node);
			else
				addToBooleanTable(source);
		}
	}
	
	@Override
	public void visit(final ASTFunctionClauseNode node, final Boolean assignment) {
		for (int i = node.getArgumentCount(); i-->0;)
			addToNodeTable(node.getArgResolvable(i), node.jjtGetChild(i));
		node.getBodyNode().jjtAccept(this, assignment);
	}
	
	@Override
	public void visit(final ASTFunctionNode node, final Boolean assignment) {
		for (int i = node.getArgumentCount(); i-->0;)
			addToNodeTable(node.getArgResolvable(i), node.jjtGetChild(i));
		node.getBodyNode().jjtAccept(this, assignment);
	}

	@Override
	public void visit(final ASTAssignmentExpressionNode node, Boolean assignment) {
		for (int i = node.getAssignableNodeCount(); i-- > 0;) {
			// For ASTVariableNodes, we add it to the list of unused variables.
			// For ASTPropertyExpressionNodes, we need to mark every
			// occurring variable as being used.
			final Node n = node.getAssignableNode(i);
			final Node first = n.getFirstChildOrNull();
			assignment = assignment || n.jjtGetNodeId() == FormExpressionParserTreeConstants.JJTVARIABLENODE;
			if (first != null)
				first.jjtAccept(this, assignment);
		}
	}

	private void defineScopeDefs(final IScopeDefinitions scopeDefs) {
		for (final IHeaderNode header : scopeDefs.getGlobal())
			addToNodeTable(header, header.getNode());
		for (final Collection<IHeaderNode> coll : scopeDefs.getManual().values())
			for (final IHeaderNode header : coll)
				addToNodeTable(header, header.getNode());
	}

	public static void check(@Nonnull final Node node, @Nonnull final IScopeDefinitions scopeDefs,
			final int symbolTableSize, @Nonnull final IEvaluationContext ec) {
		final UnusedVariableCheckVisitor v = new UnusedVariableCheckVisitor(symbolTableSize);
		v.defineScopeDefs(scopeDefs);
		node.jjtAccept(v, CmnCnst.NonnullConstant.BOOLEAN_FALSE);
		v.addWarnings(ec);
	}
}