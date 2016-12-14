package de.xima.fc.form.expression.enums;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.util.CmnCnst;

@ParametersAreNonnullByDefault
public enum ELogLevel {
	ERROR(1) {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.error(message);
		}
		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_ERROR;
		}
	},
	WARN(2) {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.warn(message);
		}
		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_WARN;
		}
	},
	INFO(3) {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.info(message);
		}
		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_INFO;
		}
	},
	DEBUG(4) {
		@Override
		public void log(final ILogger logger, final String message) {
			logger.debug(message);
		}
		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_DEBUG;
		}
	};
	public final int numeric;
	private ELogLevel(final int numeric) {
		this.numeric = numeric;
	}
	public abstract void log(ILogger logger, String message);
	public abstract String getSyntaxName();
}