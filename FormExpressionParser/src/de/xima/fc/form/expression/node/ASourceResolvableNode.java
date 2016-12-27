package de.xima.fc.form.expression.node;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;

@ParametersAreNonnullByDefault
public abstract class ASourceResolvableNode extends ANode implements ISourceResolvable {
	private static final long serialVersionUID = 1L;
	private String name = CmnCnst.NonnullConstant.STRING_EMPTY;
	/**
	 * Source of the variable. If >= 0, it is a variable on the heap, Otherwise,
	 * see {@link EVariableSource#getSourceId()}
	 */
	private int source = -1;
	private EVariableSource sourceType = EVariableSource.UNRESOLVED;

	public ASourceResolvableNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public ASourceResolvableNode(final Node prototype, final int nodeId) {
		super(prototype, nodeId);
	}

	@Override
	public final void resolveSource(final int source, final EVariableSource sourceType) throws IllegalVariableSourceResolutionException {
		if (isSourceResolved())
			throw new IllegalVariableSourceResolutionException(this, this, source);
		this.source = source;
		this.sourceType = sourceType;
	}
	
	@Override
	public void remapSource(final int source) {
		this.source = source;
	}
	
	@Override
	public void convertEnvironmentalToClosure() throws IllegalVariableSourceResolutionException {
		if (sourceType != EVariableSource.ENVIRONMENTAL)
			throw new IllegalVariableSourceResolutionException(this, this, sourceType.ordinal());
		sourceType = EVariableSource.CLOSURE;
	}

	@Override
	public final boolean isSourceResolved() {
		return sourceType.isResolved();
	}

	@Override
	public final int getSource() {
		return source;
	}
	
	@Override
	public final EVariableSource getSourceType() {
		return sourceType;
	}

	@Override
	public final String getVariableName() {
		return name;
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(name).append(',').append(source).append(',').append(sourceType).append(',');
	}

	public void init(@Nullable final EMethod method, final String name) throws ParseException {
		assertNonNull(name, CmnCnst.Error.VARIABLE_NODE_NULL_NAME);
		super.init(method);
		this.name = name;
	}
}