package de.xima.fc.form.expression.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IBinding;
import de.xima.fc.form.expression.iface.context.IEmbedment;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.ILogger;
import de.xima.fc.form.expression.iface.context.INamespace;
import de.xima.fc.form.expression.iface.context.IScope;
import de.xima.fc.form.expression.iface.context.ITracer;
import de.xima.fc.form.expression.impl.logger.SystemLogger;
import de.xima.fc.form.expression.impl.tracer.DummyTracer;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

/**
 * Optionally, a scope may be specified for a variable by prepending it
 * separated with to columns, eg. <code>fields::tf1</code>. Variables qualified
 * with a scope never nest. <br>
 * <br>
 * Scopes may be imported into the local scope via
 *
 * <pre>
 *   with scope fields {
 *     tf1; // refers to fields::tf1
 *   }
 * </pre>
 *
 * <br>
 * <br>
 * The following rules apply to variable lookup, depending on whether a scope
 * was specified.
 *
 * <table>
 * <th>
 * <td><b>Reading</b></td>
 * <td><b>Writing</b></td></th>
 * <tr>
 * <td>without scope</td>
 * <td>looked up in the local scope first, then in imported scopes</td>
 * <td>looked up only in the local scope, never in any other scopes</td>
 * </tr>
 * <tr>
 * <td>with scope</td>
 * <td>looked up in the specified scope, and nowhere else</td>
 * <td>looked up in the specified scope, and nowhere else</td>
 * </tr>
 * *
 * </table>
 *
 * @author mad_gaksha
 *
 */
public class ReadScopedEvaluationContext extends GenericEvaluationContext {

	@Nonnull
	private final List<String> defaultScopeList = new ArrayList<>(16);

	private ReadScopedEvaluationContext(@Nonnull final IBinding binding, @Nonnull final IScope scope,
			@Nonnull final INamespace namespace, @Nonnull final ITracer<Node> tracer,
			@Nonnull final IEmbedment embedment, @Nonnull final ILogger logger) {
		super(binding, scope, namespace, tracer, embedment, logger);
	}

	@Override
	public ALangObject getUnqualifiedVariable(final String name) throws EvaluationException {
		// try local variables first
		final ALangObject loc = getBinding().getVariable(name);
		if (loc != null)
			return loc;
		// with() {} is deprecated, so this should not do anything performance-wise
		for (int i = defaultScopeList.size() - 1; i >= 0; --i) {
			// We never add anything that is null, see beginDefaultScope(String).
			@SuppressWarnings("null")
			final ALangObject scp = getScope().getVariable(defaultScopeList.get(i), name, this);
			if (scp != null)
				return scp;
		}
		// embedded blocks with scopes should only
		// be used for basic access like [%tf1%]
		final String[] embedmentScopeList = embedment.getScopeList();
		for (int i = embedmentScopeList.length - 1; i >= 0; --i) {
			// Embedments must not return a scope list with nulls.
			@SuppressWarnings("null")
			final ALangObject scp = getScope().getVariable(embedmentScopeList[i], name, this);
			if (scp != null)
				return scp;
		}
		throw new VariableNotDefinedException(name, this);
	}

	@Override
	public void setUnqualifiedVariable(final String name, final ALangObject value) throws EvaluationException {
		getBinding().setVariable(name, value);
	}

	@Override
	public void beginDefaultScope(@Nonnull final String scope) {
		defaultScopeList.add(scope);
	}

	@Override
	public void endDefaultScope() {
		defaultScopeList.remove(defaultScopeList.size() - 1);
	}

	public final static class Builder {
		private ILogger logger;
		private ITracer<Node> tracer;
		private INamespace namespace;
		private IBinding binding;
		private IScope scope;
		private IEmbedment embedment;

		public Builder() {
			reinit();
		}

		private void reinit() {
			logger = null;
			tracer = null;
			namespace = null;
			binding = null;
			scope = null;
			embedment = null;
		}

		@Nonnull
		public Builder setNamespace(@Nullable final INamespace namespace) {
			if (namespace != null)
				this.namespace = namespace;
			return this;
		}

		@Nonnull
		public Builder setBinding(@Nullable final IBinding binding) {
			if (binding != null)
				this.binding = binding;
			return this;
		}

		@Nonnull
		public Builder setScope(@Nullable final IScope scope) {
			if (scope != null)
				this.scope = scope;
			return this;
		}

		@Nonnull
		public Builder setLogger(@Nullable final ILogger logger) {
			if (logger != null)
				this.logger = logger;
			return this;
		}

		@Nonnull
		public Builder setTracer(@Nullable final ITracer<Node> tracer) {
			if (tracer != null)
				this.tracer = tracer;
			return this;
		}

		@Nonnull
		public Builder setEmbedment(@Nullable final IEmbedment embedment) {
			if (embedment != null)
				this.embedment = embedment;
			return this;
		}

		@Nonnull
		public IEvaluationContext build() throws IllegalStateException {
			final IBinding binding = this.binding;
			final IScope scope = this.scope;
			ILogger logger = this.logger;
			IEmbedment embedment = this.embedment;
			ITracer<Node> tracer = this.tracer;
			INamespace namespace = this.namespace;
			if (binding == null) throw new IllegalStateException(CmnCnst.Error.ILLEGAL_STATE_EC_BUILDER_BINDING);
			if (scope == null) throw new IllegalStateException(CmnCnst.Error.ILLEGAL_STATE_EC_BUILDER_SCOPE);
			if (logger == null)	logger = SystemLogger.getInfoLogger();
			if (embedment == null) embedment = GenericEmbedment.getGenericEmbedment();
			if (tracer == null) tracer = DummyTracer.INSTANCE;
			if (namespace == null) namespace = GenericNamespace.getGenericNamespaceInstance();
			final IEvaluationContext retVal = new ReadScopedEvaluationContext(binding, scope, namespace, tracer,
					embedment, logger);
			reinit();
			return retVal;
		}
	}
}