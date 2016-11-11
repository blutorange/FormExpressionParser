package de.xima.fc.form.expression.impl.writer;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;

public class DummyWriter extends Writer {

	private final static class InstanceHolder {
		@Nonnull public final static DummyWriter INSTANCE = new DummyWriter();
	}
	private DummyWriter(){}
	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
	}
	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {
	}
	@Nonnull
	public static DummyWriter getInstance() {
		return InstanceHolder.INSTANCE;
	}
}
