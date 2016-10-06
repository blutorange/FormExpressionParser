package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.io.Writer;

public class StringBuilderWriter extends Writer {
	private final StringBuilder sb;
	
	public StringBuilderWriter() {
		sb = new StringBuilder();
	}

	@Override
	public void write(char[] str, int offset, int len) throws IOException {
		sb.append(str, offset, len);
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public Writer append(CharSequence seq, int start, int end) {
		sb.append(seq, start, end);
		return this;
	}

	@Override
	public void write(String s) {
		sb.append(s);
	}

	@Override
	public void write(String s, int off, int len) {
		if (len == 0)
			return;
		sb.append(s, off, off + len - 1);
	}

	@Override
	public Writer append(CharSequence seq) {
		sb.append(seq);
		return this;
	}

	@Override
	public void write(char[] buffer) {
		sb.append(buffer);
	}

	@Override
	public void close() throws IOException {
		// StringBuilder does not need to be closed.
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
