package timeline.ui;

import java.time.temporal.ValueRange;

@FunctionalInterface
interface RangeCoveredListener {
	public void rangeCovered(ValueRange range, LineStack log);
}
