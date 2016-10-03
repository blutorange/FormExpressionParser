package de.xima.fc.form.expression.impl.embedment;

import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;

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
	public void beginEmbedment(final String name) {
	}

	@Override
	public void endEmbedment() {
	}
}
