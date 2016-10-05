package de.xima.fc.form.expression.impl.embedment;

import java.io.Writer;

import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.EvaluationException;

/**
 * A dummy embedment that simply prints any data to stdout.
 * @author madgaksha
 *
 */
public enum DummyEmbedment implements IEmbedment {
	INSTANCE
	;

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

	@Override
	public void setWriter(Writer writer) {
	}

	@Override
	public void flushWriter(IEvaluationContext ec) throws EmbedmentOutputException {
	}
}
