package de.xima.fc.form.expression.impl.embedment;

import org.apache.commons.lang3.ArrayUtils;

import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;

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
	public Void reset() {
		return null;
	}	

	public static IEmbedment getInstance() {
		return InstanceHolder.INSTANCE;
	}
	@Override
	public void setCurrentEmbedment(String name) {
	}
	@Override
	public String[] getScopeList() {
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}
	@Override
	public void outputCode(String data, IEvaluationContext ec) throws EmbedmentOutputException {
		System.out.print(data);
	}
	@Override
	public void outputText(String data, IEvaluationContext ec) throws EmbedmentOutputException {
		System.out.print(data);
	}
}
