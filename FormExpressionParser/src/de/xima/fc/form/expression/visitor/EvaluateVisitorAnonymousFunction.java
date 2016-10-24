package de.xima.fc.form.expression.visitor;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

class EvaluateVisitorAnonymousFunction implements IFunction<NullLangObject> {
	private final EvaluateVisitor visitor;
	private final Node body;
	private final String[] argList;

	public EvaluateVisitorAnonymousFunction(final EvaluateVisitor visitor, final ASTFunctionNode node,
			final IEvaluationContext ec) {
		this.visitor = visitor;
		final Node[] children = node.getChildArray();
		body = children[children.length - 1];
		argList = new String[children.length - 1];
		for (int i = node.isHasReturnTypeDeclaration() ? 1 : 0; i != argList.length; ++i)
			argList[i] = children[i].jjtAccept(visitor, ec).coerceString(ec).stringValue();
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
		return StringUtils.EMPTY;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final NullLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		final IBinding binding = ec.getBinding();
		final String[] names = getDeclaredArgumentList();
		// Set special variables.
		binding.setVariable(CmnCnst.Variable.ARGUMENTS, ArrayLangObject.create(args));
		binding.setVariable(CmnCnst.Variable.THIS, thisContext);
		// Set variables passed as function arguments
		for (int i = 0; i != names.length; ++i)
			binding.setVariable(names[i], i < args.length ? args[i] : NullLangObject.getInstance());
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