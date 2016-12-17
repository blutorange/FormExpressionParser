package de.xima.fc.form.expression.iface.evaluate;

import java.util.Comparator;

import javax.annotation.Nonnull;

public interface IEvaluationWarning {
	@Nonnull
	public String getMessage();
	public int getStartLine();
	public int getStartColumn();
	public int getEndLine();
	public int getEndColumn();

	public static Comparator<IEvaluationWarning> COMPARATOR = new Comparator<IEvaluationWarning>() {
		@Override
		public int compare(final IEvaluationWarning o1, final IEvaluationWarning o2) {
			if (o1 == null || o2 == null)
				throw new NullPointerException();
			if (o1.getStartLine() == o2.getStartLine()) {
				if (o1.getStartColumn() < o2.getStartColumn())
					return -1;
				else if (o1.getStartColumn() == o2.getStartColumn())
					return 0;
				return 1;
			}
			if (o1.getStartLine() < o2.getStartLine())
				return -1;
			return 1;
		}
	};
}