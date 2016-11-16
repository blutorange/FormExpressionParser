package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.util.CmnCnst.Syntax;

public final class UnparseVisitorConfig {
	public final String indentPrefix;
	public final String linefeed;
	public final String optionalSpace;
	public final String requiredSpace;
	public final boolean keepComments;
	private UnparseVisitorConfig(final String indentPrefix, final String linefeed, final String optionalSpace, final String requiredSpace, final boolean keepComments) {
		this.indentPrefix = indentPrefix;
		this.linefeed = linefeed;
		this.optionalSpace = optionalSpace;
		this.requiredSpace = requiredSpace;
		this.keepComments = keepComments;
	}
	public static class Builder {
		public String indentPrefix;
		public String linefeed;
		public String optionalSpace;
		public String requiredSpace;
		public boolean keepComments;
		public Builder setIndentPrefix(final String indentPrefix) {
			this.indentPrefix = indentPrefix;
			return this;
		}
		public Builder setLinefeed(final String linefeed) {
			this.linefeed = linefeed;
			return this;
		}
		public Builder setOptionalSpace(int optionalSpace) {
			if (optionalSpace < 0) optionalSpace = 0;
			this.optionalSpace = StringUtils.repeat(' ', optionalSpace);
			return this;
		}
		public Builder setRequiredSpace(int requiredSpace) {
			if (requiredSpace < 1) requiredSpace = 1;
			this.requiredSpace = StringUtils.repeat(' ', requiredSpace);
			return this;
		}
		public Builder setKeepComments(final boolean keepComments) {
			this.keepComments = keepComments;
			return this;
		}
		@Nonnull
		public UnparseVisitorConfig build() {
			if (indentPrefix == null) indentPrefix = StringUtils.SPACE;
			if (linefeed == null) linefeed = StringUtils.LF;
			if (optionalSpace == null) optionalSpace = StringUtils.EMPTY;
			if (requiredSpace == null) requiredSpace = StringUtils.SPACE;
			indentPrefix = indentPrefix.replaceAll("[^\t ]", StringUtils.EMPTY); //$NON-NLS-1$
			linefeed = linefeed.replaceAll("[^\r\n]", StringUtils.EMPTY); //$NON-NLS-1$
			if (linefeed.isEmpty()) linefeed = StringUtils.LF;
			return new UnparseVisitorConfig(indentPrefix, linefeed, optionalSpace, requiredSpace, keepComments);
		}
		public Builder setLinefeed(final char linefeed) {
			this.linefeed = Character.toString(linefeed);
			return this;
		}
	}

	private final static class InstanceHolder {
		@Nonnull public final static UnparseVisitorConfig STYLED = new Builder()
				.setLinefeed(Syntax.LINEFEED)
				.setIndentPrefix(Syntax.INDENT)
				.setOptionalSpace(1)
				.setRequiredSpace(1)
				.setKeepComments(true)
				.build();
		@Nonnull public final static UnparseVisitorConfig STYLED_WITHOUT_COMMENTS = new Builder()
				.setLinefeed(Syntax.LINEFEED)
				.setIndentPrefix(Syntax.INDENT)
				.setOptionalSpace(1)
				.setRequiredSpace(1)
				.setKeepComments(false)
				.build();
		@Nonnull public final static UnparseVisitorConfig UNSTYLED_WITH_COMMENTS = new Builder()
				.setLinefeed(StringUtils.EMPTY)
				.setIndentPrefix(StringUtils.EMPTY)
				.setOptionalSpace(0)
				.setRequiredSpace(1)
				.setKeepComments(true)
				.build();
		@Nonnull public final static UnparseVisitorConfig UNSTYLED_WITHOUT_COMMENTS = new Builder()
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
	@Nonnull
	public static UnparseVisitorConfig getDefaultConfig() {
		return InstanceHolder.STYLED;
	}

	@Nonnull
	public static UnparseVisitorConfig getStyledWithCommentsConfig() {
		return InstanceHolder.STYLED;
	}

	@Nonnull
	public static UnparseVisitorConfig getStyledWithoutCommentsConfig() {
		return InstanceHolder.STYLED_WITHOUT_COMMENTS;
	}

	/**
	 * @return A config that keeps comments, but does not add any optional spaces or newlines.
	 */
	@Nonnull
	public static UnparseVisitorConfig getUnstyledWithCommentsConfig() {
		return InstanceHolder.UNSTYLED_WITH_COMMENTS;
	}

	/**
	 * @return A config that does not keep comments, nor does it add any optional spaces or newlines.
	 */
	@Nonnull
	public static UnparseVisitorConfig getUnstyledWithoutCommentsConfig() {
		return InstanceHolder.UNSTYLED_WITHOUT_COMMENTS;
	}
}

