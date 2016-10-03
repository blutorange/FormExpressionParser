package de.xima.fc.form.expression.processor;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

@EvaluateProcessorTask(id = FormExpressionParserTreeConstants.JJTIFCLAUSENODE)
public enum ETasksIfClauseNode implements IEvaluateProcessorTask {
	@EvaluateProcessorInitialTask
	IF_INITIAL {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			p.queueChild(0, IF_BRANCH);
		}
	},
	IF_BRANCH {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			// Check whether condition holds true.
			if (p.returnStack.pop().coerceBoolean(p.object).booleanValue()) {
				p.tailCallChild(1); // Queue evaluation of if-body, this will be the return value of this node.
			}
			else if (p.currentChildren.length==3) {
				p.tailCallChild(2); // Queue evaluation of else-body, return that value as the return value of this node.
			}
			else {
				p.returnStack.push(NullLangObject.getInstance()); // No else, if-conditions not satisifed. Return null.
			}
		}
	}
	;
	@Override
	public abstract void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> processor)
			throws EvaluationException;
}
