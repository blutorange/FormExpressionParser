package de.xima.fc.form.expression.impl.externalcontext;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.context.IExternalContextCommand;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.impl.contextcommand.ESystemOutCommand;
import de.xima.fc.form.expression.impl.writer.SystemOutWriter;
import de.xima.fc.form.expression.object.ALangObject;

public enum SystemExternalContext implements IExternalContext {
	OUT {
		@Override
		public void write(String data) {
			if (!outputDisabled) System.out.print(data);
		}
	},
	ERR {
		@Override
		public void write(String data) {
			if (!outputDisabled) System.err.print(data);
		}		
	};
	
	protected boolean outputDisabled = false;

	@Override
	public void finishWriting() throws EmbedmentOutputException {
		SystemOutWriter.getInstance().flush();
	}
	
	@Override
	public abstract void write(String data);
	
	@Override
	public ALangObject fetchScopedVariable(String scope, String name, IEvaluationContext ec) {
		return null;
	}
	
	@Override
	public void process(IExternalContextCommand command, IEvaluationContext ec) {
		switch (command.castOrNull(ESystemOutCommand.class)) {
		case DISABLE_OUTPUT:
			outputDisabled = true;
			break;		
		case ENABLE_OUTPUT:
			outputDisabled = false;
			break;		
		default:
			ec.getLogger().info(String.format("Command %s cannot be processed by SystemOutExternalContext.", command));
			break;		
		}
	}
	@Override
	public void beginWriting() throws EmbedmentOutputException {
		SystemOutWriter.getInstance().flush();
	}
}