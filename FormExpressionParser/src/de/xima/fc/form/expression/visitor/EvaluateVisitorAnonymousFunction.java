package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.util.CmnCnst;

class EvaluateVisitorAnonymousFunction extends EvaluateVisitorNamedFunction {
	public EvaluateVisitorAnonymousFunction(@Nonnull final EvaluateVisitor visitor, @Nonnull final ASTFunctionNode node,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
		super(visitor, node, CmnCnst.NonnullConstant.STRING_EMPTY, ec);
	}
}