package timeline.ui;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import timeline.core.RangeList;

public abstract class AbstractRangeSelectionWidget extends Canvas {
	protected RangeList rangeList;
	protected List<RangeCoveredListener> rangeCoveredListeners = new ArrayList<>();
	private ValueRange baseRange;
	protected Color rangeColor;
	protected Rectangle currentSize;

	public AbstractRangeSelectionWidget(Composite shell, int style, ValueRange baseRange, RangeList rangeList) {
		super(shell, style);
		this.baseRange = baseRange;
		this.rangeList = rangeList;

		final AtomicBoolean isMouseDown = new AtomicBoolean();
		isMouseDown.set(false);
		final AtomicInteger startX = new AtomicInteger(-1);
		final AtomicInteger currentMouseX = new AtomicInteger(-1);
		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				if (!isMouseDown.get()) {
					return;
				}
				if ((e.stateMask & SWT.BUTTON1) == SWT.BUTTON1) {
					currentMouseX.set(e.x);
					// canvas.redraw();
					int start = startX.get();
					int end = currentMouseX.get();
					if (end <= start) {
						// System.out.println(String.format("%s - %s", start,
						// end)); // FIXME
						return;
					}
					updatePeriod(ValueRange.of(start, end));
				}
			}

			private void updatePeriod(ValueRange valueRange) {
				int start = (int) valueRange.getMinimum();
				int end = (int) valueRange.getMaximum();
				int width = end - start;
				redraw(start, getTop() + 1, width, getLineHeight() - 1, false);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				int start = startX.get();
				int end = e.x;
				if (end <= start) {
					// System.out.println(String.format("%s - %s", start, end));
					// // FIXME
					return;
				}
				ValueRange range = ValueRange.of(start, end);
				rangeCoveredListeners.forEach(listener -> listener.rangeCovered(range,
						AbstractRangeSelectionWidget.this));
				isMouseDown.set(false);
				startX.set(-1);
				redraw();

			}

			@Override
			public void mouseDown(MouseEvent e) {
				isMouseDown.set(true);
				startX.set(e.x);
				// System.out.println(String.format("start %s", e.x)); // FIXME
			}
		});

		// Create a paint handler for the canvas
		addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				currentSize = ((Canvas) e.widget).getBounds();
				GC gc = e.gc;
				drawBackground(gc, currentSize);
				for (ValueRange valueRange : rangeList.getRanges()) {
					drawPeriod(gc, currentSize, valueRange, getRangeColor());
				}

				if (isMouseDown.get()) {
					gc.setAlpha(getSelectionAlpha());
					drawRectangle(gc, currentSize, startX.get(), currentMouseX.get(), getSelectionColor());
					gc.setAlpha(255);
				}

				drawBorder(gc, e, currentSize);

			}
		});
	}

	public AbstractRangeSelectionWidget(Composite parent, int style) {
		super(parent, style);
	}

	public void addRange(ValueRange newRange) {
		rangeList.addRange(newRange);
	}

	public void addRangeCoveredListener(RangeCoveredListener rangeCoveredAdapter) {
		rangeCoveredListeners.add(rangeCoveredAdapter);
	}

	public void removeRangeCoveredListener(RangeCoveredListener rangeCoveredAdapter) {
		rangeCoveredListeners.remove(rangeCoveredAdapter);
	}

	public List<ValueRange> getRanges() {
		return rangeList.getRanges();
	}

	protected void clearRanges() {
		rangeList.clear();
	}

	public ValueRange getBaseRange() {
		return baseRange;
	}

	protected Color getRangeColor() {
		return getDisplay().getSystemColor(SWT.COLOR_RED);
	}

	protected int getSelectionAlpha() {
		return 40;
	}

	protected Color getSelectionColor() {
		return getDisplay().getSystemColor(SWT.COLOR_BLACK);
	}

	protected Color getBorderColor() {
		return getDisplay().getSystemColor(SWT.COLOR_BLACK);
	}

	protected Color getForegroundColor() {
		return getDisplay().getSystemColor(SWT.COLOR_BLACK);
	}

	protected int getRangeAlpha() {
		return 255;
	}


	protected void drawBackground(GC gc, Rectangle rect) {
		// gc.drawFocus(5, 5, rect.width - 10, rect.height - 10);
	}

	private void drawPeriod(GC gc, Rectangle rect, ValueRange valueRange, Color color) {
		drawRectangle(gc, rect, (int) valueRange.getMinimum(), (int) valueRange.getMaximum(), color);
	}

	private void drawBorder(GC gc, PaintEvent e, Rectangle rect) {
		gc.setForeground(getForegroundColor());
		gc.drawRectangle(getLeft(), getTop(), getRight(), getLineHeight());
	}

	abstract protected int getRight();

	abstract protected int getTop();

	abstract protected int getLeft();

	protected int getLineHeight() {
		return 45;
	}

	private void drawRectangle(GC gc, Rectangle rect, int start, int end, Color color) {
		int oldAlpha = gc.getAlpha();
		gc.setAlpha(getRangeAlpha());
		gc.setForeground(color);
		gc.setBackground(color);
		int width = end - start;
		// gc.drawRectangle(start, LINE_TOP, width, LINE_HEIGHT);
		gc.fillRectangle(start, getTop(), width, getLineHeight());
		gc.setAlpha(oldAlpha);
	}
}
