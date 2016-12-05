package de.xima.fc.form.expression.iface.evaluate;

import java.util.Comparator;

import javax.annotation.Nonnull;

public interface IEvaluationWarning {
	@Nonnull
	public String getMessage();
	public int getLine();
	public int getColumn();
	
	public static Comparator<IEvaluationWarning> COMPARATOR = new Comparator<IEvaluationWarning>() {
		@Override
		public int compare(final IEvaluationWarning o1, final IEvaluationWarning o2) {
			if (o1 == null || o2 == null)
				throw new NullPointerException();
			if (o1.getLine() == o2.getLine()) {
				if (o1.getColumn() < o2.getColumn())
					return -1;
				else if (o1.getColumn() == o2.getColumn())
					return 0;
				return 1;
			}
			if (o1.getLine() < o2.getLine())
				return -1;
			return 1;
		}
	};
}