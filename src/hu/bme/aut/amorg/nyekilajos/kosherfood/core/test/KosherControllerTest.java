package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.control.KosherController;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.control.KosherFoodModel;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.drawable.Food;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.drawable.Plate;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.scheduled.ScheduledTaskInit;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.scheduled.ScheduledTaskInitFourPlate;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.scheduled.ScheduledTaskNewPlate;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.scheduled.ScheduledTaskRepaint;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.scheduled.TaskScheduler;
import hu.bme.aut.amorg.nyekilajos.kosherfood.test.TestKosherFoodGuiceModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
	private SurfaceHolder mockSurfaceHolder;

	@Mock
	private View mockView;

	@Mock
	private MotionEvent mockEvent;

	@Mock
	private Food mockFood;

	@Mock
	private Plate mockPlate;

	@Mock
	private TaskScheduler mockTaskScheduler;

	@Mock
	private ScheduledTaskInitFourPlate mockScheduledTaskInitFourPlate;

	@Mock
	private ScheduledTaskRepaint mockScheduledTaskRepaint;

	@Mock
	private ScheduledTaskNewPlate mockScheduledTaskNewPlate;

	private RoboActivity mockActivity;

	private KosherController kosherControllerUnderTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(KosherFoodModel.class, mockKosherFoodModel);
		testModule.addBinding(TaskScheduler.class, mockTaskScheduler);
		testModule.addBinding(ScheduledTaskRepaint.class,
				mockScheduledTaskRepaint);
		testModule.addBinding(ScheduledTaskNewPlate.class,
				mockScheduledTaskNewPlate);
		testModule.addBinding(ScheduledTaskInit.class,
				mockScheduledTaskInitFourPlate);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		kosherControllerUnderTest = new KosherController(mockActivity);
	}

	@Test(expected = NullPointerException.class)
	public void testStartGameWithoutHolder() {
		kosherControllerUnderTest.startGame();
		verify(mockScheduledTaskRepaint, Mockito.never()).setHolder(
				mockSurfaceHolder);
		verify(mockScheduledTaskInitFourPlate, Mockito.never());
	}

	@Test
	public void testStartGame() {
		stub(mockScheduledTaskRepaint.setHolder(mockSurfaceHolder)).toReturn(
				mockScheduledTaskRepaint);
		kosherControllerUnderTest.setHolder(mockSurfaceHolder);
		kosherControllerUnderTest.startGame();
		verify(mockTaskScheduler).add(mockScheduledTaskRepaint);
		verify(mockScheduledTaskRepaint).setHolder(mockSurfaceHolder);
		verify(mockTaskScheduler).add(mockScheduledTaskInitFourPlate);
	}

	@Test
	public void testOnTouchForEmptyingPlate() {
		float X = 300;
		float Y = 400;

		stub(mockEvent.getX()).toReturn(X);
		stub(mockEvent.getY()).toReturn(Y);
		stub(mockKosherFoodModel.selectTouchableFood(X, Y)).toReturn(null);
		stub(mockKosherFoodModel.selectTouchablePlate(X, Y))
				.toReturn(mockPlate);
		stub(mockScheduledTaskNewPlate.setPlate(mockPlate)).toReturn(
				mockScheduledTaskNewPlate);
		
		stub(mockEvent.getAction()).toReturn(MotionEvent.ACTION_DOWN);
		kosherControllerUnderTest.onTouch(mockView, mockEvent);

		verify(mockKosherFoodModel).RemoveFoodFromPlate(mockPlate);
		verify(mockTaskScheduler).add(mockScheduledTaskNewPlate);
	}
	
	@Test
	public void testOnTouchForMovingPlate() {
		float X = 300;
		float Y = 400;

		stub(mockEvent.getX()).toReturn(X);
		stub(mockEvent.getY()).toReturn(Y);
		stub(mockKosherFoodModel.selectTouchableFood(X, Y)).toReturn(mockFood);
		stub(mockKosherFoodModel.selectTouchablePlate(X, Y))
				.toReturn(null);
		stub(mockKosherFoodModel.selectPlate(mockFood)).toReturn(null);

		stub(mockEvent.getAction()).toReturn(MotionEvent.ACTION_DOWN);
		kosherControllerUnderTest.onTouch(mockView, mockEvent);

		stub(mockEvent.getAction()).toReturn(MotionEvent.ACTION_MOVE);
		kosherControllerUnderTest.onTouch(mockView, mockEvent);

		verify(mockFood).setX(X);
		verify(mockFood).setY(Y);
	}
	
	@Test
	public void testOnTouchForPuttingFoodToPlate() {
		float X = 300;
		float Y = 400;

		stub(mockEvent.getX()).toReturn(X);
		stub(mockEvent.getY()).toReturn(Y);
		stub(mockKosherFoodModel.selectTouchableFood(X, Y)).toReturn(mockFood);
		stub(mockKosherFoodModel.selectTouchablePlate(X, Y))
				.toReturn(null);
		stub(mockKosherFoodModel.selectPlate(mockFood)).toReturn(mockPlate);

		stub(mockEvent.getAction()).toReturn(MotionEvent.ACTION_DOWN);
		kosherControllerUnderTest.onTouch(mockView, mockEvent);

		stub(mockEvent.getAction()).toReturn(MotionEvent.ACTION_MOVE);
		kosherControllerUnderTest.onTouch(mockView, mockEvent);

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

		verify(mockFood).freeResources();
		verify(mockKosherFoodModel).destroyModel();
		verify(mockTaskScheduler).shutDown();
	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
		kosherControllerUnderTest.destroyGame();
	}

}
