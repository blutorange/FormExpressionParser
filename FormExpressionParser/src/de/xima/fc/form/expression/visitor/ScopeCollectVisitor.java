package de.xima.fc.form.expression.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.parse.DuplicateRequireScopeDeclarationException;
import de.xima.fc.form.expression.exception.parse.DuplicateScopedVariableDeclarationException;
import de.xima.fc.form.expression.exception.parse.FunctionNameAlreadyDefinedException;
import de.xima.fc.form.expression.exception.parse.ManualScopeAlreadyRequiredException;
import de.xima.fc.form.expression.exception.parse.ScopedFunctionOutsideHeaderException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.impl.ImmutableScopeDefinitions;
import de.xima.fc.form.expression.impl.variable.HeaderNodeImpl;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTScopeExternalNode;
import de.xima.fc.form.expression.node.ASTScopeGlobalNode;
import de.xima.fc.form.expression.node.ASTScopeManualNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;

public class ScopeCollectVisitor extends FormExpressionVoidVoidVisitorAdapter<SemanticsException>
implements IScopeDefinitionsBuilder {
	@Nullable
	private String currentScope;
	@Nullable
	private Map<String,IHeaderNode> currentMap;
	@Nullable
	private List<Node> detachQueue;

	@Nonnull
	private final Map<String, IHeaderNode> globalMap;
	@Nonnull
	private final Set<String> requiredSet;
	@Nonnull
	private final Map<String, Map<String, IHeaderNode>> manualMap;

	private final boolean treatScopedFunctionOutsideHeaderAsError;

	@Nonnull
	public static IScopeDefinitionsBuilder collect(final Node node, final boolean strictMode) throws ParseException {
		final ScopeCollectVisitor v = new ScopeCollectVisitor(strictMode);
		node.jjtAccept(v);
		v.currentMap = null;
		v.currentScope = null;
		v.detachNodes();
		return v;
	}

	private ScopeCollectVisitor(final boolean treatScopedFunctionOutsideHeaderAsError) {
		globalMap = new HashMap<>();
		requiredSet = new HashSet<>();
		manualMap = new HashMap<>();
		this.treatScopedFunctionOutsideHeaderAsError = treatScopedFunctionOutsideHeaderAsError;
	}

	private void detachNodes() throws ParseException {
		for (final Node node : getDetachQueue())
			node.detach();
		getDetachQueue().clear();
	}

	@Nonnull
	private List<Node> getDetachQueue() {
		return detachQueue != null ? detachQueue  : (detachQueue = new ArrayList<>());
	}

	@Override
	public void visit(final ASTScopeExternalNode node) throws SemanticsException {
		// Throw an error when the scope has already been required.
		// Otherwise, add it to the list of required scopes.
		if (requiredSet.contains(node.getScopeName()) || manualMap.containsKey(node.getScopeName()))
			throw new DuplicateRequireScopeDeclarationException(node);
		requiredSet.add(node.getScopeName());
		getDetachQueue().add(node);
	}

	@Override
	public void visit(final ASTScopeManualNode node) throws SemanticsException {
		// Throw an error when the scope has already been required.
		// Otherwise, add the scope and the variable.
		currentScope = node.getScopeName();
		if (requiredSet.contains(currentScope))
			throw new ManualScopeAlreadyRequiredException(node);
		@Nullable Map<String,IHeaderNode> map = manualMap.get(currentScope);
		if (map == null) {
			map = new HashMap<String, IHeaderNode>();
			manualMap.put(node.getScopeName(), map);
		}
		currentMap = map;
		visitChildren(node);
		currentMap = null;
		currentScope = null;
		getDetachQueue().add(node);
	}

	@Override
	public void visit(final ASTScopeGlobalNode node) throws SemanticsException {
		currentScope = null;
		currentMap = globalMap;
		visitChildren(node);
		currentMap = null;
		getDetachQueue().add(node);
	}

	@Override
	public void visit(final ASTVariableDeclarationClauseNode node) throws SemanticsException {
		// Add the node to a manual or global scope.
		final Map<String, IHeaderNode> map = currentMap;
		if (map != null) {
			if (map.containsKey(node.getVariableName()))
				throw new DuplicateScopedVariableDeclarationException(node, currentScope);
			map.put(node.getVariableName(), new HeaderNodeImpl(node));
		}
	}

	@Override
	public void visit(final ASTFunctionClauseNode node) throws SemanticsException {
		// Put function declaration at the top inside the global declaration.
		final Map<String, IHeaderNode> map = currentMap;
		if (map != null) {
			// Function inside scope (global / manual)
			if (map.containsKey(node.getVariableName()))
				throw new DuplicateScopedVariableDeclarationException(currentScope, node.getVariableName(), node);
			node.supplyScope(currentScope);
			map.put(node.getVariableName(), new HeaderNodeImpl(node));
		}
		else {
			// Add function outside scope to the corresponding scope
			final String scope = node.getScope();
			if (scope == null) {
				if (hasGlobal(node.getVariableName()))
					throw new FunctionNameAlreadyDefinedException(node);
				addGlobal(node.getVariableName(), new HeaderNodeImpl(node));
			}
			else {
				if (hasManual(scope) && hasManual(scope, node.getVariableName()))
					throw new FunctionNameAlreadyDefinedException(node);
				if (treatScopedFunctionOutsideHeaderAsError)
					throw new ScopedFunctionOutsideHeaderException(node);
				addManual(scope, node.getVariableName(), new HeaderNodeImpl(node));
			}
		}
		// Collect everything from function bodies as well.
		node.getBodyNode().jjtAccept(this);
		getDetachQueue().add(node);
	}

	@Override
	public boolean hasGlobal(final String name) {
		return globalMap.containsKey(name);
	}

	@Override
	public boolean hasManual(final String scope, final String name) {
		final Map<String,?> m = manualMap.get(scope);
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
	public void addExternal(final String name) {
		requiredSet.add(name);
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
	public IHeaderNode getGlobal(final String name) {
		return globalMap.get(name);
	}

	@Override
	public IHeaderNode getManual(final String scope, final String name) {
		final Map<String, IHeaderNode> m = manualMap.get(scope);
		return m != null ? m.get(name) : null;
	}

	@SuppressWarnings("null")
	@Override
	public Iterator<Entry<String, IHeaderNode>> getGlobal() {
		return globalMap.entrySet().iterator();
	}

	@Override
	public Iterator<Entry<String, IHeaderNode>> getManual(final String scope) {
		final Map<String, IHeaderNode> m = manualMap.get(scope);
		return m != null ? m.entrySet().iterator() : null;
	}

	@SuppressWarnings("null")
	@Override
	public Iterator<String> getManual() {
		return manualMap.keySet().iterator();
	}

	@SuppressWarnings("null")
	@Override
	public Iterator<String> getExternal() {
		return requiredSet.iterator();
	}

	@Override
	public IScopeDefinitions build() {
		return new ImmutableScopeDefinitions(globalMap, manualMap, requiredSet);
	}

	@Override
	public Iterator<IHeaderNode> getManualAll() {
		return new ManIter();
	}

	private class ManIter implements Iterator<IHeaderNode> {
		private final Iterator<Map<String, IHeaderNode>> it = manualMap.values().iterator();
		private Iterator<IHeaderNode> it2;
		@Override
		public boolean hasNext() {
			return it2.hasNext() || it.hasNext();
		}
		@Override
		public IHeaderNode next() {
			return (it2.hasNext() ? it2 : (it2 = it.next().values().iterator())).next();
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}