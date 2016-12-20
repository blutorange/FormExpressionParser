package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.iface.factory.IEmbedmentContractFactory;
import de.xima.fc.form.expression.iface.factory.IExternalContextContractFactory;
import de.xima.fc.form.expression.iface.factory.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.factory.ILoggerContractFactory;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory;

/**
 * Contains methods for getting details on the {@link IEvaluationContext}
 * created by {@link #getContextWithExternal(IExternalContext)}.
 *
 * @author madgaksha
 *
 * @param <T>
 *            Type of the required object for the external context.
 */
@ParametersAreNonnullByDefault
public interface IEvaluationContextContract<T> extends Serializable {
	/**
	 * Returns an embedment factory for creating embedments and providing
	 * information such as which scopes the embedment provides.
	 * @return An embedment factory with info.
	 */
	public IEmbedmentContractFactory getEmbedmentFactory();

	/**
	 * @return A tracer to be used when creating evaluation contexts.
	 */
	public ITracer<Node> makeTracer();

	/**
	 * @return A factory for creating loggers.
	 */
	public ILoggerContractFactory getLoggerFactory();

	public INamespaceContractFactory getNamespaceFactory();

	/**
	 * Returns a contract factory which should be used by all {@link IEvaluationContext}
	 * created by this factory.
	 * @return Library factory with info.
	 */
	public ILibraryContractFactory getLibraryFactory();

	/**
	 * Returns an external factory for creating external contexts and
	 * providing information such as which scopes and variables that
	 * external context can provide.
	 * @return External factory with info.
	 */
	public IExternalContextContractFactory<T> getExternalFactory();
}
