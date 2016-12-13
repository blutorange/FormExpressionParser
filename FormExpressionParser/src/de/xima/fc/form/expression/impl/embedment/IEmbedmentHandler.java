package de.xima.fc.form.expression.impl.embedment;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface IEmbedmentHandler {
	/** @return List of scopes this handler defines. */
	public String[] getScopeList();
	/** @return Whether this handler allows output. */
	public boolean isDoOutput();
}