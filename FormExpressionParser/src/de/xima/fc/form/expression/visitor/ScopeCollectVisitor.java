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
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.impl.ImmutableScopeDefinitions;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTScopeExternalNode;
import de.xima.fc.form.expression.node.ASTScopeGlobalNode;
import de.xima.fc.form.expression.node.ASTScopeManualNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;

public class ScopeCollectVisitor extends FormExpressionVoidVoidVisitorAdapter<SemanticsException>
		implements IScopeDefinitionsBuilder {
	@Nullable
	private String currentScope = null;
	@Nullable
	private Map<String,Node> currentMap = null;
	@Nonnull
	private final List<Node> detachQueue = new ArrayList<>();

	@Nonnull
	private final Map<String,Node> globalMap;
	@Nonnull
	private final Set<String> requiredSet;
	@Nonnull
	private final Map<String, Map<String,Node>> manualMap;

	@Nonnull	
	public static IScopeDefinitionsBuilder collect(final Node node) throws ParseException {
		final ScopeCollectVisitor v = new ScopeCollectVisitor();
		node.jjtAccept(v);
		v.currentMap = null;
		v.currentScope = null;
		v.detachNodes();
		return v;
	}

	private ScopeCollectVisitor() {
		globalMap = new HashMap<>();
		requiredSet = new HashSet<>();
		manualMap = new HashMap<>();
	}

	private void detachNodes() throws ParseException {
		for (final Node node : detachQueue)
			node.detach();
		detachQueue.clear();
	}

	@Override
	public void visit(final ASTScopeExternalNode node) throws SemanticsException {
		if (requiredSet.contains(node.getScopeName()) || manualMap.containsKey(node))
			throw new DuplicateRequireScopeDeclarationException(node);
		requiredSet.add(node.getScopeName());
		detachQueue.add(node);
	}

	@Override
	public void visit(final ASTScopeManualNode node) throws SemanticsException {
		currentScope = node.getScopeName();
		@Nullable Map<String,Node> map = manualMap.get(currentScope);
		if (map == null) {
			map = new HashMap<String,Node>();
			manualMap.put(node.getScopeName(), map);
		}
		currentMap = map;
		visitChildren(node);
		currentMap = null;
		currentScope = null;
		detachQueue.add(node);
	}

	@Override
	public void visit(final ASTScopeGlobalNode node) throws SemanticsException {
		currentScope = null;
		currentMap = globalMap;
		visitChildren(node);
		currentMap = null;
		detachQueue.add(node);
	}

	@Override
	public void visit(final ASTVariableDeclarationClauseNode node) throws SemanticsException {
		final Map<String, Node> map = currentMap;
		if (map != null) {
			if (map.containsKey(node.getVariableName()))
				throw new DuplicateScopedVariableDeclarationException(node, currentScope);
			map.put(node.getVariableName(), node);
		}
	}
	
	@Override
	public void visit(final ASTFunctionClauseNode node) throws SemanticsException {
		final String scope = node.getScope();
		if (scope == null) {
			if (hasGlobal(node.getVariableName()))
				throw new FunctionNameAlreadyDefinedException(node);
			addGlobal(node.getVariableName(), node);
		}
		else {
			if (hasManual(scope) && hasManual(scope, node.getVariableName()))
				throw new FunctionNameAlreadyDefinedException(node);
			addManual(scope, node.getVariableName(), node);
		}
		final Node last = node.getLastChildOrNull();
		if (last != null) last.jjtAccept(this);
		detachQueue.add(node);
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
	public void addGlobal(final String name, final Node node) {
		globalMap.put(name, node);
	}

	@Override
	public void addExternal(final String name) {
		requiredSet.add(name);
	}

	@Override
	public void addManual(final String scope, final String name, final Node node) {
		Map<String,Node> m = manualMap.get(scope);
		if (m == null) {
			m = new HashMap<>();
			manualMap.put(scope, m);
		}
		m.put(name, node);
	}

	@Override
	public Node getGlobal(final String name) {
		return globalMap.get(name);
	}

	@Override
	public Node getManual(final String scope, final String name) {
		final Map<String,Node> m = manualMap.get(scope);
		return m != null ? m.get(name) : null;
	}

	@SuppressWarnings("null")
	@Override
	public Iterator<Entry<String, Node>> getGlobal() {
		return globalMap.entrySet().iterator();
	}

	@Override
	public Iterator<Entry<String, Node>> getManualFor(final String scope) {
		final Map<String,Node> m = manualMap.get(scope);
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
}
