package de.xima.fc.form.expression.visitor;

import java.util.Collection;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.impl.warning.EmptyStatementWarning;
import de.xima.fc.form.expression.node.ASTEmptyNode;
import de.xima.fc.form.expression.node.ASTVariableNode;

@ParametersAreNonnullByDefault
public class SimulateVisitor extends FormExpressionVoidVoidVisitorAdapter<EvaluationException> {
	private final IEvaluationContext ec;
	@Nullable
	private final IExternalContext ex;

	private SimulateVisitor(final IEvaluationContext ec) {
		this.ec = ec;
		ex = ec.getExternalContext();
	}

	@Override
	public void visitChildren(final Node node) throws EvaluationException {
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			final Node n = node.jjtGetChild(i);
			ec.getTracer().setCurrentlyProcessed(n);
			n.jjtAccept(this);
		}
		ec.getTracer().setCurrentlyProcessed(node);
	}

	@Override
	public void visit(final ASTVariableNode node) throws EvaluationException {
		final String scope = node.getScope();
		if (node.getSource() == EVariableSource.ID_EXTERNAL_CONTEXT && scope != null) {
			final IExternalContext ex = this.ex;
			if (ex != null) {
				ex.fetchScopedVariable(scope, node.getVariableName(), ec);
			}
		}
	}

	@Override
	public void visit(final ASTEmptyNode node) throws EvaluationException {
		ec.getTracer().appendWarning(new EmptyStatementWarning(node));
	}

	public static void simulate(final Node node, final IScopeDefinitions scopeDefs, final IEvaluationContext ec)
			throws EvaluationException {
		ec.getTracer().enableWarnings();
		final SimulateVisitor v = new SimulateVisitor(ec);
		v.acceptScopeDefs(scopeDefs);
		node.jjtAccept(v);
	}

	private void acceptScopeDefs(final IScopeDefinitions scopeDefs) throws EvaluationException {
		for (final IHeaderNode header: scopeDefs.getGlobal())
			if (header.hasNode())
				header.getNode().jjtAccept(this);
		for (final Collection<IHeaderNode> coll : scopeDefs.getManual().values())
			for (final IHeaderNode header: coll)
				if (header.hasNode())
					header.getNode().jjtAccept(this);
	}
}