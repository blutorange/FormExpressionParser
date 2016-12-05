package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.InvalidTemplateDataException;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand;
import de.xima.fc.form.expression.object.ALangObject;

public interface IExternalContext {
	/**
	 * Called once before evaluation begins.
	 * 
	 * @throws EmbedmentOutputException
	 *             When there is an I/O error.
	 */
	public void beginWriting() throws EmbedmentOutputException;

	/**
	 * Called during evaluation. Writes the given data. Data needs to be written
	 * when evaluating template files.
	 * 
	 * @param data
	 *            The data to be written.
	 * @throws EmbedmentOutputException
	 *             When an I/O error occurs.
	 * @throw InvalidTemplateDataException When the data is syntactically or
	 *        semantically invalid.
	 */
	public void write(@Nonnull String data) throws EmbedmentOutputException, InvalidTemplateDataException;

	/**
	 * Called once after evaluation is finished.
	 * 
	 * @throws EmbedmentOutputException
	 *             When an I/O error occurs.
	 * @throw InvalidTemplateDataException When the data is syntactically or
	 *        semantically invalid.
	 */
	public void finishWriting() throws EmbedmentOutputException, InvalidTemplateDataException;

	/**
	 * Processes an isused command. For example, a function may issue the
	 * command {@link DocumentCommand#removeParagraph}. When the external
	 * context supports it, it takes that command and removes the current
	 * paragraph (eg. the current &lt;p&gt; tag) from the output.
	 * 
	 * @param command
	 *            The command to process.
	 */
	public void process(@Nonnull IExternalContextCommand command, @Nonnull IEvaluationContext ec);

	/**
	 * <p>
	 * Used by external contexts to provide custom variables. By defining an
	 * appropriate embedment, these variables can be used even without their
	 * fully qualified name. For example, the embedment <code>[% %]</code>
	 * defines the default scope <code>fields</code>, which allow
	 * <code>fields::tf1</code> to be written as <code>tf1</code>. Note however
	 * that local variables take precedence:
	 * </p>
	 * <p>
	 * <code>[% tf1 = 0; tf1 != fields::tf1;%]</code>
	 * </p>
	 * <p>
	 * This comparison will yield <code>true</code> in general.
	 * </p>
	 *
	 * @param scope
	 *            Name of the requested scope.
	 * @param name
	 *            Name of the requested variable.
	 * @param ec
	 *            Current evaluation context.
	 * @return The value of the variable. It must adhere to the contract
	 *  given by {@link IEvaluationContextContractFactory} and may opt
	 *  to return a default value for some instances. 
	 * @throws EvaluationException Only when a scope and variable is
	 * requested not defined by the {@link IEvaluationContextContractFactory}.
	 * @see IEvaluationContextContractFactory
	 */
	@Nonnull
	public ALangObject fetchScopedVariable(@Nonnull String scope, @Nonnull String name, @Nonnull IEvaluationContext ec)
			throws EvaluationException;
}