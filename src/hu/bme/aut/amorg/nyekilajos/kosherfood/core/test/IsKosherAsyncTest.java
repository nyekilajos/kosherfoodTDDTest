package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherDbObj;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.control.IsKosherAsync;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.drawable.Plate;
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
public class IsKosherAsyncTest {

	@Mock
	private RoboActivity mockActivity;

	@Mock
	private Plate mockPlate;

	private IsKosherAsync isKosherAsyncUnderTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		isKosherAsyncUnderTest = new IsKosherAsync(mockActivity);

	}

	@Test(expected = NullPointerException.class)
	public void testCallWithoutPlate() throws Exception {
		isKosherAsyncUnderTest.call();
	}

	@Test
	public void testCall() throws Exception {
		isKosherAsyncUnderTest.setPlate(mockPlate);
		isKosherAsyncUnderTest.call();
		verify(mockPlate).searchDatabase();

	}

	@Test
	public void testOnFinally() {
		isKosherAsyncUnderTest.setPlate(mockPlate);
		isKosherAsyncUnderTest.onFinally();
		verify(mockPlate).postDbResultCallback(any(KosherDbObj.class));

	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
	}

}
