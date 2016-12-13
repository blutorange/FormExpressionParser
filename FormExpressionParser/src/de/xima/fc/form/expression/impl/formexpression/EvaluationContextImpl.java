package de.xima.fc.form.expression.impl.formexpression;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.ILibrary;
import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableReference;
import de.xima.fc.form.expression.impl.variable.GenericVariableReference;
import de.xima.fc.form.expression.util.CmnCnst;

public final class EvaluationContextImpl implements IEvaluationContext {

	@Nonnull private final INamespace namespace;
	@Nonnull private final ILogger logger;
	@Nonnull private final ITracer<Node> tracer;
	@Nonnull private final ILibrary library;
	@Nonnull private final IEmbedment embedment;
	@Nullable private IExternalContext externalContext;
	@Nonnull private IVariableReference[] symbolTable;

	/**
	 * Creates a new evaluation context.
	 * @param ecFactory Factory for creating an evaluation context.
	 */
	public EvaluationContextImpl(final IEvaluationContextContractFactory<?> ecFactory) {
		this.namespace = ecFactory.getNamespaceFactory().makeNamespace();
		this.library = ecFactory.getLibraryFactory().makeLibrary();
		this.tracer = ecFactory.makeTracer();
		this.logger = ecFactory.makeLogger();
		this.embedment = ecFactory.getEmbedmentFactory().makeEmbedment();
		this.symbolTable = CmnCnst.NonnullConstant.EMPTY_SYMBOL_TABLE;
	}

	@Override
	public INamespace getNamespace() {
		return namespace;
	}

	@Override
	public ILogger getLogger() {
		return logger;
	}

	@Override
	public IEmbedment getEmbedment() {
		return embedment;
	}

	@Override
	public boolean variableNameEquals(final String name1, final String name2) {
		return name1.equals(name2);
	}

	@Override
	public ILibrary getLibrary() {
		return library;
	}

	@Override
	public ITracer<Node> getTracer() {
		return tracer;
	}

	@Override
	public void reset() {
		library.reset();
		embedment.reset();
		tracer.reset();
		symbolTable = CmnCnst.NonnullConstant.EMPTY_SYMBOL_TABLE;
		externalContext = null;
	}

	@Override
	public void setExternalContext(final IExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	@Override
	public IExternalContext getExternalContext() {
		return externalContext;
	}

	@Override
	public void createSymbolTable(int symbolTableSize) {
		symbolTableSize = symbolTableSize < 0 ? 0 : symbolTableSize;
		if (symbolTable.length != symbolTableSize)
			symbolTable = new IVariableReference[symbolTableSize];
		for (int i = symbolTableSize; i-- > 0;)
			symbolTable[i] = new GenericVariableReference();
	}

	@Override
	public IVariableReference[] getSymbolTable() {
		return symbolTable;
	}
}