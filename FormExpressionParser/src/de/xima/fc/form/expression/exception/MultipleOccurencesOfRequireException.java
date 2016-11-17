package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.node.ASTScopeExternalNode;
import de.xima.fc.form.expression.util.NullUtil;

public class MultipleOccurencesOfRequireException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public MultipleOccurencesOfRequireException(@Nonnull final ASTScopeExternalNode node) {
		super(NullUtil.format("External scope %s was already required previously.", node.getScopeName()),
				node.getStartLine(), node.getStartColumn());
	}
}
