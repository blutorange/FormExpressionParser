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
package de.xima.fc.form.expression.visualize;

import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;

/**
 * Creates "Sample" trees, e.g. to be used in demonstrations.
 *
 * @author Udo Borkowski (ub@abego.org)
 */
public class FormExpressionTreeFactory {
	public static DefaultTreeForTreeLayout<TextInBox> createForExpression(String expression) throws ParseException {
		expression = expression == null ? "EMPTY" : expression; //$NON-NLS-1$
		final TextInBox root = new TextInBox(expression.isEmpty() ? "<EMPTY>" : expression); //$NON-NLS-1$
		final DefaultTreeForTreeLayout<TextInBox> tree = new DefaultTreeForTreeLayout<TextInBox>(
				root);

		final Node rootNode = FormExpressionFactory.Program.asNode(expression);
		addNodes(tree, rootNode, root);
		return tree;
	}

	private static void addNodes(final DefaultTreeForTreeLayout<TextInBox> tree, final Node node, final TextInBox parent) {
		final TextInBox vertex = new TextInBox(node.toString());
		tree.addChild(parent, vertex);
		for (int i = 0; i != node.jjtGetNumChildren(); ++i)
			addNodes(tree, node.jjtGetChild(i), vertex);
	}
}