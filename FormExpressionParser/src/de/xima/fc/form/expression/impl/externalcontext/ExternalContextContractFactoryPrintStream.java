package de.xima.fc.form.expression.impl.externalcontext;

import java.io.PrintStream;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;
import de.xima.fc.form.expression.iface.factory.IExternalContextContractFactory;
import de.xima.fc.form.expression.impl.contextcommand.ESystemOutCommand;
import de.xima.fc.form.expression.impl.warning.GenericWarning;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public final class ExternalContextContractFactoryPrintStream extends AGenericExternalContextFactory<PrintStream> {
	private static final long serialVersionUID = 1L;

	protected ExternalContextContractFactoryPrintStream() {
	}

	@Override
	public AGenericExternalContext make(final PrintStream printStream) {
		return new ExImpl(printStream);
	}

	public static IExternalContextContractFactory<PrintStream> getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private static class InstanceHolder {
		public final static IExternalContextContractFactory<PrintStream> INSTANCE = new ExternalContextContractFactoryPrintStream();
	}

	private static class ExImpl extends AGenericExternalContext implements IExternalContext {
		private final PrintStream printStream;
		protected boolean outputDisabled = false;

		protected ExImpl(final PrintStream printStream) {
			this.printStream = printStream;
		}

		@Override
		public void finishWriting() throws EmbedmentOutputException {
			printStream.flush();
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
					ec.getTracer().appendWarning(new GenericWarning(
							NullUtil.messageFormat(CmnCnst.Error.UNKNOWN_COMMAND_FOR_SYSTEM_OUT_CONTEXT,
									command.getClass().getCanonicalName(), command),
							ec));
					break;
				}
			}
			else {
				ec.getLogger().info(NullUtil.messageFormat(CmnCnst.Error.UNKNOWN_COMMAND_FOR_SYSTEM_OUT_CONTEXT, command), null);
			}
		}

		@Override
		public void beginWriting() throws EmbedmentOutputException {
			printStream.flush();
		}

		@Override
		public void reset() {
		}
	}
}