package timeline.ui;

import java.time.temporal.ValueRange;
import org.eclipse.swt.widgets.Composite;

import timeline.core.RangeList;

public class TimeZoomWidget extends AbstractRangeSelectionWidget {

	public TimeZoomWidget(Composite shell, int style, ValueRange baseRange) {
		super(shell, style, new RangeList());
	}

	@Override
	public void addRange(ValueRange newRange) {
		clearRanges();
		super.addRange(newRange);
	}
}