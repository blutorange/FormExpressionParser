package de.xima.fc.form.expression.impl.logger;

import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.iface.factory.ILoggerContractFactory;

public enum ELoggerContractFactory implements ILoggerContractFactory {
	SYSTEM {
		@Override
		public ILogger make(final String name, @Nullable final ELogLevel level) {
			if (level == null)
				return DummyLogger.INSTANCE;
			return SystemLogger.get(name, level);
		}
	},
	JAVA {
		@Override
		public ILogger make(final String name, @Nullable final ELogLevel level) {
			if (level == null)
				return DummyLogger.INSTANCE;
			return new JavaLogger(name, level);
		}
	},
	SL4J {
		@Override
		public ILogger make(final String name, @Nullable final ELogLevel level) {
			if (level == null)
				return DummyLogger.INSTANCE;
			return new Slf4jLogger(name, level);
		}
	},
	DUMMY {
		@Override
		public ILogger make(final String name, @Nullable final ELogLevel level) {
			return DummyLogger.INSTANCE;
		}
	}
	;
}
