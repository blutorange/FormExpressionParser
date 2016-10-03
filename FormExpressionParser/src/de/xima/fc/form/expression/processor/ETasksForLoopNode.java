package de.xima.fc.form.expression.processor;

import java.util.Iterator;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

@EvaluateProcessorTask(id = FormExpressionParserTreeConstants.JJTFORLOOPNODE)
enum ETasksForLoopNode implements IEvaluateProcessorTask {
	@EvaluateProcessorInitialTask
	LOOP_INIT {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			final String var = ((ASTForLoopNode)p.currentNode).getIteratingLoopVariable();
			if (var == null) { // Plain for loop
				p.returnStack.push(NullLangObject.getInstance()); // Default return value is null.
				p.queueChild(0, PLAIN_LOOP_CONDITION); // Evaluate loop initializer.
			}
			else { // enhanced for loop
				p.argObjectStack.push(var); // Save reference to iteration variable name for late.
				p.queueChild(0, ENHANCED_LOOP_BEGIN);// Evaluate object to iterate over.
			}
		}
	},

	PLAIN_LOOP_CONDITION {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			p.returnStack.pop(); // Discard return value from for loop initializer.
			p.queueChild(1, PLAIN_LOOP_BODY); // Evaluate loop condition.
		}
	},
	PLAIN_LOOP_BODY {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			if (p.returnStack.pop().coerceBoolean(p.object).booleanValue()) { // Check evaluated loop condition.
				p.queueChild(3, PLAIN_LOOP_BETWEEN); // Evaluate loop body.
			}
			else {
				p.endNode(); // Done processing this node, remove from the stack.
				// Return value has already been pushed.
			}
		}
	},
	PLAIN_LOOP_BETWEEN {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			final ALangObject res = p.returnStack.pop(); // Get evaluated result of body.
			p.returnStack.pop(); // Remove current return value
			p.returnStack.push(res); // and replace with new return value from the body.
			p.queueChild(2, PLAIN_LOOP_CONDITION); // Evaluate loop condition again.
		}
	},

	ENHANCED_LOOP_BEGIN {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			// Get evaluated iterable, eg. "for (i:10)".
			final Iterator<ALangObject> iter = p.returnStack.pop().getIterable(p.object).iterator();
			if (iter.hasNext()) {
				p.object.getBinding().setVariable((String)p.argObjectStack.peek(), iter.next()); // Set variable value for current iteration.
				p.argObjectStack.push(iter); // Save reference to iterator.
				p.queueChild(1, ENHANCED_LOOP_MAIN); // Evaluate body of the loop.
			}
			else {
				p.returnStack.push(NullLangObject.getInstance()); // Nothing to do. Push null and return.
				p.endNode();
			}
		}
	},
	ENHANCED_LOOP_MAIN {
		@Override
		public void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> p)
				throws EvaluationException {
			@SuppressWarnings("unchecked")
			final Iterator<ALangObject> iter = (Iterator<ALangObject>)p.argObjectStack.peek(); // Get saved iterator.
			if (iter.hasNext()) {
				p.returnStack.pop(); // Discard return value, return value of one of the next iterations will be used.
				p.object.getBinding().setVariable((String)p.argObjectStack.peek2(), iter.next()); // Set variable value for current iteration.
				p.queueChild(1, ENHANCED_LOOP_MAIN); // Evaluate body of the loop.
			}
			else {
				p.argObjectStack.pop(); // Remove reference to iterator.
				p.argObjectStack.pop(); // Remove reference to iteration variable name.
				p.endNode(); // We are done, loop does not continue anymore.
				// Return value is on the stack already.
			}
		}
	}
	;
	@Override
	public abstract void process(final GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> processor)
			throws EvaluationException;
}
