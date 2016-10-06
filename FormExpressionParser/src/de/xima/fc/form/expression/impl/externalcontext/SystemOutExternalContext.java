package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.io.Writer;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.object.ALangObject;

public enum SystemOutExternalContext implements IExternalContext {
	INSTANCE;
	private final static Writer writer = new Writer() {
		@Override
		public void write(char[] arg0, int arg1, int arg2) throws IOException {
			for (int i = arg1; i<arg2; ++i) System.out.append(arg0[i]);
		}
		@Override
		public void flush() throws IOException {
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
			System.out.append(s, off, off+len);
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
		public void close() throws IOException {
			// We will not close stdout.
			//System.out.close();
		}
	};
	@Override
	public void flushWriter() {
		if (writer != null) flushWriter();
	}
	@Override
	public Writer getWriter() {
		return writer;
	}
	@Override
	public ALangObject fetchScopedVariable(String scope, String name, IEvaluationContext ec) {
		return null;
	}
}