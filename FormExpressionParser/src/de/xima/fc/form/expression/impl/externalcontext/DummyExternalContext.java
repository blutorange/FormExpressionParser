package de.xima.fc.form.expression.impl.externalcontext;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.context.IExternalContextCommand;
import de.xima.fc.form.expression.object.ALangObject;

public enum DummyExternalContext implements IExternalContext {
	INSTANCE;
	@Override
	public void finishWriting() {
	}
	@Override
	public void write(String data) {
	}
	@Override
	public ALangObject fetchScopedVariable(String scope, String name, IEvaluationContext ec) {
		return null;
	}
	@Override
	public void process(IExternalContextCommand command, IEvaluationContext ec) {
	}
	@Override
	public void beginWriting() {
	}
}