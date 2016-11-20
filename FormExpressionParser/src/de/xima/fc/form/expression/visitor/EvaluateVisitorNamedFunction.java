package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

class EvaluateVisitorNamedFunction implements IFunction<NullLangObject> {
	@Nonnull
	private final EvaluateVisitor visitor;
	@Nonnull
	private final Node body;
	@Nonnull
	private final String[] argList;
	@Nonnull
	private final String name;

	public EvaluateVisitorNamedFunction(@Nonnull final EvaluateVisitor visitor,
			@Nonnull final ASTFunctionClauseNode node, @Nonnull final IEvaluationContext ec) throws EvaluationException {
		this(visitor, node, node.getCanonicalName(), getArgList(visitor, node, ec), ec);
	}

	protected EvaluateVisitorNamedFunction(@Nonnull final EvaluateVisitor visitor, @Nonnull final Node node,
			@Nonnull final String name, @Nonnull final String[] argList, @Nonnull final IEvaluationContext ec) throws UncatchableEvaluationException {
		final Node b = node.getLastChildOrNull();
		if (b == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_CHILD_NODE);
		if (node.jjtGetNumChildren() == 0)
			throw new UncatchableEvaluationException(ec, NullUtil.format(CmnCnst.Error.NODE_COUNT_AT_LEAST, 1, 0));
		this.visitor = visitor;
		this.name = name;
		this.argList = argList;
		body = b;
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
		return name;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final NullLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		final String[] names = getDeclaredArgumentList();
		// Set special variables.
		binding.setVariable(CmnCnst.Name.VARIABLE_ARGUMENTS, ArrayLangObject.create(args));
		binding.setVariable(CmnCnst.Name.VARIABLE_THIS, thisContext);
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
		return body.jjtAccept(visitor);
	}

	@Override
	public Type getThisContextType() {
		return Type.NULL;
	}

	@Override
	public String getVarArgsName() {
		return null;
	}

	@Nonnull
	private static String[] getArgList(@Nonnull final EvaluateVisitor visitor, @Nonnull final Node node,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
		final String[] argList = new String[node.jjtGetNumChildren() - 2];
		for (int i = 0; i != argList.length; ++i)
			argList[i] = node.jjtGetChild(i + 1).jjtAccept(visitor).coerceString(ec).stringValue();
		return argList;
	}
}