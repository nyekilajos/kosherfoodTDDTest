package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.GameThread;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherFoodModel;
import hu.bme.aut.amorg.nyekilajos.kosherfood.test.TestKosherFoodGuiceModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import roboguice.activity.RoboActivity;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

@RunWith(RobolectricTestRunner.class)
public class GameThreadTest {
	private GameThread gameThreadUnderTest;

	@Mock
	private KosherFoodModel mockKosherFoodModel;

	@Mock
	private SurfaceHolder mockSurfaceHolder;

	@Mock
	private Canvas mockCanvas;

	private RoboActivity mockActivity;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(KosherFoodModel.class, mockKosherFoodModel);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		gameThreadUnderTest = new GameThread(mockActivity);
	}

	@Test
	public void testRunCorretly() {

		stub(mockSurfaceHolder.lockCanvas(null)).toReturn(mockCanvas);

		gameThreadUnderTest.setHolder(mockSurfaceHolder);
		gameThreadUnderTest.start();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		verify(mockKosherFoodModel, VerificationModeFactory.atLeastOnce())
				.doDraw(mockCanvas);
		verify(mockSurfaceHolder, VerificationModeFactory.atLeastOnce())
				.unlockCanvasAndPost(mockCanvas);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		verify(mockKosherFoodModel, VerificationModeFactory.atLeastOnce())
				.doDraw(mockCanvas);
		verify(mockSurfaceHolder, VerificationModeFactory.atLeastOnce())
				.unlockCanvasAndPost(mockCanvas);
		assertEquals(true, gameThreadUnderTest.isAlive());
	}

	@Test
	public void testRunWithoutSurfaceHolder() {
		gameThreadUnderTest.start();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(false, gameThreadUnderTest.isAlive());

	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
		boolean retry = true;

		gameThreadUnderTest.stopRunning();
		while (retry) {
			try {
				gameThreadUnderTest.join();
				retry = false;
			} catch (InterruptedException e) {

			}
		}
	}

}
