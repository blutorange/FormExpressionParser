package de.xima.fc.form.expression.processor;

import java.lang.reflect.Field;
import java.util.Set;

import org.reflections.Reflections;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

public class EvaluateProcessor extends GenericProcessor<ALangObject, IEvaluationContext, EvaluationException> {

	private static int NODE_TYPE_COUNT = 30;
	private static IEvaluateProcessorTask[] tasksForNode;
	static {
		tasksForNode = new IEvaluateProcessorTask[NODE_TYPE_COUNT];
		final Set<Class<?>> taskList = new Reflections(EvaluateProcessor.class.getPackage().getName())
				.getTypesAnnotatedWith(EvaluateProcessorTask.class);
		for (final Class<?> task : taskList) {
			final EvaluateProcessorTask t = task.getAnnotation(EvaluateProcessorTask.class);
			if (t == null) continue;
			if (t.id() >= NODE_TYPE_COUNT)
				throw new ExceptionInInitializerError(String.format("Bad node type id %s for task set %s",
						new Integer(t.id()), task.getCanonicalName()));
			for (final Field field : task.getDeclaredFields()) {
				if (field.getAnnotation(EvaluateProcessorInitialTask.class) != null) {
					final Object o;
					try {
						o = field.get(null);
					}
					catch (final IllegalAccessError e) {
						throw new ExceptionInInitializerError(
								String.format("Failed to read initial task field %s.", field));
					}
					catch (final IllegalArgumentException e) {
						throw new ExceptionInInitializerError(
								String.format("Failed to read initial task field %s.", field));
					}
					catch (final IllegalAccessException e) {
						throw new ExceptionInInitializerError(
								String.format("Failed to read initial task field %s.", field));
					}
					if (!(o instanceof IEvaluateProcessorTask))
						throw new ExceptionInInitializerError(
								String.format("Initial task of class %s is not of the correct class.",
										o.getClass().getCanonicalName()));
					tasksForNode[t.id()] = (IEvaluateProcessorTask) o;
					break;
				}
			}
		}
	}

	public EvaluateProcessor(final IEvaluationContext ec) {
		super(ec, tasksForNode);
	}
}
