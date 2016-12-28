package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.enums.ESeverityOption.TREAT_MISSING_EXPLICIT_RETURN_AS_ERROR;
import static de.xima.fc.form.expression.enums.ESeverityOption.TREAT_UNMATCHING_SWITCH_TYPE_AS_ERROR;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTEMPTYNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTVARIABLENODE;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableCollection;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.exception.parse.IllegalJumpClauseException;
import de.xima.fc.form.expression.exception.parse.IllegalNumberOfFunctionParametersException;
import de.xima.fc.form.expression.exception.parse.IllegalNumberOfVarArgFunctionParametersException;
import de.xima.fc.form.expression.exception.parse.IncompatibleBracketAccessorTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleBracketAssignerPropertyTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleBracketAssignerValueTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleConditionTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleDotAssignerTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleExpressionMethodTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleForLoopHeaderTypeAssignmentException;
import de.xima.fc.form.expression.exception.parse.IncompatibleFunctionParameterTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleFunctionReturnTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleSwitchCaseTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleVariableAssignmentTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleVariableConversionTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleVoidReturnTypeException;
import de.xima.fc.form.expression.exception.parse.IterationNotSupportedException;
import de.xima.fc.form.expression.exception.parse.MissingReturnException;
import de.xima.fc.form.expression.exception.parse.NoSuchBracketAccessorException;
import de.xima.fc.form.expression.exception.parse.NoSuchBracketAssignerException;
import de.xima.fc.form.expression.exception.parse.NoSuchDotAccessorException;
import de.xima.fc.form.expression.exception.parse.NoSuchDotAssignerException;
import de.xima.fc.form.expression.exception.parse.NoSuchExpressionMethodException;
import de.xima.fc.form.expression.exception.parse.NoSuchScopeException;
import de.xima.fc.form.expression.exception.parse.NotAFunctionException;
import de.xima.fc.form.expression.exception.parse.ScopeMissingVariableException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.exception.parse.UnhandledEnumException;
import de.xima.fc.form.expression.exception.parse.UnreachableCodeException;
import de.xima.fc.form.expression.exception.parse.VariableNotResolvableException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory.IPropertyReturn;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory.IPropertyValue;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory.IReturn;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory.IValue;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory.IValueReturn;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.IScopedSourceResolvable;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.iface.parse.IVariableTypeBuilder;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.impl.variable.VariableTypeBuilder;
import de.xima.fc.form.expression.impl.variable.VoidType;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTComparisonExpressionNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTEmptyNode;
import de.xima.fc.form.expression.node.ASTEqualExpressionNode;
import de.xima.fc.form.expression.node.ASTExceptionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionArgumentNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIdentifierNameNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTLogNode;
import de.xima.fc.form.expression.node.ASTLosNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTParenthesisExpressionNode;
import de.xima.fc.form.expression.node.ASTPostUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTPropertyExpressionNode;
import de.xima.fc.form.expression.node.ASTRegexNode;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTScopeExternalNode;
import de.xima.fc.form.expression.node.ASTScopeGlobalNode;
import de.xima.fc.form.expression.node.ASTScopeManualNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringCharactersNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTernaryExpressionNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTVariableTypeNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;
import de.xima.fc.form.expression.visitor.VariableTypeCheckVisitor.NodeInfo;

/**
 * <p>
 * Checks whether the variable types are consistent and will not throw an error
 * on runtime. For example, the program given below is illegal because variable
 * <code>a<code> was declared as a
 * {@link NumberLangObject} but is assigned to a {@link StringLangObject}.
 * <pre>
 * global scope {
 *   number a;
 * }
 * a = 'foo';
 * </pre>
 * </p><p>
 * Functions may return a value in two ways, by an explicit
 * return statement or by the last expression or clause executed.
 * Each clause and expression always returns some {@link ALangObject}.
 * Note that in strict mode functions are required to return a result
 * explicitly.
 * </p><p>
 * Empty statement nodes simple pass through the value of the last
 * node. Therefore the function below returns a string:
 * <pre>
 *   function string foo() {
 *     "bar";;;;;
 *   }
 * </pre>
 * </p><p>
 * To verify program, we proceed as follows. For each node,
 * need to know:
 * <ul>
 *   <li> a (unified) type, if any, the node returns via return clauses;</li>
 *   <li> the list of types the node always returns implicitly; and</li>
 *   <li> whether the node can possibly complete normally.</li>
 * </ul>
 * The list of types cannot be empty normally, as each node always returns
 * something. The exception to the rule are throw clause nodes that never
 * return anything implicitly. As a result, other nodes may not return
 * anything either, when all code branches include a return clause:
 * <pre>
 *   function number foo() {
 *     if (true) return 1;
 *     else return 0;
 *   }
 * </p><p>
 * For some nodes such as if-clause nodes this list may contain more than
 * one entry. These types do not need be mutually compatible necessarily,
 * as the implicit return value of a node is not always used. For example,
 * consider the following program:
 * <pre>
 *   function string foo() {
 *     if (someCondition())
 *       5;
 *     else
 *       true;
 *    "bar";
 *   }
 * </pre>
 * The function <code>foo</code> always returns a string indeed, as its
 * signature claims. The if-clause node implicitly returns either a
 * {@link NumberLangObject} (<code>5</code>) or a {@link BooleanLangObject},
 * depending on the condition. However, this implicit return type is never
 * needed as it is not the last statement of the function body.
 * </p>
 * <p>
 * Next we make a slight modification to the above program:
 *
 * <pre>
 *   function number foo() {
 *     if (someCondition())
 *       return 9;
 *     else
 *       true;
 *     throw exception('bar');
 *   }
 * </pre>
 *
 * The if-clause node implicitly returns a {@link BooleanLangObject} which may
 * or may not be needed; and may return a {@link NumberLangObject} via a return
 * clause that is definitely needed. The last node, a throw clause, never
 * returns anything. Thus, the program is valid as it cannot return anything
 * other than a number.
 * </p>
 * <p>
 * Regarding loops and break or continue statement, consider this program:
 *
 * <pre>
 *   function string foo() {
 *     while<label1< (true) {
 *       while<label2> (true) {
 *         if (someCondition()) break;
 *       }
 *       return '999';
 *     }
 *   }
 * </pre>
 *
 * When <code>someCondition</code> is not fulfilled, the function
 * <code>foo</code> returns a {@link StringLangObject} via a return clause, in
 * accordance with the function's signature. Otherwise, it impicitly returns the
 * implicit return type the the last node, which is the implicit return type of
 * the if-clause, which is a {@link NullLangObject} implicitly returned by the
 * break clause.
 * </p>
 * <p>
 * Break and continue clauses demand a careful treatment:
 *
 * <pre>
 *  function string foo() {
 *    var i = 0;
 *    while (i==0) {
 *      if (i == 1) break;
 *      42;
 *    }
 *  }
 *
 *  function string bar() {
 *    var i = 0;
 *    while (i==0) {
 *      if (i == 0) break;
 *      42;
 *    }
 *  }
 * </pre>
 *
 * Function <code>foo</code> returns a {@link NumberLangObject}, while
 * <code>bar</code> returns a {@link NullLangObject}. Yet the syntactical
 * structure of both programs is the same, they only differ semantically. To
 * solve this issue, we make the assumption each code segment could possibly
 * reached by some path and thus needs to be inspected. Thus both of the above
 * programs are invalid as they might return a {@link NumberLangObject}, which
 * violates promise of the function's signature. Note that for some programs
 * such as the one below it is possible to prove that there is unreachable code
 * and a warning or error could be issued accordingly.
 *
 * <pre>
 *  function number foo() {
 *    return 5;
 *    return "5";
 *  }
 * </pre>
 * </p>
 * <p>
 * Regarding possibly infinite loops:
 *
 * <pre>
 *   function string foo() {
 *     while (someCondition()) {
 *     }
 *   }
 * </pre>
 *
 * Function <code>foo</code> implicitly returns the implicit return type of its
 * body node, which is an emtpy node that return {@link NullLangObject}.
 * <code>null</code> is compatible with a <code>string</code>, thus the above
 * code is semantically correct.
 * </p>
 * <p>
 * Concerning exceptions and try clauses:
 *
 * <pre>
 *   function number foo() {
 *       throw exception(5);
 *   }
 *   function number bar() {
 *     try {
 *      raiseError(false);
 *     }
 *     catch(e) {
 *       return e;
 *     }
 *   }
 * </pre>
 *
 * Function <code>foo</code> implicitly returns {@link NullLangObject}, but
 * never completes normally and is thus legal. <code>bar</code> may return
 * either an {@link ExceptionLangObject} or whatever is returned by the function
 * <code>raiseError</code>, and is thus illegal.
 * </p>
 */
