package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.parse.MissingRequireScopeStatementException;
import de.xima.fc.form.expression.exception.parse.NoSuchScopeException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.exception.parse.VariableUsageBeforeDeclarationException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.impl.variable.HeaderNodeImpl;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTLosNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * Checks whether all variables are declared before using them in assignments.
 *
 * @author madgaksha
 */
public class VariableHoistVisitor extends AVariableBindingVisitor<Boolean> {
	private final IEvaluationContextContractFactory<?> contractFactory;
	private final boolean treatMissingDeclarationAsError;
	private final boolean treatMissingRequireScopeAsError;
	private final boolean treatMissingScopeDeclarationAsError;
	private final IScopeDefinitionsBuilder scopeDefBuilder;

	public VariableHoistVisitor(final IScopeDefinitionsBuilder scopeDefBuilder,
			final IEvaluationContextContractFactory<?> contractFactory, final boolean treatMissingDeclarationAsError,
			final boolean treatMissingRequireScopeAsError, final boolean treatMissingScopeDeclarationAsError) {
		this.scopeDefBuilder = scopeDefBuilder;
		this.treatMissingDeclarationAsError = treatMissingDeclarationAsError;
		this.treatMissingRequireScopeAsError = treatMissingRequireScopeAsError;
		this.contractFactory = contractFactory;
		this.treatMissingScopeDeclarationAsError = treatMissingScopeDeclarationAsError;
	}

	private void processAssignment(final ASTVariableNode node) throws ParseException {
		// Check if scope of scoped variable exists.
		// If not, add the required "import", or create a new manual scope.
		final String scope = node.getScope();
		if (scope != null && !scopeDefBuilder.hasManual(scope) && !scopeDefBuilder.hasExternal(scope)) {
			if (contractFactory.isProvidingExternalScope(scope)) {
				if (treatMissingRequireScopeAsError)
					throw new MissingRequireScopeStatementException(scope, node);
				scopeDefBuilder.addExternal(scope);
			}
			else {
				if (treatMissingScopeDeclarationAsError)
					throw new NoSuchScopeException(scope, node);
				scopeDefBuilder.addManual(scope, node.getVariableName(), new HeaderNodeImpl(node.getVariableName(), node));
			}
		}
		else if (scope == null && binding.getVariable(node.getVariableName()) == null) {
			if (!scopeDefBuilder.hasGlobal(node.getVariableName())) {
				if (treatMissingDeclarationAsError)
					throw new VariableUsageBeforeDeclarationException(node);
				scopeDefBuilder.addGlobal(node.getVariableName(), new HeaderNodeImpl(node.getVariableName(), node));
			}
		}
	}

	@Override
	public void visit(final ASTLosNode node) throws ParseException {
		// Check if all scopes needed by template code are available.
		// When require scope aka "import" is missing, add them.
		final String embedment = node.getEmbedment();
		if (embedment == null)
			return;
		final String[] scopes = contractFactory.getScopesForEmbedment(embedment);
		for (final String scope : scopes) {
			if (scope != null && !(scopeDefBuilder.hasManual(scope) || scopeDefBuilder.hasExternal(scope))) {
				if (!contractFactory.isProvidingExternalScope(scope)) {
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
	public void visit(final ASTAssignmentExpressionNode node) throws ParseException {
		// Check if variable was declared locally or globally.
		// If not, throw an error when in strict mode.
		// Otherwise, add it as a global variable.

		// We need to visit nodes in reverse order. Consider eg.
		//   j = 0;
		//   k = i = (k = j);
		node.jjtGetChild(node.jjtGetNumChildren()-1).jjtAccept(this);
		for (int i = node.jjtGetNumChildren() - 1; i --> 0;) {
			switch (node.jjtGetChild(i).jjtGetNodeId()) {
			case FormExpressionParserTreeConstants.JJTVARIABLENODE:
				final ASTVariableNode n = (ASTVariableNode) node.jjtGetChild(i);
				processAssignment(n);
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

	@Override
	protected Boolean getNewObjectToSet(final ISourceResolvable res) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	public static void hoist(@Nonnull final Node node, @Nonnull final IScopeDefinitionsBuilder scopeDefBuilder,
			@Nonnull final IEvaluationContextContractFactory<?> contractFactory, final boolean strictMode)
			throws ParseException {
		hoist(node, scopeDefBuilder, contractFactory, strictMode, strictMode, strictMode);
	}

	public static void hoist(@Nonnull final Node node, @Nonnull final IScopeDefinitionsBuilder scopeDefBuilder,
			@Nonnull final IEvaluationContextContractFactory<?> contractFactory,
			final boolean treatMissingDeclarationAsError, final boolean treatMissingRequireScopeAsError,
			final boolean treatMissingScopeDeclarationAsError) throws ParseException {
		final VariableHoistVisitor v = new VariableHoistVisitor(scopeDefBuilder, contractFactory,
				treatMissingDeclarationAsError, treatMissingRequireScopeAsError, treatMissingScopeDeclarationAsError);
		v.resolveFunctions(scopeDefBuilder);
		node.jjtAccept(v);
		v.binding.reset();
	}
}
