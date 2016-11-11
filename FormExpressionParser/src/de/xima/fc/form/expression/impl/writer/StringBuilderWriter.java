package de.xima.fc.form.expression.impl.writer;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;

public class StringBuilderWriter extends Writer {
	@Nonnull private final StringBuilder sb;

	public StringBuilderWriter() {
		sb = new StringBuilder();
	}

	@Override
	public void write(final char[] str, final int offset, final int len) throws IOException {
		sb.append(str, offset, len);
	}

	@Override
	public void flush() {
	}

	@Override
	public Writer append(final CharSequence seq, final int start, final int end) {
		sb.append(seq, start, end);
		return this;
	}

	@Override
	public void write(final String s) {
		sb.append(s);
	}

	@Override
	public void write(final int c) {
		sb.append((char)c);
	}

	@Override
	public void write(final String s, final int off, final int len) {
		if (len == 0)
			return;
		sb.append(s, off, off + len - 1);
	}

	@Override
	public Writer append(final CharSequence seq) {
		sb.append(seq);
		return this;
	}

	@Override
	public void write(final char[] buffer) {
		sb.append(buffer);
	}

	@Override
	public void close() {
		flush();
		// StringBuilder does not need to be closed.
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
