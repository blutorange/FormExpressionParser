package de.xima.fc.form.expression.impl.warning;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class EmptyStatementWarning extends GenericWarning {
	public EmptyStatementWarning(final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Warning.EMPTY_STATEMENT), node);
	}
}