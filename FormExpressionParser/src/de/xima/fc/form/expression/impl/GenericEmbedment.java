package de.xima.fc.form.expression.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

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
	private final Map<String, IEmbedmentHandler> map;
	private IEmbedmentHandler handler;
	private final Writer writer;
   
	private GenericEmbedment(final Map<String, IEmbedmentHandler> map, final Writer object) throws IllegalArgumentException {
		this.map = map;
		this.writer = object;
	}

	public final static class Builder {
		private Writer object;
		private Map<String, IEmbedmentHandler> map;
		public Builder(final Writer object) {
			reinit();
			setObject(object);
		}
		private void reinit() {
			map = null;
			object = null;
		}
		private Map<String, IEmbedmentHandler> getMap() {
			if (map == null) map = new HashMap<>();
			return map;
		}
		public Builder setObject(final Writer object) {
			if (object != null)	this.object = object;
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
		public GenericEmbedment build() throws IllegalStateException {
			if (object == null) throw new IllegalStateException("Object cannot be null.");
			final GenericEmbedment embedment = new GenericEmbedment(getMap(), object);
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
		if (handler != null && !handler.isDoOutput()) return;
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
}