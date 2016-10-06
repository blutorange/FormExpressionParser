package de.xima.fc.form.expression.impl;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.ILogger;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.impl.logger.SystemLogger;
import de.xima.fc.form.expression.impl.tracer.DummyTracer;
import de.xima.fc.form.expression.object.ALangObject;

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

	private final List<String> defaultScopeList = new ArrayList<>(16);

	private ReadScopedEvaluationContext(final IBinding binding, final IScope scope, final INamespace namespace,
			final ITracer<Node> tracer, final IEmbedment embedment, final ILogger logger) {
		super(binding, scope, namespace, tracer, embedment, logger);
	}

	@Override
	public ALangObject getUnqualifiedVariable(final String name) throws EvaluationException {
		final ALangObject loc = getBinding().getVariable(name);
		if (loc != null)
			return loc;
		// with() {} is deprecated, so this should not do much performance-wise
		for (int i = defaultScopeList.size() - 1; i >= 0; --i) {
			final ALangObject scp = getScope().getVariable(defaultScopeList.get(i), name, this);
			if (scp != null)
				return scp;
		}
		// embedded blocks with scopes should only
		// be used for basic access like [%tf1%]
		String[] embedmentScopeList = embedment.getScopeList();
		for (int i = embedmentScopeList.length - 1; i >= 0; --i) {
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
	public void beginDefaultScope(final String scope) {
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

		public Builder setNamespace(final INamespace namespace) {
			if (namespace != null)
				this.namespace = namespace;
			return this;
		}

		public Builder setBinding(final IBinding binding) {
			if (binding != null)
				this.binding = binding;
			return this;
		}

		public Builder setScope(final IScope scope) {
			if (scope != null)
				this.scope = scope;
			return this;
		}

		public Builder setLogger(final ILogger logger) {
			if (logger != null)
				this.logger = logger;
			return this;
		}

		public Builder setTracer(final ITracer<Node> tracer) {
			if (tracer != null)
				this.tracer = tracer;
			return this;
		}

		public Builder setEmbedment(final IEmbedment embedment) {
			if (embedment != null)
				this.embedment = embedment;
			return this;
		}

		public IEvaluationContext build() throws IllegalStateException {
			if (binding == null) throw new IllegalStateException("Binding not set.");
			if (scope == null) throw new IllegalStateException("Scope not set.");
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