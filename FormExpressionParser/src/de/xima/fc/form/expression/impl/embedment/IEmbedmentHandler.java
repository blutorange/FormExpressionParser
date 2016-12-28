package de.xima.fc.form.expression.impl.embedment;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IEmbedmentHandler extends Serializable {
	/** @return List of scopes this handler defines. */
	public String[] getScopeList();
	/** @return Whether this handler allows output. */
	public boolean isDoOutput();
}