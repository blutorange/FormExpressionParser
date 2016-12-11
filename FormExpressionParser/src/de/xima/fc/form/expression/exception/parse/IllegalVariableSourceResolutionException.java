package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IllegalVariableSourceResolutionException extends IllegalVariableResolutionException {
	private static final long serialVersionUID = 1L;

	public IllegalVariableSourceResolutionException(final IHeaderNode header, final int newSource) {
		this(header, header.getNode(), newSource);
	}

	public IllegalVariableSourceResolutionException(final ISourceResolvable resolvable, final Node node,
			final int newSource) {
		super(NullUtil.messageFormat(CmnCnst.Error.VARIABLE_SOURCE_ALREADY_RESOLVED, resolvable.getVariableName(),
				newSource, resolvable.getSource()), resolvable, node);
		this.newSource = newSource;
		this.oldSource = resolvable.getSource();
	}

	public final int oldSource;
	public final int newSource;
}