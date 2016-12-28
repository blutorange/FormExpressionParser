package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.enums.ESeverityOption.TREAT_MISSING_REQUIRE_SCOPE_AS_ERROR;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.parse.IllegalVariableAssignmentException;
import de.xima.fc.form.expression.exception.parse.MissingRequireScopeStatementException;
import de.xima.fc.form.expression.exception.parse.NoSuchScopeException;
import de.xima.fc.form.expression.exception.parse.ScopeMissingVariableException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.exception.parse.VariableNotResolvableException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IFunctionNode;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.iface.parse.IVariableResolutionResult;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;
import de.xima.fc.form.expression.visitor.VariableResolveVisitor.IdPair;

@NonNullByDefault
public class VariableResolveVisitor extends AVariableBindingVisitor<IdPair, Integer> {
	protected final Set<Integer> globalVariables;
	protected final Map<Integer, FunctionInfo> functionInfoMap;

	private final IScopeDefinitionsBuilder scopeDefBuilder;
	private final IEvaluationContextContract<?> factory;
	private final Stack<String> defaultScopeStack = new Stack<>();
	private final ISeverityConfig config;
	protected int variableIdProvider = 0;
	private int functionIdProvider = 0;

	private VariableResolveVisitor(final IScopeDefinitionsBuilder scopeDefBuilder,
			final IEvaluationContextContract<?> factory, final ISeverityConfig config) {
		this.scopeDefBuilder = scopeDefBuilder;
		this.factory = factory;
		this.config = config;
		this.globalVariables = new HashSet<>();
		this.functionInfoMap = new HashMap<>();
	}

	private boolean resolveVariableWithScope(final String scope, final String name,
			final ASTVariableNode node, final boolean doThrow) throws ParseException {
		// First, look it up in manually defined scopes.
		// Otherwise, try external scopes, including those from the external
		// context.

		// Try manual scope.
		if (scopeDefBuilder.hasManual(scope)) {
			final IHeaderNode header = scopeDefBuilder.getManual(scope, name);
			if (header != null) {
				if (!header.isBasicSourceResolved())
					// Should not happen as these variables are resolved
					// earlier.
					throw new VariableNotResolvableException(node);
				node.resolveSource(header.getBasicSource(), EVariableSource.ENVIRONMENTAL, scope);
				return true;
			}
			if (doThrow)
				throw new ScopeMissingVariableException(scope, name, node);
			return false;
		}

		// Try external scope.
		final ILibraryScopeContractFactory<?> info = scopeInfo(scope);
		if (info != null) {
			if (!info.isProviding(name)) {
				if (doThrow)
					throw new ScopeMissingVariableException(scope, name, node);
				return false;
			}
			if (!scopeDefBuilder.hasExternal(scope) && config.hasOption(TREAT_MISSING_REQUIRE_SCOPE_AS_ERROR)) {
				if (doThrow)
					throw new MissingRequireScopeStatementException(scope, node);
				return false;
			}
			scopeDefBuilder.addExternal(scope);
			node.resolveSource(-1, info.getSource(), scope);
			return true;
		}

		// Give up.
		if (doThrow)
			throw new NoSuchScopeException(scope, node);
		return false;
	}

	/**
	 * When we know a variable must be a closure variable, we transfer the local variable
	 * to the set of closure variables.
	 * @param variableId Variable ID to transfer.
	 * @param functionId Call ID of the function owning the local variables.
	 * @param node Resolvable node referring to the closure variable.
	 * @throws SemanticsException When the function does not own the closure variable.
	 */
	private <T extends ISourceResolvable & Node> void transferLocalToClosure(final Integer variableId, final Integer functionId,
			final T node) throws SemanticsException {
		final FunctionInfo functionInfo = getFunctionInfoFor(functionId, node);
		final boolean inLocal = functionInfo.localVariables.remove(variableId);
		final boolean notInClosure = functionInfo.closureVariables.put(variableId, Integer.valueOf(0)) == null;
		if (!inLocal && notInClosure)
			throw new SemanticsException(NullUtil.messageFormat(CmnCnst.Error.NO_MAPPING_FROM_LOCAL_TO_CLOSURE, node.getVariableName()), node);
	}

	private FunctionInfo getFunctionInfoFor(final Integer functionId, final Node node) throws SemanticsException {
		final FunctionInfo functionInfo = functionInfoMap.get(functionId);
		if (functionInfo == null)
			throw new SemanticsException(NullUtil.messageFormat(CmnCnst.Error.FUNCTION_INFO_NOT_SET, functionId), node);
		return functionInfo;
	}

