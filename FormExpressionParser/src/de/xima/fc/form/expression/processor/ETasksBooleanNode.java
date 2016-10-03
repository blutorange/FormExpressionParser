package de.xima.fc.form.expression.processor;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;

@EvaluateProcessorTask(id = FormExpressionParserTreeConstants.JJTBOOLEANNODE)
public enum ETasksBooleanNode implements IEvaluateProcessorTask {
	@EvaluateProcessorInitialTask
	INITIAL {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			p.returnStack.push(BooleanLangObject.create(((ASTBooleanNode)p.currentNode).getBooleanValue()));
			p.endNode();
		}
	},
	;
	@Override
	public abstract void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> processor)
			throws EvaluationException;
}
