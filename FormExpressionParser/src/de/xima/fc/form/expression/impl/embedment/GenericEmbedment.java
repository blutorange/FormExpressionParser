package de.xima.fc.form.expression.impl.embedment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkNotNull;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.InvalidTemplateDataException;
import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.iface.evaluate.IEmbedmentContractFactory;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.util.CmnCnst;

/**
 * Generic embedment allowing you to inject different implementations.
 *
 * @author madgaksha
 *
 * @param Type
 *            of the argument passed to
 *            {@link IEmbedmentHandler#output(String, Object)}.
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

	private GenericEmbedment(final ImmutableMap<String, IEmbedmentHandler> map) throws IllegalArgumentException {
		if (map == null)
			throw new IllegalArgumentException(CmnCnst.Error.NULL_MAP);
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
			if (map == null)
				map = new com.google.common.collect.ImmutableMap.Builder<>();
			return map;
		}

		public Builder addHandler(@Nonnull final String name, @Nonnull final IEmbedmentHandler handler) {
			getMap().put(checkNotNull(name), checkNotNull(handler));
			return this;
		}

		@SuppressWarnings("null")
		public Builder addHandler(@Nonnull final String name, @Nonnull final IEmbedmentHandler[] handlerList) {
			checkNotNull(name);
			checkNotNull(handlerList);
			for (@Nonnull
					final IEmbedmentHandler handler : handlerList)
				getMap().put(name, handler);
			return this;
		}

		public Builder addHandler(@Nonnull final IEmbedmentHandlerNamed handler) {
			checkNotNull(handler);
			getMap().put(handler.getEmbedmentName(), handler);
			return this;
		}

		public Builder addHandler(@Nonnull final IEmbedmentHandlerNamed[] handlerList) {
			checkNotNull(handlerList);
			for (final IEmbedmentHandlerNamed handler : handlerList)
				getMap().put(handler.getEmbedmentName(), handler);
			return this;
		}

		@Nonnull
		public IEmbedmentContractFactory build() {
			final IEmbedmentContractFactory factory = new EmbedmentFactoryImpl(getMap().build());
			reinit();
			return factory;
		}
	}

	@Override
	public void setCurrentEmbedment(final String embedment) {
		currentEmbedment = embedment;
	}

	@Nullable
	private IEmbedmentHandler getHandler() {
		final String e = currentEmbedment;
		if (e == null)
			return null;
		if (e.equals(handlerEmbedment))
			return handler;
		return handler = map.get(handlerEmbedment = e);
	}

	@Override
	public void outputText(final String data, final IEvaluationContext ec)
			throws EmbedmentOutputException, InvalidTemplateDataException {
		output(data, ec);
	}

	@Override
	public void outputCode(final String data, final IEvaluationContext ec)
			throws EmbedmentOutputException, InvalidTemplateDataException {
		final IEmbedmentHandler handler = getHandler();
		if ((handler != null && !handler.isDoOutput()))
			return;
		output(data, ec);
	}

	@Override
	public void reset() {
		currentEmbedment = handlerEmbedment = null;
		handler = null;
	}

	@Override
	public String[] getScopeListForCurrentEmbedment() {
		final IEmbedmentHandler handler = getHandler();
		return handler != null ? handler.getScopeList() : CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
	}

	@SuppressWarnings("null")
	@Override
	public String[] getScopeList(final String embedment) {
		final IEmbedmentHandler handler = map.get(embedment);
		return handler != null ? handler.getScopeList() : null;
	}

	@Override
	public String[] getEmbedmentList() {
		return map.keySet().toArray(CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY);
	}

	private static void output(@Nonnull final String data, @Nonnull final IEvaluationContext ec)
			throws EmbedmentOutputException, InvalidTemplateDataException {
		final IExternalContext ex = ec.getExternalContext();
		if (ex != null)
			ex.write(data);
	}

	@Immutable
	private static class EmbedmentFactoryImpl implements IEmbedmentContractFactory {
		private static final long serialVersionUID = 1L;
		@Nonnull
		public final ImmutableMap<String, IEmbedmentHandler> map;
		public EmbedmentFactoryImpl(@Nonnull final ImmutableMap<String, IEmbedmentHandler> map) {
			this.map = map;
		}
		@Override
		public IEmbedment makeEmbedment() {
			return new GenericEmbedment(map);
		}
		@Override
		public String[] getScopesForEmbedment(final String embedment) {
			return map.get(embedment).getScopeList();
		}
	}
}