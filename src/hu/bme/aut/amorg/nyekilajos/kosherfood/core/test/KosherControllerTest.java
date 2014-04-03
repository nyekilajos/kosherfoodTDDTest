package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.Food;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.GameThread;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherController;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherFoodModel;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.Plate;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.ScheduledTasks;
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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

@RunWith(RobolectricTestRunner.class)
public class KosherControllerTest {

	@Mock
	private KosherFoodModel mockKosherFoodModel;

	@Mock
	private GameThread mockGameThread;

	@Mock
	private SurfaceHolder mockSurfaceHolder;

	@Mock
	private ScheduledTasks mockScheduledTasks;

	@Mock
	private View mockView;

	@Mock
	private MotionEvent mockEvent;

	@Mock
	private Food mockFood;

	@Mock
	private Plate mockPlate;

	private RoboActivity mockActivity;

	private KosherController kosherControllerUnderTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(KosherFoodModel.class, mockKosherFoodModel);
		testModule.addBinding(GameThread.class, mockGameThread);
		testModule.addBinding(ScheduledTasks.class, mockScheduledTasks);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		kosherControllerUnderTest = new KosherController(mockActivity);
	}

	@Test(expected = NullPointerException.class)
	public void testStartGameWithoutHolder() {
		kosherControllerUnderTest.startGame();
		verify(mockGameThread).setHolder(mockSurfaceHolder);
	}

	@Test
	public void testStartGame() {
		kosherControllerUnderTest.setHolder(mockSurfaceHolder);
		kosherControllerUnderTest.startGame();
		verify(mockGameThread).setHolder(mockSurfaceHolder);
		verify(mockGameThread).start();
		verify(mockScheduledTasks, VerificationModeFactory.atLeast(4))
				.setAction(anyInt());
	}

	@Test
	public void testOnTouch() {
		float X = 300;
		float Y = 400;

		stub(mockEvent.getX()).toReturn(X);
		stub(mockEvent.getY()).toReturn(Y);
		stub(mockKosherFoodModel.selectTouchableFood(X, Y)).toReturn(mockFood);
		stub(mockKosherFoodModel.selectTouchablePlate(X, Y))
				.toReturn(mockPlate);
		stub(mockKosherFoodModel.selectPlate(mockFood)).toReturn(mockPlate);

		stub(mockEvent.getAction()).toReturn(MotionEvent.ACTION_DOWN);
		kosherControllerUnderTest.onTouch(mockView, mockEvent);

		verify(mockKosherFoodModel).RemoveFoodFromPlate(mockPlate);
		verify(mockScheduledTasks).setAction(ScheduledTasks.ACTION_NEW_PLATE);
		verify(mockPlate).initCoordinates();

		stub(mockEvent.getAction()).toReturn(MotionEvent.ACTION_MOVE);
		kosherControllerUnderTest.onTouch(mockView, mockEvent);

		verify(mockFood).setX(X);
		verify(mockFood).setY(Y);

		stub(mockEvent.getAction()).toReturn(MotionEvent.ACTION_UP);
		kosherControllerUnderTest.onTouch(mockView, mockEvent);

		verify(mockKosherFoodModel).PutFoodToPlate(mockFood, mockPlate);
	}

	@Test
	public void testDestroyGame() {
		float X = 300;
		float Y = 400;

		stub(mockEvent.getX()).toReturn(X);
		stub(mockEvent.getY()).toReturn(Y);
		stub(mockKosherFoodModel.selectTouchableFood(X, Y)).toReturn(mockFood);
		stub(mockKosherFoodModel.selectTouchablePlate(X, Y))
				.toReturn(mockPlate);
		stub(mockKosherFoodModel.selectPlate(mockFood)).toReturn(mockPlate);

		stub(mockEvent.getAction()).toReturn(MotionEvent.ACTION_DOWN);
		kosherControllerUnderTest.onTouch(mockView, mockEvent);

		kosherControllerUnderTest.destroyGame();

		verify(mockGameThread).stopRunning();
		try {
			verify(mockGameThread).join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		verify(mockFood).freeResources();
		verify(mockKosherFoodModel).destroyModel();
	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
		kosherControllerUnderTest.destroyGame();
	}

}
