package de.xima.fc.form.expression.visitor;

import java.util.Stack;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.map.MultiKeyMap;

import de.xima.fc.form.expression.exception.parse.MissingRequireScopeStatementException;
import de.xima.fc.form.expression.exception.parse.NoSuchScopeException;
import de.xima.fc.form.expression.exception.parse.ScopeMissingVariableException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.exception.parse.VariableNotResolvableException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTIdentifierNameNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;

public class VariableResolveVisitor extends AVariableBindingVisitor<Integer> {
	@Nonnull
	private final IScopeDefinitionsBuilder scopeDefBuilder;
	@Nonnull
	private final IEvaluationContextContractFactory<?> contractFactory;

	@Nonnull
	private final MultiKeyMap<String, Integer> scopeSourceMap = new MultiKeyMap<>();
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
		if (scopeDefBuilder.hasManual(scope)) {
			if (scopeDefBuilder.hasManual(scope, name)) {
				node.resolveScope(scope);
				Integer scopeSource = scopeSourceMap.get(scope, name);
				if (scopeSource == null) {
					scopeSource = symbolTableSize++;
					scopeSourceMap.put(scope, name, symbolTableSize++);
				}
				node.resolveSource(scopeSource);
				return true;
			} else {
				if (doThrow)
					throw new ScopeMissingVariableException(scope, name, node);
				return false;
			}
		}

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
			node.resolveScope(scope);
			node.resolveSource(info.getSource().getSourceId());
			return true;
		}

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

		// Check if variable exists locally.
		if (scopeDefBuilder.hasGlobal(name)) {
			Integer scopeSource = scopeSourceMap.get(null, name);
			if (scopeSource == null) {
				scopeSource = symbolTableSize++;
				scopeSourceMap.put(null, name, symbolTableSize++);
			}
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
		mode = Mode.INSIDE_WITH_CLAUSE_HEADER;
		try {
			for (int i = 0; i < node.getScopeCount(); ++i)
				node.getScopeNode(i).jjtAccept(this);
			node.getBodyNode().jjtAccept(this);
		} finally {
			mode = Mode.NONE;
			for (int i = node.getScopeCount(); i-- > 0; --i)
				defaultScopeStack.pop();
		}
	}

	@Override
	public void visitIdentifierNameNode(final ASTIdentifierNameNode node) throws SemanticsException {
		if (mode == Mode.INSIDE_WITH_CLAUSE_HEADER)
			defaultScopeStack.push(node.getName());
	}

	@Override
	protected Integer getNewObjectToSet() {
		return symbolTableSize++;
	}
	
	@Override
	protected void enhancedForLoopIteratingLoopVariable(final ASTForLoopNode node, final Integer object) {
		node.resolveSource(object);
	}

	// TODO check for illegal assignment of external scoped variables, eg field::tf1 = 8;
	
	public static int resolve(final Node node, final @Nonnull IScopeDefinitionsBuilder scopeDefBuilder,
			@Nonnull final IEvaluationContextContractFactory<?> contractFactory,
			final boolean treatMissingRequireScopeAsError) throws ParseException {
		final VariableResolveVisitor v = new VariableResolveVisitor(scopeDefBuilder, contractFactory,
				treatMissingRequireScopeAsError);
		v.resolveFunctions(scopeDefBuilder);
		node.jjtAccept(v);
		v.binding.reset();
		return v.symbolTableSize;
	}	
}