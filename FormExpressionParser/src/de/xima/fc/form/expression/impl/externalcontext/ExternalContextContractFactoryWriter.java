package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextContractFactory;
import de.xima.fc.form.expression.impl.contextcommand.ESystemOutCommand;
import de.xima.fc.form.expression.impl.warning.GenericWarning;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class ExternalContextContractFactoryWriter extends AGenericExternalContextFactory<Writer> {
	private static final long serialVersionUID = 1L;

	@Override
	public AGenericExternalContext make(final Writer writer) {
		return new ExImpl(writer);
	}

	@Nonnull
	public static IExternalContextContractFactory<Writer> getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private static class InstanceHolder {
		@Nonnull
		public final static IExternalContextContractFactory<Writer> INSTANCE = new ExternalContextContractFactoryWriter();
	}

	private static class ExImpl extends AGenericExternalContext implements IExternalContext {
		private final Writer writer;
		private boolean outputDisabled = false;

		private ExImpl(final Writer writer) {
			this.writer = writer;
		}

		@Override
		public void write(final String data) throws EmbedmentOutputException {
			if (outputDisabled)
				return;
			try {
				writer.write(data);
			}
			catch (final IOException e) {
				throw new EmbedmentOutputException(e, this);
			}
		}

		@Override
		public void finishWriting() throws EmbedmentOutputException {
			try {
				writer.flush();
			}
			catch (final IOException e) {
				throw new EmbedmentOutputException(e, this);
			}
		}

		/**
		 * @return The return value of the toString method of the writer. Useful
		 *         for {@link StringBuilderWriter}.
		 */
		@Override
		public String toString() {
			return writer.toString();
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
				ec.getLogger().info(String.format(CmnCnst.Error.UNKNOWN_COMMAND_FOR_SYSTEM_OUT_CONTEXT, command));
			}
		}

		@Override
		public void beginWriting() {
		}

		@Override
		public void reset() {
		}
	}
}