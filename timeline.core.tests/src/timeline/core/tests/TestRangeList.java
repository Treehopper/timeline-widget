package timeline.core.tests;

import static org.junit.Assert.assertTrue;

import java.time.temporal.ValueRange;

import org.junit.Before;
import org.junit.Test;

import timeline.core.RangeList;

public class TestRangeList {
	private RangeList rangeList;

	@Before
	public void setUp() {
		rangeList = new RangeList();
	}

	@Test
	public void test() {
		rangeList.addRange(ValueRange.of(10, 20));
		assertTrue(rangeList.getRanges().size() == 1);
	}
}
