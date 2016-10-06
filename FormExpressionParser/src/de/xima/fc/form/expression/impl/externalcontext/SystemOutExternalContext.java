package de.xima.fc.form.expression.impl.externalcontext;

import java.io.Writer;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.object.ALangObject;

public enum SystemOutExternalContext implements IExternalContext {
	INSTANCE;
	@Override
	public void flushWriter() throws EmbedmentOutputException{
		SystemOutWriter.getInstance().flush();
	}
	@Override
	public Writer getWriter() {
		return SystemOutWriter.getInstance();
	}
	@Override
	public ALangObject fetchScopedVariable(String scope, String name, IEvaluationContext ec) {
		return null;
	}
}