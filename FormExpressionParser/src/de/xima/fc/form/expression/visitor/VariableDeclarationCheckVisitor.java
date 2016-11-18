package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.exception.IllegalVariableDeclarationAtGlobalScopeException;
import de.xima.fc.form.expression.exception.MissingRequireScopeStatementException;
import de.xima.fc.form.expression.exception.NoSuchScopeException;
import de.xima.fc.form.expression.exception.SemanticsException;
import de.xima.fc.form.expression.exception.VariableDeclaredTwiceException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.VariableUsageBeforeDeclarationException;
import de.xima.fc.form.expression.iface.context.IBinding;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.impl.binding.CloneBinding;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTLosNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * Checks whether all variables are declared before using them in assignments.
 *
 * @author madgaksha
 */
public class VariableDeclarationCheckVisitor extends FormExpressionVoidVoidVisitorAdapter<SemanticsException> {
	private final IBinding<Boolean> binding;
	private final IEvaluationContextContractFactory<?> contractFactory;
	private final boolean treatMissingDeclarationAsError;
	private final boolean treatMissingRequireScopeAsError;
	private final boolean treatMissingScopeDeclarationAsError;
	private final IScopeDefinitionsBuilder scopeDefBuilder;


	public VariableDeclarationCheckVisitor(final IScopeDefinitionsBuilder scopeDefBuilder,
			final IEvaluationContextContractFactory<?> contractFactory, final boolean treatMissingDeclarationAsError,
			final boolean treatMissingRequireScopeAsError, final boolean treatMissingScopeDeclarationAsError) {
		binding = new CloneBinding<Boolean>();
		this.scopeDefBuilder = scopeDefBuilder;
		this.treatMissingDeclarationAsError = treatMissingDeclarationAsError;
		this.treatMissingRequireScopeAsError = treatMissingRequireScopeAsError;
		this.contractFactory = contractFactory;
		this.treatMissingScopeDeclarationAsError = treatMissingScopeDeclarationAsError;
	}

	@Override
	public void visit(final ASTVariableDeclarationClauseNode node) throws SemanticsException {
		// Throw an error when variable declaration is at global nesting level
		// or
		// variable has already been declared. Otherwise, define the variable
		// at the current nesting level.
		if (binding.isGlobal())
			throw new IllegalVariableDeclarationAtGlobalScopeException(node);
		if (binding.hasVariableAtCurrentLevel(node.getVariableName()))
			throw new VariableDeclaredTwiceException(node);
		binding.defineVariable(node.getVariableName(), CmnCnst.NonnullConstant.BOOLEAN_TRUE);
	}

	@Override
	public void visit(final ASTVariableNode node) throws SemanticsException {
		// Check if scope of scoped variable exists.
		// If not, add the required "import", or create a new manual scope.
		final String scope = node.getScope();
		if (scope != null && !(scopeDefBuilder.hasManual(scope) || scopeDefBuilder.hasExternal(scope))) {
			if (contractFactory.isProvidingExternalScope(scope)) {
				if (treatMissingRequireScopeAsError)
					throw new MissingRequireScopeStatementException(scope, node);
				scopeDefBuilder.addExternal(scope);
			}
			else {
				if (treatMissingScopeDeclarationAsError)
					throw new NoSuchScopeException(scope, node);
				scopeDefBuilder.addManual(scope, node.getVariableName(), new ASTNullNode(node.getEmbedment()));
			}
		}
	}

	@Override
	public void visit(final ASTLosNode node) throws SemanticsException {
		// Check if all scopes needed by template code are available.
		// When require scope aka "import" is missing, add them.
		final String[] scopes = contractFactory.getScopesForEmbedment(node.getEmbedment());
		for (final String scope : scopes) {
			if (scope != null && !(scopeDefBuilder.hasManual(scope) || scopeDefBuilder.hasExternal(scope))) {
				if (contractFactory.isProvidingExternalScope(scope)) {
					if (treatMissingRequireScopeAsError)
						throw new MissingRequireScopeStatementException(scope, node);
					scopeDefBuilder.addExternal(scope);
				}
				else {
					throw new NoSuchScopeException(scope, node);
				}
			}
		}
	}

	@Override
	public void visit(final ASTAssignmentExpressionNode node) throws SemanticsException {
		// Check if variable was declared locally or globally.
		// If not, throw an error when in strict mode or add it as
		// a global variable.
		for (int i = 0; i < node.jjtGetNumChildren() - 1; ++i) {
			switch (node.jjtGetChild(i).jjtGetNodeId()) {
			case FormExpressionParserTreeConstants.JJTVARIABLENODE:
				final ASTVariableNode n = (ASTVariableNode) node.jjtGetChild(i);
				if (binding.getVariable(n.getVariableName()) == null) {
					if (scopeDefBuilder.hasGlobal(n.getVariableName())) {
						if (treatMissingDeclarationAsError)
							throw new VariableUsageBeforeDeclarationException(n);
						scopeDefBuilder.addGlobal(n.getVariableName(), new ASTNullNode(n.getEmbedment()));
					}
				}
				break;
			case FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE:
				break;
			default:
				throw new SemanticsException(NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_ASSIGNMENT,
						node.jjtGetChild(i).jjtGetNodeId(), node.getClass().getSimpleName()), node.jjtGetChild(i));
			}
		}
		visitChildren(node);
	}

	public static void check(final Node node, final IScopeDefinitionsBuilder scopeDefBuilder,
			final IEvaluationContextContractFactory<?> contractFactory, final boolean strictMode) throws SemanticsException {
		check(node, scopeDefBuilder, contractFactory, strictMode, strictMode, strictMode);
	}

	public static void check(final Node node, final IScopeDefinitionsBuilder scopeDefBuilder,
			final IEvaluationContextContractFactory<?> contractFactory, final boolean treatMissingDeclarationAsError,
			final boolean treatMissingRequireScopeAsError, final boolean treatMissingScopeDeclarationAsError) throws SemanticsException {
		final VariableDeclarationCheckVisitor visitor = new VariableDeclarationCheckVisitor(scopeDefBuilder,
				contractFactory, treatMissingDeclarationAsError, treatMissingRequireScopeAsError, treatMissingScopeDeclarationAsError);
		node.jjtAccept(visitor);
	}
}
