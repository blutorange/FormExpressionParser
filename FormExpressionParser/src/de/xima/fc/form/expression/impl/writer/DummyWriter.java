package de.xima.fc.form.expression.impl.writer;

import java.io.IOException;
import java.io.Writer;

public class DummyWriter extends Writer {

	private final static class InstanceHolder {
		public final static DummyWriter INSTANCE = new DummyWriter();
	}
	private DummyWriter(){}
	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
	}
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
	}
	public static DummyWriter getInstance() {
		return InstanceHolder.INSTANCE;
	}
}
