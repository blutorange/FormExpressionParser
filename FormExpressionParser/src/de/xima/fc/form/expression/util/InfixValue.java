package de.xima.fc.form.expression.util;

import de.xima.fc.form.expression.node.MySimpleNode;

public final class InfixValue {
	public final MySimpleNode left;
	public final MySimpleNode right;
	public InfixValue(final MySimpleNode left, final MySimpleNode right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return "left=" +left + ";right" + "=" + right;
	}
}
