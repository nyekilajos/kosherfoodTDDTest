package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.Food;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherFoodModel;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherFoodModelFourPlate;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.Plate;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.SurfaceSize;
import hu.bme.aut.amorg.nyekilajos.kosherfood.test.TestKosherFoodGuiceModule;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import roboguice.activity.RoboActivity;
import android.media.SoundPool;

@RunWith(RobolectricTestRunner.class)
public class KosherFoodModelFourPlateTest {

	@Mock
	private SurfaceSize mockSurfaceSize;

	@Mock
	private Food mockFood1;

	@Mock
	private Food mockFood2;

	@Mock
	private Plate mockPlate;

	@Mock
	private SoundPool mockSoundPool;

	private RoboActivity mockActivity;

	private KosherFoodModel kosherFoodModelUnderTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(SurfaceSize.class, mockSurfaceSize);
		testModule.addBinding(SoundPool.class, mockSoundPool);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		kosherFoodModelUnderTest = new KosherFoodModelFourPlate(mockActivity);
		kosherFoodModelUnderTest.initGame();
	}

	@Test
	public void testPutFoodToPlate() {
		kosherFoodModelUnderTest.PutFoodToPlate(mockFood1, mockPlate);
		verify(mockPlate).addFoodToPlate(mockFood1);
	}

	@Test
	public void testRemoveFoodFromKosherPlate() {
		stub(mockPlate.isKosher()).toReturn(true);

		List<Food> mockFoodList = new ArrayList<Food>();
		mockFoodList.add(mockFood1);
		mockFoodList.add(mockFood2);

		stub(mockPlate.removeFoodsFromPlate()).toReturn(mockFoodList);
		kosherFoodModelUnderTest.RemoveFoodFromPlate(mockPlate);
		verify(mockFood1).initCoordinates();
		verify(mockFood2).initCoordinates();
	}

	@Test
	public void testRemoveFoodFromNotKosherPlate() {
		stub(mockPlate.isKosher()).toReturn(false);
		kosherFoodModelUnderTest.RemoveFoodFromPlate(mockPlate);

		List<Food> mockFoodList = new ArrayList<Food>();
		mockFoodList.add(mockFood1);
		mockFoodList.add(mockFood2);

		stub(mockPlate.removeFoodsFromPlate()).toReturn(mockFoodList);
		kosherFoodModelUnderTest.RemoveFoodFromPlate(mockPlate);
		verify(mockSoundPool).play(anyInt(), anyFloat(), anyFloat(), anyInt(),
				anyInt(), anyFloat());
		verify(mockFood1).initCoordinates();
		verify(mockFood2).initCoordinates();
	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
		kosherFoodModelUnderTest.destroyModel();
	}

}
