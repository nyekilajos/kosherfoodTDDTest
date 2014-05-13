package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hu.bme.aut.amorg.nyekilajos.kosherfood.R;
import hu.bme.aut.amorg.nyekilajos.kosherfood.activities.KosherSurfaceActivity;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.drawable.Food;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

@RunWith(RobolectricTestRunner.class)
public class FoodTest {
	private Food foodUnderTest;

	private static final int TEST_FOOD_ID = 1;
	private static final String TEST_FOOD_NAME = "testFood";
	private static final float TEST_FOOD_INIT_X = 30;
	private static final float TEST_FOOD_INIT_Y = 40;
	private static final float TEST_FOOD_WIDTH = 50;
	private static final float TEST_FOOD_HEIGHT = 60;

	private static final float TEST_FOOD_NEW_X = 70;
	private static final float TEST_FOOD_NEW_Y = 80;

	private static final double DELTA_FOR_FLOAT_TEST = 0.00000000001;

	@Test
	public void testPreconditions() throws Exception {
		assertEquals(
				"ID does not match with the ID was set in setUp() function.",
				TEST_FOOD_ID, foodUnderTest.getId());
		assertEquals(
				"Name does not match with the Name was set in setUp() function.",
				TEST_FOOD_NAME, foodUnderTest.getName());

		assertEquals(
				"X coordinate does not match with the coordinate was set in setUp() function.",
				TEST_FOOD_INIT_X, foodUnderTest.getX(), DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"Y coordinate does not match with the coordinate was set in setUp() function.",
				TEST_FOOD_INIT_Y, foodUnderTest.getY(), DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"Width of the picture does not match with the width was set in setUp() function.",
				TEST_FOOD_WIDTH, foodUnderTest.getWidth(), DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"Height of the picture does not match with the height was set in setUp() function.",
				TEST_FOOD_HEIGHT, foodUnderTest.getHeight(),
				DELTA_FOR_FLOAT_TEST);
		assertNotNull(
				"The picture is null dispite it was set is setUp() funtion.",
				foodUnderTest.getPiture());

		RectF testRectF = new RectF(TEST_FOOD_INIT_X - TEST_FOOD_WIDTH / 2,
				TEST_FOOD_INIT_Y - TEST_FOOD_HEIGHT / 2, TEST_FOOD_INIT_X
						+ TEST_FOOD_WIDTH / 2, TEST_FOOD_INIT_Y
						+ TEST_FOOD_HEIGHT / 2);

		RectF actualRectF = foodUnderTest.getRectF();
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
				(int) (TEST_FOOD_INIT_X - TEST_FOOD_WIDTH / 2),
				(int) (TEST_FOOD_INIT_Y - TEST_FOOD_HEIGHT / 2),
				(int) (TEST_FOOD_INIT_X + TEST_FOOD_WIDTH / 2),
				(int) (TEST_FOOD_INIT_Y + TEST_FOOD_HEIGHT / 2));

		Rect actualRect = foodUnderTest.getRect();
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
		foodUnderTest.setX(TEST_FOOD_NEW_X);
		foodUnderTest.setY(TEST_FOOD_NEW_Y);

		assertEquals(
				"Movement failure.The setX(float x) funtion does not work correctly.",
				TEST_FOOD_NEW_X, foodUnderTest.getX(), DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"Movement failure.The setX(float x) funtion does not work correctly.",
				TEST_FOOD_NEW_Y, foodUnderTest.getY(), DELTA_FOR_FLOAT_TEST);

		foodUnderTest.initCoordinates();

		assertEquals(
				"The initCoordinates function does not set the object back to the initial X coordinate.",
				TEST_FOOD_INIT_X, foodUnderTest.getX(), DELTA_FOR_FLOAT_TEST);
		assertEquals(
				"The initCoordinates function does not set the object back to the initial X coordinate.",
				TEST_FOOD_INIT_Y, foodUnderTest.getY(), DELTA_FOR_FLOAT_TEST);

	}

	@Before
	public void setUp() throws Exception {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[32 * 1024];

		KosherSurfaceActivity activity = Robolectric
				.buildActivity(KosherSurfaceActivity.class).create().visible()
				.get();

		foodUnderTest = new Food(TEST_FOOD_ID, TEST_FOOD_NAME,
				TEST_FOOD_INIT_X, TEST_FOOD_INIT_Y,
				BitmapFactory.decodeResource(activity.getResources(),
						R.drawable.pig, opts), TEST_FOOD_WIDTH,
				TEST_FOOD_HEIGHT);
	}

	@After
	public void tearDown() throws Exception {
		assertNotNull("Object is null dispite we initialized it before.",
				foodUnderTest);
		foodUnderTest.freeResources();
	}

}
