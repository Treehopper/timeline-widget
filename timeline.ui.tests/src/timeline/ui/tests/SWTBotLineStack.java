package timeline.ui.tests;

import java.time.temporal.ValueRange;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.hamcrest.SelfDescribing;

import timeline.ui.LineStack;

public class SWTBotLineStack extends AbstractSWTBot<LineStack> {

	public SWTBotLineStack(LineStack w) throws WidgetNotFoundException {
		this(w, null);
	}

	public SWTBotLineStack(LineStack w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	public List<ValueRange> getRanges() {
		return syncExec(new ListResult<ValueRange>() {
			@Override
			public List<ValueRange> run() {
				return widget.getRanges();
			}
		});
	}

	public void dnd(LineStack widget, Point start, Point finish) {
		// setFocus();
		asyncExec(new VoidResult() {
			public void run() {
				moveMouse(start, 1);
				mouseDown(start, 1);
				moveMouse(finish, 1);
				mouseUp(finish, 1);
			}
		});
	}

	private void moveMouse(Point point, int button) {
		asyncExec(new VoidResult() {
			public void run() {
				Point widgetPoint = widget.toDisplay(point);
				Event event = createMouseEvent(widgetPoint.x, widgetPoint.y, button, 0, 0);
				event.type = SWT.MouseMove;
				widget.getDisplay().post(event);
			}
		});
	}

	private void mouseDown(Point point, final int button) {
		asyncExec(new VoidResult() {
			public void run() {
				Point widgetPoint = widget.toDisplay(point);
				Event event = createMouseEvent(widgetPoint.x, widgetPoint.y, button, 0, 0);
				event.type = SWT.MouseDown;
				widget.getDisplay().post(event);
			}
		});
	}

	private void mouseUp(Point point, final int button) {
		asyncExec(new VoidResult() {
			public void run() {
				Point widgetPoint = widget.toDisplay(point);
				Event event = createMouseEvent(widgetPoint.x, widgetPoint.y, button, 0, 0);
				event.type = SWT.MouseUp;
				widget.getDisplay().post(event);
			}
		});
	}

}