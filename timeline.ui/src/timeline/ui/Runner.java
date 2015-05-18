package timeline.ui;

import java.time.temporal.ValueRange;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import timeline.core.RangeList;

/**
 * This class demonstrates a Canvas
 */
public class Runner {
	public void run() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("Canvas Example");
		AbstractRangeSelectionWidget stack = new LineStack(shell, SWT.NONE, ValueRange.of(10, 100), new RangeList());
		stack.addRangeCoveredListener((range, line) -> line.addRange(range));
		// stack.createContents(shell);
		// createContents(shell);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}


	/**
	 * The application entry point
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		new Runner().run();
	}
}