	private void resolveScopeDefs(final IScopeDefinitionsBuilder scopeDefBuilder) throws SemanticsException {
		// Global.
		for (final Iterator<Entry<String, IHeaderNode>> it = scopeDefBuilder.getGlobal(); it.hasNext();) {
			final IHeaderNode header = it.next().getValue();
			header.resolveSource(getNewObjectToSet(Integer.valueOf(-1), header.getNode()).intValue(),
					EVariableSource.ENVIRONMENTAL);
			if (header.isFunction())
				((ASTFunctionClauseNode) header.getNode()).resolveSource(header.getBasicSource(),
						EVariableSource.ENVIRONMENTAL);
		}
		// Manual scopes.
		for (final Iterator<String> it = scopeDefBuilder.getManual(); it.hasNext();) {
			final String scope = it.next();
			if (scope != null) {
				final Iterator<Entry<String, IHeaderNode>> it2 = scopeDefBuilder.getManual(scope);
				if (it2 != null) {
					while (it2.hasNext()) {
						final IHeaderNode header = it2.next().getValue();
						header.resolveSource(getNewObjectToSet(Integer.valueOf(-1), header.getNode()).intValue(),
								EVariableSource.ENVIRONMENTAL);
						if (header.isFunction())
							((ASTFunctionClauseNode) header.getNode()).resolveSource(header.getBasicSource(),
									EVariableSource.ENVIRONMENTAL);
					}
				}
			}
		}
	}

	@Nullable
	private ILibraryScopeContractFactory<?> scopeInfo(final String scope) {
		final ILibraryScopeContractFactory<?> f1 = factory.getLibraryFactory().getScopeFactory(scope);
		final ILibraryScopeContractFactory<?> f2 = factory.getExternalFactory().getScopeFactory(scope);
		return f1 == null ? f2 : f1;
	}

	@Override
	protected Integer beforeFunctionNode(final IFunctionNode node, final Integer functionId) {
		final Integer newFunctionId = Integer.valueOf(functionIdProvider++);
		node.resolveFunctionId(newFunctionId);
		functionInfoMap.put(newFunctionId, new FunctionInfo(functionId));
		return newFunctionId;
	}

