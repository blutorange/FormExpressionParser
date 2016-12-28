package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class MissingRequireScopeStatementException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public MissingRequireScopeStatementException(final String scope, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.MISSING_REQUIRE_SCOPE_STATEMENT, scope), node);
		this.scope = scope;
	}

	public final String scope;
}
