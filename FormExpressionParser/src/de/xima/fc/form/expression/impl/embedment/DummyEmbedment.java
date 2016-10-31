package de.xima.fc.form.expression.impl.embedment;

import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.util.CmnCnst;

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
	public void reset() {
	}

	public static IEmbedment getInstance() {
		return InstanceHolder.INSTANCE;
	}
	@Override
	public void setCurrentEmbedment(final String name) {
	}
	@Override
	public String[] getScopeList() {
		return CmnCnst.EMPTY_STRING_ARRAY;
	}
	@Override
	public void outputCode(final String data, final IEvaluationContext ec) throws EmbedmentOutputException {
		System.out.print(data);
	}
	@Override
	public void outputText(final String data, final IEvaluationContext ec) throws EmbedmentOutputException {
		System.out.print(data);
	}
}
