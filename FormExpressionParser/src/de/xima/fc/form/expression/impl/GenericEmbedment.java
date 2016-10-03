package de.xima.fc.form.expression.impl;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandler;
import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;
import de.xima.fc.form.expression.impl.embedment.handler.EmbedmentHandlerBundleFormcycle;
import de.xima.fc.form.expression.impl.embedment.handler.EmbedmentHandlerBundleGeneral;

/**
 * Generic embedment allowing you to inject different implementations.
 * @author madgaksha
 *
 * @param <T> Type of the argument passed to {@link IEmbedmentHandler#output(String, Object)}.
 */
public class GenericEmbedment<T> implements IEmbedment {

	private final Map<String, IEmbedmentHandler<T>> map;
	private IEmbedmentHandler<T> handler;
	private final T object;

	private GenericEmbedment(final Map<String, IEmbedmentHandler<T>> map, final T object) throws IllegalArgumentException {
		this.map = map;
		this.object = object;
	}

	public final static class Builder<T> {
		private T object;
		private Map<String, IEmbedmentHandler<T>> map;
		public Builder(final T object) {
			reinit();
			setObject(object);
		}
		private void reinit() {
			map = null;
			object = null;
		}
		private Map<String, IEmbedmentHandler<T>> getMap() {
			if (map == null) map = new HashMap<>();
			return map;
		}
		public Builder<T> setObject(final T object) {
			if (object != null)	this.object = object;
			return this;
		}
		public Builder<T> addHandler(final String name, final IEmbedmentHandler<T> handler) {
			if (handler!= null)
				getMap().put(name, handler);
			return this;
		}
		public Builder<T> addHandler(final String name, final IEmbedmentHandler<T>[] handlerList) {
			if (handlerList != null)
				for (final IEmbedmentHandler<T> handler : handlerList)
					getMap().put(name, handler);
			return this;
		}
		public Builder<T> addHandler(final IEmbedmentHandlerNamed<T> handler) {
			if (handler != null)
				getMap().put(handler.getEmbedmentName(), handler);
			return this;
		}
		public Builder<T> addHandler(final IEmbedmentHandlerNamed<T>[] handlerList) {
			if (handlerList != null)
				for (final IEmbedmentHandlerNamed<T> handler : handlerList)
					getMap().put(handler.getEmbedmentName(), handler);
			return this;
		}
		public GenericEmbedment<T> build() throws IllegalStateException {
			if (object == null) throw new IllegalStateException("Object cannot be null.");
			final GenericEmbedment<T> embedment = new GenericEmbedment<T>(getMap(), object);
			reinit();
			return embedment;
		}
	}

	@Override
	public void beginEmbedment(final String name) {
		handler = map.get(name);
		if (handler != null) handler.beginEmbedment();
	}

	@Override
	public void output(final String data, final IEvaluationContext ec) throws EmbedmentOutputException {
		if (handler != null) handler.output(data, object, ec);
	}

	@Override
	public void endEmbedment() {
		if (handler != null) handler.endEmbedment();
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
		final Builder<Writer> builder = new Builder<Writer>(writer);
		builder.addHandler(EmbedmentHandlerBundleGeneral.values());
		return builder.build();
	}

	public static IEmbedment getNewFormcycleEmbedment(final Writer writer) {
		final Builder<Writer> builder = new Builder<Writer>(writer);
		builder.addHandler(EmbedmentHandlerBundleGeneral.values());
		builder.addHandler(EmbedmentHandlerBundleFormcycle.values());
		return builder.build();
	}
}