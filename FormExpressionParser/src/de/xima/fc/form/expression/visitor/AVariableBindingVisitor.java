package de.xima.fc.form.expression.visitor;

import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.parse.DuplicateFunctionArgumentException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableDeclarationAtGlobalScopeException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;
import de.xima.fc.form.expression.exception.parse.UnhandledEnumException;
import de.xima.fc.form.expression.exception.parse.VariableDeclaredTwiceException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IBinding;
import de.xima.fc.form.expression.iface.parse.IFunctionNode;
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

/**
 * For preparing the binding so we know which variables are in scope at which
 * point.
 *
 * @author mad_gaksha
 * @param <T>
 *            Type of the objects of the binding.
 */
@NonNullByDefault
public abstract class AVariableBindingVisitor<T,D> extends FormExpressionVoidDataVisitorAdapter<D, ParseException> {
	protected final IBinding<T> binding;

	protected AVariableBindingVisitor() {
		this.binding = new CloneBinding<>();
	}

	private void visitNested(final Node node, final D data) throws ParseException {
		final int bookmark = binding.getBookmark();
		binding.nest();
		try {
			node.jjtAccept(this, data);
		}
		finally {
			binding.gotoBookmark(bookmark);
		}
	}

	@Override
	public final void visit(final ASTVariableDeclarationClauseNode node, final D data) throws ParseException {
		// We need to visit the children first. Consider
		//  var i = i;
		// Here, i at the right hand side has not been initialized.
		if (node.hasAssignment())
			node.getAssignmentNode().jjtAccept(this, data);
		visitSourceResolvable(node, data);
	}

	@Override
	public final void visit(final ASTFunctionArgumentNode node, final D data) throws ParseException {
		visitSourceResolvable(node, data);
	}

	@Override
	public final void visit(final ASTIfClauseNode node, final D data) throws ParseException {
		node.getConditionNode().jjtAccept(this, data);
		visitNested(node.getIfNode(), data);
		if (node.hasElseNode())
			visitNested(node.getElseNode(), data);
	}

	@Override
	public final void visit(final ASTDoWhileLoopNode node, final D data) throws ParseException {
		visitNested(node.getBodyNode(), data);
		node.getDoFooterNode().jjtAccept(this, data);
	}

	@Override
	public final void visit(final ASTWhileLoopNode node, final D data) throws ParseException {
		node.getWhileHeaderNode().jjtAccept(this, data);
		visitNested(node.getBodyNode(), data);
	}

	@Override
	public final void visit(final ASTForLoopNode node, final D data) throws ParseException {
		if (node.isEnhancedLoop()) {
			final int bookmark = binding.getBookmark();
			binding.nest();
			try {
				node.getEnhancedIteratorNode().jjtAccept(this, data);
				final T object = getNewObjectToSet(node, node, data);
				binding.defineVariable(node.getVariableName(), object);
				node.getBodyNode().jjtAccept(this, data);
			}
			finally {
				binding.gotoBookmark(bookmark);
			}
		}
		else {
			final int bookmark = binding.getBookmark();
			binding.nest();
			try {
				node.getPlainInitializerNode().jjtAccept(this, data);
				node.getPlainConditionNode().jjtAccept(this, data);
				node.getBodyNode().jjtAccept(this, data);
				node.getPlainIncrementNode().jjtAccept(this, data);
			}
			finally {
				binding.gotoBookmark(bookmark);
			}
		}
	}

	@Override
	public final void visit(final ASTFunctionNode node, final D data) throws ParseException {
		visitFunctionNode(node, data, false);
	}

	@Override
	public final void visit(final ASTFunctionClauseNode node, final D data) throws ParseException {
		visitFunctionNode(node, data, true);
	}

	private void visitFunctionNode(final IFunctionNode node, final D data, final boolean nestLocal) throws ParseException {
		final D newData = beforeFunctionNode(node, data);
		final int bookmark = binding.getBookmark();
		if (nestLocal)
			binding.nestLocal();
		else
			binding.nest();
		try {
			for (int i = 0; i < node.getArgumentCount(); ++i) {
				final ISourceResolvable res = node.getArgResolvable(i);
				if (binding.hasVariableAtCurrentLevel(res.getVariableName()))
					throw new DuplicateFunctionArgumentException(res.getVariableName(), node);
				binding.defineVariable(res.getVariableName(), getNewObjectToSet(res, node.getArgumentNode(i), newData));
			}
			node.getBodyNode().jjtAccept(this, newData);
		}
		finally {
			binding.gotoBookmark(bookmark);
		}
	}

