package de.xima.fc.form.expression.impl.factory;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.ICustomScope;
import de.xima.fc.form.expression.iface.context.IEmbedment;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalScope;
import de.xima.fc.form.expression.iface.context.ITracer;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.impl.GenericEmbedment;
import de.xima.fc.form.expression.impl.GenericEvaluationContext;
import de.xima.fc.form.expression.impl.GenericExternalScope;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.scope.MathScope;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;

public enum FormcycleEcContractFactory implements IEvaluationContextContractFactory<FormcycleExternalContext> {
	INSTANCE;
	private final static IEmbedment embedment = GenericEmbedment.getNewFormcycleEmbedment();
	private final static Map<String,IScopeInfo> external = new HashMap<>(16);
	static {
		external.put(MathScope.INSTANCE.getScopeName(), MathScope.INSTANCE);
	}

	@Override
	public IEvaluationContext getContextWithExternal(final FormcycleExternalContext ex) {
		final IEvaluationContext ec = makeEc();
		ec.setExternalContext(ex);
		return ec;
	}

	private static IExternalScope makeScope() {
		final ICustomScope mathScope = MathScope.INSTANCE;
		return new GenericExternalScope.Builder().addCustomScope(mathScope).build();
	}

	private static IEvaluationContext makeEc() {
		final ITracer<Node> tracer = new GenericTracer();
		final GenericEvaluationContext.Builder builder = new GenericEvaluationContext.Builder();
		final IExternalScope scope = makeScope();
		builder.setEmbedment(GenericEmbedment.getNewFormcycleEmbedment());
		builder.setScope(scope);
		builder.setTracer(tracer);
		return builder.build();
	}

	@Override
	public boolean isProvidingExternalScope(final String scope) {
		return external.containsKey(scope);
	}

	@Override
	public String[] getScopesForEmbedment(final String name) {
		return embedment.getScopeList(name);
	}

	@Override
	public IScopeInfo getExternalScopeInfo(final String scope) {
		final IScopeInfo info = external.get(scope);
		if (info != null)
			return info;
		return FormcycleExternalContext.getScopeInfo(scope);
	}
}