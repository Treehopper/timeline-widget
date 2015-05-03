package timeline.ui.tests;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import timeline.ui.LineStackWindow;

public abstract class AbstractLineStackTest extends SWTBotTestCase {

	protected SWTBot bot;

	protected static Thread uiThread;

	protected static Shell shell;

	private final static CyclicBarrier swtBarrier = new CyclicBarrier(2);

	@BeforeClass
	public static void setupApp() {

		if (uiThread == null) {
			uiThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						while (true) {
							// open and layout the shell
							LineStackWindow window = new LineStackWindow();
							window.open();
							shell = window.getShell();

							// wait for the test setup
							swtBarrier.await();

							// run the event loop
							window.eventLoop(Display.getDefault());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			uiThread.setDaemon(true);
			uiThread.start();
		}
	}

	@Before
	public final void setupSWTBot() throws InterruptedException, BrokenBarrierException {
		// synchronize with the thread opening the shell
		swtBarrier.await();
		bot = new SWTBot(shell);
	}

	@After
	public void closeShell() throws InterruptedException {
		// close the shell
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				shell.close();
			}
		});
	}

	protected void assertResultGivenInput(String input, String expectedResult) {
		bot.textWithLabel("input").setText(input);
		bot.button("Compute").click();
		assertEquals(expectedResult, bot.textWithLabel("result").getText());
	}

}
