package timeline.ui.tests;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.time.temporal.ValueRange;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import timeline.ui.LineStack;

@RunWith(SWTBotJunit4ClassRunner.class)
public class LineStackTest extends AbstractLineStackTest {
	@Test
	public void testSimpleRanges() {
		LineStack widget = bot.widget(widgetOfType(LineStack.class));
		SWTBotLineStack stack = new SWTBotLineStack(widget);

		stack.dnd(widget, new Point(10, 10), new Point(20, 10));
		stack.dnd(widget, new Point(30, 10), new Point(40, 10));

		List<ValueRange> ranges = stack.getRanges();

		assertEquals(2, ranges.size());

		assertEquals(ValueRange.of(10, 20), ranges.get(0));
		assertEquals(ValueRange.of(30, 40), ranges.get(1));
	}

	@Test
	public void testFullOverlaps() {
		LineStack widget = bot.widget(widgetOfType(LineStack.class));
		SWTBotLineStack stack = new SWTBotLineStack(widget);

		stack.dnd(widget, new Point(10, 10), new Point(20, 10));
		stack.dnd(widget, new Point(30, 10), new Point(40, 10));

		stack.dnd(widget, new Point(10, 10), new Point(40, 10));

		List<ValueRange> ranges = stack.getRanges();

		assertEquals(1, ranges.size());
		assertEquals(ValueRange.of(10, 40), ranges.get(0));
	}

	@Test
	public void testIntersect() {
		LineStack widget = bot.widget(widgetOfType(LineStack.class));
		SWTBotLineStack stack = new SWTBotLineStack(widget);

		stack.dnd(widget, new Point(10, 10), new Point(40, 10));
		stack.dnd(widget, new Point(30, 10), new Point(60, 10));

		List<ValueRange> ranges = stack.getRanges();

		assertEquals(1, ranges.size());
		assertEquals(ValueRange.of(10, 60), ranges.get(0));
	}

	@Test
	public void testJoin() {
		LineStack widget = bot.widget(widgetOfType(LineStack.class));
		SWTBotLineStack stack = new SWTBotLineStack(widget);

		stack.dnd(widget, new Point(10, 10), new Point(20, 10));
		stack.dnd(widget, new Point(20, 10), new Point(40, 10));
		// gap here
		stack.dnd(widget, new Point(50, 10), new Point(60, 10));

		// fill the gap
		stack.dnd(widget, new Point(40, 10), new Point(50, 10));

		List<ValueRange> ranges = stack.getRanges();

		assertEquals(1, ranges.size());
		assertEquals(ValueRange.of(10, 60), ranges.get(0));
	}
}