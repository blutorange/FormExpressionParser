package de.xima.fc.form.expression.impl.pool;

import javax.annotation.Nonnull;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IBinding;
import de.xima.fc.form.expression.iface.context.ICustomScope;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IScope;
import de.xima.fc.form.expression.iface.context.ITracer;
import de.xima.fc.form.expression.impl.GenericEmbedment;
import de.xima.fc.form.expression.impl.GenericScope;
import de.xima.fc.form.expression.impl.ReadScopedEvaluationContext.Builder;
import de.xima.fc.form.expression.impl.binding.OnDemandLookUpBinding;
import de.xima.fc.form.expression.impl.scope.MathScope;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;

public class FormcycleEcFactory extends BasePooledObjectFactory<IEvaluationContext> {

	private final static class InstanceHolder {
		@Nonnull public final static ObjectPool<IEvaluationContext> INSTANCE = new GenericObjectPool<>(new FormcycleEcFactory());
	}

	@Override
	public void passivateObject(final PooledObject<IEvaluationContext> ec) throws Exception {
		if (ec.getObject() == null) return;
		ec.getObject().reset();
	}

	@Override
	public IEvaluationContext create() throws Exception {
		return makeEc();
	}

	@Override
	public PooledObject<IEvaluationContext> wrap(final IEvaluationContext ec) {
		return new DefaultPooledObject<IEvaluationContext>(ec);
	}

	private static IScope makeScope() {
		final ICustomScope mathScope = MathScope.INSTANCE;
		return new GenericScope.Builder()
				.addCustomScope(mathScope)
				.build();
	}

	private static IEvaluationContext makeEc() {
		final IBinding binding = new OnDemandLookUpBinding();
		final ITracer<Node> tracer = new GenericTracer();
		final Builder builder = new Builder();
		final IScope scope = makeScope();
		builder.setEmbedment(GenericEmbedment.getFormcycleEmbedment());
		builder.setBinding(binding);
		builder.setScope(scope);
		builder.setTracer(tracer);
		return builder.build();
	}

	@Nonnull
	public static ObjectPool<IEvaluationContext> getPoolInstance() {
		return InstanceHolder.INSTANCE;
	}
}