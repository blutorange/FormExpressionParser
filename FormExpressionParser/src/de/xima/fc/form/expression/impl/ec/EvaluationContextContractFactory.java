package de.xima.fc.form.expression.impl.ec;

import java.io.Writer;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.impl.embedment.DummyEmbedmentContractFactory;
import de.xima.fc.form.expression.impl.embedment.EEmbedmentFactory;
import de.xima.fc.form.expression.impl.externalcontext.DummyExternalContextFactory;
import de.xima.fc.form.expression.impl.externalcontext.WriterExternalContextContractFactory;
import de.xima.fc.form.expression.impl.library.DummyLibraryContractFactory;
import de.xima.fc.form.expression.impl.logger.DummyLogger;
import de.xima.fc.form.expression.impl.logger.SystemLogger;
import de.xima.fc.form.expression.util.Void;

@ParametersAreNonnullByDefault
public class EvaluationContextContractFactory {
	public static IEvaluationContextContractFactory<Writer> WRITER = new GenericContractFactory.Builder<Writer>()
			.setEmbedment(EEmbedmentFactory.GENERAL)
			.setExternal(WriterExternalContextContractFactory.getInstance())
			.setLogger(SystemLogger.getInfoLogger())
			.setTracing(true)
			.build();
	public static IEvaluationContextContractFactory<Void> DUMMY = new GenericContractFactory.Builder<Void>()
			.setEmbedment(DummyEmbedmentContractFactory.getInstance())
			.setExternal(DummyExternalContextFactory.<Void>getFactory())
			.setLibrary(DummyLibraryContractFactory.getInstance())
			.setLogger(DummyLogger.INSTANCE)
			.setTracing(false)
			.build();
}