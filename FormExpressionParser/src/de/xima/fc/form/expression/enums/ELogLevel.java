package de.xima.fc.form.expression.enums;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.util.CmnCnst;

@ParametersAreNonnullByDefault
public enum ELogLevel {
	ERROR {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.error(message);
		}

		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_ERROR;
		}
	},
	WARN {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.warn(message);
		}

		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_WARN;
		}
	},
	INFO {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.info(message);
		}

		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_INFO;
		}
	},
	DEBUG {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.debug(message);
		}

		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_DEBUG;
		}
	};

	public abstract void log(ILogger logger, String message);
	public abstract String getSyntaxName();
}