@NonNullByDefault
public final class VariableTypeCheckVisitor implements IFormExpressionReturnVoidVisitor<NodeInfo, SemanticsException> {

	private final IVariableType[] table;
	private final ISeverityConfig config;
	private final IEvaluationContextContract<?> factory;

	private VariableTypeCheckVisitor(final IVariableType[] symbolTypeTable, final IEvaluationContextContract<?> factory,
			final ISeverityConfig config) {
		this.table = symbolTypeTable;
		this.config = config;
		this.factory = factory;
	}

	/**
	 * @param info Variable type info tpo check.
	 * @param node The node to check.
	 * @throws IncompatibleVariableConversionTypeException When the given object is not an object or a subclass.
	 */
	private static void checkObject(final NodeInfo info, final Node node) throws IncompatibleVariableConversionTypeException {
		if (!ELangObjectClass.OBJECT.isSuperClassOf(info.getImplicitType().getBasicLangClass()))
			throw new IncompatibleVariableConversionTypeException(SimpleVariableType.OBJECT,
					info.getImplicitType(), node);
	}

	private NodeInfo visitLoop(final Node conditionNode, final Node bodyNode, @Nullable final String label,
			final boolean isHeaderControlled) throws SemanticsException {
		// Check if condition is boolean.
		final NodeInfo infoCondition = conditionNode.jjtAccept(this);
		if (!infoCondition.hasImplicitType()) {
			if (isHeaderControlled)
				// Code in the loop's body can never be reached.
				throw new UnreachableCodeException(bodyNode);
		}
		else if (!infoCondition.getImplicitType()
				.equalsType(SimpleVariableType.BOOLEAN))
			throw new IncompatibleConditionTypeException(infoCondition.getImplicitType(), conditionNode);
		// Get return type of while body.
		final NodeInfo infoBody = bodyNode.jjtAccept(this);
		if (infoBody.removeLabel(label))
			// Break or continue this loop.
			infoBody.unifiyImplicitType(SimpleVariableType.NULL);
		if (isHeaderControlled)
			// May return null when the condition is not fulfilled initially.
			infoBody.unifiyImplicitType(SimpleVariableType.NULL);
		return infoBody;
	}

	private IVariableType getDeclaredType(@Nullable final String scope, final ISourceResolvable resolvable, final Node node) throws SemanticsException {
		if (!resolvable.isBasicSourceResolved())
			throw new VariableNotResolvableException(scope, resolvable.getVariableName(), node);
		final int source = resolvable.getBasicSource();
		if (source >= 0) {
			final IVariableType type = table[source];
			return type != null ? type : SimpleVariableType.OBJECT;
		}
		if (scope == null)
			throw new VariableNotResolvableException(scope, resolvable.getVariableName(), node);
		final ILibraryScopeContractFactory<?> info = getScopeInfo(scope);
		if (info == null)
			throw new NoSuchScopeException(scope, node);
		final IVariableType type = info.getVariableType(resolvable.getVariableName());
		if (type == null)
			throw new ScopeMissingVariableException(scope, resolvable.getVariableName(), node);
		return type;
	}

	private <T extends ISourceResolvable & Node> IVariableType getDeclaredType(@Nullable final String scope,
			final T node) throws SemanticsException {
		return getDeclaredType(scope, node, node);
	}

	@Nullable
	private ILibraryScopeContractFactory<?> getScopeInfo(final String scope) {
		final ILibraryScopeContractFactory<?> f1 = factory.getLibraryFactory().getScopeFactory(scope);
		final ILibraryScopeContractFactory<?> f2 = factory.getExternalFactory().getScopeFactory(scope);
		return f1 == null ? f2 : f1;
	}

	private <T extends IScopedSourceResolvable & Node> NodeInfo getInfo(final T node) throws SemanticsException {
		return new NodeInfo(null, getDeclaredType(node.getScope(), node));
	}

	/**
	 * Starting at the first clause node of a switch, visits all nodes until the
	 * next case or default node. Works the same way as for an
	 * {@link ASTStatementListNode}.
	 *
	 * @param node
	 *            Switch node.
	 * @param startIndex
	 *            Index of the first clause node.
	 * @param NodeInfo
	 *            Should be a new, empty {@link NodeInfo} and stores the return
	 *            type.
	 * @return The next index that is not a clause node.
	 * @throws SemanticsException
	 *             When there is a semantic error.
	 */
	private int visitSwitchClause(final ASTSwitchClauseNode node, final EMethod type, final int startIndex, final NodeInfo info)
			throws SemanticsException {
		info.unifiyImplicitType(SimpleVariableType.NULL);
		int i;
		for (i = startIndex; i < node.getCaseCount() && node.getCaseType(i) == type; ++i) {
			final NodeInfo infoClause = node.getCaseNode(i).jjtAccept(this);
			if (infoClause.hasImplicitType())
				info.replaceImplicitType(infoClause.getImplicitType());
			else if (i < node.getCaseCount() - 1 && node.getCaseType(i + 1) == type)
				// Unreachable code when this node does not complete normally
				// and the next node is not a case or default declaration.
				throw new UnreachableCodeException(node.jjtGetChild(i + 1));
			else
				info.clearImplicitType();
			info.unifyJumps(infoClause);
		}
		return i;
	}

	private IVariableType getFunctionType(final IVariableType typeDeclaredReturn, final ASTFunctionNode node)
			throws SemanticsException {
		final IVariableTypeBuilder builder = new VariableTypeBuilder();
		builder.setBasicType(ELangObjectClass.FUNCTION);
		builder.append(typeDeclaredReturn);
		for (int i = 0; i < node.getArgumentCount(); ++i) {
			final NodeInfo infoArg = node.getArgumentNode(i).jjtAccept(this);
			final IVariableType typeArg = infoArg.hasImplicitType() ? infoArg.getImplicitType()
					: SimpleVariableType.OBJECT;
			if (node.hasVarArgs() && i == node.getArgumentCount() - 1)
				builder.setFlag(EVariableTypeFlag.VARARG);
			builder.append(typeArg);
		}
		try {
			return builder.build();
		}
		catch (final IllegalVariableTypeException e) {
			throw new SemanticsException(NullUtil.orEmpty(e.getMessage()), node);
		}
	}

	private void assertFunctionReturnType(final NodeInfo infoActual, final IVariableType typeDeclared, final Node node)
			throws SemanticsException {
		if (VoidType.INSTANCE.equalsType(typeDeclared)) {
			// Void function.
			// Void functions are not allowed to return anything. We must allow the empty return
			// statement "return;", however.
			if (infoActual.hasReturnType() && VoidType.INSTANCE.equalsType(typeDeclared) && !VoidType.INSTANCE.equalsType(infoActual.getReturnType()))
				throw new IncompatibleVoidReturnTypeException(infoActual.getReturnType(), node);
		}
		else {
			// Non-void function.
			// Check if type returned via return statements is compatible with the declared type.
			if (infoActual.hasReturnType() && !typeDeclared.isAssignableFrom(infoActual.getReturnType()))
				throw new IncompatibleFunctionReturnTypeException(typeDeclared, infoActual.getReturnType(), node);
			// In strict mode non-void functions must return explicitly.
			if (config.hasOption(TREAT_MISSING_EXPLICIT_RETURN_AS_ERROR) && infoActual.hasImplicitType())
				throw new MissingReturnException(typeDeclared, node);
			// Check if type returned implicitly via the last statement is compatible with the declared type.
			if (infoActual.hasImplicitType() && !typeDeclared.isAssignableFrom(infoActual.getImplicitType()))
				throw new IncompatibleFunctionReturnTypeException(typeDeclared, infoActual.getImplicitType(), node);
		}
	}

