package de.xima.fc.form.expression.impl.embedment;

import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;

/**
 * A dummy embedment that simply prints any data to stdout.
 * @author madgaksha
 *
 */
public class DummyEmbedment implements IEmbedment {
	private final static class InstanceHolder {
		public final static DummyEmbedment INSTANCE = new DummyEmbedment();
	}
	@Override
	public void output(final String data, final IEvaluationContext ec) {
		System.out.print(data);
	}
	@Override
	public void beginEmbedment(String name, IEvaluationContext ec) throws EvaluationException {
	}
	@Override
	public void endEmbedment(IEvaluationContext ec) throws EvaluationException {		
	}
	@Override
	public Void reset() {
		return null;
	}	

	public static IEmbedment getInstance() {
		return InstanceHolder.INSTANCE;
	}
}
