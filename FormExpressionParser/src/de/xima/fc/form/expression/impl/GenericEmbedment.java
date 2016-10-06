package de.xima.fc.form.expression.impl;

import java.io.IOException;

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
   
	private final static class InstanceHolder {
		public final static GenericEmbedment GENERIC = new Builder().addHandler(EmbedmentHandlerBundleGeneral.values())
				.build();
		public final static GenericEmbedment FORMCYCLE = new Builder()
				.addHandler(EmbedmentHandlerBundleGeneral.values()).addHandler(EmbedmentHandlerBundleFormcycle.values())
				.build();
	}
	
	private GenericEmbedment(final ImmutableMap<String, IEmbedmentHandler> map) throws IllegalArgumentException {
		if (map == null) throw new IllegalArgumentException("map must not be null");
		this.map = map;
	}

	public final static class Builder {
		private com.google.common.collect.ImmutableMap.Builder<String, IEmbedmentHandler> map;
		public Builder() {
			reinit();
		}
		private void reinit() {
			map = null;
		}
		private com.google.common.collect.ImmutableMap.Builder<String, IEmbedmentHandler> getMap() {
			if (map == null) map = new com.google.common.collect.ImmutableMap.Builder<>();
			return map;
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
			final GenericEmbedment embedment = new GenericEmbedment(getMap().build());
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
		if ((handler != null && !handler.isDoOutput())) return;
		if (ec.getExternalContext() == null || ec.getExternalContext().getWriter()==null) return;
		try {
			ec.getExternalContext().getWriter().write(data);
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

	public static IEmbedment getGenericEmbedment() {
		return InstanceHolder.GENERIC;
	}

	public static IEmbedment getFormcycleEmbedment() {
		return InstanceHolder.FORMCYCLE;

	}

	@Override
	public Void reset() {
		return null;
	}		
}