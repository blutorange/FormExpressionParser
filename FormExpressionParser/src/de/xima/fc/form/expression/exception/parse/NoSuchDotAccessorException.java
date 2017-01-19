package de.xima.fc.form.expression.exception.parse;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class NoSuchDotAccessorException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public NoSuchDotAccessorException(final IVariableType thisContext, final String property,
			final IVariableType[] dotGenerics, final Node node) {
		super(getMessage(thisContext, property, dotGenerics), node);
		this.property = property;
		this.dotGenerics = dotGenerics;
	}

	private static String getMessage(final IVariableType thisContext, final String property,
			final IVariableType[] dotGenerics) {
		if (dotGenerics.length > 0)
			return NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_DOT_ACCESSOR_WITH_GENERICS,
					StringUtils.join(dotGenerics,','), property, thisContext);
		return NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_DOT_ACCESSOR_WITHOUT_GENERICS, property, thisContext);
	}

	public final String property;
	public final IVariableType[] dotGenerics;
}