package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;

public abstract class ASourceResolvableNode extends ANode implements ISourceResolvable {
	private static final long serialVersionUID = 1L;
	@Nonnull
	private String name = CmnCnst.NonnullConstant.STRING_EMPTY;
	/**
	 * Source of the variable. If >= 0, it is a variable on the heap, Otherwise,
	 * see {@link EVariableSource#getSourceId()}
	 */
	private int source = EVariableSource.ID_UNRESOLVED;

	public ASourceResolvableNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public ASourceResolvableNode(@Nonnull final Node prototype, final int nodeId) {
		super(prototype, nodeId);
	}

	@Override
	public final void resolveSource(final int source) throws IllegalVariableSourceResolutionException {
		if (this.source != EVariableSource.ID_UNRESOLVED && this.source != source)
			throw new IllegalVariableSourceResolutionException(this, this, source);
		this.source = source;
	}

	@Override
	public final boolean isResolved() {
		return source != EVariableSource.ID_UNRESOLVED;
	}

	@Override
	public final int getSource() {
		return source;
	}

	@Override
	public final String getVariableName() {
		return name;
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(name).append(',').append(source).append(',');
	}

	public void init(@Nullable final EMethod method, @Nonnull final String name) throws ParseException {
		assertNonNull(name, CmnCnst.Error.VARIABLE_NODE_NULL_NAME);
		super.init(method);
		this.name = name;
	}
}