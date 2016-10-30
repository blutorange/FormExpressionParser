package de.xima.fc.form.expression.impl.embedment;

import javax.annotation.Nonnull;

public interface IEmbedmentHandler {
	/** @return List of scopes this embedment defines. */
	@Nonnull
	public String[] getScopeList();
	public boolean isDoOutput();
}