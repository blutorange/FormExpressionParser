package de.xima.fc.form.expression.impl.config;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.iface.config.IUnparseConfig;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;

/**
 * A configuration affecting the output of {@link IFormExpression#unparseBody(UnparseConfig)}.
 * It offers several options to change how FormExpression code will be formatted.
 * @author mad_gaksha
 */
@NonNullByDefault
public final class UnparseConfig implements IUnparseConfig {
	private final String indentPrefix;
	private final String linefeed;
	private final String optionalSpace;
	private final String requiredSpace;
	private final boolean keepComments;

	protected UnparseConfig(final String indentPrefix, final String linefeed,
			final String optionalSpace, final String requiredSpace,
			final boolean keepComments) {
		this.indentPrefix = indentPrefix;
		this.linefeed = linefeed;
		this.optionalSpace = optionalSpace;
		this.requiredSpace = requiredSpace;
		this.keepComments = keepComments;
	}

	@Override
	public String getIndentPrefix() {
		return indentPrefix;
	}

	@Override
	public String getLinefeed() {
		return linefeed;
	}

	@Override
	public String getOptionalSpace() {
		return optionalSpace;
	}

	@Override
	public String getRequiredSpace() {
		return requiredSpace;
	}

	@Override
	public boolean isKeepComments() {
		return keepComments;
	}

	public static class Builder {
		@Nullable private String indentPrefix;
		@Nullable private String linefeed;
		@Nullable private String optionalSpace;
		@Nullable private String requiredSpace;
		private boolean keepComments;

		/**
		 * Sets the prefix to the beginning of each line for each indentation level.
		 * @param indentPrefix Prefix for indentation. Defaults to two spaces (<code>  </code>).
		 * @return this
		 */
		public Builder setIndentPrefix(@Nullable final String indentPrefix) {
			this.indentPrefix = indentPrefix;
			return this;
		}
		/**
		 * String to be used as a linefeed, must be either \n, \t or \r\n.
		 * @param linefeed Character to be used for linefeeds, defaults to \n.
		 * @return this
		 */
		public Builder setLinefeed(@Nullable final String linefeed) {
			this.linefeed = linefeed;
			return this;
		}
		/**
		 * Sets the number spaces to be added when spaces are not required syntactically.
		 * <p>When set to 0: <code>if(true)</code></p>
		 * <p>When set to 4: <code>if    (true)</code></p>
		 * @param requiredSpace Number of required spaces, defaults to 1.
		 * @return this.
		 */
		public Builder setOptionalSpace(int optionalSpace) {
			if (optionalSpace < 0) optionalSpace = 0;
			this.optionalSpace = StringUtils.repeat(' ', optionalSpace);
			return this;
		}
		/**
		 * Sets the number spaces to be added when spaces are required syntactically.
		 * <p>When set to 1: <code>function foo(){}</code></p>
		 * <p>When set to 4: <code>function    foo(){}</code></p>
		 * @param requiredSpace Number of required spaces, default 1.
		 * @return this.
		 */
		public Builder setRequiredSpace(int requiredSpace) {
			if (requiredSpace < 1) requiredSpace = 1;
			this.requiredSpace = StringUtils.repeat(' ', requiredSpace);
			return this;
		}
		/**
		 * @param keepComments Whether the output should contain comments.
		 * @return this
		 */
		public Builder setKeepComments(final boolean keepComments) {
			this.keepComments = keepComments;
			return this;
		}
		/**
		 * @param linefeed Character to be used as for newlines.
		 * @return this
		 * @see #setLinefeed(String)
		 */
		public Builder setLinefeed(final char linefeed) {
			this.linefeed = Character.toString(linefeed);
			return this;
		}
		/** @return A new, sane configuration with the configured options. */
		public UnparseConfig build() {
			String indentPrefix = this.indentPrefix;
			String linefeed = this.linefeed;
			String optionalSpace = this.optionalSpace;
			String requiredSpace = this.requiredSpace;
			if (indentPrefix == null) indentPrefix = CmnCnst.NonnullConstant.STRING_SPACE;
			if (linefeed == null) linefeed = CmnCnst.NonnullConstant.STRING_LF;
			if (optionalSpace == null) optionalSpace = CmnCnst.NonnullConstant.STRING_EMPTY;
			if (requiredSpace == null) requiredSpace = CmnCnst.NonnullConstant.STRING_SPACE;
			indentPrefix = indentPrefix.replaceAll("[^\t ]", CmnCnst.NonnullConstant.STRING_EMPTY); //$NON-NLS-1$
			linefeed = linefeed.replaceAll("[^\r\n]", CmnCnst.NonnullConstant.STRING_EMPTY); //$NON-NLS-1$
			if (indentPrefix == null) indentPrefix = CmnCnst.NonnullConstant.STRING_SPACE;
			if (linefeed == null) linefeed = CmnCnst.NonnullConstant.STRING_LF;
			return new UnparseConfig(indentPrefix, linefeed, optionalSpace, requiredSpace, keepComments);
		}
	}

	private final static class InstanceHolder {
		public final static UnparseConfig STYLED = new Builder()
				.setLinefeed(Syntax.LINEFEED)
				.setIndentPrefix(Syntax.INDENT)
				.setOptionalSpace(1)
				.setRequiredSpace(1)
				.setKeepComments(true)
				.build();
		public final static UnparseConfig STYLED_WITHOUT_COMMENTS = new Builder()
				.setLinefeed(Syntax.LINEFEED)
				.setIndentPrefix(Syntax.INDENT)
				.setOptionalSpace(1)
				.setRequiredSpace(1)
				.setKeepComments(false)
				.build();
		public final static UnparseConfig UNSTYLED_WITH_COMMENTS = new Builder()
				.setLinefeed(StringUtils.EMPTY)
				.setIndentPrefix(StringUtils.EMPTY)
				.setOptionalSpace(0)
				.setRequiredSpace(1)
				.setKeepComments(true)
				.build();
		public final static UnparseConfig UNSTYLED_WITHOUT_COMMENTS = new Builder()
				.setLinefeed(StringUtils.EMPTY)
				.setIndentPrefix(StringUtils.EMPTY)
				.setOptionalSpace(0)
				.setRequiredSpace(1)
				.setKeepComments(false)
				.build();
	}

	/**
	 * @return Some (working) configuration, no guarantees on its details.
	 */
	public static UnparseConfig getDefaultConfig() {
		return InstanceHolder.STYLED;
	}

	public static UnparseConfig getStyledWithCommentsConfig() {
		return InstanceHolder.STYLED;
	}

	public static UnparseConfig getStyledWithoutCommentsConfig() {
		return InstanceHolder.STYLED_WITHOUT_COMMENTS;
	}

	/**
	 * @return A config that keeps comments, but does not add any optional spaces or newlines.
	 */
	public static UnparseConfig getUnstyledWithCommentsConfig() {
		return InstanceHolder.UNSTYLED_WITH_COMMENTS;
	}

	/**
	 * @return A config that does not keep comments, nor does it add any optional spaces or newlines.
	 */
	public static UnparseConfig getUnstyledWithoutCommentsConfig() {
		return InstanceHolder.UNSTYLED_WITHOUT_COMMENTS;
	}
}