	@Override
	public void visit(final ASTVariableNode node, final Integer functionId) throws ParseException {
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
		final IdPair source = binding.getVariable(name);
		if (source != null) {
			node.resolveSource(source.variableId.intValue(), EVariableSource.ENVIRONMENTAL);
			// Closure variable, transfer from local variables to closure variables.
			if (!source.functionId.equals(functionId))
				transferLocalToClosure(source.variableId, source.functionId, node);
			return;
		}

		// Check if variable exists globally.
		final IHeaderNode header = scopeDefBuilder.getGlobal(name);
		if (header != null) {
			if (!header.isBasicSourceResolved())
				throw new VariableNotResolvableException(node);
			node.resolveSource(header.getBasicSource(), EVariableSource.ENVIRONMENTAL);
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
		final String[] embedmentScopeList = embedment != null ? factory.getEmbedmentFactory().getScopesForEmbedment(embedment)
				: CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
		if (embedmentScopeList != null)
			for (final String defaultScope : embedmentScopeList)
				if (defaultScope != null && resolveVariableWithScope(defaultScope, name, node, false))
					return;

		throw new VariableNotResolvableException(node);
	}

	//
	// Getting the default scopes.
	//
	@Override
	public void visit(final ASTWithClauseNode node, final Integer functionId) throws ParseException {
		try {
			for (int i = node.getScopeCount(); i-- > 0;)
				defaultScopeStack.push(node.getScope(i));
			node.getBodyNode().jjtAccept(this, functionId);
		}
		finally {
			for (int i = node.getScopeCount(); i-- > 0; --i)
				defaultScopeStack.pop();
		}
	}

	@Override
	public void visit(final ASTAssignmentExpressionNode node, final Integer functionId) throws ParseException {
		// Resolve all variables first.
		visitChildren(node, functionId);
		// Now check whether we assign to any variable from an external scope.
		for (int i = 0; i < node.getAssignableNodeCount(); ++i) {
			final ASTVariableNode var = node.getAssignableNode(i).getAsOrNull(ASTVariableNode.class);
			if (var != null) {
				if (!var.getSourceType().isAssignable()) {
					throw new IllegalVariableAssignmentException(var);
				}

			}
		}
	}

	private Integer getNewObjectToSet(final Integer functionId, final Node node) throws SemanticsException {
		final Integer newId = Integer.valueOf(variableIdProvider++);
		if (functionId.intValue() >= 0) {
			// Some local scope.
			final FunctionInfo functionInfo = getFunctionInfoFor(functionId, node);
			functionInfo.localVariables.add(newId);
		}
		else
			// Global scope.
			globalVariables.add(newId);
		return newId;
	}

	@Override
	protected IdPair getNewObjectToSet(final ISourceResolvable res, final Node node, final Integer functionId)
			throws SemanticsException {
		final Integer object = getNewObjectToSet(functionId, node);
		res.resolveSource(object.intValue(), EVariableSource.ENVIRONMENTAL);
		return new IdPair(object, functionId);
	}

	public static IVariableResolutionResult resolve(final Node node, final IScopeDefinitionsBuilder scopeDefBuilder,
			final IEvaluationContextContract<?> factory, final ISeverityConfig config)
					throws ParseException {
		final VariableResolveVisitor v = new VariableResolveVisitor(scopeDefBuilder, factory, config);
		v.resolveScopeDefs(scopeDefBuilder);
		v.bindScopeDefValues(scopeDefBuilder, Integer.valueOf(-1));
		node.jjtAccept(v, Integer.valueOf(-1));
		v.binding.reset();
		return v.new ResImpl(node);
	}

	protected class ResImpl implements IVariableResolutionResult {
		private final Map<Integer, Integer> environmentalMap;

		public ResImpl(final Node node) throws SemanticsException {
			environmentalMap = new HashMap<>();
			mapEnvironmental();
			mapClosure(node);
		}

		@Override
		public int getEnvironmentalSize() {
			return environmentalMap.size();
		}

		@Nullable
		@Override
		public Integer getMappedEnvironmental(final Integer oldVariableId) {
			return environmentalMap.get(oldVariableId);
		}

		@Nullable
		@Override
		public Integer getMappedClosure(final Integer functionId, final Integer source) throws IllegalArgumentException {
			@Nullable Integer id = functionId;
			int parent = 0;
			while (id != null) {
				final FunctionInfo info = functionInfoMap.get(id);
				if (info == null)
					return null;
				final Integer newId = info.closureVariables.get(source);
				if (newId != null)
					return Integer.valueOf((parent << 16) | newId.intValue());
				id = info.parent;
				++parent;
			}
			return null;
		}

		@Override
		public int getClosureSize(final Integer functionId) {
			final FunctionInfo info = functionInfoMap.get(functionId);
			return info != null ? info.closureVariables.size() : 0;
		}

		@Override
		public int getInternalVariableCount() {
			return variableIdProvider;
		}

		private void mapClosure(final Node node) throws SemanticsException {
			for (final FunctionInfo info : functionInfoMap.values()) {
				int id = 0;
				for (final Entry<Integer, Integer> entryClosure : info.closureVariables.entrySet())
					entryClosure.setValue(Integer.valueOf(id++));
				if (id > 0xFFFF)
					throw new SemanticsException(
							NullUtil.messageFormat(CmnCnst.Error.CLOSURE_VARIABLE_LIMIT_EXCEEDED, Integer.valueOf(id)), node);
			}
		}

		private void mapEnvironmental() {
			int id = 0;
			for (final Integer oldId : globalVariables)
				environmentalMap.put(oldId, Integer.valueOf(id++));
			for (final FunctionInfo info : functionInfoMap.values())
				for (final Integer oldId : info.localVariables)
					environmentalMap.put(oldId, Integer.valueOf(id++));
		}
	}

	protected static class IdPair {
		public final Integer variableId;
		public final Integer functionId;
		public IdPair(final Integer variableId, final Integer functionId) {
			this.variableId = variableId;
			this.functionId = functionId;
		}
	}

	protected static class FunctionInfo {
		public int parentCount = -1;
		@Nullable
		public final Integer parent;
		// Purely local variables.
		public final Set<Integer> localVariables;
		// Variables used by some closure.
		public final Map<Integer,Integer> closureVariables;
		public FunctionInfo(final Integer parent) {
			this.parent = parent;
			this.localVariables = new HashSet<>();
			this.closureVariables = new HashMap<>();
		}
	}
}