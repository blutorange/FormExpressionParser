package de.xima.fc.form.expression.visitor;

import java.util.Collection;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.parse.MissingVariableTypeDeclarationException;
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

public final class VariableTypeCollectVisitor
		extends FormExpressionVoidDataVisitorAdapter<IVariableTypeBuilder, SemanticsException> {

	private final IVariableType[] table;
	private final boolean treatMissingTypeAsError;

	public VariableTypeCollectVisitor(final int symbolTableSize, final boolean treatMissingTypeAsError) {
		table = new IVariableType[symbolTableSize];
		this.treatMissingTypeAsError = treatMissingTypeAsError;
	}

	@Override
	public void visit(final ASTVariableTypeNode node, final IVariableTypeBuilder builder) throws SemanticsException {
		builder.setBasicType(node.getVariableType());
		for (int i = 0; i < node.getGenericsCount(); ++i) {
			final VariableTypeBuilder newBuilder = new VariableTypeBuilder();
			node.getGenericsNode(i).jjtAccept(this, newBuilder);
			final IVariableType type;
			try {
				type = newBuilder.build();
			}
			catch (final IllegalStateException e) {
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
		visitTypedNode(node, new SimpleVariableType(ELangObjectType.EXCEPTION));
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
		// TODO varArgs -> array
		// TODO set type for *this* variable
		// TODO type for functions: function foo(){return 42;};foo();
		for (int i = node.getArgumentCount(); i-->0;) {
			visitTypedNode(node.getArgResolvable(i));
			if (node.hasVarArgs() && i == node.getArgumentCount() - 1)
				wrapVarArgs(node, i);
		}
		node.getBodyNode().jjtAccept(this, builder);
	}

	@Override
	public void visit(final ASTFunctionNode node, final IVariableTypeBuilder builder) throws SemanticsException {
		// TODO varArgs -> array
		// TODO set type for *this* variable
		for (int i = node.getArgumentCount(); i-->0;) {
			visitTypedNode(node.getArgResolvable(i));
			if (node.hasVarArgs() && i == node.getArgumentCount() - 1)
				wrapVarArgs(node, i);
		}
		node.getBodyNode().jjtAccept(this, builder);
	}

	private void wrapVarArgs(final IArgumentResolvable node, final int i) {
		final int source = node.getArgResolvable(i).getSource();
		final VariableTypeBuilder b = new VariableTypeBuilder();
		b.setBasicType(ELangObjectType.ARRAY);
		final IVariableType type = table[source];
		b.append(type != null ? type : new SimpleVariableType(ELangObjectType.NULL));
		table[source] = b.build();
	}

	@Nonnull
	private <T extends IVariableTyped & Node> IVariableType getType(@Nonnull final T typedNode) throws SemanticsException {
		final IVariableType type;
		if (!typedNode.hasType()) {
			type = new SimpleVariableType(ELangObjectType.NULL);
		}
		else {
			final VariableTypeBuilder newBuilder = new VariableTypeBuilder();
			typedNode.getTypeNode().jjtAccept(this, newBuilder);
			try {
				type = newBuilder.build();
			}
			catch (final IllegalStateException e) {
				throw new SemanticsException(NullUtil.orEmpty(e.getMessage()), typedNode);
			}
		}
		return type;
	}
	
	private <T extends IVariableTyped & ISourceResolvable & Node, S extends ISourceResolvable & Node> void visitTypedNode(
			@Nonnull final T typedResolvableNode) throws SemanticsException {
		final IVariableType type = getType(typedResolvableNode);
		visitTypedNode(typedResolvableNode, type);
	}

	private <T extends ISourceResolvable & Node> void visitTypedNode(@Nonnull final T resolvableNode,
			@Nonnull final IVariableType type) throws SemanticsException {
		visitTypedNode(resolvableNode, resolvableNode, type);
	}
	
	private void visitTypedNode(@Nonnull final ISourceResolvable resolvable, @Nonnull final Node node,
			@Nonnull final IVariableType type) throws SemanticsException {
		if (type.getBasicLangType() == ELangObjectType.NULL && treatMissingTypeAsError)
			throw new MissingVariableTypeDeclarationException(resolvable, node);
		final int source = resolvable.getSource();
		if (source < 0)
			throw new SemanticsException(CmnCnst.Error.EXTERNAL_SOURCE_FOR_MANUAL_VARIABLE, node);
		table[source] = type;
	}

	private IVariableType[] getTable() {
		return table;
	}


	private void visitHeaderNode(final IHeaderNode header) throws SemanticsException {
		final Node typedNode = header.getType();
		final IVariableType type;
		if (typedNode == null)
			type = new SimpleVariableType(ELangObjectType.NULL);
		else {
			//TODO if function, wrap in method<ret,arg1,...>
			final IVariableTypeBuilder builder = new VariableTypeBuilder();
			typedNode.jjtAccept(this, builder);
			try {
				type = builder.build();
			}
			catch (final IllegalStateException e) {
				throw new SemanticsException(NullUtil.orEmpty(e.getMessage()), header.getNode());
			}
		}
		visitTypedNode(header, header.getNode(), type);
		if (header.hasNode())
			header.getNode().jjtAccept(this, DummyVariableTypeBuilder.INSTANCE);
	}

	private void visitScopeDefs(final IScopeDefinitions scopeDefs) throws SemanticsException {
		for (final IHeaderNode header : scopeDefs.getGlobal()) {
			visitHeaderNode(header);
		}
		for (final Collection<IHeaderNode> coll : scopeDefs.getManual().values())
			for (final IHeaderNode header : coll)
				visitHeaderNode(header);
	}

	public static IVariableType[] collect(@Nonnull final Node node, final int symbolTableSize,
			@Nonnull final IScopeDefinitions scopeDefs, final boolean treatMissingTypeAsError)
			throws SemanticsException {
		final VariableTypeCollectVisitor v = new VariableTypeCollectVisitor(symbolTableSize, treatMissingTypeAsError);
		v.visitScopeDefs(scopeDefs);
		node.jjtAccept(v, DummyVariableTypeBuilder.INSTANCE);
		return v.getTable();
	}
}