package de.xima.fc.form.expression.visitor;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.parse.DuplicateFunctionArgumentException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableDeclarationAtGlobalScopeException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.exception.parse.VariableDeclaredTwiceException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IBinding;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.impl.binding.CloneBinding;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionArgumentNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * For preparing the binding so we know which variables are in scope at which
 * point.
 *
 * @author mad_gaksha
 * @param <T>
 *            Type of the objects of the binding.
 */
public abstract class AVariableBindingVisitor<T> extends FormExpressionVoidVoidVisitorAdapter<ParseException> {
	@Nonnull
	protected final IBinding<T> binding;

	protected AVariableBindingVisitor() {
		this.binding = new CloneBinding<T>();
	}

	private void visitNested(@Nonnull final Node node) throws ParseException {
		final int bookmark = binding.getBookmark();
		binding.nest();
		try {
			node.jjtAccept(this);
		}
		finally {
			binding.gotoBookmark(bookmark);
		}
	}

	private void visitNestedNullable(@Nullable final Node node) throws ParseException {
		if (node == null)
			return;
		visitNested(node);
	}

	@Override
	public final void visit(final ASTVariableDeclarationClauseNode node) throws ParseException {
		// We need to visit the children first. Consider
		//  var i = i;
		// Here, i at the right hand side has not been initialized.
		if (node.hasAssignment())
			node.getAssignmentNode().jjtAccept(this);
		visitSourceResolvable(node);
	}

	@Override
	public final void visit(final ASTFunctionArgumentNode node) throws ParseException {
		visitSourceResolvable(node);
	}

	@Override
	public final void visit(final ASTIfClauseNode node) throws ParseException {
		node.getConditionNode().jjtAccept(this);
		visitNested(node.getIfNode());
		visitNestedNullable(node.getElseNode());
	}

	@Override
	public final void visit(final ASTDoWhileLoopNode node) throws ParseException {
		visitNested(node.getBodyNode());
		node.getDoFooterNode().jjtAccept(this);
	}

	@Override
	public final void visit(final ASTWhileLoopNode node) throws ParseException {
		node.getWhileHeaderNode().jjtAccept(this);
		visitNested(node.getBodyNode());
	}

	@Override
	public final void visit(final ASTForLoopNode node) throws ParseException {
		if (node.isEnhancedLoop()) {
			final int bookmark = binding.getBookmark();
			binding.nest();
			try {
				node.getEnhancedIteratorNode().jjtAccept(this);
				final T object = getNewObjectToSet(node);
				binding.defineVariable(node.getVariableName(), object);
				node.getBodyNode().jjtAccept(this);
			}
			finally {
				binding.gotoBookmark(bookmark);
			}
		}
		else {
			final int bookmark = binding.getBookmark();
			binding.nest();
			try {
				node.getPlainInitializerNode().jjtAccept(this);
				node.getPlainConditionNode().jjtAccept(this);
				node.getBodyNode().jjtAccept(this);
				node.getPlainIncrementNode().jjtAccept(this);
			}
			finally {
				binding.gotoBookmark(bookmark);
			}
		}
	}

	@Override
	public final void visit(final ASTFunctionNode node) throws ParseException {
		visitFunctionNode(node);
	}

	@Override
	public final void visit(final ASTFunctionClauseNode node) throws ParseException {
		visitFunctionNode(node);
	}

	private <S extends IArgumentResolvable & Node> void visitFunctionNode(final S node) throws ParseException {
		final int bookmark = binding.getBookmark();
		binding.nestLocal();

		final T objectThis = getNewObjectToSet(node.getThisResolvable());
		binding.defineVariable(node.getThisResolvable().getVariableName(), objectThis);

		try {
			for (int i = 0; i < node.getArgumentCount(); ++i) {
				final ISourceResolvable res = node.getArgResolvable(i);
				if (binding.hasVariableAtCurrentLevel(res.getVariableName()))
					throw new DuplicateFunctionArgumentException(res.getVariableName(), node);
				binding.defineVariable(res.getVariableName(), getNewObjectToSet(res));
			}
			node.getBodyNode().jjtAccept(this);
		}
		finally {
			binding.gotoBookmark(bookmark);
		}
	}

	@Override
	public final void visit(final ASTTryClauseNode node) throws ParseException {
		visitNested(node.getTryNode());
		final int bookmark = binding.getBookmark();
		binding.nest();
		try {
			final T object = getNewObjectToSet(node);
			binding.defineVariable(node.getVariableName(), object);
			node.getCatchNode().jjtAccept(this);
		}
		finally {
			binding.gotoBookmark(bookmark);
		}
	}

	@Override
	public final void visit(final ASTSwitchClauseNode node) throws ParseException {
		node.getSwitchValueNode().jjtAccept(this);
		boolean nested = false;
		int bookmark = 0;
		try {
			for (int i = 1; i < node.jjtGetNumChildren(); ++i) {
				switch (node.jjtGetChild(i).getSiblingMethod()) {
				case SWITCHCASE:
					bookmark = binding.getBookmark();
					binding.nest();
					node.jjtGetChild(i).jjtAccept(this);
					nested = true;
					break;
				case SWITCHCLAUSE:
				case SWITCHDEFAULT:
					if (!nested) {
						bookmark = binding.getBookmark();
						binding.nest();
					}
					node.jjtGetChild(i).jjtAccept(this);
					binding.gotoBookmark(bookmark);
					nested = false;
					break;
					//$CASES-OMITTED$
				default:
					throw new SemanticsException(
							NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_ENUM_SWITCH, node.jjtGetChild(i).getSiblingMethod()),
							node.jjtGetChild(i));
				}
			}
		}
		finally {
			if (nested)
				binding.gotoBookmark(bookmark);
		}
	}

	private final <S extends ISourceResolvable & Node> void visitSourceResolvable(final S node) throws ParseException {
		if (binding.isGlobal())
			throw new IllegalVariableDeclarationAtGlobalScopeException(node);
		if (binding.hasVariableAtCurrentLevel(node.getVariableName()))
			throw new VariableDeclaredTwiceException(node);
		binding.defineVariable(node.getVariableName(), getNewObjectToSet(node));
	}

	/**
	 * Resolves code inside function bodies. Function have already been detached
	 * from the main program and put at the top.
	 *
	 * @param scopeDefBuilder
	 * @throws ParseException
	 */
	protected void bindScopeDefValues(@Nonnull final IScopeDefinitionsBuilder scopeDefBuilder) throws ParseException {
		// Global scope.
		for (final Iterator<Entry<String, IHeaderNode>> it = scopeDefBuilder.getGlobal(); it.hasNext();) {
			final IHeaderNode hn = it.next().getValue();
			if (hn.hasNode())
				hn.getNode().jjtAccept(this);
		}
		// Manual scopes.
		for (final Iterator<String> it = scopeDefBuilder.getManual(); it.hasNext();) {
			final String scope = it.next();
			if (scope != null) {
				final Iterator<Entry<String, IHeaderNode>> it2 = scopeDefBuilder.getManual(scope);
				if (it2 != null) {
					while (it2.hasNext()) {
						final IHeaderNode hn = it2.next().getValue();
						if (hn.hasNode())
							hn.getNode().jjtAccept(this);
					}
				}
			}
		}
	}
	
	/**
	 * When a variable is declared, it is set to this value initially.
	 *
	 * @return The object to set.
	 * @throws IllegalVariableSourceResolutionException
	 */
	@Nonnull
	protected abstract T getNewObjectToSet(ISourceResolvable res) throws IllegalVariableSourceResolutionException;
}