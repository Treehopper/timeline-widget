package timeline.core;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

public class RangeList {
	private List<ValueRange> ranges = new ArrayList<>();

	public void addRange(ValueRange newRange) {
		newRange = getJoinedRange(newRange);
		List<ValueRange> toBeDeleted = getContainedRanges(newRange);
		ranges.removeAll(toBeDeleted);
		sortedInsert(newRange);
	}

	private ValueRange getJoinedRange(ValueRange newRange) {
		ValueRange result = newRange;
		for (ValueRange range : ranges) {
			if (range.getMaximum() == result.getMinimum() || intersectLeft(range, result)) {
				result = ValueRange.of(range.getMinimum(), result.getMaximum());
				continue;
			}
			if (range.getMinimum() == result.getMaximum() || intersectRight(range, result)) {
				result = ValueRange.of(result.getMinimum(), range.getMaximum());
			}
		}
		return result;
	}

	private boolean intersectRight(ValueRange a, ValueRange b) {
		return intersectLeft(b, a);
	}

	private boolean intersectLeft(ValueRange a, ValueRange b) {
		return a.getMaximum() > b.getMinimum() && a.getMaximum() < b.getMaximum() && a.getMinimum() < b.getMinimum();
	}

	private List<ValueRange> getContainedRanges(ValueRange newRange) {
		List<ValueRange> toBeDeleted = new ArrayList<>();
		for (ValueRange range : ranges) {
			if (isContainedIn(newRange, range)) {
				toBeDeleted.add(range);
			}
		}
		return toBeDeleted;
	}

	private boolean isContainedIn(ValueRange newRange, ValueRange range) {
		return range.getMinimum() >= newRange.getMinimum() && range.getMaximum() <= newRange.getMaximum();
	}

	private void sortedInsert(ValueRange newRange) {
		// TODO: sort
		ranges.add(newRange);
	}

	public List<ValueRange> getRanges() {
		return ranges;
	}
}