	/**
	 * No-op, may be override. Called before an {@link ASTFunctionClauseNode} and {@link ASTFunctionNode}.
	 * @param node The node, either an {@link ASTFunctionClauseNode} or {@link ASTFunctionNode}.
	 * @param data Custom data.
	 */
	protected D beforeFunctionNode(final IFunctionNode node, final D data) {
		return data;
	}

	@Override
	public final void visit(final ASTTryClauseNode node, final D data) throws ParseException {
		visitNested(node.getTryNode(), data);
		final int bookmark = binding.getBookmark();
		binding.nest();
		try {
			final T object = getNewObjectToSet(node, node, data);
			binding.defineVariable(node.getVariableName(), object);
			node.getCatchNode().jjtAccept(this, data);
		}
		finally {
			binding.gotoBookmark(bookmark);
		}
	}

	/**
	 * var x;
	 * ...
	 * switch(...)
	 *   case (var x=9):
	 *     ...
	 *   case (x):
	 *     ...
	 */

	@Override
	public final void visit(final ASTSwitchClauseNode node, final D data) throws ParseException {
		node.getSwitchValueNode().jjtAccept(this, data);
		final int caseCount = node.getCaseCount();
		int i = 0;
		while (i < caseCount) {
			switch (node.getCaseType(i)) {
			case SWITCHCLAUSE: {
				final int bookmark = binding.getBookmark();
				binding.nest();
				try {
					do {
						node.getCaseNode(i++).jjtAccept(this, data);
					} while (i < caseCount && node.getCaseType(i) == EMethod.SWITCHCLAUSE);
				}
				finally {
					binding.gotoBookmark(bookmark);
				}
				break;
			}
			case SWITCHCASE: {
				final int bookmark = binding.getBookmark();
				binding.nest();
				try {
					node.getCaseNode(i).jjtAccept(this, data);
				}
				finally {
					binding.gotoBookmark(bookmark);
				}
				binding.gotoBookmark(bookmark);
				++i;
				break;
			}
			case SWITCHDEFAULT: {
				final int bookmark = binding.getBookmark();
				binding.nest();
				try {
					do {
						node.getCaseNode(i++).jjtAccept(this, data);
					} while (i < caseCount && node.getCaseType(i) == EMethod.SWITCHDEFAULT);
				}
				finally {
					binding.gotoBookmark(bookmark);
				}
				break;
			}
			// $CASES-OMITTED$
			default:
				throw new UnhandledEnumException(node.getCaseNode(i).getSiblingMethod(), node.jjtGetChild(i));
			}
		}
	}

	private final <S extends ISourceResolvable & Node> void visitSourceResolvable(final S node, final D data) throws ParseException {
		if (binding.isGlobal())
			throw new IllegalVariableDeclarationAtGlobalScopeException(node);
		if (binding.hasVariableAtCurrentLevel(node.getVariableName()))
			throw new VariableDeclaredTwiceException(node);
		binding.defineVariable(node.getVariableName(), getNewObjectToSet(node, node, data));
	}

	/**
	 * Resolves code inside function bodies. Function have already been detached
	 * from the main program and put at the top.
	 *
	 * @param scopeDefBuilder
	 * @throws ParseException
	 */
	protected void bindScopeDefValues(final IScopeDefinitionsBuilder scopeDefBuilder, final D data) throws ParseException {
		// Global scope.
		for (final Iterator<Entry<String, IHeaderNode>> it = scopeDefBuilder.getGlobal(); it.hasNext();) {
			final IHeaderNode hn = it.next().getValue();
			hn.getNode().jjtAccept(this, data);
		}
		// Manual scopes.
		for (final Iterator<String> it = scopeDefBuilder.getManual(); it.hasNext();) {
			final String scope = it.next();
			if (scope != null) {
				final Iterator<Entry<String, IHeaderNode>> it2 = scopeDefBuilder.getManual(scope);
				if (it2 != null) {
					while (it2.hasNext()) {
						final IHeaderNode hn = it2.next().getValue();
						hn.getNode().jjtAccept(this, data);
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
	protected abstract T getNewObjectToSet(ISourceResolvable res, Node node, D data) throws ParseException;
}