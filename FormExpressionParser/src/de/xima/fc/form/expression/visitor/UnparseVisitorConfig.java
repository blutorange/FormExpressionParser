package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;

/**
 * A configuration affecting the output of {@link IFormExpression#unparse(UnparseVisitorConfig)}.
 * It offers several options to change how FormExpression code will be formatted.
 * @author mad_gaksha
 */
public final class UnparseVisitorConfig {
	@Nonnull public final String indentPrefix;
	@Nonnull public final String linefeed;
	@Nonnull public final String optionalSpace;
	@Nonnull public final String requiredSpace;
	@Nonnull public final HeaderType[] headerTypeOrder;
	public final boolean keepComments;
	
	private UnparseVisitorConfig(@Nonnull final String indentPrefix, @Nonnull final String linefeed,
			@Nonnull final String optionalSpace, @Nonnull final String requiredSpace,
			@Nonnull final HeaderType[] headerTypeOrder, final boolean keepComments) {
		this.indentPrefix = indentPrefix;
		this.linefeed = linefeed;
		this.optionalSpace = optionalSpace;
		this.requiredSpace = requiredSpace;
		this.headerTypeOrder = headerTypeOrder;
		this.keepComments = keepComments;
	}

	public static class Builder {
		@Nullable private String indentPrefix;
		@Nullable private String linefeed;
		@Nullable private String optionalSpace;
		@Nullable private String requiredSpace;
		@Nullable public HeaderType[] headerTypeOrder;
		private boolean keepComments;
		
		/**
		 * Sets the prefix to the beginning of each line for each indentation level.
		 * @param indentPrefix Prefix for indentation. Defaults to two spaces (<code>  </code>).
		 * @return this
		 */
		@Nonnull
		public Builder setIndentPrefix(@Nullable final String indentPrefix) {
			this.indentPrefix = indentPrefix;
			return this;
		}
		/**
		 * String to be used as a linefeed, must be either \n, \t or \r\n.
		 * @param linefeed Character to be used for linefeeds, defaults to \n.
		 * @return this
		 */
		@Nonnull
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
		@Nonnull
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
		@Nonnull
		public Builder setRequiredSpace(int requiredSpace) {
			if (requiredSpace < 1) requiredSpace = 1;
			this.requiredSpace = StringUtils.repeat(' ', requiredSpace);
			return this;
		}
		/**
		 * @param keepComments Whether the output should contain comments.
		 * @return this
		 */
		@Nonnull
		public Builder setKeepComments(final boolean keepComments) {
			this.keepComments = keepComments;
			return this;
		}
		
		/**
		 * In which order headers should be added. Compare:
		 * <p>
		 * With the default
		 * <code>setHeaderTypeOrder(HeaderType.REQUIRE, HeaderType.GLOBAL, HeaderType.MANUAL, HeaderType.GLOBAL_FUNCTION)</code>:
		 * <pre>
		 *   require scope math;
		 *   global {
		 *     var i;
		 *     var j;
		 *   }
		 *   scope myScope {
		 *     var k;
		 *     function scopedFunction(){}
		 *   }
		 *   function globalFunction(){}
		 * </pre>
		 * </p>
		 * <p>
		 * Compare with
		 * <code>setHeaderTypeOrder(HeaderType.GLOBAL_FUNCTION, HeaderType.MANUAL, HeaderType.GLOBAL, HeaderType.REQUIRE)</code>
		 * 
		 * <pre>
		 *   function globalFunction(){}
		 *   scope myScope {
		 *     var k;
		 *     function scopedFunction(){}
		 *   }
		 *   global {
		 *     var i;
		 *     var j;
		 *   }
		 *   require scope math;
		 * </pre>
		 * </p>
		 * 
		 * @param headerTypeOrder
		 *            Order in which headers are added, from earliest to latest.
		 *            Elements that occur more than once are ignored, elements
		 *            that do not occur at all are added in the defalt order.
		 * @return
		 */
		@Nonnull
		public Builder setHeaderTypeOrder(@Nullable final HeaderType... headerTypeOrder) {
			this.headerTypeOrder = headerTypeOrder;
			return this;
		}
		/**
		 * @param linefeed Character to be used as for newlines.
		 * @return this
		 * @see #setLinefeed(String)
		 */
		@Nonnull
		public Builder setLinefeed(final char linefeed) {
			this.linefeed = Character.toString(linefeed);
			return this;
		}
		/** @return A new, sane configuration with the configured options. */
		@Nonnull
		public UnparseVisitorConfig build() {
			String indentPrefix = this.indentPrefix;
			String linefeed = this.linefeed;
			String optionalSpace = this.optionalSpace;
			String requiredSpace = this.requiredSpace;
			HeaderType[] headerTypeOrder = this.headerTypeOrder;
			if (indentPrefix == null) indentPrefix = CmnCnst.NonnullConstant.STRING_SPACE;
			if (linefeed == null) linefeed = CmnCnst.NonnullConstant.STRING_LF;
			if (optionalSpace == null) optionalSpace = CmnCnst.NonnullConstant.STRING_EMPTY;
			if (requiredSpace == null) requiredSpace = CmnCnst.NonnullConstant.STRING_SPACE;
			if (headerTypeOrder == null) headerTypeOrder = new HeaderType[0];
			indentPrefix = indentPrefix.replaceAll("[^\t ]", CmnCnst.NonnullConstant.STRING_EMPTY); //$NON-NLS-1$
			linefeed = linefeed.replaceAll("[^\r\n]", CmnCnst.NonnullConstant.STRING_EMPTY); //$NON-NLS-1$
			if (indentPrefix == null) indentPrefix = CmnCnst.NonnullConstant.STRING_SPACE;
			if (linefeed.isEmpty()) linefeed = CmnCnst.NonnullConstant.STRING_LF;
			return new UnparseVisitorConfig(indentPrefix, linefeed, optionalSpace, requiredSpace, headerTypeOrder, keepComments);
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
	
	/**
	 * For setting the order in which headers (require declarations, global
	 * variables, manual scopes, global functions) should occur.
	 * 
	 * @see Builder#setHeaderTypeOrder(HeaderType...)
	 * @author mad_gaksha
	 *
	 */
	public static enum HeaderType {
		REQUIRE, GLOBAL, MANUAL, GLOBAL_FUNCTION;
	}
}