package de.xima.fc.form.expression.impl.contextcommand;

import de.xima.fc.form.expression.impl.AExternalContextCommand;

public class FormElementCommand extends AExternalContextCommand {
	private final EFormElementCommandType type;
	private final Object data;

	public FormElementCommand() {
		this.type = null;
		this.data = null;
	}
	
	@Override
	public Object getData() {
		return data;
	}
	
	@Override
	public EFormElementCommandType getType() {
		return type;
	}
	
	public static enum EFormElementCommandType {
		;
	}

}
