package de.xima.fc.form.expression.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandler;
import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;
import de.xima.fc.form.expression.impl.embedment.handler.EmbedmentHandlerBundleFormcycle;
import de.xima.fc.form.expression.impl.embedment.handler.EmbedmentHandlerBundleGeneral;

/**
 * Generic embedment allowing you to inject different implementations.
 * @author madgaksha
 *
 * @param  Type of the argument passed to {@link IEmbedmentHandler#output(String, Object)}.
 */
public class GenericEmbedment implements IEmbedment {
	@Nonnull
	private final ImmutableMap<String, IEmbedmentHandler> map;
	@Nullable
	private IEmbedmentHandler handler;
	@Nullable
	private Writer writer;
   
	private GenericEmbedment(final ImmutableMap<String, IEmbedmentHandler> map) throws IllegalArgumentException {
		this(map, null);
	}
	private GenericEmbedment(final ImmutableMap<String, IEmbedmentHandler> map, final Writer writer) throws IllegalArgumentException {
		if (map == null) throw new IllegalArgumentException("map must not be null");
		this.map = map;
		this.writer = writer;
	}

	public final static class Builder {
		private Writer writer;
		private com.google.common.collect.ImmutableMap.Builder<String, IEmbedmentHandler> map;
		public Builder() {
			reinit();
		}
		public Builder(final Writer writer) {
			reinit();
			setWriter(writer);
		}
		private void reinit() {
			map = null;
			writer = null;
		}
		private com.google.common.collect.ImmutableMap.Builder<String, IEmbedmentHandler> getMap() {
			if (map == null) map = new com.google.common.collect.ImmutableMap.Builder<>();
			return map;
		}
		public Builder setWriter(final Writer writer) {
			if (writer != null)	this.writer = writer;
			return this;
		}
		public Builder addHandler(final String name, final IEmbedmentHandler handler) {
			if (handler!= null)
				getMap().put(name, handler);
			return this;
		}
		public Builder addHandler(final String name, final IEmbedmentHandler[] handlerList) {
			if (handlerList != null)
				for (final IEmbedmentHandler handler : handlerList)
					getMap().put(name, handler);
			return this;
		}
		public Builder addHandler(final IEmbedmentHandlerNamed handler) {
			if (handler != null)
				getMap().put(handler.getEmbedmentName(), handler);
			return this;
		}
		public Builder addHandler(final IEmbedmentHandlerNamed[] handlerList) {
			if (handlerList != null)
				for (final IEmbedmentHandlerNamed handler : handlerList)
					getMap().put(handler.getEmbedmentName(), handler);
			return this;
		}
		public GenericEmbedment build() {
			final GenericEmbedment embedment = new GenericEmbedment(getMap().build(), writer);
			reinit();
			return embedment;
		}
	}

	@Override
	public void beginEmbedment(final String name, final IEvaluationContext ec) throws EvaluationException {
		handler = map.get(name);
		if (handler != null) handler.beginEmbedment(ec);
	}

	@Override
	public void output(final String data, final IEvaluationContext ec) throws EmbedmentOutputException {
		if (writer == null || (handler != null && !handler.isDoOutput())) return;
		try {
			writer.write(data);
		}
		catch (IOException e) {
			throw new EmbedmentOutputException(e, ec);
		}
	}

	@Override
	public void endEmbedment(final IEvaluationContext ec) throws EvaluationException{
		if (handler != null) handler.endEmbedment(ec);
		handler = null;
	}

	private final static class InstanceHolder {
		public final static IEmbedment SYSTEM_OUT = getNewGenericEmbedment(new OutputStreamWriter(System.out));
		public final static IEmbedment SYSTEM_ERR = getNewGenericEmbedment(new OutputStreamWriter(System.err));
	}

	public static IEmbedment getSystemOutGenericEmbedment() {
		return InstanceHolder.SYSTEM_OUT;
	}
	public static IEmbedment getSystemErrGenericEmbedment() {
		return InstanceHolder.SYSTEM_ERR;
	}

	public static IEmbedment getNewGenericEmbedment(final Writer writer) {
		final Builder builder = new Builder(writer);
		builder.addHandler(EmbedmentHandlerBundleGeneral.values());
		return builder.build();
	}

	public static IEmbedment getNewFormcycleEmbedment(final Writer writer) {
		final Builder builder = new Builder(writer);
		builder.addHandler(EmbedmentHandlerBundleGeneral.values());
		builder.addHandler(EmbedmentHandlerBundleFormcycle.values());
		return builder.build();
	}

	@Override
	public void setWriter(final Writer writer) {
		this.writer = writer;
	}
	
	@Override
	public Void reset() {
		this.writer = null;
		return null;
	}
	@Override
	public void flushWriter(IEvaluationContext ec) throws EmbedmentOutputException {
		if (writer != null) try {
			writer.flush();
		}
		catch (IOException e) {
			throw new EmbedmentOutputException(e, ec);
		}
	}
}