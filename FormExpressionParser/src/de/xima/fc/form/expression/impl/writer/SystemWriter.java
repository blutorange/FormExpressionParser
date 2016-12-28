package de.xima.fc.form.expression.impl.writer;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;

import javax.annotation.Nonnull;

public final class SystemWriter extends Writer {
	private final static class InstanceHolder {
		@Nonnull public final static SystemWriter SYSTEM_OUT = new SystemWriter(System.out);
		@Nonnull public final static SystemWriter SYSTEM_ERR = new SystemWriter(System.err);
	}

	private final PrintStream printStream;

	protected SystemWriter(final PrintStream printStream) {
		this.printStream = printStream;
	}
	@Override
	public void write(final char[] arg0, final int arg1, final int arg2) throws IOException {
		for (int i = arg1; i<arg2; ++i) System.out.append(arg0[i]);
	}
	@Override
	public void flush() {
		printStream.flush();
	}
	@Override
	public Writer append(final CharSequence seq, final int start, final int end) {
		printStream.append(seq, start, end);
		return this;
	}
	@Override
	public void write(final String s) {
		printStream.print(s);
	}
	@Override
	public void write(final String s, final int off, final int len) {
		if (len < 1) return;
		printStream.append(s, off, off+len-1);
	}
	@Override
	public Writer append(final CharSequence seq) {
		System.out.append(seq);
		return this;
	}
	@Override
	public void write(final char[] buffer) {
		printStream.print(buffer);
	}
	@Override
	public void write(final int c) {
		printStream.print((char)c);
	}
	@Override
	public void close() throws IOException {
		// We will not close stdout.
		flush();
	}

	@Nonnull
	public static SystemWriter getSystemOutInstance() {
		return InstanceHolder.SYSTEM_OUT;
	}

	@Nonnull
	public static SystemWriter getSystemErrInstance() {
		return InstanceHolder.SYSTEM_ERR;
	}
}