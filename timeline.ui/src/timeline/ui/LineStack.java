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

public class LineStack extends Canvas {
	private static final int LINE_RIGHT = 60;
	private static final int LINE_LEFT = 30;
	private static final int LINE_TOP = 10;
	private static final int LINE_HEIGHT = 45;

	private RangeList rangeList;

	private List<RangeCoveredListener> rangeCoveredListeners = new ArrayList<>();

	public LineStack(Composite shell, int style, RangeList rangeList) {
		super(shell, style);
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
						//						System.out.println(String.format("%s - %s", start, end)); // FIXME
						return;
					}
					updatePeriod(ValueRange.of(start, end));
				}
			}

			private void updatePeriod(ValueRange valueRange) {
				int start = (int) valueRange.getMinimum();
				int end = (int) valueRange.getMaximum();
				int width = end - start;
				redraw(start, LINE_TOP + 1, width, LINE_HEIGHT - 1, false);
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
				rangeCoveredListeners.forEach(listener -> listener.rangeCovered(range, LineStack.this));
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
				// Do some drawing
				Rectangle rect = ((Canvas) e.widget).getBounds();
				GC gc = e.gc;
				gc.drawFocus(5, 5, rect.width - 10, rect.height - 10);
				for (ValueRange valueRange : rangeList.getRanges()) {
					Color red = e.display.getSystemColor(SWT.COLOR_RED);
					drawPeriod(gc, valueRange, red);
				}

				if (isMouseDown.get()) {
					gc.setAlpha(40);
					drawRectangle(gc, startX.get(), currentMouseX.get(), e.display.getSystemColor(SWT.COLOR_BLACK));
					gc.setAlpha(255);
				}

				drawBorder(e, rect, gc);

			}

			private void drawPeriod(GC gc, ValueRange valueRange, Color red) {
				drawRectangle(gc, (int) valueRange.getMinimum(), (int) valueRange.getMaximum(), red);
			}


			private void drawBorder(PaintEvent e, Rectangle rect, GC gc) {
				gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
				gc.drawRectangle(LINE_LEFT, LINE_TOP, rect.width - LINE_RIGHT, LINE_HEIGHT);
			}

			private void drawRectangle(GC gc, int start, int end, Color color) {
				gc.setForeground(color);
				gc.setBackground(color);
				int width = end - start;
				// gc.drawRectangle(start, LINE_TOP, width, LINE_HEIGHT);
				gc.fillRectangle(start, LINE_TOP, width, LINE_HEIGHT);
			}
		});
	}

	public void addRangeCoveredListener(RangeCoveredListener rangeCoveredAdapter) {
		rangeCoveredListeners.add(rangeCoveredAdapter);
	}

	public void removeRangeCoveredListener(RangeCoveredListener rangeCoveredAdapter) {
		rangeCoveredListeners.remove(rangeCoveredAdapter);
	}

	public void addRange(ValueRange newRange) {
		rangeList.addRange(newRange);
	}

	public List<ValueRange> getRanges() {
		return rangeList.getRanges();
	}
}