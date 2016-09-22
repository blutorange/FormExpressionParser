/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.xima.fc.form.expression.visualize.svg;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visualize.FormExpressionTreeFactory;
import de.xima.fc.form.expression.visualize.TextInBox;
import de.xima.fc.form.expression.visualize.TextInBoxNodeExtentProvider;

/**
 * Demonstrates how to use the {@link TreeLayout} to create a tree diagram with
 * SVG (Scalable Vector Graphic)
 * <p>
 * The rendered SVG looks like this:
 * <p>
 * <img src="doc-files/svgdemo.png" alt="A tree rendered using SVG">
 * <p>
 * Link: <a href="doc-files/svgdemo.svg">The SVG file (only viewable in SVG aware
 * browsers)</a>
 *
 * @author Udo Borkowski (ub@abego.org)
 */
public class SVGRender {

	/**
	 * Returns an SVG text displaying a tree with nodes placed according to a
	 * layout created by {@link TreeLayout}.
	 *
	 * @param args [unused]
	 */
	public static void main(final String[] args) {
		// get the sample tree
		if (args.length != 1) {
			help();
			return;
		}
		System.out.println(renderSvg(args[0]));
	}

	public static String renderSvg(final String arg) {
		final String expression = arg == null ? StringUtils.EMPTY : arg;

		DefaultTreeForTreeLayout<TextInBox> tree;
		try {
			tree = FormExpressionTreeFactory.createForExpression(expression);
		} catch (final ParseException e) {
			final TextInBox root = new TextInBox(e.getMessage());
			tree = new DefaultTreeForTreeLayout<TextInBox>(root);
			final TextInBox msg = new TextInBox(StringEscapeUtils.escapeHtml4(StringUtils.join("\n",e.getStackTrace())));
			tree.addChild(root, msg);
		}

		// setup the tree layout configuration
		final double gapBetweenLevels = 50;
		final double gapBetweenNodes = 10;
		final DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
				gapBetweenLevels, gapBetweenNodes);

		// create the NodeExtentProvider for TextInBox nodes
		final TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

		// create the layout
		final TreeLayout<TextInBox> treeLayout = new TreeLayout<TextInBox>(tree,
				nodeExtentProvider, configuration);

		// Generate the SVG and write it to System.out
		final SVGForTextInBoxTree generator = new SVGForTextInBoxTree(treeLayout);
		return generator.getSVG();
	}


	private static void help() {
		System.out.println("Usage: svgDemo expression");
	}

}