	private void visitScopeDefs(final IScopeDefinitions scopeDefs) throws SemanticsException {
		for (final IHeaderNode headerNode : scopeDefs.getGlobal())
			visitHeaderNode(null, headerNode);
		for (final Entry<String, ImmutableCollection<IHeaderNode>> entry : scopeDefs.getManual().entrySet())
			for (final IHeaderNode headerNode : entry.getValue())
				visitHeaderNode(entry.getKey(), headerNode);
	}

	private void visitHeaderNode(@Nullable final String scope, @Nullable final IHeaderNode headerNode) throws SemanticsException {
		if (headerNode != null) {
			final NodeInfo infoAssign = headerNode.getNode().jjtAccept(this);
			if (!headerNode.isFunction()) {
				final IVariableType typeDeclared = getDeclaredType(scope, headerNode, headerNode.getNode());
				if (!infoAssign.hasImplicitType())
					throw new UnreachableCodeException(headerNode.hasType() ? headerNode.getTypeNode() : headerNode.getNode());
				if (!typeDeclared.isAssignableFrom(infoAssign.getImplicitType()))
					throw new IncompatibleVariableAssignmentTypeException(typeDeclared, infoAssign.getImplicitType(), headerNode, headerNode.getNode());
			}
		}
	}

	private NodeInfo visitPropertyExpression(final ASTPropertyExpressionNode node, final int propertyNodeCount)
			throws SemanticsException {
		final NodeInfo infoRes = node.getStartNode().jjtAccept(this);
		if (!infoRes.hasImplicitType()) {
			if (node.getPropertyNodeCount() > 0)
				throw new UnreachableCodeException(node.getPropertyNode(0));
			return infoRes;
		}
		for (int i = 0; i < propertyNodeCount; ++i) {
			switch (node.getPropertyType(i)) {
			case DOT: {
				final String property = node.getDotPropertyName(i);
				final IReturn typeReturn = factory.getNamespaceFactory().getDotAccessorInfo(infoRes.getImplicitType(),
						property);
				if (typeReturn == null)
					throw new NoSuchDotAccessorException(infoRes.getImplicitType(), property,
							i == 0 ? node.getStartNode() : node.getPropertyNode(i - 1));
				infoRes.replaceImplicitType(typeReturn.getReturn());
				break;
			}
			case BRACKET: {
				final NodeInfo infoProperty = node.getPropertyNode(i).jjtAccept(this);
				infoRes.unifyJumps(infoProperty);
				if (!infoProperty.hasImplicitType()) {
					if (i < node.getPropertyNodeCount() - 1)
						throw new UnreachableCodeException(node.jjtGetChild(i + 1));
					return infoRes;
				}
				final IPropertyReturn typePropertyReturn = factory.getNamespaceFactory()
						.getBracketAccessorInfo(infoRes.getImplicitType());
				if (typePropertyReturn == null)
					throw new NoSuchBracketAccessorException(infoRes.getImplicitType(),
							i == 0 ? node.getStartNode() : node.getPropertyNode(i - 1));
				if (!typePropertyReturn.getProperty().isAssignableFrom(infoProperty.getImplicitType()))
					throw new IncompatibleBracketAccessorTypeException(infoRes.getImplicitType(),
							typePropertyReturn.getProperty(), infoProperty.getImplicitType(), node.getPropertyNode(i));
				infoRes.replaceImplicitType(typePropertyReturn.getReturn());
				break;
			}
			case PARENTHESIS: {
				// We cannot call anything that is not a function.
				final IVariableType typeFunction = infoRes.getImplicitType();
				if (!typeFunction.isA(ELangObjectClass.FUNCTION))
					throw new NotAFunctionException(infoRes.getImplicitType(), node);
				final int declaredArgCount = typeFunction.getGenericCount() - 1;
				final int actualArgCount = node.getParenthesisArgNodeCount(i);
				final int indexOneAfterEnd;
				if (typeFunction.hasFlag(EVariableTypeFlag.VARARG)) {
					// Function with varargs.
					indexOneAfterEnd = declaredArgCount - 1;
					if (actualArgCount < declaredArgCount - 1)
						throw new IllegalNumberOfVarArgFunctionParametersException(declaredArgCount, actualArgCount,
								node.getPropertyNode(i));
					IVariableType typeVarArg = SimpleVariableType.NULL;
					for (int j = indexOneAfterEnd; j < actualArgCount; ++j) {
						final NodeInfo infoVarArg = node.getParenthesisArgNode(i, j).jjtAccept(this);
						if (!infoVarArg.hasImplicitType())
							throw new UnreachableCodeException(node.getPropertyNode(i));
						infoRes.unifyJumps(infoVarArg);
						typeVarArg = typeVarArg.union(infoVarArg.getImplicitType());
						if (!typeFunction.getGeneric(typeFunction.getGenericCount() - 1).isAssignableFrom(typeVarArg))
							throw new IncompatibleFunctionParameterTypeException(
									typeFunction.getGeneric(typeFunction.getGenericCount() - 1),
									infoVarArg.getImplicitType(), node.getParenthesisArgNode(i, j));
					}
				}
				else {
					// Function without varargs.
					if (declaredArgCount != actualArgCount)
						throw new IllegalNumberOfFunctionParametersException(declaredArgCount, actualArgCount,
								node.getPropertyNode(i));
					indexOneAfterEnd = actualArgCount;
				}
				for (int j = 0; j < indexOneAfterEnd; ++j) {
					final NodeInfo infoArg = node.getParenthesisArgNode(i, j).jjtAccept(this);
					if (!infoArg.hasImplicitType())
							throw new UnreachableCodeException(node.getPropertyNode(i));
					if (!typeFunction.getGeneric(j+1).isAssignableFrom(infoArg.getImplicitType()))
						throw new IncompatibleFunctionParameterTypeException(typeFunction.getGeneric(j + 1),
								infoArg.getImplicitType(), node.getParenthesisArgNode(i, j));
					infoRes.unifyJumps(infoArg);
				}
				// Return declared return type.
				infoRes.replaceImplicitType(typeFunction.getGeneric(0));
				break;
			}
			// $CASES-OMITTED$
			default:
				throw new UnhandledEnumException(node.getPropertyType(i), node.getPropertyNode(i));
			}
		}
		return infoRes;
	}

