package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.enums.ESeverityOption.TREAT_SCOPED_FUNCTION_OUTSIDE_HEADER_AS_ERROR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.exception.parse.DuplicateRequireScopeDeclarationException;
import de.xima.fc.form.expression.exception.parse.DuplicateScopedVariableDeclarationException;
import de.xima.fc.form.expression.exception.parse.FunctionNameAlreadyDefinedException;
import de.xima.fc.form.expression.exception.parse.ManualScopeAlreadyRequiredException;
import de.xima.fc.form.expression.exception.parse.NoSuchScopeException;
import de.xima.fc.form.expression.exception.parse.ScopedFunctionOutsideHeaderException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.impl.ImmutableScopeDefinitions;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTScopeExternalNode;
import de.xima.fc.form.expression.node.ASTScopeGlobalNode;
import de.xima.fc.form.expression.node.ASTScopeManualNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;

@NonNullByDefault
public final class ScopeCollectVisitor
		extends FormExpressionVoidDataVisitorAdapter<Optional<Map<String, IHeaderNode>>, SemanticsException>
		implements IScopeDefinitionsBuilder {
	@Nullable
	private String currentScope;
	@Nullable
	private List<Node> detachQueue;

	private final Map<String, IHeaderNode> globalMap;
	private final Set<String> requiredSet;
	protected final Map<String, Map<String, IHeaderNode>> manualMap;
	private final IEvaluationContextContract<?> factory;
	private final ISeverityConfig config;

	public static IScopeDefinitionsBuilder collect(final Node node,
			final IEvaluationContextContract<?> factory, final ISeverityConfig config)
					throws ParseException {
		final ScopeCollectVisitor v = new ScopeCollectVisitor(config, factory);
		node.jjtAccept(v, Optional.<Map<String, IHeaderNode>> absent());
		v.currentScope = null;
		v.detachNodes();
		return v;
	}

	private ScopeCollectVisitor(final ISeverityConfig config,
			final IEvaluationContextContract<?> factory) {
		globalMap = new HashMap<>();
		requiredSet = new HashSet<>();
		manualMap = new HashMap<>();
		this.factory = factory;
		this.config = config;
	}

	private void detachNodes() throws ParseException {
		for (final Node node : getDetachQueue())
			node.detach();
		getDetachQueue().clear();
	}

	private boolean provides(final String scope) {
		return factory.getLibraryFactory().isProvidingScope(scope) || factory.getExternalFactory().isProvidingScope(scope);
	}

	private List<Node> getDetachQueue() {
		return detachQueue != null ? detachQueue : (detachQueue = new ArrayList<>());
	}

	@Override
	public void visit(final ASTScopeExternalNode node, final Optional<Map<String, IHeaderNode>> mapToAddTo)
			throws SemanticsException {
		// Throw an error when the scope has already been required.
		// Otherwise, add it to the list of required scopes.
		if (!provides(node.getScopeName()))
			throw new NoSuchScopeException(node.getScopeName(), node);
		if (requiredSet.contains(node.getScopeName()) || manualMap.containsKey(node.getScopeName()))
			throw new DuplicateRequireScopeDeclarationException(node);
		requiredSet.add(node.getScopeName());
		getDetachQueue().add(node);
	}

	@Override
	public void visit(final ASTScopeManualNode node, final Optional<Map<String, IHeaderNode>> mapToAddTo)
			throws SemanticsException {
		// Throw an error when the scope has already been required.
		// Otherwise, add the scope and the variable.
		currentScope = node.getScopeName();
		if (requiredSet.contains(currentScope))
			throw new ManualScopeAlreadyRequiredException(node);
		Map<String, IHeaderNode> map = manualMap.get(currentScope);
		if (map == null) {
			map = new HashMap<>();
			manualMap.put(node.getScopeName(), map);
		}
		visitChildren(node, Optional.of(map));
		currentScope = null;
		getDetachQueue().add(node);
	}

	@Override
	public void visit(final ASTScopeGlobalNode node, final Optional<Map<String, IHeaderNode>> mapToAddTo)
			throws SemanticsException {
		currentScope = null;
		visitChildren(node, Optional.of(globalMap));
		getDetachQueue().add(node);
	}

	@Override
	public void visit(final ASTVariableDeclarationClauseNode node, final Optional<Map<String, IHeaderNode>> mapToAddTo)
			throws SemanticsException {
		// Add the node to a manual or global scope.
		if (mapToAddTo.isPresent()) {
			final Map<String, IHeaderNode> map = mapToAddTo.get();
			visitChildren(node, Optional.<Map<String, IHeaderNode>> absent());
			if (map.containsKey(node.getVariableName()))
				throw new DuplicateScopedVariableDeclarationException(node, currentScope);
			makeDefaultAssignmentNode(node);
			map.put(node.getVariableName(), node);
		}
	}

	private void makeDefaultAssignmentNode(final ASTVariableDeclarationClauseNode node) throws SemanticsException {
		// Create default node for default value.
		if (!node.hasAssignment()) {
			final Node assignmentNode = node.getLangObjectClass().makeDefaultNode(node);
			node.addAssignmentNode(assignmentNode != null ? assignmentNode : new ASTNullNode(node));
		}
	}

	@Override
	public void visit(final ASTFunctionClauseNode node, final Optional<Map<String, IHeaderNode>> mapToAddTo)
			throws SemanticsException {
		// Put function declaration at the top inside the global declaration.
		if (mapToAddTo.isPresent()) {
			// Function inside scope (global / manual)
			final Map<String, IHeaderNode> map = mapToAddTo.get();
			if (map.containsKey(node.getVariableName()))
				throw new DuplicateScopedVariableDeclarationException(currentScope, node.getVariableName(), node);
			node.supplyScope(currentScope);
			map.put(node.getVariableName(), node);
		}
		else {
			// Add function outside scope to the corresponding scope
			final String scope = node.getScope();
			if (scope == null) {
				if (hasGlobal(node.getVariableName()))
					throw new FunctionNameAlreadyDefinedException(node);
				addGlobal(node.getVariableName(), node);
			}
			else {
				if (hasManual(scope) && hasManual(scope, node.getVariableName()))
					throw new FunctionNameAlreadyDefinedException(node);
				if (config.hasOption(TREAT_SCOPED_FUNCTION_OUTSIDE_HEADER_AS_ERROR))
					throw new ScopedFunctionOutsideHeaderException(node);
				addManual(scope, node.getVariableName(), node);
			}
		}
		// Collect everything from function bodies as well.
		node.getBodyNode().jjtAccept(this, Optional.<Map<String, IHeaderNode>> absent());
		getDetachQueue().add(node);
	}

	@Override
	public boolean hasGlobal(final String name) {
		return globalMap.containsKey(name);
	}

	@Override
	public boolean hasManual(final String scope, final String name) {
		final Map<String, ?> m = manualMap.get(scope);
		return m == null ? false : m.containsKey(scope);
	}

	@Override
	public boolean hasManual(final String scope) {
		return manualMap.containsKey(scope);
	}

	@Override
	public boolean hasExternal(final String name) {
		return requiredSet.contains(name);
	}

	@Override
	public void addGlobal(final String name, final IHeaderNode node) {
		globalMap.put(name, node);
	}

	@Override
	public void addGlobal(final Map<String, IHeaderNode> entries) {
		globalMap.putAll(entries);
	}

	@Override
	public void addExternal(final String name) {
		requiredSet.add(name);
	}

	@Override
	public void addExternal(final Collection<String> names) {
		requiredSet.addAll(names);
	}

	@Override
	public void addManual(final String scope, final String name, final IHeaderNode node) {
		Map<String, IHeaderNode> m = manualMap.get(scope);
		if (m == null) {
			m = new HashMap<>();
			manualMap.put(scope, m);
		}
		m.put(name, node);
	}

	@Override
	public void addManual(final String scope, final Map<String, IHeaderNode> entries) {
		Map<String, IHeaderNode> m = manualMap.get(scope);
		if (m == null) {
			m = new HashMap<>();
			manualMap.put(scope, m);
		}
		m.putAll(entries);
	}

	@Nullable
	@Override
	public IHeaderNode getGlobal(final String name) {
		return globalMap.get(name);
	}

	@Nullable
	@Override
	public IHeaderNode getManual(final String scope, final String name) {
		final Map<String, IHeaderNode> m = manualMap.get(scope);
		return m != null ? m.get(name) : null;
	}

	@Override
	public Map<String, IHeaderNode> getGlobal() {
		return globalMap;
	}

	@Override
	public Map<String, Map<String, IHeaderNode>> getManual() {
		return manualMap;
	}

	@Override
	public Set<String> getExternal() {
		return requiredSet;
	}

	@Override
	public IScopeDefinitions build() {
		return new ImmutableScopeDefinitions(globalMap, manualMap, requiredSet);
	}
}