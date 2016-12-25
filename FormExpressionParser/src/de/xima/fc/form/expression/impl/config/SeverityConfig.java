package de.xima.fc.form.expression.impl.config;

import java.util.Collections;
import java.util.EnumSet;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import de.xima.fc.form.expression.enums.ESeverityOption;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * A configuration affecting the output of {@link IFormExpression#unparseBody(SeverityConfig)}.
 * It offers several options to change how FormExpression code will be formatted.
 * @author mad_gaksha
 */
@ParametersAreNonnullByDefault
public final class SeverityConfig implements ISeverityConfig {
	private final EnumSet<ESeverityOption> options;

	private SeverityConfig(final EnumSet<ESeverityOption> options) {
		this.options = NullUtil.checkNotNull(EnumSet.copyOf(options));
	}

	@Override
	public boolean hasOption(final ESeverityOption option) {
		return options.contains(option);
	}

	public static class Builder {
		private final EnumSet<ESeverityOption> options;

		public Builder() {
			options = NullUtil.checkNotNull(EnumSet.noneOf(ESeverityOption.class));
		}

		public Builder enableOption(final ESeverityOption... option) {
			Preconditions.checkNotNull(option);
			Collections.addAll(options, option);
			return this;
		}

		public Builder disableOption(final ESeverityOption... option) {
			for (final ESeverityOption o : option)
				options.remove(o);
			return this;
		}

		/** @return A new, sane configuration with the configured options. */
		public SeverityConfig build() {
			return new SeverityConfig(options);
		}
	}

	private final static class InstanceHolder {
		public final static SeverityConfig STRICT = new Builder()
				.enableOption(NullUtil.checkNotNull(ESeverityOption.values()))
				.build();
		public final static SeverityConfig LOOSE = new Builder()
				.disableOption(NullUtil.checkNotNull(ESeverityOption.values()))
				.build();
	}

	/** @return A configuration with all options enabled. */
	public static SeverityConfig getStrictConfig() {
		return InstanceHolder.STRICT;
	}

	/** @return A configuration with all options disabled. */
	public static SeverityConfig getLooseConfig() {
		return InstanceHolder.LOOSE;
	}
}