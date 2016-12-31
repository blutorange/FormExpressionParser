package de.xima.fc.form.expression.impl.variable;

import javax.annotation.concurrent.Immutable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@Immutable
@NonNullByDefault
public class HeaderNodeImpl implements IHeaderNode {
	private static final long serialVersionUID = 1L;

	private final Node node;
	private final String variableName;
	private int source = -1;
	private int closureSource = -1;
	private EVariableSource sourceType = EVariableSource.UNRESOLVED;

	/**
	 * A new header node with its value set to {@link NullLangObject}.
	 *
	 * @param variableName
	 *            Variable name.
	 */
	public HeaderNodeImpl(final String variableName, final Node prototype) {
		this.variableName = variableName;
		this.node = new ASTNullNode(prototype);
	}

	@Override
	public Node getHeaderValueNode() {
		return node;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public Node getTypeNode() {
		throw new FormExpressionException(CmnCnst.Error.GET_TYPE_NODE_CALLED_BUT_NO_TYPE_NODE_PRESENT);
	}

	@Override
	public boolean hasType() {
		return false;
	}

	@Override
	public void resolveSource(final int source, final EVariableSource sourceType) throws IllegalVariableSourceResolutionException {
		if (isBasicSourceResolved())
			throw new IllegalVariableSourceResolutionException(this, source);
		this.source = source;
		this.sourceType = sourceType;
	}

	@Override
	public void resolveClosureSource(final int closureSource) throws IllegalVariableSourceResolutionException {
		if (isClosureSourceResolved())
			throw new IllegalVariableSourceResolutionException(this, closureSource);
		this.closureSource = closureSource;
	}

	@Override
	public void convertEnvironmentalToClosure() throws IllegalVariableSourceResolutionException {
		if (sourceType != EVariableSource.ENVIRONMENTAL)
			throw new IllegalVariableSourceResolutionException(this, sourceType.ordinal());
		sourceType = EVariableSource.CLOSURE;
	}

	@Override
	public int getBasicSource() {
		return source;
	}

	@Override
	public int getClosureSource() {
		return closureSource;
	}

	@Override
	public EVariableSource getSourceType() {
		return sourceType;
	}

	@Override
	public boolean isBasicSourceResolved() {
		return source >= 0;
	}

	@Override
	public boolean isClosureSourceResolved() {
		return closureSource >= 0;
	}

	@Override
	public String getVariableName() {
		return variableName;
	}

	@Override
	public String toString() {
		return NullUtil.messageFormat(CmnCnst.ToString.HEADER_NODE_IMPL, variableName, Integer.valueOf(source));
	}

	@Override
	public Node getHeaderDeclarationNode() {
		return node;
	}
}