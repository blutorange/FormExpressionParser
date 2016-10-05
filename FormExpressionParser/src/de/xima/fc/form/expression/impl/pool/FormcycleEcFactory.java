package de.xima.fc.form.expression.impl.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.ICustomScope;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.impl.GenericEmbedment;
import de.xima.fc.form.expression.impl.GenericScope;
import de.xima.fc.form.expression.impl.ReadScopedEvaluationContext.Builder;
import de.xima.fc.form.expression.impl.binding.OnDemandLookUpBinding;
import de.xima.fc.form.expression.impl.scope.FormFieldScope;
import de.xima.fc.form.expression.impl.scope.FormFieldScope.FormVersion;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;

public class FormcycleEcFactory extends BasePooledObjectFactory<IEvaluationContext> {

	private final static class InstanceHolder {
		public final static ObjectPool<IEvaluationContext> INSTANCE = new GenericObjectPool<>(new FormcycleEcFactory());
	}	
	
	@Override
	public void passivateObject(PooledObject<IEvaluationContext> ec) throws Exception {
		if (ec.getObject() == null) return;
		try {
			ec.getObject().getEmbedment().flushWriter(ec.getObject());
		}
		finally {
			ec.getObject().reset();
		}
	}
	
	@Override
	public IEvaluationContext create() throws Exception {
		return makeEc();
	}

	@Override
	public PooledObject<IEvaluationContext> wrap(IEvaluationContext ec) {
		return new DefaultPooledObject<IEvaluationContext>(ec);
	}

	private static IScope makeScope() {
		final ICustomScope formFieldScope = new FormFieldScope(new FormVersion());
		final GenericScope.Builder builder = new GenericScope.Builder();
		builder.addCustomScope(formFieldScope);
		return builder.build();
	}

	private static IEvaluationContext makeEc() {
		final IBinding binding = new OnDemandLookUpBinding();
		final ITracer<Node> tracer = new GenericTracer();
		final Builder builder = new Builder();
		final IScope scope = makeScope();
		builder.setEmbedment(GenericEmbedment.getNewFormcycleEmbedment(null));
		builder.setBinding(binding);
		builder.setScope(scope);
		builder.setTracer(tracer);
		return builder.build();
	}

	public static ObjectPool<IEvaluationContext> getPoolInstance() {
		return InstanceHolder.INSTANCE;
	}
}