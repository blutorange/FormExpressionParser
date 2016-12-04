package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UnresolvedVariableSourceException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

class EvaluateVisitorNamedFunction implements IFunction<NullLangObject> {
	@Nonnull
	private final EvaluateVisitor visitor;
	@Nonnull
	private final IArgumentResolvable node;
	@Nonnull
	private final String name;
	@Nullable
	private String[] argList;

	public EvaluateVisitorNamedFunction(@Nonnull final EvaluateVisitor visitor,
			@Nonnull final ASTFunctionClauseNode node, @Nonnull final IEvaluationContext ec)
			throws UncatchableEvaluationException {
		this(visitor, node, node.getCanonicalName(), ec);
	}

	protected <T extends Node & IArgumentResolvable> EvaluateVisitorNamedFunction(
			@Nonnull final EvaluateVisitor visitor, @Nonnull final T node, @Nonnull final String name,
			@Nonnull final IEvaluationContext ec) throws UncatchableEvaluationException {
		final Node b = node.getLastChildOrNull();
		if (b == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_CHILD_NODE);
		if (node.jjtGetNumChildren() == 0)
			throw new UncatchableEvaluationException(ec, NullUtil.format(CmnCnst.Error.NODE_COUNT_AT_LEAST, 1, 0));
		this.visitor = visitor;
		this.name = name;
		this.node = node;
	}

	@Override
	public Node getNode() {
		return node.getBodyNode();
	}

	@Override
	public String[] getDeclaredArgumentList() {
		if (argList != null)
			return argList;
		final String[] list = new String[node.getArgumentCount()];
		for (int i = node.getArgumentCount(); i-- > 0;)
			list[i] = node.getArgResolvable(i).getVariableName();
		return argList = list;
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
		// Set special variables 'this' and 'arguments'.
		// TODO remove the arguments variable, not needed and wont work with var
		// types
		set(ec, node.getThisResolvable(), thisContext);
		final int normalArgCount;
		if (node.hasVarArgs()) {
			normalArgCount = node.getArgumentCount() - 1;
			// Set varArgs array.
			if (node.hasVarArgs()) {
				final ALangObject tmp = ArrayLangObject.create(args, normalArgCount);
				set(ec, node.getArgResolvable(normalArgCount), tmp);
			}
		}
		else
			normalArgCount = node.getArgumentCount();
		// Set variables passed as function arguments.
		for (int i = normalArgCount; i-- > 0;) {
			// Need to check for null as java7 does not allow us to mark
			// non-null arrays
			final ALangObject tmp = args[i];
			set(ec, node.getArgResolvable(i), tmp != null ? tmp : NullLangObject.getInstance());
		}
		// Evaluate function.
		return node.getBodyNode().jjtAccept(visitor);
	}

	private void set(@Nonnull final IEvaluationContext ec, @Nonnull final ISourceResolvable res,
			@Nonnull final ALangObject val) throws UnresolvedVariableSourceException {
		if (res.getSource() < 0)
			throw new UnresolvedVariableSourceException(null, res.getVariableName(), ec);
		ec.getSymbolTable()[res.getSource()].setCurrentObject(val);
	}

	@Override
	public ELangObjectType getThisContextType() {
		return ELangObjectType.NULL;
	}

	@Override
	public boolean hasVarArgs() {
		return node.hasVarArgs();
	}
}