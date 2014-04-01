package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import hu.bme.aut.amorg.nyekilajos.kosherfood.R;
import hu.bme.aut.amorg.nyekilajos.kosherfood.activities.KosherSurfaceActivity;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.Food;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.IsKosherAsync;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherDbObj;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.Plate;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.Foods;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.FoodsDataSource;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.NotKosherPairs;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.NotKosherPairsDataSource;
import hu.bme.aut.amorg.nyekilajos.kosherfood.test.TestKosherFoodGuiceModule;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

@RunWith(RobolectricTestRunner.class)
public class PlateTest {

	private Plate plateUnderTest;
	private KosherSurfaceActivity mockActivity;

	@Mock
	private Food mockFood1;
	@Mock
	private Food mockFood2;
	@Mock
	private Food mockFood3;

	@Mock
	private IsKosherAsync mockIsKosherAsync;

	@Mock
	private Foods mockFoods1;
	@Mock
	private Foods mockFoods2;
	@Mock
	private Foods mockFoods3;

	@Mock
	private NotKosherPairs mockNotKosherPairs;
;
	
	@Mock
	private FoodsDataSource mockFoodsDataSource;

	@Mock
	private NotKosherPairsDataSource mockNotKosherPairsDataSource;
	

	private static final int TEST_PLATE_ID = 1;
	private static final float TEST_PLATE_INIT_X = 30;
	private static final float TEST_PLATE_INIT_Y = 40;
	private static final float TEST_PLATE_WIDTH = 50;
	private static final float TEST_PLATE_HEIGHT = 60;

	private static final float TEST_PLATE_NEW_X = 70;
	private static final float TEST_PLATE_NEW_Y = 80;

	private static final double DELTA_FOR_FLOAT_TEST = 0.00000000001;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockActivity = Robolectric.buildActivity(KosherSurfaceActivity.class)
				.create().visible().get();

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(IsKosherAsync.class, mockIsKosherAsync);
		testModule.addBinding(FoodsDataSource.class, mockFoodsDataSource);
		testModule.addBinding(NotKosherPairsDataSource.class,
				mockNotKosherPairsDataSource);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[32 * 1024];

