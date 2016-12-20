package de.xima.fc.form.expression.iface.evaluate;

import java.util.Comparator;

import javax.annotation.Nonnull;

public interface IEvaluationWarning {
	@Nonnull
	public String getMessage();
	public int getBeginLine();
	public int getBeginColumn();
	public int getEndLine();
	public int getEndColumn();

	public static Comparator<IEvaluationWarning> COMPARATOR = new Comparator<IEvaluationWarning>() {
		@Override
		public int compare(final IEvaluationWarning o1, final IEvaluationWarning o2) {
			if (o1 == null || o2 == null)
				throw new NullPointerException();
			if (o1.getBeginLine() == o2.getBeginLine()) {
				if (o1.getBeginColumn() < o2.getBeginColumn())
					return -1;
				else if (o1.getBeginColumn() == o2.getBeginColumn())
					return 0;
				return 1;
			}
			if (o1.getBeginLine() < o2.getBeginLine())
				return -1;
			return 1;
		}
	};
}