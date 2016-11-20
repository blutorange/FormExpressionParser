package de.xima.fc.form.expression.impl.externalcontext;

import java.io.PrintStream;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.context.IExternalContextCommand;
import de.xima.fc.form.expression.impl.contextcommand.ESystemOutCommand;
import de.xima.fc.form.expression.impl.writer.SystemWriter;
import de.xima.fc.form.expression.util.CmnCnst;

public final class SystemExternalContext extends AGenericExternalContext implements IExternalContext {
	private final PrintStream printStream;
	protected boolean outputDisabled = false;

	private static class InstanceHolder {
		public final static SystemExternalContext OUT = new SystemExternalContext(System.out);
		public final static SystemExternalContext ERR = new SystemExternalContext(System.err);
	}

	private SystemExternalContext(final PrintStream printStream) {
		this.printStream = printStream;
	}

	@Override
	public void finishWriting() throws EmbedmentOutputException {
		SystemWriter.getSystemOutInstance().flush();
	}

	@Override
	public void write(final String data) {
		if (!outputDisabled) printStream.print(data);
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

	public static AGenericExternalContext getSystemOutInstance() {
		return InstanceHolder.OUT;
	}
	public static AGenericExternalContext getSystemErrInstance() {
		return InstanceHolder.ERR;
	}
}