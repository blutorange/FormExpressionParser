package de.xima.fc.form.expression.visitor;

import java.util.Collection;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.iface.parse.IVariableTypeBuilder;
import de.xima.fc.form.expression.iface.parse.IVariableTyped;
import de.xima.fc.form.expression.impl.variable.DummyVariableTypeBuilder;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.impl.variable.VariableTypeBuilder;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTVariableTypeNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * Creates a symbol table and fills it with the type of each variable.
 * @author madgaksha
 */
@ParametersAreNonnullByDefault
public final class VariableTypeCollectVisitor
extends FormExpressionVoidDataVisitorAdapter<IVariableTypeBuilder, SemanticsException> {

	private final IVariableType[] table;

	public VariableTypeCollectVisitor(final int symbolTableSize) {
		table = new IVariableType[symbolTableSize];
	}

	@Override
	public void visit(final ASTVariableTypeNode node, final IVariableTypeBuilder builder) throws SemanticsException {
		builder.setBasicType(node.getVariableType());
		for (final EVariableTypeFlag flag : node.getFlags())
			if (flag != null)
				builder.setFlag(flag);
		for (int i = 0; i < node.getGenericsCount(); ++i) {
			final VariableTypeBuilder newBuilder = new VariableTypeBuilder();
			node.getGenericsNode(i).jjtAccept(this, newBuilder);
			final IVariableType type;
			try {
				type = newBuilder.build();
			}
			catch (final IllegalVariableTypeException e) {
				throw new SemanticsException(NullUtil.orEmpty(e.getMessage()), node);
			}
			builder.append(type);
		}
	}

	@Override
	public void visit(final ASTVariableDeclarationClauseNode node, final IVariableTypeBuilder builder)
			throws SemanticsException {
		visitTypedNode(node);
	}

	@Override
	public void visit(final ASTTryClauseNode node, final IVariableTypeBuilder builder) throws SemanticsException {
		visitTypedNode(node, SimpleVariableType.EXCEPTION);
		node.getTryNode().jjtAccept(this, builder);
		node.getCatchNode().jjtAccept(this, builder);
	}

	@Override
	public void visit(final ASTForLoopNode node, final IVariableTypeBuilder builder) throws SemanticsException {
		if (node.isEnhancedLoop()) {
			visitTypedNode(node);
			node.getEnhancedIteratorNode().jjtAccept(this, builder);
			node.getBodyNode().jjtAccept(this, builder);
		}
		else
			visitChildren(node, builder);
	}

	@Override
	public void visit(final ASTFunctionClauseNode node, final IVariableTypeBuilder builder) throws SemanticsException {
		builder.setBasicType(ELangObjectClass.FUNCTION);
		builder.append(getType(node));
		for (int i = node.getArgumentCount(); i-->0;) {
			IVariableType type = visitTypedNode(node.getArgResolvable(i));
			if (node.hasVarArgs() && i == node.getArgumentCount() - 1) {
				builder.setFlag(EVariableTypeFlag.VARARG);
				type = varArgParam(node, i);
			}
			builder.append(type);
		}
		node.getBodyNode().jjtAccept(this, builder);
	}

	@Override
	public void visit(final ASTFunctionNode node, final IVariableTypeBuilder builder) throws SemanticsException {
		for (int i = node.getArgumentCount(); i-->0;) {
			visitTypedNode(node.getArgResolvable(i));
			if (node.hasVarArgs() && i == node.getArgumentCount() - 1) {
				builder.setFlag(EVariableTypeFlag.VARARG);
				varArgParam(node, i);
			}
		}
		node.getBodyNode().jjtAccept(this, builder);
	}

	private <T extends IArgumentResolvable & Node> IVariableType varArgParam(final T node, final int i) throws SemanticsException, IllegalVariableTypeException {
		IVariableType type = table[node.getArgResolvable(i).getSource()];
		type = type != null ? type : SimpleVariableType.OBJECT;
		final IVariableType wrapped = GenericVariableType.forArray(type);
		resolveTypedNode(node.getArgResolvable(i), node, wrapped);
		return type;
	}

	private <T extends IVariableTyped & Node> IVariableType getType(final T typedNode) throws SemanticsException {
		return getType(typedNode, typedNode);
	}

	private IVariableType getType(final IVariableTyped typed, final Node node) throws SemanticsException {
		if (!typed.hasType()) {
			return SimpleVariableType.OBJECT;
		}
		final VariableTypeBuilder newBuilder = new VariableTypeBuilder();
		typed.getTypeNode().jjtAccept(this, newBuilder);
		try {
			return newBuilder.build();
		}
		catch (final IllegalVariableTypeException e) {
			throw new SemanticsException(NullUtil.orEmpty(e.getMessage()), node);
		}
	}

	private <T extends IVariableTyped & ISourceResolvable & Node> IVariableType visitTypedNode(
			final T typedResolvableNode) throws SemanticsException {
		final IVariableType type = getType(typedResolvableNode);
		return visitTypedNode(typedResolvableNode, type);
	}

	private <T extends ISourceResolvable & Node> IVariableType visitTypedNode(final T resolvableNode,
			final IVariableType type) throws SemanticsException {
		return resolveTypedNode(resolvableNode, resolvableNode, type);
	}

	private IVariableType resolveTypedNode(final ISourceResolvable resolvable, final Node node,
			final IVariableType type) throws SemanticsException {
		final int source = resolvable.getSource();
		if (source < 0)
			throw new SemanticsException(CmnCnst.Error.EXTERNAL_SOURCE_FOR_MANUAL_VARIABLE, node);
		return table[source] = type;
	}

	private IVariableType[] getTable() {
		return table;
	}

	private void visitHeaderNode(final IHeaderNode header) throws SemanticsException {
		final IVariableType type;
		if (header.hasType())
			type = getType(header, header.getNode());
		else
			type = SimpleVariableType.OBJECT;
		resolveTypedNode(header, header.getNode(), type);
		header.getNode().jjtAccept(this, DummyVariableTypeBuilder.INSTANCE);
	}

	private void visitScopeDefs(final IScopeDefinitions scopeDefs) throws SemanticsException {
		for (final IHeaderNode header : scopeDefs.getGlobal()) {
			if (header != null)
				visitHeaderNode(header);
		}
		for (final Collection<IHeaderNode> coll : scopeDefs.getManual().values())
			for (final IHeaderNode header : coll)
				if (header != null)
					visitHeaderNode(header);
	}

	public static IVariableType[] collect(final Node node, final int symbolTableSize, final IScopeDefinitions scopeDefs)
					throws SemanticsException {
		final VariableTypeCollectVisitor v = new VariableTypeCollectVisitor(symbolTableSize);
		v.visitScopeDefs(scopeDefs);
		node.jjtAccept(v, DummyVariableTypeBuilder.INSTANCE);
		return v.getTable();
	}
}