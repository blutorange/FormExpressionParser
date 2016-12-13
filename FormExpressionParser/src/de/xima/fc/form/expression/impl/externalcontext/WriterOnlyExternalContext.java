package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextContractFactory;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;

public class WriterOnlyExternalContext extends AGenericExternalContext implements IExternalContext {
	private final Writer writer;

	private WriterOnlyExternalContext(final Writer writer) {
		this.writer = writer;
	}

	@Override
	public void write(final String data) throws EmbedmentOutputException {
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

	/** @return The return value of the toString method of the writer. Useful for {@link StringBuilderWriter}. */
	@Override
	public String toString() {
		return writer.toString();
	}

	@Override
	public void process(final IExternalContextCommand command, final IEvaluationContext ec) {
	}

	@Override
	public void beginWriting() {
	}

	@Nonnull
	public static IExternalContextContractFactory<Writer> getFactory() {
		return InstanceHolder.INSTANCE;
	}

	private static class InstanceHolder {
		@Nonnull public final static IExternalContextContractFactory<Writer> INSTANCE = new WriterOnlyExternalContextContractFactory();
	}

	private static class WriterOnlyExternalContextContractFactory extends AGenericExternalContextFactory<Writer> {
		private static final long serialVersionUID = 1L;
		@Override
		public AGenericExternalContext makeExternalContext(final Writer writer) {
			return new WriterOnlyExternalContext(writer);
		}
	}
}