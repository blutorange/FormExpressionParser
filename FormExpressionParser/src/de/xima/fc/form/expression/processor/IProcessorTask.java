package de.xima.fc.form.expression.processor;

public interface IProcessorTask<R, T, E extends Exception> {
	public void process(GenericProcessor<R, T, E> processor) throws E;
}
