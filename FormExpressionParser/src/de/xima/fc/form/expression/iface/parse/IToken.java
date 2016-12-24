package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface IToken {
	public int getBeginColumn();
	public int getBeginLine();
	public int getEndColumn();
	public int getEndLine();
	public String getImage();
	public int getKind();
	@Nullable
	public IToken getNext();
	@Nullable
	public IToken getSpecial();
}