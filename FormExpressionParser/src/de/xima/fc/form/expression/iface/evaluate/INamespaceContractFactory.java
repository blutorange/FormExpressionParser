package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface INamespaceContractFactory extends Serializable {
	public INamespace makeNamespace();
}
