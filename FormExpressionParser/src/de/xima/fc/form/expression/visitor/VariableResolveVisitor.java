package de.xima.fc.form.expression.visitor;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.parse.IllegalExternalScopeAssignmentException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;
import de.xima.fc.form.expression.exception.parse.MissingRequireScopeStatementException;
import de.xima.fc.form.expression.exception.parse.NoSuchScopeException;
import de.xima.fc.form.expression.exception.parse.ScopeMissingVariableException;
import de.xima.fc.form.expression.exception.parse.VariableNotResolvableException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;

public class VariableResolveVisitor extends AVariableBindingVisitor<Integer> {
	@Nonnull
	private final IScopeDefinitionsBuilder scopeDefBuilder;
	@Nonnull
	private final IEvaluationContextContractFactory<?> contractFactory;
	@Nonnull
	private final Stack<String> defaultScopeStack = new Stack<String>();

	private final boolean treatMissingRequireScopeAsError;
	private int symbolTableSize = 0;

	private VariableResolveVisitor(final @Nonnull IScopeDefinitionsBuilder scopeDefBuilder,
			final @Nonnull IEvaluationContextContractFactory<?> contractFactory,
			final boolean treatMissingRequireScopeAsError) {
		this.scopeDefBuilder = scopeDefBuilder;
		this.contractFactory = contractFactory;
		this.treatMissingRequireScopeAsError = treatMissingRequireScopeAsError;
	}

	private boolean resolveVariableWithScope(@Nonnull final String scope, @Nonnull final String name,
			final @Nonnull ASTVariableNode node, final boolean doThrow) throws ParseException {
		// First, look it up in manually defined scopes.
		// Otherwise, try external scopes, including those from the external
		// context.

		// Try manual scope.
		if (scopeDefBuilder.hasManual(scope)) {
			final IHeaderNode header = scopeDefBuilder.getManual(scope, name);
			if (header != null) {
				if (!header.isResolved())
					// Should not happen as these variables are resolved
					// earlier.
					throw new VariableNotResolvableException(node);
				node.resolveSource(header.getSource(), scope);
				return true;
			}
			else {
				if (doThrow)
					throw new ScopeMissingVariableException(scope, name, node);
				return false;
			}
		}

		// Try external scope.
		final IScopeInfo info = contractFactory.getExternalScopeInfo(scope);
		if (info != null) {
			if (!info.isProviding(name)) {
				if (doThrow)
					throw new ScopeMissingVariableException(scope, name, node);
				return false;
			}
			if (!scopeDefBuilder.hasExternal(scope) && treatMissingRequireScopeAsError) {
				if (doThrow)
					throw new MissingRequireScopeStatementException(scope, node);
				return false;
			}
			scopeDefBuilder.addExternal(scope);
			node.resolveSource(info.getSource(), scope);
			return true;
		}

		// Give up.
		if (doThrow)
			throw new NoSuchScopeException(scope, node);
		return false;
	}

	@Override
	public void visit(final ASTVariableNode node) throws ParseException {
		// First, check if variable exists locally.
		// If not, check if it exists globally.
		// If not, check if it exists within any default scope introduced via
		// with(...){}.
		// If not, check if it exists within any default scoped introduced by
		// the current embedment.
		final String scope = node.getScope();
		final String name = node.getVariableName();

		// If scope is defined, we check if variable is defined there and done.
		if (scope != null) {
			resolveVariableWithScope(scope, node.getVariableName(), node, true);
			return;
		}

		// Check if variable exists locally, using the binding.
		final Integer source = binding.getVariable(name);
		if (source != null) {
			node.resolveSource(source.intValue());
			return;
		}

		// Check if variable exists globally.
		final IHeaderNode header = scopeDefBuilder.getGlobal(name);
		if (header != null) {
			if (!header.isResolved())
				throw new VariableNotResolvableException(node);
			node.resolveSource(header.getSource());
			return;
		}

		// Check if variable exists within any scope introduced by with(...){}
		for (final String defaultScope : defaultScopeStack) {
			if (defaultScope != null && resolveVariableWithScope(defaultScope, name, node, false))
				return;
		}

		// Check if variable exists within any scope introduced by the current
		// embedment.
		final String embedment = node.getEmbedment();
		final String[] embedmentScopeList = embedment != null ? contractFactory.getScopesForEmbedment(embedment)
				: CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
		for (final String defaultScope : embedmentScopeList) {
			if (defaultScope != null && resolveVariableWithScope(defaultScope, name, node, false))
				return;
		}

		throw new VariableNotResolvableException(node);
	}

	//
	// Getting the default scopes.
	//
	@Override
	public void visit(final ASTWithClauseNode node) throws ParseException {
		try {
			for (int i = node.getScopeCount(); i-->0;)
				defaultScopeStack.push(node.getScope(i));
			node.getBodyNode().jjtAccept(this);
		}
		finally {
			for (int i = node.getScopeCount(); i-- > 0; --i)
				defaultScopeStack.pop();
		}
	}
	
	@Override
	public void visit(final ASTAssignmentExpressionNode node) throws ParseException {
		// Resolve all variables first.
		visitChildren(node);
		// Now check whether we assign to any variable from an external scope.
		for (int i = 0; i < node.getAssignableNodeCount(); ++i) {
			final ASTVariableNode var = node.getAssignableNode(i).getAsOrNull(ASTVariableNode.class);
			if (var != null) {
				if (var.getSource() == EVariableSource.ID_EXTERNAL_CONTEXT
						|| var.getSource() == EVariableSource.ID_LIBRARY) {
					throw new IllegalExternalScopeAssignmentException(var);
				}
			}
		}
	}

	@Nonnull
	private Integer getNewObjectToSet() {
		return symbolTableSize++;
	}

	@Override
	protected Integer getNewObjectToSet(final ISourceResolvable res) throws IllegalVariableSourceResolutionException {
		final Integer object = getNewObjectToSet();
		res.resolveSource(object);
		return object;
	}

	// TODO mark unused variables
	
	public static int resolve(final Node node, final @Nonnull IScopeDefinitionsBuilder scopeDefBuilder,
			@Nonnull final IEvaluationContextContractFactory<?> contractFactory,
			final boolean treatMissingRequireScopeAsError) throws ParseException {
		final VariableResolveVisitor v = new VariableResolveVisitor(scopeDefBuilder, contractFactory,
				treatMissingRequireScopeAsError);
		v.resolveScopeDefs(scopeDefBuilder);
		v.bindScopeDefValues(scopeDefBuilder);
		node.jjtAccept(v);
		v.binding.reset();
		return v.symbolTableSize;
	}

	private void resolveScopeDefs(final IScopeDefinitionsBuilder scopeDefBuilder)
			throws IllegalVariableSourceResolutionException {
		// Global.
		for (final Iterator<Entry<String, IHeaderNode>> it = scopeDefBuilder.getGlobal(); it.hasNext();) {
			final IHeaderNode header = it.next().getValue();
			header.resolveSource(getNewObjectToSet());
		}
		// Manual scopes.
		for (final Iterator<String> it = scopeDefBuilder.getManual(); it.hasNext();) {
			final String scope = it.next();
			if (scope != null) {
				final Iterator<Entry<String, IHeaderNode>> it2 = scopeDefBuilder.getManual(scope);
				if (it2 != null) {
					while (it2.hasNext()) {
						it2.next().getValue().resolveSource(getNewObjectToSet());
					}
				}
			}
		}
	}
}