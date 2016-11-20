package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.impl.externalcontext.AHtmlExternalContext;

/**
 * Thrown when invalid data is written by template scripts. For example,
 * this is thrown when {@link AHtmlExternalContext} encounters bad HTML.
 * @author mad_gaksha
 */
public class InvalidTemplateDataException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public InvalidTemplateDataException(@Nonnull final String msg, @Nonnull final Throwable throwable) {
		super(msg, throwable);
	}
}
