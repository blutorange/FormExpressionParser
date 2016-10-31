package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.impl.externalcontext.AHtmlExternalContext;

/**
 * Thrown when invalid data is written by template scripts. For example,
 * this is thrown when {@link AHtmlExternalContext} encounters bad HTML.
 * @author mad_gaksha
 */
public class InvalidTemplateDataException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public InvalidTemplateDataException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}
}
