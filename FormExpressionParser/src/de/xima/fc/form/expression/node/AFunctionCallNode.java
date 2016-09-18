/* Generated By:JJTree: Do not edit this line. ASTFunctionCall.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EChainType;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;

public abstract class AFunctionCallNode extends MySimpleNode {

	public EChainType chainType;

	public AFunctionCallNode(final int id, final EChainType chainType) {
		super(id);
		this.chainType = chainType;
	}

	public AFunctionCallNode(final FormExpressionParser p, final int id, final EChainType chainType) {
		super(p, id);
		this.chainType = chainType;
	}

	public final EChainType getChainType() {
		return chainType;
	}

	//	public abstract ALangObject chain(final ALangObject thisContext, final IEvaluationContext ec)
	//			throws EvaluationException;

	public abstract void init(String name) throws ParseException;

	public abstract String getMethodName();

}