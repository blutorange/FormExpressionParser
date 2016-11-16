package de.xima.fc.form.expression.impl.externalcontext;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.context.IExternalContextCommand;
import de.xima.fc.form.expression.impl.contextcommand.ESystemOutCommand;
import de.xima.fc.form.expression.impl.writer.SystemWriter;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public enum SystemExternalContext implements IExternalContext {
	OUT {
		@Override
		public void write(final String data) {
			if (!outputDisabled) System.out.print(data);
		}
	},
	ERR {
		@Override
		public void write(final String data) {
			if (!outputDisabled) System.err.print(data);
		}
	};

	protected boolean outputDisabled = false;

	@Override
	public void finishWriting() throws EmbedmentOutputException {
		SystemWriter.getSystemOutInstance().flush();
	}

	@Override
	public abstract void write(String data);

	@Override
	public ALangObject fetchScopedVariable(final String scope, final String name, final IEvaluationContext ec) {
		return null;
	}

	@Override
	public void process(final IExternalContextCommand command, final IEvaluationContext ec) {
		final Optional<ESystemOutCommand> c = command.castTo(ESystemOutCommand.class);
		if (c.isPresent()) {
			switch (c.get()) {
			case DISABLE_OUTPUT:
				outputDisabled = true;
				break;
			case ENABLE_OUTPUT:
				outputDisabled = false;
				break;
			default:
				ec.getLogger().info(String.format(CmnCnst.Error.UNKNOWN_COMMAND_FOR_SYSTEM_OUT_CONTEXT, command));
				break;
			}
		}
		else {
			ec.getLogger().info(String.format(CmnCnst.Error.UNKNOWN_COMMAND_FOR_SYSTEM_OUT_CONTEXT, command));
		}
	}
	@Override
	public void beginWriting() throws EmbedmentOutputException {
		SystemWriter.getSystemOutInstance().flush();
	}
}