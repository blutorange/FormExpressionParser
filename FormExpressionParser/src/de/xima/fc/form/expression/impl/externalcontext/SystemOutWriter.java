package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.io.Writer;

public final class SystemOutWriter extends Writer {
	private final static class InstanceHolder {
		public final static SystemOutWriter INSTANCE = new SystemOutWriter();
	}
	private SystemOutWriter() {}
	@Override
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		for (int i = arg1; i<arg2; ++i) System.out.append(arg0[i]);
	}
	@Override
	public void flush() {
		System.out.flush();
	}
	@Override
	public Writer append(CharSequence seq, int start, int end) {
		System.out.append(seq, start, end);
		return this;
	}
	@Override
	public void write(String s) {
		System.out.print(s);
	}
	@Override
	public void write(String s, int off, int len) {
		if (len < 1) return;
		System.out.append(s, off, off+len-1);
	}
	@Override
	public Writer append(CharSequence seq) {
		System.out.append(seq);
		return this;
	}
	@Override
	public void write(char[] buffer) {
		System.out.print(buffer);
	}
	@Override
	public void write(int c) {
		System.out.print((char)c);
	}
	@Override
	public void close() throws IOException {
		// We will not close stdout.
		flush();
	}
	
	public static SystemOutWriter getInstance() {
		return InstanceHolder.INSTANCE;
	}
}