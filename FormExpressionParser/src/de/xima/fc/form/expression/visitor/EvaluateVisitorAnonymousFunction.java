package de.xima.fc.form.expression.visitor;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.util.CmnCnst;

@NonNullByDefault
class EvaluateVisitorAnonymousFunction extends EvaluateVisitorNamedFunction {
	public EvaluateVisitorAnonymousFunction(final EvaluateVisitor visitor, final ASTFunctionNode node,
			final IEvaluationContext ec) throws EvaluationException {
		super(visitor, node, CmnCnst.NonnullConstant.STRING_EMPTY, ec);
	}
}