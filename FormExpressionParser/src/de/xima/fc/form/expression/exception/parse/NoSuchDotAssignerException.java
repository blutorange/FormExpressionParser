package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class NoSuchDotAssignerException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public NoSuchDotAssignerException(final IVariableType thisContext, final String property, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_DOT_ASSIGNER, property, thisContext), node);
		this.property = property;
	}
	public final String property;
}