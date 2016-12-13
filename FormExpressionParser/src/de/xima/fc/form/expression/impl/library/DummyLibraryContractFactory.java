package de.xima.fc.form.expression.impl.library;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibrary;
import de.xima.fc.form.expression.iface.evaluate.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.Void;

@ParametersAreNonnullByDefault
public enum DummyLibraryContractFactory implements ILibraryContractFactory, ILibrary {
	INSTANCE;
	@Override
	public ILibrary makeLibrary() {
		return this;
	}

	@Override
	public boolean isProvidingScope(final String scope) {
		return false;
	}

	@Nullable
	@Override
	public ILibraryScopeContractFactory<Void> getScopeFactory(final String scope) {
		return null;
	}

	@Override
	public ALangObject getVariable(final String scope, final String name, final IEvaluationContext ec)
			throws EvaluationException {
		throw new VariableNotDefinedException(scope, name, ec);
	}

	@SuppressWarnings("null")
	@Override
	public Collection<String> getProvidedScopes() {
		return Collections.emptySet();
	}

	@Override
	public void reset() {
	}
}