package de.xima.fc.form.expression.enums;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.util.CmnCnst;

@ParametersAreNonnullByDefault
public enum ELogLevel {
	ERROR(1) {
		@Override
		public void log(final ILogger logger, @Nullable final String message, @Nullable final Throwable cause) {
			logger.error(message, cause);
		}
		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_ERROR;
		}
	},
	WARN(2) {
		@Override
		public void log(final ILogger logger, @Nullable final String message, @Nullable final Throwable cause) {
			logger.warn(message, cause);
		}
		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_WARN;
		}
	},
	INFO(3) {
		@Override
		public void log(final ILogger logger, @Nullable final String message, @Nullable final Throwable cause) {
			logger.info(message, cause);
		}
		@Override
		public String getSyntaxName() {
			return CmnCnst.Syntax.LOG_INFO;
		}
	},
	DEBUG(4) {
		@Override
		public void log(final ILogger logger, @Nullable final String message, @Nullable final Throwable cause) {
			logger.debug(message, cause);
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
	public abstract void log(ILogger logger, @Nullable final String message, @Nullable Throwable cause);
	public abstract String getSyntaxName();
}