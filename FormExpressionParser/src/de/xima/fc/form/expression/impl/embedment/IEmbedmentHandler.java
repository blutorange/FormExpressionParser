package de.xima.fc.form.expression.impl.embedment;

import javax.annotation.Nonnull;

public interface IEmbedmentHandler {
	@Nonnull
	public String[] getScopeList();
	public boolean isDoOutput();
}