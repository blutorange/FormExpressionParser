package de.xima.fc.form.expression.processor;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.object.ALangObject;

@EvaluateProcessorTask(id = FormExpressionParserTreeConstants.JJTSTATEMENTLISTNODE)
public enum ETasksStatementListNode implements IEvaluateProcessorTask {
	@EvaluateProcessorInitialTask
	QUEUE {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			p.queueChild(p.currentChildren.length-1, FINISH); // Last child's return value will be used.
			for (int i = p.currentChildren.length-1; i --> 0;) // Process the other children.
				p.queueChild(i, PROCESS);
		}
	},
	PROCESS {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			p.returnStack.pop(); // Discard return value, only the result of the last statement matters.
		}
	},
	FINISH {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			p.endNode(); // Pop this node from the stack and return the result of the last child.
		}
	}
	;
	@Override
	public abstract void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> processor)
			throws EvaluationException;
}
