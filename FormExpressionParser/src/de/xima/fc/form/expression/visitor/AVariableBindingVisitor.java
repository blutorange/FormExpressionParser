package de.xima.fc.form.expression.visitor;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import de.xima.fc.form.expression.exception.parse.DuplicateFunctionArgumentException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableDeclarationAtGlobalScopeException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.exception.parse.VariableDeclaredTwiceException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.context.IBinding;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.impl.binding.CloneBinding;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTIdentifierNameNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * For preparing the binding so we know which variables are in scope at which point.
 * @author mad_gaksha
 * @param <T> Type of the objects of the binding.
 */
public abstract class AVariableBindingVisitor<T> extends FormExpressionVoidVoidVisitorAdapter<ParseException> {
	@Nonnull
	protected final IBinding<T> binding;
	@Nonnull
	protected Mode mode = Mode.NONE;

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
	
	@OverridingMethodsMustInvokeSuper
	@Override
	public final void visit(final ASTVariableDeclarationClauseNode node) throws ParseException {
		if (binding.isGlobal())
			throw new IllegalVariableDeclarationAtGlobalScopeException(node);
		if (binding.hasVariableAtCurrentLevel(node.getVariableName()))
			throw new VariableDeclaredTwiceException(node);
		binding.defineVariable(node.getVariableName(), getNewObjectToSet());
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public final void visit(final ASTIfClauseNode node) throws ParseException {
		node.getConditionNode().jjtAccept(this);
		visitNested(node.getIfNode());
		visitNestedNullable(node.getElseNode());
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public final void visit(final ASTDoWhileLoopNode node) throws ParseException {
		visitNested(node.getBodyNode());
		node.getDoFooterNode().jjtAccept(this);
	}

	@OverridingMethodsMustInvokeSuper
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
				final T object = getNewObjectToSet();
				binding.defineVariable(node.getIteratingLoopVariable(), object);
				enhancedForLoopIteratingLoopVariable(node, object);
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
	public final void visit(final ASTFunctionClauseNode node) throws ParseException {
		final int bookmark = binding.getBookmark();
		binding.nestLocal();
		// TODO set arguments/this variables. does this make sense?
		binding.defineVariable(CmnCnst.Name.VARIABLE_THIS, getNewObjectToSet());
		binding.defineVariable(CmnCnst.Name.VARIABLE_ARGUMENTS, getNewObjectToSet());
		try {
			mode = Mode.INSIDE_FUNCTION_ARGUMENT;
			for (int i = 0; i < node.getArgumentCount(); ++i)
				node.getArgumentNode(i).jjtAccept(this);
			mode = Mode.NONE;
			node.getBodyNode().jjtAccept(this);
		} finally {
			binding.gotoBookmark(bookmark);
		}
	}

	@Override
	public final void visit(final ASTTryClauseNode node) throws ParseException {
		visitNested(node.getTryNode());
		final int bookmark = binding.getBookmark();
		binding.nest();
		try {
			binding.defineVariable(node.getErrorVariableName(), getNewObjectToSet());
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
				default:
					throw new SemanticsException(
							NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_SWITCH, node.jjtGetChild(i).getSiblingMethod()),
							node.jjtGetChild(i));
				}
			}
		}
		finally {
			if (nested)
				binding.gotoBookmark(bookmark);
		}		
	}
	
	@Override
	public final void visit(final ASTIdentifierNameNode node) throws SemanticsException {
		if (mode == Mode.INSIDE_FUNCTION_ARGUMENT) {
			if (binding.hasVariableAtCurrentLevel(node.getName()))
				throw new DuplicateFunctionArgumentException(node.getName(), node);
			binding.defineVariable(node.getName(), getNewObjectToSet());
		}
		visitIdentifierNameNode(node);
	}
	
	protected void resolveFunctions(@Nonnull final IScopeDefinitionsBuilder scopeDefBuilder) throws ParseException {
		for (final Iterator<Entry<String,Node>> it = scopeDefBuilder.getGlobal(); it.hasNext();) {
			final Node node = it.next().getValue();
			if (node.jjtGetNodeId() == FormExpressionParserTreeConstants.JJTFUNCTIONCLAUSENODE)
				node.jjtAccept(this);
		}
	}
	
	/**
	 * When a variable is declared, it is set to this value initially.
	 * @return The object to set.
	 */
	@Nonnull
	protected abstract T getNewObjectToSet();
	
	protected void visitIdentifierNameNode(@Nonnull final ASTIdentifierNameNode node)
			throws SemanticsException {
	}

	protected void enhancedForLoopIteratingLoopVariable(final ASTForLoopNode node, final T object) {
	}

	protected static enum Mode {
		NONE,
		INSIDE_WITH_CLAUSE_HEADER,
		INSIDE_FUNCTION_ARGUMENT;
	}

}