		plateUnderTest = new Plate(TEST_PLATE_ID, TEST_PLATE_INIT_X,
				TEST_PLATE_INIT_Y, BitmapFactory.decodeResource(
						mockActivity.getResources(), R.drawable.plate, opts),
				TEST_PLATE_WIDTH, TEST_PLATE_HEIGHT, mockActivity);

	}

	@Test
	public void testPreconditions() {
		assertEquals(
				"ID does not match with the ID was set in setUp() function.",
				TEST_PLATE_ID, plateUnderTest.getId());
		assertEquals(
				"X coordinate does not match with the coordinate was set in setUp() function.",
				TEST_PLATE_INIT_X, plateUnderTest.getX(), DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"Y coordinate does not match with the coordinate was set in setUp() function.",
				TEST_PLATE_INIT_Y, plateUnderTest.getY(), DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"Width of the picture does not match with the width was set in setUp() function.",
				TEST_PLATE_WIDTH, plateUnderTest.getWidth(),
				DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"Height of the picture does not match with the height was set in setUp() function.",
				TEST_PLATE_HEIGHT, plateUnderTest.getHeight(),
				DELTA_FOR_FLOAT_TEST);
		assertNotNull(
				"The picture is null dispite it was set is setUp() funtion.",
				plateUnderTest.getPiture());

		RectF testRectF = new RectF(TEST_PLATE_INIT_X - TEST_PLATE_WIDTH / 2,
				TEST_PLATE_INIT_Y - TEST_PLATE_HEIGHT / 2, TEST_PLATE_INIT_X
						+ TEST_PLATE_WIDTH / 2, TEST_PLATE_INIT_Y
						+ TEST_PLATE_HEIGHT / 2);

		RectF actualRectF = plateUnderTest.getRectF();
		assertEquals(
				"The object does not create the RectF object correctly. The \"left\" member is incorrect.",
				testRectF.left, actualRectF.left, DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"The object does not create the RectF object correctly. The \"top\" member is incorrect.",
				testRectF.top, actualRectF.top, DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"The object does not create the RectF object correctly. The \"right\" member is incorrect.",
				testRectF.right, actualRectF.right, DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"The object does not create the RectF object correctly. The \"bottom\" member is incorrect.",
				testRectF.bottom, actualRectF.bottom, DELTA_FOR_FLOAT_TEST);

		Rect testRect = new Rect(
				(int) (TEST_PLATE_INIT_X - TEST_PLATE_WIDTH / 2),
				(int) (TEST_PLATE_INIT_Y - TEST_PLATE_HEIGHT / 2),
				(int) (TEST_PLATE_INIT_X + TEST_PLATE_WIDTH / 2),
				(int) (TEST_PLATE_INIT_Y + TEST_PLATE_HEIGHT / 2));

		Rect actualRect = plateUnderTest.getRect();
		assertEquals(
				"The object does not create the Rect object correctly. The \"left\" member is incorrect.",
				testRect.left, actualRect.left, DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"The object does not create the Rect object correctly. The \"top\" member is incorrect.",
				testRect.top, actualRect.top, DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"The object does not create the Rect object correctly. The \"right\" member is incorrect.",
				testRect.right, actualRect.right, DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"The object does not create the Rect object correctly. The \"bottom\" member is incorrect.",
				testRect.bottom, actualRect.bottom, DELTA_FOR_FLOAT_TEST);
	}

	@Test
	public void testMove() {
		plateUnderTest.setX(TEST_PLATE_NEW_X);
		plateUnderTest.setY(TEST_PLATE_NEW_Y);

		assertEquals(
				"Movement failure.The setX(float x) funtion does not work correctly.",
				TEST_PLATE_NEW_X, plateUnderTest.getX(), DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"Movement failure.The setX(float x) funtion does not work correctly.",
				TEST_PLATE_NEW_Y, plateUnderTest.getY(), DELTA_FOR_FLOAT_TEST);

		plateUnderTest.initCoordinates();

		assertEquals(
				"The initCoordinates function does not set the object back to the initial X coordinate.",
				TEST_PLATE_INIT_X, plateUnderTest.getX(), DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"The initCoordinates function does not set the object back to the initial X coordinate.",
				TEST_PLATE_INIT_Y, plateUnderTest.getY(), DELTA_FOR_FLOAT_TEST);

	}

	@Test
	public void testContentOfPlate() {
		List<Food> foodList = plateUnderTest.removeFoodsFromPlate();
		assertEquals("The plate should have been empty first time.", 0,
				foodList.size());

		plateUnderTest.addFoodToPlate(mockFood1);
		plateUnderTest.addFoodToPlate(mockFood2);
		foodList = plateUnderTest.removeFoodsFromPlate();

		assertEquals("The plate should have contained 2 foods.", 2,
				foodList.size());

		foodList = plateUnderTest.removeFoodsFromPlate();
		assertEquals("The plate should have been empty after removing foods.",
				0, foodList.size());

	}

	@Test
	public void testSearchDatabaseForEmptyPlate() {
		KosherDbObj kosher = plateUnderTest.searchDatabase();
		verify(mockFoodsDataSource).open();
		verify(mockFoodsDataSource).close();

		assertEquals(true, kosher.isKosher);
		assertEquals("", kosher.information);
	}

	@Test
	public void testSearchDatabaseForKosherPlate() {
		int FOOD_ID_1 = 1;
		String KOSHER_INFO = "KI";		

		stub(mockFood1.getId()).toReturn(FOOD_ID_1);

		stub(mockFoods1.get_id()).toReturn(FOOD_ID_1);
		stub(mockFoods1.getIs_kosher()).toReturn(1);
		stub(mockFoods1.getInformation()).toReturn(KOSHER_INFO);

		stub(mockFoodsDataSource.getFood(FOOD_ID_1)).toReturn(mockFoods1);

		plateUnderTest.addFoodToPlate(mockFood1);

		KosherDbObj kosher = plateUnderTest.searchDatabase();

		verify(mockFoodsDataSource).open();
		verify(mockFoodsDataSource).close();

		assertEquals(true, kosher.isKosher);
		assertEquals(KOSHER_INFO, kosher.information);
	}

	@Test
	public void testSearchDatabaseForNotKosherPlate() {
		int FOOD_ID_1 = 1;
		String KOSHER_INFO = "KI";

		stub(mockFood1.getId()).toReturn(FOOD_ID_1);

		stub(mockFoods1.get_id()).toReturn(FOOD_ID_1);
		stub(mockFoods1.getIs_kosher()).toReturn(0);
		stub(mockFoods1.getInformation()).toReturn(KOSHER_INFO);

		stub(mockFoodsDataSource.getFood(FOOD_ID_1)).toReturn(mockFoods1);		
		
		plateUnderTest.addFoodToPlate(mockFood1);

		KosherDbObj kosher = plateUnderTest.searchDatabase();

		verify(mockFoodsDataSource).open();
		verify(mockFoodsDataSource).close();

		assertEquals(false, kosher.isKosher);
		assertEquals(KOSHER_INFO, kosher.information);
	}

	@Test
	public void testSearchDatabaseForKosherPlateMoreFoods() {
		int FOOD_ID_1 = 1;
		int FOOD_ID_2 = 2;
		int FOOD_ID_3 = 3;
		String KOSHER_INFO1 = "KI1";
		String KOSHER_INFO2 = "KI2";
		String KOSHER_INFO3 = "KI3";

		stub(mockFood1.getId()).toReturn(FOOD_ID_1);
		stub(mockFood2.getId()).toReturn(FOOD_ID_2);
		stub(mockFood3.getId()).toReturn(FOOD_ID_3);

		stub(mockFoods1.get_id()).toReturn(FOOD_ID_1);
		stub(mockFoods1.getIs_kosher()).toReturn(1);
		stub(mockFoods1.getInformation()).toReturn(KOSHER_INFO1);
		stub(mockFoods2.get_id()).toReturn(FOOD_ID_2);
		stub(mockFoods2.getIs_kosher()).toReturn(1);
		stub(mockFoods2.getInformation()).toReturn(KOSHER_INFO2);
		stub(mockFoods3.get_id()).toReturn(FOOD_ID_3);
		stub(mockFoods3.getIs_kosher()).toReturn(1);
		stub(mockFoods3.getInformation()).toReturn(KOSHER_INFO3);
		
		stub(mockFoodsDataSource.getFood(FOOD_ID_1)).toReturn(mockFoods1);
		stub(mockFoodsDataSource.getFood(FOOD_ID_2)).toReturn(mockFoods2);
		stub(mockFoodsDataSource.getFood(FOOD_ID_3)).toReturn(mockFoods3);

		plateUnderTest.addFoodToPlate(mockFood1);
		plateUnderTest.addFoodToPlate(mockFood2);
		plateUnderTest.addFoodToPlate(mockFood3);

		KosherDbObj kosher = plateUnderTest.searchDatabase();

		verify(mockFoodsDataSource).open();
		verify(mockFoodsDataSource).close();
		
		assertEquals(true, kosher.isKosher);
		assertEquals(KOSHER_INFO3, kosher.information);
	}

	@Test
	public void testSearchDatabaseForNotKosherPlateMoreFoods() {
		int FOOD_ID_1 = 1;
		int FOOD_ID_2 = 2;
		int FOOD_ID_3 = 3;
		String KOSHER_INFO1 = "KI1";
		String KOSHER_INFO2 = "KI2";
		String KOSHER_INFO3 = "KI3";

		stub(mockFood1.getId()).toReturn(FOOD_ID_1);
		stub(mockFood2.getId()).toReturn(FOOD_ID_2);
		stub(mockFood3.getId()).toReturn(FOOD_ID_3);

		stub(mockFoods1.get_id()).toReturn(FOOD_ID_1);
		stub(mockFoods1.getIs_kosher()).toReturn(1);
		stub(mockFoods1.getInformation()).toReturn(KOSHER_INFO1);
		stub(mockFoods2.get_id()).toReturn(FOOD_ID_2);
		stub(mockFoods2.getIs_kosher()).toReturn(0);
		stub(mockFoods2.getInformation()).toReturn(KOSHER_INFO2);
		stub(mockFoods3.get_id()).toReturn(FOOD_ID_3);
		stub(mockFoods3.getIs_kosher()).toReturn(1);
		stub(mockFoods3.getInformation()).toReturn(KOSHER_INFO3);
		
		stub(mockFoodsDataSource.getFood(FOOD_ID_1)).toReturn(mockFoods1);
		stub(mockFoodsDataSource.getFood(FOOD_ID_2)).toReturn(mockFoods2);
		stub(mockFoodsDataSource.getFood(FOOD_ID_3)).toReturn(mockFoods3);

		plateUnderTest.addFoodToPlate(mockFood1);
		plateUnderTest.addFoodToPlate(mockFood2);
		plateUnderTest.addFoodToPlate(mockFood3);

		KosherDbObj kosher = plateUnderTest.searchDatabase();

		verify(mockFoodsDataSource).open();
		verify(mockFoodsDataSource).close();
		
		assertEquals(false, kosher.isKosher);
		assertEquals(KOSHER_INFO2, kosher.information);
	}
	
	@Test
	public void testSearchDatabaseForKosherButNotTogetherPlate() {
		int FOOD_ID_1 = 1;
		int FOOD_ID_2 = 2;
		int FOOD_ID_3 = 3;
		String KOSHER_INFO1 = "KI1";
		String KOSHER_INFO2 = "KI2";
		String KOSHER_INFO3 = "KI3";
		String KOSHER_INFO_NT = "KI_NT";

		stub(mockFood1.getId()).toReturn(FOOD_ID_1);
		stub(mockFood2.getId()).toReturn(FOOD_ID_2);
		stub(mockFood3.getId()).toReturn(FOOD_ID_3);

		stub(mockFoods1.get_id()).toReturn(FOOD_ID_1);
		stub(mockFoods1.getIs_kosher()).toReturn(1);
		stub(mockFoods1.getInformation()).toReturn(KOSHER_INFO1);
		stub(mockFoods2.get_id()).toReturn(FOOD_ID_2);
		stub(mockFoods2.getIs_kosher()).toReturn(1);
		stub(mockFoods2.getInformation()).toReturn(KOSHER_INFO2);
		stub(mockFoods3.get_id()).toReturn(FOOD_ID_3);
		stub(mockFoods3.getIs_kosher()).toReturn(1);
		stub(mockFoods3.getInformation()).toReturn(KOSHER_INFO3);
		
		stub(mockNotKosherPairs.getFood_first_id()).toReturn(FOOD_ID_2);
		stub(mockNotKosherPairs.getFood_first_id()).toReturn(FOOD_ID_3);
		stub(mockNotKosherPairs.getInformation()).toReturn(KOSHER_INFO_NT);
		
		stub(mockFoodsDataSource.getFood(FOOD_ID_1)).toReturn(mockFoods1);
		stub(mockFoodsDataSource.getFood(FOOD_ID_2)).toReturn(mockFoods2);
		stub(mockFoodsDataSource.getFood(FOOD_ID_3)).toReturn(mockFoods3);

		stub(mockNotKosherPairsDataSource.getNotKosherPairs(FOOD_ID_2, FOOD_ID_3)).toReturn(mockNotKosherPairs);
		stub(mockNotKosherPairsDataSource.getNotKosherPairs(FOOD_ID_3, FOOD_ID_2)).toReturn(mockNotKosherPairs);
		
		plateUnderTest.addFoodToPlate(mockFood1);
		plateUnderTest.addFoodToPlate(mockFood2);
		plateUnderTest.addFoodToPlate(mockFood3);

		KosherDbObj kosher = plateUnderTest.searchDatabase();

		verify(mockFoodsDataSource).open();
		verify(mockFoodsDataSource).close();
		
		verify(mockNotKosherPairsDataSource).open();
		verify(mockNotKosherPairsDataSource).close();
		
		assertEquals(false, kosher.isKosher);
		assertEquals(KOSHER_INFO_NT, kosher.information);
	}

	@After
	public void tearDown() {
		assertNotNull("Object is null dispite we initialized it before.",
				plateUnderTest);
		plateUnderTest.freeResources();
		TestKosherFoodGuiceModule.tearDown();
	}

}
