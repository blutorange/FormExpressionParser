package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.enums.ESeverityOption.TREAT_MISSING_DECLARATION_AS_ERROR;
import static de.xima.fc.form.expression.enums.ESeverityOption.TREAT_MISSING_REQUIRE_SCOPE_AS_ERROR;
import static de.xima.fc.form.expression.enums.ESeverityOption.TREAT_MISSING_SCOPE_DECLARATION_AS_ERROR;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.parse.MissingRequireScopeStatementException;
import de.xima.fc.form.expression.exception.parse.NoSuchEmbedmentException;
import de.xima.fc.form.expression.exception.parse.NoSuchScopeException;
import de.xima.fc.form.expression.exception.parse.UnhandledNodeTypeException;
import de.xima.fc.form.expression.exception.parse.VariableUsageBeforeDeclarationException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.impl.variable.HeaderNodeImpl;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTLosNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.Void;

/**
 * Checks whether all variables are declared before using them in assignments.
 *
 * @author madgaksha
 */
@NonNullByDefault
public class VariableHoistVisitor extends AVariableBindingVisitor<Boolean, Void> {
	private final IEvaluationContextContract<?> contractFactory;
	private final ISeverityConfig config;

	public VariableHoistVisitor(final IScopeDefinitionsBuilder scopeDefBuilder,
			final IEvaluationContextContract<?> contractFactory, final ISeverityConfig config) {
		super(scopeDefBuilder);
		this.config = config;
		this.contractFactory = contractFactory;
	}


	private boolean provides(final String scope) {
		return contractFactory.getLibraryFactory().isProvidingScope(scope)
				|| contractFactory.getExternalFactory().isProvidingScope(scope);
	}

	private void processAssignment(final ASTVariableNode node) throws ParseException {
		// Check if scope of scoped variable exists.
		// If not, add the required "import", or create a new manual scope.
		final String scope = node.getScope();
		if (scope != null && !hasManual(scope) && !hasExternal(scope)) {
			if (provides(scope)) {
				if (config.hasOption(TREAT_MISSING_REQUIRE_SCOPE_AS_ERROR))
					throw new MissingRequireScopeStatementException(scope, node);
				addExternal(scope);
			}
			else {
				if (config.hasOption(TREAT_MISSING_SCOPE_DECLARATION_AS_ERROR))
					throw new NoSuchScopeException(scope, node);
				addManual(scope, node.getVariableName(), new HeaderNodeImpl(node.getVariableName(), node));
			}
		}
		else if (scope == null && binding.getVariable(node.getVariableName()) == null) {
			if (!hasGlobal(node.getVariableName())) {
				if (config.hasOption(TREAT_MISSING_DECLARATION_AS_ERROR))
					throw new VariableUsageBeforeDeclarationException(node);
				addGlobal(node.getVariableName(), new HeaderNodeImpl(node.getVariableName(), node));
			}
		}
	}

	@Override
	public void visit(final ASTLosNode node, final Void object) throws ParseException {
		// Check if all scopes needed by template code are available.
		// When require scope aka "import" is missing, add them.
		final String embedment = node.getEmbedment();
		if (embedment == null)
			return;
		final String[] scopes = contractFactory.getEmbedmentFactory().getScopesForEmbedment(embedment);
		if (scopes == null)
			throw new NoSuchEmbedmentException(embedment, node);
		for (final String scope : scopes) {
			if (scope != null && !(hasManual(scope) || hasExternal(scope))) {
				if (provides(scope)) {
					if (config.hasOption(TREAT_MISSING_REQUIRE_SCOPE_AS_ERROR))
						throw new MissingRequireScopeStatementException(scope, node);
					addExternal(scope);
				}
				else {
					throw new NoSuchScopeException(scope, node);
				}
			}
		}
	}

	@Override
	public void visit(final ASTAssignmentExpressionNode node, final Void object) throws ParseException {
		// Check if variable was declared locally or globally.
		// If not, throw an error when in strict mode.
		// Otherwise, add it as a global variable.

		// We need to visit nodes in reverse order. Consider eg.
		// j = 0;
		// k = i = (k = j);
		node.jjtGetChild(node.jjtGetNumChildren() - 1).jjtAccept(this, object);
		for (int i = node.jjtGetNumChildren() - 1; i-- > 0;) {
			switch (node.jjtGetChild(i).jjtGetNodeId()) {
			case FormExpressionParserTreeConstants.JJTVARIABLENODE:
				final ASTVariableNode n = (ASTVariableNode) node.jjtGetChild(i);
				processAssignment(n);
				break;
			case FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE:
				break;
			default:
				throw new UnhandledNodeTypeException(node.jjtGetChild(i));
			}
		}
		visitChildren(node, object);
	}

	@Override
	protected Boolean getNewObjectToSet(final ISourceResolvable res, final Node node, final Void object) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	public static void hoist(final Node node, final IScopeDefinitionsBuilder scopeDefBuilder,
			final IEvaluationContextContract<?> contractFactory, final ISeverityConfig config)
					throws ParseException {
		final VariableHoistVisitor v = new VariableHoistVisitor(scopeDefBuilder, contractFactory, config);
		v.bindScopeDefValues(scopeDefBuilder, Void.NULL);
		node.jjtAccept(v, Void.NULL);
		v.finishQueue();
		v.binding.reset();
	}
}