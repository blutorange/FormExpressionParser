package de.xima.fc.form.expression.impl.factory;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEmbedment;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalScope;
import de.xima.fc.form.expression.iface.context.ITracer;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.impl.GenericEmbedment;
import de.xima.fc.form.expression.impl.GenericEvaluationContext;
import de.xima.fc.form.expression.impl.GenericExternalScope;
import de.xima.fc.form.expression.impl.externalcontext.AGenericExternalContext;
import de.xima.fc.form.expression.impl.scope.MathScope;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;
import de.xima.fc.form.expression.util.CmnCnst;

public enum GenericEcContractFactory implements IEvaluationContextContractFactory<AGenericExternalContext> {
	INSTANCE;

	private final static IEmbedment embedment = GenericEmbedment.getNewGeneralEmbedment();
	private final static Map<String,IScopeInfo> external = new HashMap<>(16);
	static {
		external.put(MathScope.INSTANCE.getScopeName(), MathScope.INSTANCE);
	}

	@Override
	public IEvaluationContext getContextWithExternal(@Nonnull final AGenericExternalContext ex) {
		Preconditions.checkNotNull(ex, CmnCnst.Error.NULL_EXTERNAL_CONTEXT);
		final IEvaluationContext ec = makeEc();
		ec.setExternalContext(ex);
		return ec;
	}

	private static IEvaluationContext makeEc() {
		final ITracer<Node> tracer = new GenericTracer();
		final IEmbedment embedment = GenericEmbedment.getNewGeneralEmbedment();
		final IExternalScope scope = new GenericExternalScope.Builder().addCustomScope(MathScope.INSTANCE).build();
		return new GenericEvaluationContext.Builder()
				.setEmbedment(embedment)
				.setScope(scope)
				.setTracer(tracer)
				.build();
	}

	@Override
	public boolean isProvidingExternalScope(@Nonnull final String scope) {
		return external.containsKey(scope);
	}

	@Override
	public String[] getScopesForEmbedment(@Nonnull final String name) {
		return embedment.getScopeList(name);
	}

	@Override
	public IScopeInfo getExternalScopeInfo(@Nonnull final String scope) {
		return external.get(scope);
	}
}