package de.xima.fc.form.expression.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.MultipleOccurencesOfRequireException;
import de.xima.fc.form.expression.exception.MultipleOccurencesOfScopedVariableException;
import de.xima.fc.form.expression.exception.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTScopeExternalNode;
import de.xima.fc.form.expression.node.ASTScopeGlobalNode;
import de.xima.fc.form.expression.node.ASTScopeManualNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationNode;

public class ScopeCollectVisitor extends FormExpressionVoidVoidVisitorAdapter<SemanticsException> {

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

	public static void collect(final Node node) throws SemanticsException {
		final ScopeCollectVisitor v = new ScopeCollectVisitor();
		node.jjtAccept(v);
		v.currentMap = null;
		v.currentScope = null;
		v.detachNodes();
	}

	private ScopeCollectVisitor() {
		globalMap = new HashMap<>();
		requiredSet = new HashSet<>();
		manualMap = new HashMap<>();
	}

	private void detachNodes() {
		for (final Node node : detachQueue)
			node.detach();
		detachQueue.clear();
	}

	@Override
	public void visit(final ASTScopeExternalNode node) throws SemanticsException {
		if (requiredSet.contains(node.getScopeName()))
			throw new MultipleOccurencesOfRequireException(node);
		requiredSet.add(node.getScopeName());
		detachQueue.add(node);
	}

	@Override
	public void visit(final ASTScopeManualNode node) throws SemanticsException {
		currentScope = node.getScopeName();
		@Nullable
		Map<String,Node> map = manualMap.get(currentScope);
		if (map == null) {
			map = new HashMap<String,Node>();
			manualMap.put(node.getScopeName(), map);
		}
		currentMap = map;
		visitChildren(node);
		detachQueue.add(node);
	}

	@Override
	public void visit(final ASTScopeGlobalNode node) throws SemanticsException {
		currentScope = null;
		currentMap = globalMap;
		visitChildren(node);
		detachQueue.add(node);
	}

	@Override
	public void visit(final ASTVariableDeclarationNode node) throws SemanticsException {
		final Map<String, Node> map = currentMap;
		if (map != null) {
			if (map.containsKey(node.getVariableName()))
				throw new MultipleOccurencesOfScopedVariableException(node, currentScope);
			map.put(node.getVariableName(), node);
		}
	}
}
