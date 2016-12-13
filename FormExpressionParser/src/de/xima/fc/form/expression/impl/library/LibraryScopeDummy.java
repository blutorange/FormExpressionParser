package de.xima.fc.form.expression.impl.library;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.Void;

public final class LibraryScopeDummy {
	private LibraryScopeDummy() {}
	public static ILibraryScopeContractFactory<Void> getInstance() {
		return DummyImpl.INSTANCE;
	}
	// Make the API not expose a way for obtaining a
	// ILibraryScope<Void> implementation.
	private static enum DummyImpl implements ILibraryScope<Void>, ILibraryScopeContractFactory<Void> {
		INSTANCE;
		@Override
		public boolean isProviding(final String variableName) {
			return false;
		}
		@Override
		public String getScopeName() {
			return CmnCnst.NonnullConstant.STRING_EMPTY;
		}
		@Override
		public ALangObject fetch(@Nonnull final String variableName, final Void object, @Nonnull final IEvaluationContext ec) throws EvaluationException {
			throw new VariableNotDefinedException(variableName, ec);
		}
		@Override
		public EVariableSource getSource() {
			return EVariableSource.LIBRARY;
		}
		@Override
		public ILibraryScope<Void> makeScope() {
			return this;
		}
		@Override
		public IVariableType getVariableType(final String variableName) {
			return null;
		}
	}
}