	private void assignmentStep(final EMethod method, final Node node, final NodeInfo infoCurrent)
			throws SemanticsException {
		switch (node.jjtGetNodeId()) {
		case JJTVARIABLENODE: {
			final NodeInfo infoVar = node.jjtAccept(this);
			if (!infoVar.hasImplicitType())
				throw new UnreachableCodeException(node);
			// For method assignments such as +=, -= etc., check method types.
			if (method != EMethod.EQUAL) {
				final IValueReturn typeValueReturn = factory.getNamespaceFactory()
						.getExpressionMethodInfo(infoVar.getImplicitType(), method.equalMethod(node));
				if (typeValueReturn == null)
					throw new NoSuchExpressionMethodException(infoVar.getImplicitType(),
							method.equalMethod(node), node);
				if (!typeValueReturn.getValue().isAssignableFrom(infoCurrent.getImplicitType()))
					throw new IncompatibleExpressionMethodTypeException(infoVar.getImplicitType(),
							method.equalMethod(node), typeValueReturn.getValue(), infoCurrent.getImplicitType(), node);
				infoCurrent.replaceImplicitType(typeValueReturn.getReturn());
			}
			if (!infoVar.getImplicitType().isAssignableFrom(infoCurrent.getImplicitType())) {
				throw new IncompatibleVariableAssignmentTypeException(infoVar.getImplicitType(),
						infoCurrent.getImplicitType(), (ASTVariableNode) node);
			}
			infoCurrent.unifyJumps(infoVar);
			break;
		}
		case JJTPROPERTYEXPRESSIONNODE: {
			final ASTPropertyExpressionNode nodeProperty = (ASTPropertyExpressionNode) node;
			final NodeInfo infoProperty = visitPropertyExpression(nodeProperty, nodeProperty.getPropertyNodeCount() - 1);
			infoCurrent.unifyJumps(infoProperty);
			if (!infoProperty.hasImplicitType())
				throw new UnreachableCodeException(nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
			switch (nodeProperty.getPropertyType(nodeProperty.getPropertyNodeCount() - 1)) {
			case DOT: {
				final String property = nodeProperty.getDotPropertyName(nodeProperty.getPropertyNodeCount() - 1);
				// Compound assignments (a.b+=c etc.) are the same as a.b=a.b+c
				// etc.
				// First we need to evaluate the last attribute accessor (a.b),
				// then the expression method (+).
				if (method != EMethod.EQUAL) {
					final IReturn typeDot = factory.getNamespaceFactory()
							.getDotAccessorInfo(infoProperty.getImplicitType(), property);
					if (typeDot == null)
						throw new NoSuchDotAccessorException(infoProperty.getImplicitType(), property,
								nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
					final IValueReturn typeMethod = factory.getNamespaceFactory().getExpressionMethodInfo(typeDot.getReturn(), method.equalMethod(nodeProperty));
					if (typeMethod == null)
						throw new NoSuchExpressionMethodException(typeDot.getReturn(),
								method.equalMethod(nodeProperty), nodeProperty);
					if (!typeMethod.getValue().isAssignableFrom(infoCurrent.getImplicitType()))
						throw new IncompatibleExpressionMethodTypeException(typeDot.getReturn(),
								method.equalMethod(nodeProperty), typeMethod.getValue(), infoCurrent.getImplicitType(),
								nodeProperty);
					infoCurrent.replaceImplicitType(typeMethod.getReturn());
				}
				// Now we can call the attribute assigner and assign the
				// value.
				final IValue typeDotAssigner = factory.getNamespaceFactory()
						.getDotAssignerInfo(infoProperty.getImplicitType(), property);
				if (typeDotAssigner == null)
					throw new NoSuchDotAssignerException(infoProperty.getImplicitType(), property,
							nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
				if (!typeDotAssigner.getValue().isAssignableFrom(infoCurrent.getImplicitType()))
					throw new IncompatibleDotAssignerTypeException(infoProperty.getImplicitType(), property,
							typeDotAssigner.getValue(), infoCurrent.getImplicitType(),
							nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
				break;
			}
			case BRACKET: {
				final NodeInfo infoEvaluated = nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1)
						.jjtAccept(this);
				if (!infoEvaluated.hasImplicitType())
					throw new UnreachableCodeException(nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
				// Compound assignments (a[b]+=c etc.) are the same as
				// a[b]=a[b]+c etc.
				// First we need to evaluate the last attribute accessor (a[b]),
				// then the expression method (+).
				if (method != EMethod.EQUAL) {
					final IPropertyReturn typeBracket = factory.getNamespaceFactory()
							.getBracketAccessorInfo(infoProperty.getImplicitType());
					if (typeBracket == null)
						throw new NoSuchBracketAccessorException(infoProperty.getImplicitType(),
								nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
					if (!typeBracket.getProperty().isAssignableFrom(infoEvaluated.getImplicitType()))
						throw new IncompatibleBracketAccessorTypeException(infoProperty.getImplicitType(),
								typeBracket.getProperty(), infoEvaluated.getImplicitType(),
								nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
					final IValueReturn typeMethod = factory.getNamespaceFactory()
							.getExpressionMethodInfo(typeBracket.getReturn(), method.equalMethod(nodeProperty));
					if (typeMethod == null)
						throw new NoSuchExpressionMethodException(typeBracket.getReturn(),
								method.equalMethod(nodeProperty),
								nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
					if (!typeMethod.getValue().isAssignableFrom(infoCurrent.getImplicitType()))
						throw new IncompatibleExpressionMethodTypeException(typeBracket.getReturn(),
								method.equalMethod(nodeProperty), typeMethod.getValue(), infoCurrent.getImplicitType(),
								nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
					infoCurrent.replaceImplicitType(typeMethod.getReturn());
				}
				final IPropertyValue typeAssign = factory.getNamespaceFactory()
						.getBracketAssignerInfo(infoProperty.getImplicitType());
				if (typeAssign == null)
					throw new NoSuchBracketAssignerException(infoProperty.getImplicitType(),
							nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
				if (!typeAssign.getProperty().isAssignableFrom(infoEvaluated.getImplicitType()))
					throw new IncompatibleBracketAssignerPropertyTypeException(infoProperty.getImplicitType(),
							typeAssign.getProperty(), infoEvaluated.getImplicitType(),
							nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
				if (!typeAssign.getValue().isAssignableFrom(infoCurrent.getImplicitType()))
					throw new IncompatibleBracketAssignerValueTypeException(infoProperty.getImplicitType(),
							typeAssign.getValue(), infoCurrent.getImplicitType(),
							nodeProperty.getPropertyNode(nodeProperty.getPropertyNodeCount() - 1));
				break;
			}
			// $CASES-OMITTED$
			default:
				throw new UnhandledEnumException(nodeProperty.getPropertyType(nodeProperty.getPropertyNodeCount() - 1),
						nodeProperty);
			}
			break;
		}
		default:
			throw new SemanticsException(NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_NODE_DURING_TYPECHECKING, node),
					node);
		}
	}

	@Override
	public NodeInfo visit(final ASTExpressionNode node) throws SemanticsException {
		// Empty expression node.
		if (node.isLeaf())
			return new NodeInfo(null, SimpleVariableType.NULL);

		// Get type of left most node.
		final NodeInfo infoLhs = node.jjtGetChild(0).jjtAccept(this);
		if (!infoLhs.hasImplicitType()) {
			if (node.jjtGetNumChildren() > 1)
				throw new UnreachableCodeException(node.jjtGetChild(1));
			return infoLhs;
		}

		// Process expression methods from the left to the right.
		for (int i = 1; i < node.jjtGetNumChildren(); ++i) {
			// Get type from right hand side.
			final Node rhs = node.jjtGetChild(i);
			final NodeInfo infoRhs = rhs.jjtAccept(this);
			if (!infoRhs.hasImplicitType()) {
				if (i < node.jjtGetNumChildren() - 1)
					throw new UnreachableCodeException(node.jjtGetChild(i + 1));
				return infoRhs.unifyJumps(infoLhs);
			}
			// Check if there is such an expression method.
			final IValueReturn typeValueReturn = factory.getNamespaceFactory().getExpressionMethodInfo(infoLhs.getImplicitType(), rhs.getSiblingMethod());
			if (typeValueReturn == null)
				throw new NoSuchExpressionMethodException(infoLhs.getImplicitType(), rhs.getSiblingMethod(), node.jjtGetChild(i));
			if (!typeValueReturn.getValue().isAssignableFrom(infoRhs.getImplicitType()))
				throw new IncompatibleExpressionMethodTypeException(infoLhs.getImplicitType(),
						rhs.getSiblingMethod(), typeValueReturn.getValue(), infoRhs.getImplicitType(), rhs);
			infoLhs.unifyJumps(infoRhs);
			infoLhs.replaceImplicitType(typeValueReturn.getReturn());
		}
		return infoLhs;
	}

	@Override
	public NodeInfo visit(final ASTAssignmentExpressionNode node) throws SemanticsException {
		final NodeInfo infoResult = node.getAssignValueNode().jjtAccept(this);
		if (!infoResult.hasImplicitType())
			throw new UnreachableCodeException(node.getAssignableNode(node.getAssignableNodeCount()));
		for (int i = node.getAssignableNodeCount(); i-- > 0;)
			assignmentStep(node.getAssignMethod(i), node.getAssignableNode(i), infoResult);
		return infoResult;
	}

	/**
	 * Simply returns a {@link NumberLangObject}.
	 */
	@Override
	public NodeInfo visit(final ASTNumberNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.NUMBER);
	}

	/**
	 * An array literal consists of any number of array times, eg.
	 * <code>[1,2,3]</code>. It cannot contain any return clauses. The implicit
	 * return type is the unified return type of all all array items.
	 */
	@Override
	public NodeInfo visit(final ASTArrayNode node) throws SemanticsException {
		final NodeInfo info = new NodeInfo(null, SimpleVariableType.NULL);
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			final NodeInfo newInfo = node.jjtGetChild(i).jjtAccept(this);
			info.unify(newInfo);
			// When any item of the array cannot complete normally
			// the array cannot be constructed.
			if (!newInfo.hasImplicitType()) {
				if (i < node.jjtGetNumChildren() - 1)
					throw new UnreachableCodeException(node.jjtGetChild(i + 1));
				return newInfo;
			}
		}
		info.replaceImplicitType(GenericVariableType.forArray(info.getImplicitType()));
		return info;
	}

	/**
	 * A hash literal consists of any number of hash entries, eg.
	 * <code>{3:4,0:1}</code>. It cannot contain any return clauses. The
	 * implicit return type is the unified return type of all all hash entries.
	 */
	@Override
	public NodeInfo visit(final ASTHashNode node) throws SemanticsException {
		final NodeInfo unitKey = new NodeInfo(null, SimpleVariableType.NULL);
		final NodeInfo unitValue = new NodeInfo(null, SimpleVariableType.NULL);
		for (int i = 0; i < node.jjtGetNumChildren() - 1; i += 2) {
			// Hash key can be an identifier, as in {key: "value"}
			final NodeInfo infoKey = (node.jjtGetChild(i) instanceof ASTIdentifierNameNode)
					? new NodeInfo(null, SimpleVariableType.STRING)
					: node.jjtGetChild(i).jjtAccept(this);
			final NodeInfo infoValue = node.jjtGetChild(i + 1).jjtAccept(this);
			// When any item of the hash cannot complete normally
			// the hash can never be constructed successfully.
			unitKey.unify(infoKey);
			if (!infoKey.hasImplicitType()) {
				if (i < node.jjtGetNumChildren() - 3)
					throw new UnreachableCodeException(node.jjtGetChild(i + 2));
				return infoKey;
			}
			unitValue.unify(infoValue);
			if (!infoValue.hasImplicitType()) {
				if (i < node.jjtGetNumChildren() - 3)
					throw new UnreachableCodeException(node.jjtGetChild(i + 3));
				return infoValue;
			}
		}
		unitKey.unifyJumps(unitValue);
		unitKey.replaceImplicitType(GenericVariableType.forHash(unitKey.getImplicitType(), unitValue.getImplicitType()));
		return unitKey;
	}

	/**
	 * Simply returns the {@link NullLangObject}.
	 */
	@Override
	public NodeInfo visit(final ASTNullNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.NULL);
	}

	/**
	 * Simply returns the {@link BooleanLangObject} <code>true</code> or <code>false</code>.
	 */
	@Override
	public NodeInfo visit(final ASTBooleanNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.BOOLEAN);
	}

	/**
	 * <p>
	 * For a local variable, the type is as specified by the symbol table.
	 * Otherwise, we ask the external scope for the variable type.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTVariableNode node) throws SemanticsException {
		return getInfo(node);
	}

	/**
	 * This simply returns the string type.
	 */
	@Override
	public NodeInfo visit(final ASTStringNode node) throws SemanticsException {
		final NodeInfo info = new NodeInfo(null, SimpleVariableType.STRING);
		for (int i = 0; i < node.getStringNodeCount(); ++i) {
			final NodeInfo infoChild = node.getStringNode(i).jjtAccept(this);
			info.unifyJumps(infoChild);
			if (!infoChild.hasImplicitType()) {
				if (i < node.getStringNodeCount()-1)
					throw new UnreachableCodeException(node.getStringNode(i+1));
				infoChild.clearImplicitType();
				return infoChild;
			}
			checkObject(infoChild, node);
		}
		return info;
	}

	/**
	 * This simply returns the string type.
	 */
	@Override
	public NodeInfo visit(final ASTStringCharactersNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.STRING);
	}

	/**
	 * <p>
	 * A list of statements executed sequentially. However, a statement may
	 * contain a break, continue, return, or throw clause statement which skips
	 * the remaining statements.
	 * </p>
	 * <p>
	 * We start at the first statement and proceed sequentially. When a
	 * statement never completes normally and it is not the last statement, the
	 * remaining statements are dead code and we issue an error. Otherwise, we
	 * unify all return types and take the implicit return type of the last
	 * statement.
	 * </p><p>
	 * When a statement is the empty node (<code>;</code>) the value from the
	 * last statement is returned during evaluation, so we need to pass through
	 * the last return type as well.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTStatementListNode node) throws SemanticsException {
		final NodeInfo info = new NodeInfo(null, SimpleVariableType.NULL);
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			// Pass through value for empty nodes.
			if (node.jjtGetChild(i).jjtGetNodeId() == JJTEMPTYNODE)
				continue;
			final NodeInfo infoChild = node.jjtGetChild(i).jjtAccept(this);
			if (infoChild.hasImplicitType())
				info.replaceImplicitType(infoChild.getImplicitType());
			else if (i < node.jjtGetNumChildren() - 1)
				// Unreachable code
				throw new UnreachableCodeException(node.jjtGetChild(i + 1));
			else
				info.clearImplicitType();
			info.unifyJumps(infoChild);
		}
		return info;
	}

	/**
	 * <p>
	 * The condition cannot contain any return clauses. When it does not return
	 * normally, the if-else-clause never returns normally. Otherwise, the
	 * implicit return type must be a boolean.
	 * </p>
	 * <p>
	 * When there is an else-clause, we unify both types. In case both branches
	 * do not return normally, the if-else-clause does not return normally. When
	 * there is no else-clause, this node may return {@link NullLangObject} and
	 * we unify the if-clause type with {@link ELangObjectClass#NULL}. Finally,
	 * we return the unified type.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTIfClauseNode node) throws SemanticsException {
		final NodeInfo infoCondition = node.getConditionNode().jjtAccept(this);
		if (!infoCondition.hasImplicitType())
			throw new UnreachableCodeException(node.getConditionNode());
		if (!infoCondition.getImplicitType()
				.equalsType(SimpleVariableType.BOOLEAN))
			throw new IncompatibleConditionTypeException(infoCondition.getImplicitType(), node);
		final NodeInfo infoIf = node.getIfNode().jjtAccept(this);
		if (node.hasElseNode())
			infoIf.unify(node.getElseNode().jjtAccept(this));
		else
			infoIf.unifiyImplicitType(SimpleVariableType.NULL);
		return infoIf;
	}

	/**
	 * <p>
	 * For a plain loop, first we need to check the variable types of the
	 * for-header. The implicit return type of the for-loop is then determined
	 * solely by the body node.
	 * </p>
	 * <p>
	 * For an enhanced loop, first we need to get the type of the object for
	 * iteration; and we need to check whether it matches the declared or
	 * inferred type of the iterating variable. The implicit return type of the
	 * for-loop is then determined solely by the body node.
	 * </p>
	 * <p>
	 * Also, when there is any matching break or continue clause, we add
	 * {@link NullLangObject} to the list of implicit return types.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTForLoopNode node) throws SemanticsException {
		final NodeInfo info;
		if (node.isEnhancedLoop()) {
			// Get type from the object to iterate over.
			info = node.getEnhancedIteratorNode().jjtAccept(this);
			// Loop body is never reached.
			if (!info.hasImplicitType())
				throw new UnreachableCodeException(node.getBodyNode());
			// Check if it is iterable at all.
			if (!info.getImplicitType().isIterable())
				throw new IterationNotSupportedException(info.getImplicitType(), node.getEnhancedIteratorNode());
			// Get type of the iteration variable and verify its type.
			final IVariableType typeVariable = getDeclaredType(null, node);
			if (!typeVariable.isAssignableFrom(info.getImplicitType().getIterableItemType()))
				throw new IncompatibleForLoopHeaderTypeAssignmentException(typeVariable,
						info.getImplicitType().getIterableItemType(), node.getEnhancedIteratorNode());
		}
		else {
			// Analyze the for-header as well.
			info = node.getPlainInitializerNode().jjtAccept(this);
			info.unifyJumps(node.getPlainConditionNode().jjtAccept(this));
			info.unifyJumps(node.getPlainIncrementNode().jjtAccept(this));
		}
		// Return the return type of the for-loop's body node.
		final NodeInfo infoBody = node.getBodyNode().jjtAccept(this).unifyJumps(info);
		infoBody.removeLabel(node.getLabel());
		infoBody.unifiyImplicitType(SimpleVariableType.NULL);
		return infoBody;
	}

	/**
	 * <p>
	 * The expression in the while header must be of type
	 * {@link BooleanLangObject}. The return type of the while clause is the
	 * return type of its body node.
	 * </p>
	 * <p>
	 * When the body contains any break or continue clauses for this loop, the
	 * body may return {@link NullLangObject}, so we add it to the list of
	 * implicit return types.
	 * </p>
	 *
	 * <pre>
	 *   while(randomBoolean()) {
	 *     if (randomBoolean()) break;
	 *     2;
	 *   }
	 * </pre>
	 *
	 * The above code can return {@link NullLangObject} or
	 * {@link NumberLangObject}.
	 */
	@Override
	public NodeInfo visit(final ASTWhileLoopNode node) throws SemanticsException {
		return visitLoop(node.getWhileHeaderNode(), node.getBodyNode(), node.getLabel(), true);
	}

	/**
	 * <p>
	 * This is similar to a {@link ASTTernaryExpressionNode}, minus the
	 * condition. As try-catch clause returns either the result of the try
	 * clause or the result of the catch clause. Thus we need to unify both
	 * types.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTTryClauseNode node) throws SemanticsException {
		return node.getTryNode().jjtAccept(this).unify(node.getCatchNode().jjtAccept(this));
	}

	/**
	 * <p>
	 * In strict mode, we enforce the types of each case clause to be compatible
	 * with the type to switch on.
	 * </p>
	 * <p>
	 * Other than that, we check all child nodes. The return type is the
	 * combination of all cases, including the default case. When there is a
	 * break, we merge the implicit return type with {@link NullLangObject}.
	 * </p>
	 * <p>
	 * When there is code between a break or continue clause and the next case,
	 * this is unreachable code and an error is thrown.
	 * </p>
	 * <p>
	 * When there is no default case, we need to merge {@link NullLangObject} to
	 * the list of implicit return types as it may happen that no case applies.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTSwitchClauseNode node) throws SemanticsException {
		final NodeInfo infoResult = new NodeInfo();

		// Type of the switch expression.
		final NodeInfo infoSwitch = node.getSwitchValueNode().jjtAccept(this);
		if (infoSwitch.hasLabel())
			throw new IllegalJumpClauseException(EJump.BREAK, infoSwitch.getAnyLabel(), node.getSwitchValueNode());
		if (infoSwitch.hasReturnType())
			throw new IllegalJumpClauseException(EJump.RETURN, null, node.getSwitchValueNode());
		if (!infoSwitch.hasImplicitType())
			throw new UnreachableCodeException(node.jjtGetChild(node.jjtGetNumChildren() - 1));
		final IVariableType typeSwitch = infoSwitch.getImplicitType();
		infoResult.unifyError(infoSwitch);

		// Check cases and case bodies.
		int i = 0;
		while (i < node.getCaseCount()) {
			switch (node.getCaseType(i)) {
			case SWITCHCASE: {
				final NodeInfo infoCase = node.getCaseNode(i).jjtAccept(this);
				if (infoCase.hasLabel())
					throw new IllegalJumpClauseException(EJump.BREAK, infoCase.getAnyLabel(), node.getCaseNode(i));
				if (infoCase.hasReturnType())
					throw new IllegalJumpClauseException(EJump.RETURN, null, node.getCaseNode(i));
				if (!infoCase.hasImplicitType())
					throw new UnreachableCodeException(node.getCaseNode(i));
				if (config.hasOption(TREAT_UNMATCHING_SWITCH_TYPE_AS_ERROR)
						&& !typeSwitch.isAssignableFrom(infoCase.getImplicitType()))
					throw new IncompatibleSwitchCaseTypeException(typeSwitch, infoCase.getImplicitType(),
							node.getCaseNode(i));
				infoResult.unifyError(infoCase);
				++i;
				break;
			}
			case SWITCHDEFAULT: {
				final NodeInfo infoClause = new NodeInfo();
				i = visitSwitchClause(node, EMethod.SWITCHDEFAULT, i, infoClause);
				if (infoClause.removeLabel(node.getLabel()))
					infoClause.unifiyImplicitType(SimpleVariableType.NULL);
				if (!infoClause.hasImplicitType() && i < node.getCaseCount() - 1)
					throw new UnreachableCodeException(node.getCaseNode(i+1));
				infoResult.unify(infoClause);
				break;
			}
			case SWITCHCLAUSE: {
				final NodeInfo infoClause = new NodeInfo();
				i = visitSwitchClause(node, EMethod.SWITCHCLAUSE, i, infoClause);
				if (infoClause.removeLabel(node.getLabel()))
					infoClause.unifiyImplicitType(SimpleVariableType.NULL);
				infoResult.unify(infoClause);
				break;
			}
			// $CASES-OMITTED$
			default:
				throw new UnhandledEnumException(node.getCaseType(i), node.getCaseNode(i));
			}
		}
		// Switch may return null when there is no default case
		// and no other case is taken.
		if (!node.hasDefaultCase())
			infoResult.unifiyImplicitType(SimpleVariableType.NULL);
		return infoResult;
	}

	/**
	 * @see #visit(ASTWhileLoopNode)
	 */
	@Override
	public NodeInfo visit(final ASTDoWhileLoopNode node) throws SemanticsException {
		return visitLoop(node.getDoFooterNode(), node.getBodyNode(), node.getLabel(), false);
	}

	/**
	 * <p>
	 * (Almost) any object can be converted to a string, so we only need to check the
	 * error message node itself. However, we need to check if the type is the special
	 * void type and throw an error if it is, as the void type must not be used for
	 * anything.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTExceptionNode node) throws SemanticsException {
		final NodeInfo info = node.getErrorMessageNode().jjtAccept(this);
		if (info.hasImplicitType()) {
			checkObject(info, node.getErrorMessageNode());
			info.replaceImplicitType(SimpleVariableType.EXCEPTION);
		}
		return info;
	}

	/**
	 * <p>
	 * Never completes normally and never returns anything.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTThrowClauseNode node) throws SemanticsException {
		return new NodeInfo(true);
	}

	@Override
	public NodeInfo visit(final ASTBreakClauseNode node) throws SemanticsException {
		return new NodeInfo(node.getLabel());
	}

	@Override
	public NodeInfo visit(final ASTContinueClauseNode node) throws SemanticsException {
		return new NodeInfo(node.getLabel());
	}

	/**
	 * <p>
	 * Never completes normally and returns the type of its first node, if it
	 * exists, or nothing.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTReturnClauseNode node) throws SemanticsException {
		if (!node.hasReturn())
			return new NodeInfo(VoidType.INSTANCE, null);
		final NodeInfo info = node.getReturnNode().jjtAccept(this);
		if (!info.hasImplicitType())
			throw new UnreachableCodeException(node);
		checkObject(info, node.getReturnNode());
		info.unifyReturnType(info.getImplicitType());
		info.clearImplicitType();
		return info;
	}

	/**
	 * <p>
	 * Message node always returns the message coerced to a
	 * {@link StringLangObject}. (Almost) any object can be
	 * converted to a string. However, we need to check if
	 * the type is the special void type and throw an error
	 * if it is, as the void type must not be used for
	 * anything.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTLogNode node) throws SemanticsException {
		final NodeInfo info = node.getLogMessageNode().jjtAccept(this);
		if (info.hasImplicitType()) {
			checkObject(info, node.getLogMessageNode());
			info.replaceImplicitType(SimpleVariableType.STRING);
		}
		return info;
	}

	/**
	 * <p>
	 * This node requires two steps. Firstly, getting the return type of the
	 * function body and checking whether it is compatible with the declared
	 * type. Secondly, acquiring the types of all arguments and return the
	 * function type.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTFunctionNode node) throws SemanticsException {
		// Get actual return type.
		final NodeInfo infoActualReturn = node.getBodyNode().jjtAccept(this);

		// Declared return type.
		final IVariableType typeDeclaredReturn;
		if (node.hasType()) {
			final NodeInfo info = node.getTypeNode().jjtAccept(this);
			typeDeclaredReturn = info.hasImplicitType() ? info.getImplicitType()
					: SimpleVariableType.OBJECT;
		}
		else {
			// No declared type, guess type from actual return type.
			typeDeclaredReturn = (infoActualReturn.hasImplicitType() ? infoActualReturn.getImplicitType()
					: SimpleVariableType.NULL)
							.union(infoActualReturn.hasReturnType() ? infoActualReturn.getReturnType()
									: SimpleVariableType.NULL);
		}

		// Check with actual return type.
		assertFunctionReturnType(infoActualReturn, typeDeclaredReturn, node);

		// Return type of function.
		return new NodeInfo(null, getFunctionType(typeDeclaredReturn, node));
	}

	/** @see #visitPropertyExpression(ASTPropertyExpressionNode, int) */
	@Override
	public NodeInfo visit(final ASTPropertyExpressionNode node) throws SemanticsException {
		return visitPropertyExpression(node, node.getPropertyNodeCount());
	}

	@Override
	public NodeInfo visit(final ASTIdentifierNameNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.STRING);
	}

	/**
	 * <p>
	 * With header does not affect the return type, so we return the body type.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTWithClauseNode node) throws SemanticsException {
		return node.getBodyNode().jjtAccept(this);
	}

	/**
	 * Same as anonymous functions.
	 *
	 * @see #visit(ASTFunctionNode)
	 */
	@Override
	public NodeInfo visit(final ASTFunctionClauseNode node) throws SemanticsException {
		// Declared return type.
		final IVariableType typeFunction = getDeclaredType(node.getScope(), node);
		final IVariableType typeDeclaredReturn = typeFunction.getGeneric(0);

		// Check with actual return type.
		final NodeInfo infoActualReturn = node.getBodyNode().jjtAccept(this);
		assertFunctionReturnType(infoActualReturn, typeDeclaredReturn, node);

		// Return type of function.
		return new NodeInfo(null, typeFunction);
	}

	/**
	 * <p>
	 * Empty node returns <code>null</code>. Passing through the return type is
	 * handled by {@link #visit(ASTStatementListNode)}
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTEmptyNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.NULL);
	}

	/** <p>Return type not used for anything, but during evaluation they return <code>null</code>.</p> */
	@Override
	public NodeInfo visit(final ASTLosNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.NULL);
	}

	/** <p>Simply returns an object of type regex. */
	@Override
	public NodeInfo visit(final ASTRegexNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.REGEX);
	}

	/** @see #visit(ASTIfClauseNode) */
	@Override
	public NodeInfo visit(final ASTTernaryExpressionNode node) throws SemanticsException {
		// Check if condition is boolean.
		final NodeInfo infoCondition = node.getConditionNode().jjtAccept(this);
		if (!infoCondition.hasImplicitType())
			throw new UnreachableCodeException(node.getConditionNode());
		if (!infoCondition.getImplicitType()
				.equalsType(SimpleVariableType.BOOLEAN))
			throw new IncompatibleConditionTypeException(infoCondition.getImplicitType(), node);
		// Unify if-clause and else-clause.
		return node.getIfNode().jjtAccept(this).unify(node.getElseNode().jjtAccept(this));
	}

	/**
	 * <p>
	 * Contains only an expression in parentheses, eg. <code>(1+2)</code>, so we
	 * simply return the type of this node's only child.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTParenthesisExpressionNode node) throws SemanticsException {
		return node.getNode().jjtAccept(this);
	}

	/**
	 * <p>
	 * Equality is defined for (almost) every object and always
	 * returns a {@link BooleanLangObject}. However, we need to
	 * check whether any type is the special void type, as it must
	 * not be used in any way. The return type is always a
	 * {@link BooleanLangObject}.
	 * </p><p>
	 * The methods <code>=~</code> and <code>!~</code> both make use
	 * of the method {@link EMethod#EQUAL_TILDE} and coerce the result
	 * to a boolean. Here we need to check if this expression
	 * method is defined.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTEqualExpressionNode node) throws SemanticsException {
		// Empty expression node.
		if (node.isLeaf())
			return new NodeInfo(null, SimpleVariableType.NULL);

		// Get type for first node.
		final NodeInfo infoRes = node.jjtGetChild(0).jjtAccept(this);
		if (!infoRes.hasImplicitType()) {
			if (node.jjtGetNumChildren() > 1)
				throw new UnreachableCodeException(node.jjtGetChild(1));
			return infoRes;
		}
		checkObject(infoRes, node.jjtGetChild(0));

		// Check methods for all other nodes.
		for (int i = 1; i < node.jjtGetNumChildren(); ++i) {
			final NodeInfo infoChild = node.jjtGetChild(i).jjtAccept(this);
			infoRes.unifyJumps(infoChild);
			if (!infoChild.hasImplicitType()) {
				if (i < node.jjtGetNumChildren() - 1)
					throw new UnreachableCodeException(node.jjtGetChild(i + 1));
				infoRes.clearImplicitType();
				return infoRes;
			}
			switch (node.jjtGetChild(i).getSiblingMethod()) {
			case EXCLAMATION_TILDE:
			case EQUAL_TILDE: {
				final IValueReturn typeValueReturn= factory.getNamespaceFactory().getExpressionMethodInfo(
						infoRes.getImplicitType(), EMethod.EQUAL_TILDE);
				if (typeValueReturn == null)
					throw new NoSuchExpressionMethodException(infoRes.getImplicitType(),
							EMethod.EQUAL_TILDE, node.jjtGetChild(i-1));
				if (!typeValueReturn.getValue().isAssignableFrom(infoChild.getImplicitType()))
					throw new IncompatibleExpressionMethodTypeException(infoRes.getImplicitType(),
							EMethod.EQUAL_TILDE, typeValueReturn.getValue(), infoChild.getImplicitType(),
							node.jjtGetChild(i));
				break;
			}
			// only == != === !=== =~ !~ can occur
			//$CASES-OMITTED$
			default:
				break;
			}
			checkObject(infoChild, node.jjtGetChild(i));
			infoRes.unifiyImplicitType(infoChild.getImplicitType());
		}
		infoRes.replaceImplicitType(SimpleVariableType.BOOLEAN);
		return infoRes;
	}

	/**
	 * <p>
	 * Comparisons are defined for (almost) all objects, so we simply
	 * check all child nodes. However, we need to check for the special
	 * void type as it must not be used in any way. The return type
	 * is always a {@link BooleanLangObject}.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTComparisonExpressionNode node) throws SemanticsException {
		// Empty expression node.
		if (node.isLeaf())
			return new NodeInfo(null, SimpleVariableType.NULL);

		// Get type for first node.
		final NodeInfo infoRes = node.jjtGetChild(0).jjtAccept(this);
		if (!infoRes.hasImplicitType()) {
			if (node.jjtGetNumChildren() > 1)
				throw new UnreachableCodeException(node.jjtGetChild(1));
			return infoRes;
		}
		checkObject(infoRes, node.jjtGetChild(0));

		// Check methods for all other nodes.
		for (int i = 1; i < node.jjtGetNumChildren(); ++i) {
			final NodeInfo infoChild = node.jjtGetChild(i).jjtAccept(this);
			infoRes.unifyJumps(infoChild);
			if (!infoChild.hasImplicitType()) {
				if (i < node.jjtGetNumChildren() - 1)
					throw new UnreachableCodeException(node.jjtGetChild(i + 1));
				infoRes.clearImplicitType();
				return infoRes;
			}
			checkObject(infoChild, node.jjtGetChild(i));
			infoRes.unifiyImplicitType(infoChild.getImplicitType());
		}
		infoRes.replaceImplicitType(SimpleVariableType.BOOLEAN);
		return infoRes;
	}

	/**
	 * <p>
	 * We need to check the single child node. If the unary method
	 * is assigning (eg. <code>++i</code>), we also need to check
	 * the variable types for this assignment.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTUnaryExpressionNode node) throws SemanticsException {
		final NodeInfo infoChild = node.getUnaryNode().jjtAccept(this);
		if (!infoChild.hasImplicitType())
			throw new UnreachableCodeException(node);
		checkObject(infoChild, node.getUnaryNode());
		if (node.getUnaryMethod().isAssigning())
			assignmentStep(node.getUnaryMethod(), node.getUnaryNode(), infoChild);
		else if (node.getUnaryMethod() == EMethod.EXCLAMATION)
			infoChild.replaceImplicitType(SimpleVariableType.BOOLEAN);
		return infoChild;
	}

	/**
	 * <p>
	 * Same as {@link #visit(ASTUnaryExpressionNode}. Postfix operations <code>i++</code>
	 * and <code>i--</code> cannot change the variable type, so it is irrelevant when
	 * the result is used during evaluation.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTPostUnaryExpressionNode node) throws SemanticsException {
		final NodeInfo infoChild = node.getUnaryNode().jjtAccept(this);
		if (!infoChild.hasImplicitType())
			throw new UnreachableCodeException(node);
		checkObject(infoChild, node.getUnaryNode());
		if (node.getUnaryMethod().isAssigning())
			assignmentStep(node.getUnaryMethod(), node.getUnaryNode(), infoChild);
		return infoChild;
	}

	@Override
	public NodeInfo visit(final ASTScopeExternalNode node) throws SemanticsException {
		throw new SemanticsException(
				NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_NODE_DURING_TYPECHECKING, node.toString()), node);
	}

	@Override
	public NodeInfo visit(final ASTVariableDeclarationClauseNode node) throws SemanticsException {
		final IVariableType type = this.getDeclaredType(null, node);
		if (node.hasAssignment()) {
			final NodeInfo info = node.getAssignmentNode().jjtAccept(this);
			if (!info.hasImplicitType())
				throw new UnreachableCodeException(node);
			if (!type.isAssignableFrom(info.getImplicitType()))
				throw new IncompatibleVariableAssignmentTypeException(type, info.getImplicitType(), node);
			info.replaceImplicitType(type);
			return info;
		}
		return new NodeInfo(null, type);
	}

	@Override
	public NodeInfo visit(final ASTScopeManualNode node) throws SemanticsException {
		throw new SemanticsException(
				NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_NODE_DURING_TYPECHECKING, node.toString()), node);
	}

	@Override
	public NodeInfo visit(final ASTScopeGlobalNode node) throws SemanticsException {
		throw new SemanticsException(
				NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_NODE_DURING_TYPECHECKING, node.toString()), node);
	}

	@Override
	public NodeInfo visit(final ASTVariableTypeNode node) throws SemanticsException {
		final IVariableTypeBuilder builder = new VariableTypeBuilder();
		for (final EVariableTypeFlag flag : node.getFlags())
			if (flag != null)
				builder.setFlag(flag);
		builder.setBasicType(node.getVariableType());
		for (int i = 0; i < node.getGenericsCount(); ++i) {
			final NodeInfo info = node.getGenericsNode(i).jjtAccept(this);
			builder.append(info.hasImplicitType() ? info.getImplicitType()
					: SimpleVariableType.OBJECT);
		}
		final IVariableType type;
		try {
			type = builder.build();
		}
		catch (final IllegalVariableTypeException e) {
			throw new SemanticsException(NullUtil.orEmpty(e.getMessage()), node);
		}
		return new NodeInfo(null, type);
	}

	@Override
	public NodeInfo visit(final ASTFunctionArgumentNode node) throws SemanticsException {
		if (node.hasType())
			return node.getTypeNode().jjtAccept(this);
		return new NodeInfo(null, SimpleVariableType.OBJECT);
	}

	@Nullable
	public static IVariableType check(final Node node, final IVariableType[] symbolTypeTable,
			final IEvaluationContextContract<?> factory, final ISeverityConfig config,
			final IScopeDefinitions scopeDefs) throws SemanticsException {
		final VariableTypeCheckVisitor v = new VariableTypeCheckVisitor(symbolTypeTable, factory, config);
		v.visitScopeDefs(scopeDefs);
		final NodeInfo info = node.jjtAccept(v);
		return info.hasImplicitType() ? info.getImplicitType() : null;
	}

	protected static class NodeInfo {
		@Nullable
		private IVariableType unifiedReturnType;
		@Nullable
		private IVariableType implicitReturnType;
		@Nullable
		private Set<String> labelSet;
		private boolean hasThrowingJump;

		public NodeInfo() {
		}

		public NodeInfo(final boolean throwingJump) {
			this.hasThrowingJump = throwingJump;
		}

		public NodeInfo(@Nullable final IVariableType unifiedReturnType,
				@Nullable final IVariableType implicitReturnType) {
			this(unifiedReturnType, implicitReturnType, false, null);
		}

		private NodeInfo(@Nullable final IVariableType unifiedReturnType,
				@Nullable final IVariableType implicitReturnType, final boolean hasThrowingJump,
				@Nullable final Set<String> labelSet) {
			this.unifiedReturnType = unifiedReturnType;
			this.implicitReturnType = implicitReturnType;
			this.hasThrowingJump = hasThrowingJump;
			this.labelSet = labelSet;
		}

		public NodeInfo(@Nullable final String label) {
			getLabelSet().add(label);
		}

		public NodeInfo copy() {
			return new NodeInfo(unifiedReturnType, implicitReturnType, hasThrowingJump, new HashSet<>(labelSet));
		}

		private final Set<String> getLabelSet() {
			return (labelSet = labelSet != null ? labelSet : new HashSet<String>());
		}

		public boolean hasReturnType() {
			return unifiedReturnType != null;
		}

		public boolean hasImplicitType() {
			return implicitReturnType != null;
		}

		public boolean hasLabel() {
			return labelSet != null && !labelSet.isEmpty();
		}

		public boolean removeLabel(@Nullable final String label) {
			return labelSet != null ? labelSet.remove(label) : false;
		}

		@Nullable
		public String getAnyLabel() {
			final Set<String> set = labelSet;
			if (set == null)
				throw new NoSuchElementException();
			return set.iterator().next();
		}

		public boolean hasThrowingJump() {
			return hasThrowingJump;
		}

		public IVariableType getReturnType() {
			if (unifiedReturnType != null)
				return unifiedReturnType;
			throw new NullPointerException();
		}

		public IVariableType getImplicitType() {
			if (implicitReturnType != null)
				return implicitReturnType;
			throw new NullPointerException();
		}

		public void addLabel(final Collection<String> set) {
			getLabelSet().addAll(set);
		}

		public void addLabel(@Nullable final String label) {
			getLabelSet().add(label);
		}

		public NodeInfo addThrowingJump() {
			hasThrowingJump = true;
			return this;
		}

		public void clearImplicitType() {
			this.implicitReturnType = null;
		}

		public void clearUnifiedType() {
			this.unifiedReturnType = null;
		}

		public void replaceImplicitType(final IVariableType type) {
			this.implicitReturnType = type;
		}

		public void replaceReturnType(final IVariableType type) {
			this.unifiedReturnType = type;
		}

		public NodeInfo unify(final NodeInfo info) {
			unifyJumps(info);
			if (info.hasImplicitType())
				unifiyImplicitType(info.getImplicitType());
			return this;
		}

		public NodeInfo unifyJumps(final NodeInfo info) {
			if (info.hasReturnType())
				unifyReturnType(info.getReturnType());
			unifyError(info);
			if (info.labelSet != null)
				addLabel(info.labelSet);
			return this;
		}

		public void unifyReturnType(final IVariableType type) {
			if (unifiedReturnType != null)
				unifiedReturnType = unifiedReturnType.union(type);
			else
				unifiedReturnType = type;
		}

		public void unifiyImplicitType(final IVariableType type) {
			if (implicitReturnType != null)
				implicitReturnType = implicitReturnType.union(type);
			else
				implicitReturnType = type;
		}

		public NodeInfo unifyError(final NodeInfo info) {
			if (info.hasThrowingJump())
				this.addThrowingJump();
			return this;
		}

		@Override
		public String toString() {
			return NullUtil.messageFormat(CmnCnst.ToString.NODE_INFO, unifiedReturnType,
					implicitReturnType, Boolean.valueOf(hasThrowingJump), labelSet);
		}
	}
}