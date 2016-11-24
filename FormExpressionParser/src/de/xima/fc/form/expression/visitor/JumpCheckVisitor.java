package de.xima.fc.form.expression.visitor;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.exception.parse.IllegalJumpClauseException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ILabelled;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;

public class JumpCheckVisitor extends FormExpressionVoidDataVisitorAdapter<Set<String>, SemanticsException> {
	private int insideFunction = 0;
	private int nullLabel = 0;

	private JumpCheckVisitor() {
	}

	// Throw exception when encountering illegal jump, break, return.
	@Override
	public void visit(final ASTBreakClauseNode node, final Set<String> labelSet) throws SemanticsException {
		if (node.getLabel() == null ? nullLabel <= 0 : !labelSet.contains(node.getLabel()))
			throw new IllegalJumpClauseException(EJump.BREAK, node);
	}

	@Override
	public void visit(final ASTContinueClauseNode node, final Set<String> labelSet) throws SemanticsException {
		if (node.getLabel() == null ? nullLabel <= 0 : !labelSet.contains(node.getLabel()))
			throw new IllegalJumpClauseException(EJump.CONTINUE, node);
	}

	@Override
	public void visit(final ASTReturnClauseNode node, final Set<String> labelSet) throws SemanticsException {
		if (insideFunction <= 0)
			throw new IllegalJumpClauseException(EJump.RETURN, node);
	}

	// Check whether we are inside a function.
	@Override
	public void visit(final ASTFunctionClauseNode node, final Set<String> labelSet) throws SemanticsException {
		checkFunction(node);
	}

	@Override
	public void visit(final ASTFunctionNode node, final Set<String> labelSet) throws SemanticsException {
		checkFunction(node);
	}
	
	private void checkFunction(@Nonnull final Node node) throws SemanticsException {
		++insideFunction;
		final Set<String> newLabelSet = new HashSet<>();
		visitChildren(node, newLabelSet);
		newLabelSet.clear();
		--insideFunction;
	}

	// Add all labels currently in scope to the labelSet.
	@Override
	public void visit(final ASTForLoopNode node, final Set<String> labelSet) throws SemanticsException {
		visitLabelledNode(node, labelSet);
	}

	@Override
	public void visit(final ASTSwitchClauseNode node, final Set<String> labelSet) throws SemanticsException {
		visitLabelledNode(node, labelSet);
	}

	@Override
	public void visit(final ASTDoWhileLoopNode node, final Set<String> labelSet) throws SemanticsException {
		visitLabelledNode(node, labelSet);
	}

	@Override
	public void visit(final ASTWhileLoopNode node, final Set<String> labelSet) throws SemanticsException {
		visitLabelledNode(node, labelSet);
	}

	private <T extends ILabelled & Node> void visitLabelledNode(@Nonnull final T node, @Nonnull final Set<String> labelSet)
			throws SemanticsException {
		final String label = node.getLabel();
		if (label == null) {
			++nullLabel;
			visitChildren(node, labelSet);
			--nullLabel;
		}
		else {
			if (labelSet.contains(label))
				throw new DuplicateLabelException(node);
			labelSet.add(label);
			visitChildren(node, labelSet);
			labelSet.remove(label);
		}
	}

	public static void check(@Nonnull final Node node) throws SemanticsException {
		final JumpCheckVisitor v = new JumpCheckVisitor();
		final Set<String> labelSet = new HashSet<>();
		node.jjtAccept(v, labelSet);
		labelSet.clear();
	}
}