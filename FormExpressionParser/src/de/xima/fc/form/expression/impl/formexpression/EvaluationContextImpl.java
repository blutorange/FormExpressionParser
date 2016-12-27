package de.xima.fc.form.expression.impl.formexpression;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IClosure;
import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.ILibrary;
import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;

@ParametersAreNonnullByDefault
public final class EvaluationContextImpl implements IEvaluationContext {

	private final INamespace namespace;
	private final ILogger logger;
	private final ITracer<Node> tracer;
	private final ILibrary library;
	private final IEmbedment embedment;
	
	@Nullable
	private IExternalContext externalContext;

	private IClosure[] closureStack;
	private int closurePos;

	private EJump jumpType;
	@Nullable private String jumpLabel;
	
	/**
	 * Creates a new evaluation context.
	 *
	 * @param ecFactory
	 *            Factory for creating an evaluation context.
	 */
	public EvaluationContextImpl(final IEvaluationContextContract<?> ecFactory, final String logName,
			@Nullable final ELogLevel logLevel) {
		this.namespace = ecFactory.getNamespaceFactory().make();
		this.library = ecFactory.getLibraryFactory().make();
		this.tracer = ecFactory.makeTracer();
		this.logger = ecFactory.getLoggerFactory().make(logName, logLevel);
		this.embedment = ecFactory.getEmbedmentFactory().make();
		this.closureStack =  new IClosure[16];
		this.closurePos = -1;
		this.jumpType = EJump.NONE;
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
		namespace.reset();
		logger.reset();
		if (externalContext != null)
			externalContext.reset();
		externalContext = null;
		closureStack =  new IClosure[16];
		closurePos = -1;
		jumpType = EJump.NONE;
		jumpLabel = null;
	}

	@Override
	public void setExternalContext(@Nullable final IExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	@Nullable
	@Override
	public IExternalContext getExternalContext() {
		return externalContext;
	}
	
	@Override
 	@SuppressWarnings("null") // Arrays.copyOf does not return null.
	public void closureStackPush(@Nonnull final IClosure closure) {
		++closurePos;
		if (closurePos >= closureStack.length)
			closureStack = Arrays.copyOf(closureStack, closurePos*2);
		closureStack[closurePos] = closure;
	}
 	
	@Override
	public void closureStackPop() {
		if (closurePos >= 0) {
	 		closureStack[closurePos] = null;
			--closurePos;
		}
	}
	
	@Nullable
	@Override
	public IClosure closureStackPeek() {
		return closurePos >= 0 ? closureStack[closurePos] : null;
	}

	@Override
	public EJump getJumpType() {
		return jumpType;
	}

	@Nullable
	@Override
	public String getJumpLabel() {
		return jumpLabel;
	}

	@Override
	public void unsetJump() {
		jumpType = EJump.NONE;
		jumpLabel = null;
	}

	@Override
	public boolean hasJump() {
		return jumpType != EJump.NONE;
	}

	@Override
	public void setJump(final EJump jumpType, @Nullable final String jumpLabel) {
		this.jumpType = jumpType;
		this.jumpLabel = jumpType == EJump.RETURN ? null : jumpLabel;
	}

	@Override
	public boolean matchesNamedJump(@Nullable final String label) {
		//if (jumpType == EJump.NONE || jumpType == EJump.RETURN)
		//	return false;
		if (jumpLabel != null)
			return jumpLabel.equals(label);
		return true;
	}
}