package hu.bme.aut.amorg.nyekilajos.kosherfood.database.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.Foods;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.FoodsDataSource;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.KosherDbHelper;
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
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@RunWith(RobolectricTestRunner.class)
public class FoodsDataSourceTest {

	@Mock
	private KosherDbHelper mockKosherDbHelper;

	@Mock
	private SQLiteDatabase mockDatabase;

	@Mock
	private Foods mockFoods;

	@Mock
	private Cursor mockCursor;

	private RoboActivity mockActivity;

	private FoodsDataSource foodsDataSourceUnderTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(KosherDbHelper.class, mockKosherDbHelper);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		foodsDataSourceUnderTest = new FoodsDataSource(mockActivity);
	}

	@Test
	public void testInsert() {
		stub(mockKosherDbHelper.getWritableDatabase()).toReturn(mockDatabase);
		foodsDataSourceUnderTest.open();
		foodsDataSourceUnderTest.insert(mockFoods);

		verify(mockDatabase).beginTransaction();
		verify(mockDatabase).insert(eq(KosherDbHelper.FOODS_TABLE),
				anyString(), any(ContentValues.class));
		verify(mockDatabase).setTransactionSuccessful();
		verify(mockDatabase).endTransaction();
	}

	@Test
	public void testDelete() {
		stub(mockKosherDbHelper.getWritableDatabase()).toReturn(mockDatabase);
		foodsDataSourceUnderTest.open();
		foodsDataSourceUnderTest.delete(mockFoods);

		verify(mockDatabase).delete(eq(KosherDbHelper.FOODS_TABLE),
				anyString(), any(String[].class));
	}

	@Test
	public void testGetFoods() {
		stub(mockKosherDbHelper.getWritableDatabase()).toReturn(mockDatabase);
		stub(
				mockDatabase.query(eq(KosherDbHelper.FOODS_TABLE),
						any(String[].class), anyString(), any(String[].class),
						anyString(), anyString(), anyString())).toReturn(
				mockCursor);

		foodsDataSourceUnderTest.open();
		Foods foodsReturned = foodsDataSourceUnderTest.getFood(0);

		verify(mockCursor).moveToFirst();
		verify(mockCursor).close();
		assertNotNull(foodsReturned);

	}

	@Test
	public void testTruncate() {
		stub(mockKosherDbHelper.getWritableDatabase()).toReturn(mockDatabase);
		foodsDataSourceUnderTest.open();
		foodsDataSourceUnderTest.truncateFoods();

		verify(mockDatabase).beginTransaction();
		verify(mockDatabase).execSQL(anyString());
		verify(mockDatabase).setTransactionSuccessful();
		verify(mockDatabase).endTransaction();
	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
	}

}
