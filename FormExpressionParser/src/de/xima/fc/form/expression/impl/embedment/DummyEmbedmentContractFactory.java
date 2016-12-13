package de.xima.fc.form.expression.impl.embedment;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.iface.evaluate.IEmbedmentContractFactory;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

/**
 * A dummy embedment that simply prints any data to stdout.
 *
 * @author madgaksha
 */
@ParametersAreNonnullByDefault
public final class DummyEmbedmentContractFactory {
	private DummyEmbedmentContractFactory() {
	}

	public static IEmbedmentContractFactory getInstance() {
		return DummyImpl.INSTANCE;
	}

	private static enum DummyImpl implements IEmbedment, IEmbedmentContractFactory {
		INSTANCE;

		@Override
		public void reset() {
		}

		@Override
		public void setCurrentEmbedment(@Nullable final String name) {
		}

		@Override
		public String[] getScopeListForCurrentEmbedment() {
			return CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
		}

		@Override
		public void outputCode(final String data, final IEvaluationContext ec) throws EmbedmentOutputException {
			System.out.print(data);
		}

		@Override
		public void outputText(final String data, final IEvaluationContext ec) throws EmbedmentOutputException {
			System.out.print(data);
		}

		@Nullable
		@Override
		public String[] getScopeList(final String embedment) {
			return null;
		}

		@Override
		public String[] getEmbedmentList() {
			return CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
		}

		@Override
		public IEmbedment makeEmbedment() {
			return this;
		}

		@Nullable
		@Override
		public String[] getScopesForEmbedment(final String embedment) {
			return null;
		}
	}
}