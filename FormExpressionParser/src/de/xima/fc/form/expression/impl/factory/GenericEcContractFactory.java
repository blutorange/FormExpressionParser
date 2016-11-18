package de.xima.fc.form.expression.impl.factory;

import java.util.HashSet;
import java.util.Set;

import de.xima.fc.form.expression.exception.CannotAcquireEvaluationContextException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.ICustomScope;
import de.xima.fc.form.expression.iface.context.IEmbedment;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IScope;
import de.xima.fc.form.expression.iface.context.ITracer;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.impl.GenericEmbedment;
import de.xima.fc.form.expression.impl.GenericScope;
import de.xima.fc.form.expression.impl.ReadScopedEvaluationContext.Builder;
import de.xima.fc.form.expression.impl.externalcontext.AGenericExternalContext;
import de.xima.fc.form.expression.impl.scope.MathScope;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;

public enum GenericEcContractFactory implements IEvaluationContextContractFactory<AGenericExternalContext> {
	INSTANCE;

	private final static IEmbedment embedment = GenericEmbedment.getNewGeneralEmbedment();
	private final static Set<String> external = new HashSet<>();
	static {
		external.add(MathScope.INSTANCE.getScopeName());
	}

	@Override
	public IEvaluationContext getContextWithExternal(final AGenericExternalContext ex) throws CannotAcquireEvaluationContextException {
		final IEvaluationContext ec = makeEc();
		ec.setExternalContext(ex);
		return ec;
	}

	private static IScope makeScope() {
		final ICustomScope mathScope = MathScope.INSTANCE;
		return new GenericScope.Builder().addCustomScope(mathScope).build();
	}

	private static IEvaluationContext makeEc() {
		final ITracer<Node> tracer = new GenericTracer();
		final IEmbedment embedment = GenericEmbedment.getNewGeneralEmbedment();
		final IScope scope = makeScope();
		return new Builder()
				.setEmbedment(embedment)
				.setScope(scope)
				.setTracer(tracer)
				.build();
	}

	@Override
	public boolean isProvidingExternalScope(final String scope) {
		return external.contains(scope);
	}

	@Override
	public String[] getScopesForEmbedment(final String name) {
		return embedment.getScopeList(name);
	}

}
