package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.Food;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherFoodModel;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.Plate;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.SurfaceSize;
import hu.bme.aut.amorg.nyekilajos.kosherfood.test.TestKosherFoodGuiceModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import roboguice.activity.RoboActivity;

@RunWith(RobolectricTestRunner.class)
public class KosherFoodModelTest {

	@Mock
	private SurfaceSize mockSurfaceSize;

	@Mock
	private Food mockFood;

	@Mock
	private Plate mockPlate;

	private RoboActivity mockActivity;

	private KosherFoodModel kosherFoodModelUnderTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(SurfaceSize.class, mockSurfaceSize);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		kosherFoodModelUnderTest = new KosherFoodModel(mockActivity);
		kosherFoodModelUnderTest.initGame();
	}

	@Test
	public void testPutFoodToPlate() {
		kosherFoodModelUnderTest.PutFoodToPlate(mockFood, mockPlate);
		verify(mockPlate).addFoodToPlate(mockFood);
	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
		kosherFoodModelUnderTest.destroyModel();
	}

}
