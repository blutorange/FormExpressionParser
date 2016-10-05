package de.xima.fc.form.expression.util;

import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.CannotAcquireEvaluationContextException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.pool.FormcycleEcFactory;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;

public final class FormExpressionEvaluationUtil {
	private final static Logger LOG = LoggerFactory.getLogger(FormExpressionEvaluationUtil.class);
	private FormExpressionEvaluationUtil(){}
	
	public final static class Formcycle {
		private Formcycle(){}
		
		public static ALangObject evalProgram(final String code, final Writer documentWriter) throws ParseException, EvaluationException {
			final Node node = FormExpressionParseFactory.Program.parse(code);
			return eval(node, documentWriter);
		}
		
		public static ALangObject evalTemplate(final String code, final Writer documentWriter) throws ParseException, EvaluationException {
			final Node node = FormExpressionParseFactory.Template.parse(code);
			return eval(node, documentWriter);
		}
		
		public static ALangObject eval(final Node node, final Writer documentWriter) throws EvaluationException, IllegalArgumentException {
			if (node == null) return NullLangObject.getInstance();
			final IEvaluationContext ec;
			try {
				ec = FormcycleEcFactory.getPoolInstance().borrowObject();
			}
			catch (Exception exception) {
				throw new CannotAcquireEvaluationContextException(exception);
			}
			ec.getEmbedment().setWriter(documentWriter);
			try {
				return EvaluateVisitor.evaluateCode(node, ec);
			}
			finally {
				try {
					FormcycleEcFactory.getPoolInstance().returnObject(ec);
				}
				catch (Exception e) {
					LOG.error("Failed to return evaluation context to pool.", e);
				}
			}
		}
	}
}
