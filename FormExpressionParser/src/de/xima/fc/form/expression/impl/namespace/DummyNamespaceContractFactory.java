package de.xima.fc.form.expression.impl.namespace;

import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.evaluate.INamespaceContractFactory;

public enum DummyNamespaceContractFactory implements INamespaceContractFactory {
	INSTANCE
	;
	@Override
	public INamespace makeNamespace() {
		return GenericNamespace.getGenericNamespaceInstance();
	}
}
