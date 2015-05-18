package timeline.ui;

import java.time.temporal.ValueRange;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import timeline.core.RangeList;

public class TimeZoomWidget extends AbstractRangeSelectionWidget {
	protected static final int LINE_RIGHT = 60;
	protected static final int LINE_LEFT = 30;
	protected static final int LINE_TOP = 10;

	public TimeZoomWidget(Composite shell, int style, ValueRange baseRange) {
		super(shell, style, baseRange, new RangeList());
	}

	@Override
	public void addRange(ValueRange newRange) {
		clearRanges();
		super.addRange(newRange);
	}

	@Override
	public Color getRangeColor() {
		return getDisplay().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	protected int getRangeAlpha() {
		return 40;
	}

	@Override
	protected void drawBackground(GC gc, Rectangle rect) {
		ValueRange range = getBaseRange();
		int minimum = getLeft();
		int maximum = currentSize.width - getLeft();
		int numberOfSteps = 10;
		double step = (maximum - minimum) / numberOfSteps;
		int top = getTop();
		int bottom = getTop() + getLineHeight() - 5;

		for (int i = 1; i <= numberOfSteps - 1; i++) {
			int x = minimum + (int) (i * step);
			gc.drawLine(x, top + 5, x, bottom);
		}
	}

	@Override
	protected int getRight() {
		return currentSize.width - LINE_RIGHT;
	}

	@Override
	protected int getTop() {
		return LINE_TOP;
	}

	@Override
	protected int getLeft() {
		return LINE_LEFT;
	}

	@Override
	protected int getLineHeight() {
		return 25;
	}
}