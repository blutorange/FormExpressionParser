package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

class EvaluateVisitorAnonymousFunction implements IFunction<NullLangObject> {
	@Nonnull private final EvaluateVisitor visitor;
	@Nonnull private final Node body;
	@Nonnull private final String[] argList;

	public EvaluateVisitorAnonymousFunction(@Nonnull final EvaluateVisitor visitor, @Nonnull final ASTFunctionNode node,
			@Nonnull final IEvaluationContext ec) {
		final Node b = node.getLastChild();
		if (b == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_CHILD_NODE);
		if (node.jjtGetNumChildren() == 0)
			throw new UncatchableEvaluationException(ec, NullUtil.format(CmnCnst.Error.NODE_COUNT_AT_LEAST, 1, 0));
		this.visitor = visitor;
		body = b;
		argList = new String[node.jjtGetNumChildren() - 1];
		for (int i = 0; i != argList.length; ++i)
			argList[i] = node.jjtGetChild(i).jjtAccept(visitor, ec).coerceString(ec).stringValue();
	}

	@Override
	public Node getNode() {
		return body;
	}

	@Override
	public String[] getDeclaredArgumentList() {
		return argList;
	}

	@Override
	public String getDeclaredName() {
		return CmnCnst.EMPTY_STRING;
	}

	@Override
	public ALangObject evaluate(@Nonnull final IEvaluationContext ec, @Nonnull final NullLangObject thisContext,
			@Nonnull final ALangObject... args) throws EvaluationException {
		final IBinding binding = ec.getBinding();
		final String[] names = getDeclaredArgumentList();
		// Set special variables.
		binding.setVariable(CmnCnst.Variable.ARGUMENTS, ArrayLangObject.create(args));
		binding.setVariable(CmnCnst.Variable.THIS, thisContext);
		// Set variables passed as function arguments
		for (int i = 0; i != names.length; ++i) {
			@SuppressWarnings("null")
			@Nonnull
			final ALangObject tmp = i < args.length ? args[i] : NullLangObject.getInstance();
			@SuppressWarnings("null")
			@Nonnull
			final String name = names[i];
			binding.setVariable(name, tmp);
		}
		// Evaluate function.
		return body.jjtAccept(visitor, ec);
	}

	@Override
	public Type getThisContextType() {
		return Type.NULL;
	}

	@Override
	public String getVarArgsName() {
		return null;
	}
}