package de.xima.fc.form.expression.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
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
	private String currentEmbedment;
	@Nullable
	private String handlerEmbedment;

	private final static class InstanceHolder {
		public final static GenericEmbedment GENERIC = new Builder()
				.addHandler(EmbedmentHandlerBundleGeneral.values())
				.build();
		public final static GenericEmbedment FORMCYCLE = new Builder()
				.addHandler(EmbedmentHandlerBundleGeneral.values())
				.addHandler(EmbedmentHandlerBundleFormcycle.values())
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
	public void setCurrentEmbedment(final String embedment) throws EvaluationException {
		currentEmbedment = embedment;
	}

	private IEmbedmentHandler getHandler() {
		if (currentEmbedment == null) return null;
		if (currentEmbedment.equals(handlerEmbedment)) return handler;
		return handler = map.get(handlerEmbedment = currentEmbedment);
	}

	private void output(final String data, final IEvaluationContext ec) throws EmbedmentOutputException {
		final IExternalContext external = ec.getExternalContext();
		if (external != null) external.write(data);
	}

	@Override
	public void outputText(final String data, final IEvaluationContext ec) throws EmbedmentOutputException {
		output(data, ec);
	}

	@Override
	public void outputCode(final String data, final IEvaluationContext ec) throws EmbedmentOutputException {
		final IEmbedmentHandler handler = getHandler();
		if ((handler != null && !handler.isDoOutput())) return;
		output(data, ec);
	}

	public static IEmbedment getGenericEmbedment() {
		return InstanceHolder.GENERIC;
	}

	public static IEmbedment getFormcycleEmbedment() {
		return InstanceHolder.FORMCYCLE;

	}

	@Override
	public Void reset() {
		currentEmbedment = handlerEmbedment = null;
		handler = null;
		return null;
	}

	@Override
	public String[] getScopeList() {
		final IEmbedmentHandler handler = getHandler();
		return handler != null ? handler.getScopeList() : ArrayUtils.EMPTY_STRING_ARRAY;
	}
}