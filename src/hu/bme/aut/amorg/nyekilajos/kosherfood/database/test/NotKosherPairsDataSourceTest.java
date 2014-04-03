package hu.bme.aut.amorg.nyekilajos.kosherfood.database.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.KosherDbHelper;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.NotKosherPairs;
import hu.bme.aut.amorg.nyekilajos.kosherfood.database.NotKosherPairsDataSource;
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
public class NotKosherPairsDataSourceTest {
	@Mock
	private KosherDbHelper mockKosherDbHelper;

	@Mock
	private SQLiteDatabase mockDatabase;

	@Mock
	private NotKosherPairs mockNotKosherPairs;

	@Mock
	private Cursor mockCursor;

	private RoboActivity mockActivity;

	private NotKosherPairsDataSource notKosherPairsDataSourceUnderTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(KosherDbHelper.class, mockKosherDbHelper);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		notKosherPairsDataSourceUnderTest = new NotKosherPairsDataSource(
				mockActivity);
	}

	@Test
	public void testInsert() {
		stub(mockKosherDbHelper.getWritableDatabase()).toReturn(mockDatabase);
		notKosherPairsDataSourceUnderTest.open();
		notKosherPairsDataSourceUnderTest.insert(mockNotKosherPairs);

		verify(mockDatabase).beginTransaction();
		verify(mockDatabase).insert(eq(KosherDbHelper.NOT_KOSHER_PAIRS_TABLE),
				anyString(), any(ContentValues.class));
		verify(mockDatabase).setTransactionSuccessful();
		verify(mockDatabase).endTransaction();
	}

	@Test
	public void testDelete() {
		stub(mockKosherDbHelper.getWritableDatabase()).toReturn(mockDatabase);
		notKosherPairsDataSourceUnderTest.open();
		notKosherPairsDataSourceUnderTest.delete(mockNotKosherPairs);

		verify(mockDatabase).delete(eq(KosherDbHelper.NOT_KOSHER_PAIRS_TABLE),
				anyString(), any(String[].class));
	}

	@Test
	public void testGetNotKosherPairsWithResult() {
		stub(mockKosherDbHelper.getWritableDatabase()).toReturn(mockDatabase);
		stub(
				mockDatabase.query(eq(KosherDbHelper.NOT_KOSHER_PAIRS_TABLE),
						any(String[].class), anyString(), any(String[].class),
						anyString(), anyString(), anyString())).toReturn(
				mockCursor);
		stub(mockCursor.getCount()).toReturn(1);

		notKosherPairsDataSourceUnderTest.open();
		NotKosherPairs notKosherPairsReturned = notKosherPairsDataSourceUnderTest
				.getNotKosherPairs(0, 1);

		verify(mockCursor).moveToFirst();
		verify(mockCursor).close();
		assertNotNull(notKosherPairsReturned);

	}

	@Test
	public void testGetNotKosherPairsWithoutResult() {
		stub(mockKosherDbHelper.getWritableDatabase()).toReturn(mockDatabase);
		stub(
				mockDatabase.query(eq(KosherDbHelper.NOT_KOSHER_PAIRS_TABLE),
						any(String[].class), anyString(), any(String[].class),
						anyString(), anyString(), anyString())).toReturn(
				mockCursor);

		notKosherPairsDataSourceUnderTest.open();
		NotKosherPairs notKosherPairsReturned = notKosherPairsDataSourceUnderTest
				.getNotKosherPairs(0, 1);

		verify(mockCursor).close();
		assertNull(notKosherPairsReturned);
	}

	@Test
	public void testTruncate() {
		stub(mockKosherDbHelper.getWritableDatabase()).toReturn(mockDatabase);
		notKosherPairsDataSourceUnderTest.open();
		notKosherPairsDataSourceUnderTest.truncateNotKosherPairs();

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
