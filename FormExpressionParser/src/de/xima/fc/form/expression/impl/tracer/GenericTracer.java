package de.xima.fc.form.expression.impl.tracer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class GenericTracer implements ITracer<Node> {
	private @Nullable Node processed;
	private Node[] stackTrace;
	private @Nullable List<IEvaluationWarning> warnList;
	private int pos;
	private final int initialSize;
	private boolean warningsEnabled;

	public GenericTracer() {
		this(16);
	}

	public GenericTracer(int initialSize) {
		if (initialSize < 1)
			initialSize = 1;
		stackTrace = new Node[initialSize];
		pos = -1;
		this.initialSize = initialSize;
	}

	@Override
	public void setCurrentlyProcessed(@Nullable final Node object) {
		processed = object;
	}

	@Nullable
	@Override
	public Node getCurrentlyProcessed() {
		return processed;
	}

	@Override
	public void descend() {
		if (pos >= stackTrace.length - 1)
			stackTrace = Arrays.copyOf(stackTrace, pos * 2);
		stackTrace[++pos] = processed;
	}

	@Override
	public void ascend() {
		--pos;
	}

	@Override
	public Node[] getStackTrace() {
		final Node[] newArray = new Node[pos + 1];
		for (int i = pos; i >= 0; --i)
			newArray[pos - i] = stackTrace[i];
		return newArray;
	}

	@Override
	public void reset() {
		Arrays.fill(stackTrace, null);
		if (stackTrace.length != initialSize)
			stackTrace = new Node[initialSize];
		pos = -1;
		if (warnList != null)
			warnList.clear();
		warnList = null;
		warningsEnabled = false;
	}

	@Override
	public void appendWarning(final IEvaluationWarning warning) {
		if (warningsEnabled) {
			getWarnList().add(warning);
		}
	}

	private List<IEvaluationWarning> getWarnList() {
		if (warnList != null)
			return warnList;
		return warnList = new ArrayList<>();
	}

	@Override
	public void enableWarnings() {
		warningsEnabled = true;
	}

	@Override
	public void disableWarnings() {
		warningsEnabled = false;
	}

	@Override
	public boolean isWarningsEnabled() {
		return warningsEnabled;
	}

	@Override
	public List<IEvaluationWarning> buildWarnings() {
		return warnList != null ? new ArrayList<>(warnList)
				: NullUtil.checkNotNull(Collections.<IEvaluationWarning> emptyList());
	}
}