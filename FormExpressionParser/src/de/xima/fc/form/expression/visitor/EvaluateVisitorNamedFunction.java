package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IArgumentResolvableNode;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IUnparsableFunction;
import de.xima.fc.form.expression.impl.config.UnparseConfig;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
class EvaluateVisitorNamedFunction implements IUnparsableFunction<NullLangObject> {
	private final EvaluateVisitor visitor;
	private final IArgumentResolvableNode node;
	private final String name;

	public EvaluateVisitorNamedFunction(final EvaluateVisitor visitor, final ASTFunctionClauseNode node,
			final IEvaluationContext ec) throws UncatchableEvaluationException {
		this(visitor, node, node.getCanonicalName(), ec);
	}

	protected EvaluateVisitorNamedFunction(final EvaluateVisitor visitor, final IArgumentResolvableNode node,
			final String name, final IEvaluationContext ec) throws UncatchableEvaluationException {
		final Node b = node.getLastChildOrNull();
		if (b == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_CHILD_NODE);
		if (node.jjtGetNumChildren() == 0)
			throw new UncatchableEvaluationException(ec,
					NullUtil.messageFormat(CmnCnst.Error.NODE_COUNT_AT_LEAST, 1, 0));
		this.visitor = visitor;
		this.name = name;
		this.node = node;
	}

	@Override
	public String getDeclaredArgument(final int i) {
		return node.getArgResolvable(i).getVariableName();
	}

	@Override
	public int getDeclaredArgumentCount() {
		return node.getArgumentCount();
	}

	@Override
	public String getDeclaredName() {
		return name;
	}

	@Override
	public ALangObject evaluate(@Nonnull final IEvaluationContext ec, @Nonnull final NullLangObject thisContext,
			@Nonnull final ALangObject... args) throws EvaluationException {
		final int normalArgCount;
		if (node.hasVarArgs()) {
			normalArgCount = node.getArgumentCount() - 1;
			// Set varArgs array.
			if (node.hasVarArgs()) {
				final ALangObject tmp = ArrayLangObject.create(args, normalArgCount);
				visitor.setVariable(node.getArgResolvable(normalArgCount), tmp);
			}
		}
		else
			normalArgCount = node.getArgumentCount();
		// Set variables passed as function arguments.
		for (int i = normalArgCount; i-- > 0;) {
			// Need to check for null as java7 does not allow us to mark
			// non-null arrays
			final ALangObject tmp = args[i];
			visitor.setVariable(node.getArgResolvable(i), tmp != null ? tmp : NullLangObject.getInstance());
		}
		// Evaluate function.
		return node.getBodyNode().jjtAccept(visitor);
	}

	@Override
	public ILangObjectClass getThisContextType() {
		return ELangObjectClass.NULL;
	}

	@Override
	public boolean hasVarArgs() {
		return node.hasVarArgs();
	}

	@Override
	public void unparseBody(final StringBuilder builder) {
		builder.append(UnparseVisitor.unparse(node.getBodyNode(), UnparseConfig.getUnstyledWithoutCommentsConfig()));
	}
}