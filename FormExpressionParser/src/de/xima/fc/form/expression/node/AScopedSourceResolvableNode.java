package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.parse.IllegalVariableResolutionException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableScopeResolutionException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IScopedSourceResolvable;

public abstract class AScopedSourceResolvableNode extends ASourceResolvableNode implements IScopedSourceResolvable {
	private static final long serialVersionUID = 1L;
	@Nullable
	private String scope;
	
	public AScopedSourceResolvableNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public AScopedSourceResolvableNode(@Nonnull final Node prototype, final int nodeId) {
		super(prototype, nodeId);
	}
	
	@Override
	public final void resolveSource(@Nonnull final EVariableSource source, @Nonnull final String scope)
			throws IllegalVariableResolutionException {
		resolveSource(source.sourceId, scope);
	}

	@Override
	public final void resolveSource(final int source, @Nonnull final String scope)
			throws IllegalVariableResolutionException {
		resolveSource(source);
		if (this.scope != null && !this.scope.equals(scope))
			throw new IllegalVariableScopeResolutionException(this, this, scope);
		this.scope = scope;
	}
	
	/**
	 * @return The scope, or <code>null</code> when it is a local variable.
	 */
	@Override
	@Nullable
	public final String getScope() {
		return scope;
	}
	
	@Override
	public final boolean hasScope() {
		return scope != null;
	}

	public void init(@Nullable final EMethod method, @Nullable final String scope, @Nonnull final String name) throws ParseException {
		super.init(method, name);
		this.scope = scope;
	}
	
	protected void setScope(@Nullable final String scope) {
		this.scope = scope;
	}
}