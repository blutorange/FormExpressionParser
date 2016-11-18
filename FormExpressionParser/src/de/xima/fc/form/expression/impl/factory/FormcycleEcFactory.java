package de.xima.fc.form.expression.impl.factory;

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

class FormcycleEcFactory extends BasePooledObjectFactory<IEvaluationContext> {
	private final static class InstanceHolder {
		@Nonnull
		public final static BasePooledObjectFactory<IEvaluationContext> FACTORY_INSTANCE = new FormcycleEcFactory();
		@Nonnull
		public final static ObjectPool<IEvaluationContext> POOL_INSTANCE = new GenericObjectPool<>(FACTORY_INSTANCE);
	}

	private FormcycleEcFactory(){}

	@Override
	public void passivateObject(final PooledObject<IEvaluationContext> pooledEc) throws Exception {
		final IEvaluationContext ec = pooledEc.getObject();
		if (ec != null)
			ec.reset();
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
		return new GenericScope.Builder().addCustomScope(mathScope).build();
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
	public static BasePooledObjectFactory<IEvaluationContext> getFactoryInstance() {
		return InstanceHolder.FACTORY_INSTANCE;
	}

	@Nonnull
	public static ObjectPool<IEvaluationContext> getPoolInstance() {
		return InstanceHolder.POOL_INSTANCE;
	}
}