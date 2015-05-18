package timeline.ui;

import java.time.temporal.ValueRange;

import org.eclipse.swt.widgets.Composite;

import timeline.core.RangeList;

public class LineStack extends AbstractRangeSelectionWidget {
	protected static final int LINE_RIGHT = 60;
	protected static final int LINE_LEFT = 30;
	protected static final int LINE_TOP = 10;

	public LineStack(Composite shell, int style, ValueRange baseRange, RangeList rangeList) {
		super(shell, style, baseRange, rangeList);
	}

	@Override
	protected int getRight() {
		return LINE_RIGHT;
	}

	@Override
	protected int getTop() {
		return LINE_TOP;
	}

	@Override
	protected int getLeft() {
		return LINE_LEFT;
	}
}