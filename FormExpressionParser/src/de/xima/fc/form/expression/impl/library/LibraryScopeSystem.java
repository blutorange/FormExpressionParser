package de.xima.fc.form.expression.impl.library;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.Void;

@ParametersAreNonnullByDefault
public enum LibraryScopeSystem implements ILibraryScopeContractFactory<Void> {
	MATH(
		new GenericLibraryScopeFactory.Builder(CmnCnst.CustomScope.MATH)
			.addVariable("pi", new LibVar(NumberLangObject.getPiInstance()))
			.addVariable("e", new LibVar(NumberLangObject.getEInstance()))
			.build()
	);

	private final ILibraryScopeContractFactory<Void> impl;

	private LibraryScopeSystem(final ILibraryScopeContractFactory<Void> impl) {
		this.impl = impl;
	}

	@Override
	public String getScopeName() {
		return impl.getScopeName();
	}

	@Override
	public boolean isProviding(final String variableName) {
		return impl.isProviding(variableName);
	}

	@Override
	public EVariableSource getSource() {
		return impl.getSource();
	}

	@Override
	public ILibraryScope<Void> makeScope() {
		return impl.makeScope();
	}

	@Nullable
	@Override
	public IVariableType getVariableType(final String variableName) {
		return impl.getVariableType(variableName);
	}
}