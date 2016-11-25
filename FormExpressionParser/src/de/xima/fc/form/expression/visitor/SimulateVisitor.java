package de.xima.fc.form.expression.visitor;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IEvaluationWarning;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.node.ASTVariableNode;

public class SimulateVisitor extends FormExpressionVoidVoidVisitorAdapter<EvaluationException> {
	@Nonnull
	private final IEvaluationContext ec;
	@Nullable
	private final IExternalContext ex;

	private SimulateVisitor(@Nonnull final IEvaluationContext ec) {
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

	@Nonnull
	public static ImmutableCollection<IEvaluationWarning> simulate(@Nonnull final Node node,
			@Nonnull final IScopeDefinitions scopeDefs, @Nonnull final IEvaluationContext ec) throws EvaluationException {
		ec.getTracer().enableWarnings();
		final SimulateVisitor v = new SimulateVisitor(ec);
		v.acceptScopeDefs(scopeDefs);
		node.jjtAccept(v);
		return ImmutableList.copyOf(ec.getTracer().getWarnings());
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