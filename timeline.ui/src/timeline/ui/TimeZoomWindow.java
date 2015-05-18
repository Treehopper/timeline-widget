package timeline.ui;

import java.time.temporal.ValueRange;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TimeZoomWindow {
	protected Shell shell;

	public static void main(String[] args) {
		TimeZoomWindow window = new TimeZoomWindow();
		window.open();
		window.eventLoop(Display.getDefault());
	}

	public void open() {
		Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
	}

	public void eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public Shell getShell() {
		return shell;
	}

	protected void createContents() {
		shell = new Shell();
		shell.setLayout(new FillLayout());

		AbstractRangeSelectionWidget stack = new TimeZoomWidget(shell, SWT.NONE, ValueRange.of(100, 1000));
		stack.addRangeCoveredListener((range, line) -> line.addRange(range));
	}
}