package de.xima.fc.form.expression.impl.externalcontext;

import java.io.PrintStream;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextContractFactory;
import de.xima.fc.form.expression.impl.contextcommand.ESystemOutCommand;
import de.xima.fc.form.expression.impl.writer.SystemWriter;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public final class SystemExternalContextFactory extends AGenericExternalContextFactory<PrintStream> {
	private static final long serialVersionUID = 1L;

	private SystemExternalContextFactory(){}

	@Override
	public AGenericExternalContext makeExternalContext(final PrintStream printStream) {
		return new SystemExternalContext(printStream);
	}

	public static IExternalContextContractFactory<PrintStream> getFactory() {
		return InstanceHolder.INSTANCE;
	}

	private static class InstanceHolder {
		public final static IExternalContextContractFactory<PrintStream> INSTANCE = new SystemExternalContextFactory();
	}

	private static class SystemExternalContext extends AGenericExternalContext implements IExternalContext {
		private final PrintStream printStream;
		protected boolean outputDisabled = false;

		private SystemExternalContext(final PrintStream printStream) {
			this.printStream = printStream;
		}

		@Override
		public void finishWriting() throws EmbedmentOutputException {
			SystemWriter.getSystemOutInstance().flush();
		}

		@Override
		public void write(final String data) {
			if (!outputDisabled)
				printStream.print(data);
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
					ec.getLogger().info(NullUtil.messageFormat(CmnCnst.Error.UNKNOWN_COMMAND_FOR_SYSTEM_OUT_CONTEXT,
							command.getClass().getCanonicalName(), command));
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
}