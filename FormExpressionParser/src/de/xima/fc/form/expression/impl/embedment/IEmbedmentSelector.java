package de.xima.fc.form.expression.impl.embedment;

public interface IEmbedmentSelector<T> {
	public IEmbedmentHandler<T> forName(String name);
}
