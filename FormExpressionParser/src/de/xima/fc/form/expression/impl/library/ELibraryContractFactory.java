package de.xima.fc.form.expression.impl.library;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.ILibrary;
import de.xima.fc.form.expression.iface.evaluate.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.util.Void;

@ParametersAreNonnullByDefault
public enum ELibraryContractFactory implements ILibraryContractFactory {
	/** An empty library, acting as a dummy. */
	EMPTY(LibraryContractFactoryVoid.buildWith()),
	/** A generic library for getting started which is subject to change. */
	GENERIC(LibraryContractFactoryVoid.buildWith(ELibraryScopeContractFactoryVoid.values()))
	;

	private final ILibraryContractFactory impl;

	private ELibraryContractFactory(final ILibraryContractFactory impl) {
		this.impl = impl;
	}

	@Override
	public ILibrary make() {
		return impl.make();
	}

	@Override
	public boolean isProvidingScope(final String scope) {
		return impl.isProvidingScope(scope);
	}

	@Nullable
	@Override
	public ILibraryScopeContractFactory<Void> getScopeFactory(final String scope) {
		return impl.getScopeFactory(scope);
	}
}