package de.xima.fc.form.expression.enums;

import de.xima.fc.form.expression.iface.evaluate.ILogger;

public enum ELogLevel {
	ERROR {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.error(message);
		}
	},
	WARN {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.warn(message);
		}
	},
	INFO {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.info(message);
		}
	},
	DEBUG {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.debug(message);
		}
	};

	public abstract void log(ILogger logger, String message);
}
