package de.xima.fc.form.expression.impl.externalcontext;

import java.io.Writer;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.object.ALangObject;

public enum DummyExternalContext implements IExternalContext {
	INSTANCE;
	@Override
	public void flushWriter() {
	}
	@Override
	public Writer getWriter() {
		return DummyWriter.getInstance();
	}
	@Override
	public ALangObject fetchScopedVariable(String scope, String name, IEvaluationContext ec) {
		return null;
	}
}