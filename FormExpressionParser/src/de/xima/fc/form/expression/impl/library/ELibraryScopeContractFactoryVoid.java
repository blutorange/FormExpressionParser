package de.xima.fc.form.expression.impl.library;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.impl.variable.provider.NumberVariableProvider.StaticNumberVariableProvider;
import de.xima.fc.form.expression.impl.variable.provider.SimpleFunctionVariableProvider.SingleArgFunctionVariableProvider;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.Void;

@NonNullByDefault
public enum ELibraryScopeContractFactoryVoid implements ILibraryScopeContractFactory<Void> {
	EMPTY(new LibraryScopeContractFactoryVoid.Builder(CmnCnst.NonnullConstant.STRING_EMPTY).build()),
	MATH(new LibraryScopeContractFactoryVoid.Builder(CmnCnst.CustomScope.MATH)
					.addVariable(CmnCnst.CustomScope.MATH_PI, new StaticNumberVariableProvider(Math.PI))
					.addVariable(CmnCnst.CustomScope.MATH_E, new StaticNumberVariableProvider(Math.E))
					.build()
	),
	@SuppressWarnings("serial")
	DOC(new LibraryScopeContractFactoryVoid.Builder(CmnCnst.CustomScope.DOC)
			.addFunction(new SingleArgFunctionVariableProvider<StringLangObject>(CmnCnst.CustomScope.DOC_REMOVE_ENCLOSING_TAG,
					CmnCnst.CustomScope.DOC_REMOVE_ENCLOSING_TAG_ARG0, SimpleVariableType.STRING) {
				@Override
				protected void compute(final StringLangObject arg, final IEvaluationContext ec) {
					final IExternalContext ex = ec.getExternalContext();
					if (ex != null)
						ex.process(DocumentCommand.newRemoveEnclosingTag(arg.stringValue()), ec);
				}
			})
			.addFunction(new SingleArgFunctionVariableProvider<StringLangObject>(CmnCnst.CustomScope.DOC_OUTPUT,
					CmnCnst.CustomScope.DOC_OUTPUT_ARG0, SimpleVariableType.STRING) {
				@Override
				protected void compute(final StringLangObject arg, final IEvaluationContext ec) throws EvaluationException {
					final IExternalContext ex = ec.getExternalContext();
					if (ex != null)
						ex.write(arg.stringValue());
				}
			})
			.addFunction(new SingleArgFunctionVariableProvider<StringLangObject>(CmnCnst.CustomScope.DOC_OUTPUT_NEWLINE,
					CmnCnst.CustomScope.DOC_OUTPUT_NEWLINE_ARG0, SimpleVariableType.STRING) {
				@Override
				protected void compute(final StringLangObject arg, final IEvaluationContext ec) throws EvaluationException {
					final IExternalContext ex = ec.getExternalContext();
					if (ex != null) {
						ex.write(arg.stringValue());
						ex.write(CmnCnst.NonnullConstant.STRING_LF);
					}
				}
			})
			.build()),
	;

	private final ILibraryScopeContractFactory<Void> impl;

	private ELibraryScopeContractFactoryVoid(final ILibraryScopeContractFactory<Void> impl) {
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
	public ILibraryScope<Void> make() {
		return impl.make();
	}

	@Nullable
	@Override
	public IVariableType getVariableType(final String variableName) {
		return impl.getVariableType(variableName);
	}
}