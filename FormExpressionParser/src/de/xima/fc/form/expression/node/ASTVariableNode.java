package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.EScopeSource;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTVariableNode extends ANode {
	private static final long serialVersionUID = 1L;

	public ASTVariableNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Nonnull
	private String name = CmnCnst.NonnullConstant.EMPTY_STRING;
	@Nullable
	private String scope;
	/**
	 * Source of the variable.
	 * If >= 0, it is a variable on the heap,
	 * Otherwise, see {@link EScopeSource#getSourceId()}
	 */
	private int source = EScopeSource.ID_UNRESOLVED;

	@Nonnull
	public String getVariableName() {
		return name;
	}

	/**
	 * @return The scope, or <code>null</code> when it is a local variable.
	 */
	@Nullable
	public String getScope() {
		return scope;
	}

	public boolean hasScope() {
		return scope != null;
	}

	public void init(@Nullable final EMethod method, @Nullable final String scope, @Nonnull final String name) throws ParseException {
		assertChildrenAtMost(1);
		assertNonNull(name, CmnCnst.Error.VARIABLE_NODE_NULL_NAME);
		super.init(method);
		this.name =  name;
		this.scope = scope;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(scope).append(',').append(name).append(',');
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data) throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	/**
	 * Does nothing when the variable is already scoped. Otherwise,
	 * it sets the scope to the provided scope.
	 * @param scope Scope to set.
	 */
	public void resolveScope(@Nonnull final String scope) {
		if (this.scope == null)
			this.scope = NullUtil.checkNotNull(scope);
	}

	public void resolveSource(final int source) {
		this.source = source;
	}
	
	public int getSource() {
		return source;
	}
	
	public boolean isOnHeap() {
		return source >= 0;
	}
}