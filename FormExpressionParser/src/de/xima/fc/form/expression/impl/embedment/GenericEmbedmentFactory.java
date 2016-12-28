package de.xima.fc.form.expression.impl.embedment;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkNotNull;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.InvalidTemplateDataException;
import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.factory.IEmbedmentContractFactory;
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
@Immutable
@NonNullByDefault
public class GenericEmbedmentFactory implements IEmbedmentContractFactory {
	private static final long serialVersionUID = 1L;

	private final ImmutableMap<String, IEmbedmentHandler> map;

	protected GenericEmbedmentFactory(final ImmutableMap<String, IEmbedmentHandler> map) {
		this.map = map;
	}

	@Override
	public IEmbedment make() {
		return new EmbedmentImpl(map);
	}

	@Nullable
	@Override
	public String[] getScopesForEmbedment(final String embedment) {
		final IEmbedmentHandler handler = map.get(embedment);
		return handler != null ? handler.getScopeList() : null;
	}

	private final static class EmbedmentImpl implements IEmbedment {
		private final ImmutableMap<String, IEmbedmentHandler> map;
		@Nullable
		private IEmbedmentHandler handler;
		@Nullable
		private String currentEmbedment;
		@Nullable
		private String handlerEmbedment;

		protected EmbedmentImpl(final ImmutableMap<String, IEmbedmentHandler> map) {
			this.map = map;
		}

		@Override
		public void setCurrentEmbedment(@Nullable final String embedment) {
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

		@Nullable
		@Override
		public String[] getScopeList(final String embedment) {
			final IEmbedmentHandler handler = map.get(embedment);
			return handler != null ? handler.getScopeList() : null;
		}

		@Override
		public String[] getEmbedmentList() {
			return map.keySet().toArray(CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY);
		}

		private void output(final String data, final IEvaluationContext ec)
				throws EmbedmentOutputException, InvalidTemplateDataException {
			final IExternalContext ex = ec.getExternalContext();
			if (ex != null)
				ex.write(data);
		}
	}

	public final static class Builder {
		@Nullable
		private ImmutableMap.Builder<String, IEmbedmentHandler> map;

		public Builder() {
			reinit();
		}

		private void reinit() {
			map = null;
		}

		private ImmutableMap.Builder<String, IEmbedmentHandler> getMap() {
			if (map != null)
				return map;
			return map = new ImmutableMap.Builder<>();
		}

		public Builder addHandler(final String name, final IEmbedmentHandler handler) {
			getMap().put(checkNotNull(name), checkNotNull(handler));
			return this;
		}

		public Builder addHandler(final IEmbedmentHandlerNamed handler) {
			checkNotNull(handler);
			getMap().put(handler.getEmbedmentName(), handler);
			return this;
		}

		public Builder addHandler(final IEmbedmentHandlerNamed[] handlerList) {
			checkNotNull(handlerList);
			for (final IEmbedmentHandlerNamed handler : handlerList)
				getMap().put(handler.getEmbedmentName(), handler);
			return this;
		}

		public IEmbedmentContractFactory build() {
			final IEmbedmentContractFactory factory = new GenericEmbedmentFactory(getMap().build());
			reinit();
			return factory;
		}
	}
}