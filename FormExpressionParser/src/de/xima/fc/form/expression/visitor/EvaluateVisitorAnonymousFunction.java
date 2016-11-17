package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.util.CmnCnst;

class EvaluateVisitorAnonymousFunction extends EvaluateVisitorNamedFunction {
	public EvaluateVisitorAnonymousFunction(@Nonnull final EvaluateVisitor visitor, @Nonnull final ASTFunctionNode node,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
		super(visitor, node, CmnCnst.EMPTY_STRING, getArgList(visitor, node, ec), ec);
	}

	@Nonnull
	private static String[] getArgList(@Nonnull final EvaluateVisitor visitor, @Nonnull final Node node,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
		final String[] argList = new String[node.jjtGetNumChildren() - 1];
		for (int i = 0; i != argList.length; ++i)
			argList[i] = node.jjtGetChild(i).jjtAccept(visitor, ec).coerceString(ec).stringValue();
		return argList;
	}
}