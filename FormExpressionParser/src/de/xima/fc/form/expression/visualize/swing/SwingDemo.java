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
package de.xima.fc.form.expression.visualize.swing;

import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.apache.commons.io.FileUtils;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visualize.FormExpressionTreeFactory;
import de.xima.fc.form.expression.visualize.TextInBox;
import de.xima.fc.form.expression.visualize.TextInBoxNodeExtentProvider;

/**
 * Demonstrates how to use the {@link TreeLayout} to render a tree in a Swing
 * application.
 * <p>
 * Intentionally the sample code is kept simple. I.e. it does not include stuff
 * like anti-aliasing and other stuff one would add to make the output look
 * nice.
 * <p>
 * Screenshot:
 * <p>
 * <img src="doc-files/swingdemo.png" alt="A tree rendered using Swing">
 *
 * @author Udo Borkowski (ub@abego.org)
 */
public class SwingDemo {

	private static void showInDialog(final JComponent panel) {
		final JScrollPane scrollPane = new JScrollPane(panel);
		final JFrame dummy = new JFrame("Graph for expression");
		dummy.add(scrollPane);
		dummy.pack();
		dummy.setLocationRelativeTo(null);
		dummy.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		dummy.setVisible(true);
	}

	private static TreeForTreeLayout<TextInBox> getSampleTree(final String expression) throws ParseException {
		return FormExpressionTreeFactory.createForExpression(expression);
	}

	/**
	 * Shows a dialog with a tree in a layout created by {@link TreeLayout},
	 * using the Swing component {@link TextInBoxTreePane}.
	 *
	 * @param args args[0]: treeName (default="")
	 */
	public static void main(final String[] args) {
		String expression;
		if (args.length < 1) {
			help();
			expression = JOptionPane.showInputDialog("Enter expression:");
		} else
			try {
				expression = FileUtils.readFileToString(new File(args[0]), "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

		TreeForTreeLayout<TextInBox> tree;
		try {
			tree = getSampleTree(expression);
		}
		catch (final ParseException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Could not parse expression", JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Uncaught exception", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Setup the tree layout configuration
		final double gapBetweenLevels = 50;
		final double gapBetweenNodes = 10;
		final DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
				gapBetweenLevels, gapBetweenNodes);

		// Create the NodeExtentProvider for TextInBox nodes
		final TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

		// Create the layout
		final TreeLayout<TextInBox> treeLayout = new TreeLayout<TextInBox>(tree,
				nodeExtentProvider, configuration);

		// Create a panel that draws the nodes and edges and show the panel
		final TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);

		showInDialog(panel);
	}

	private static void help() {
		System.out.println("Usage: swingDemo [expression]");
	}
}
