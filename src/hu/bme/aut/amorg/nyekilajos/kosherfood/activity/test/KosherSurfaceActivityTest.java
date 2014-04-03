package hu.bme.aut.amorg.nyekilajos.kosherfood.activity.test;

import hu.bme.aut.amorg.nyekilajos.kosherfood.activities.KosherSurfaceActivity;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherSurface;
import hu.bme.aut.amorg.nyekilajos.kosherfood.test.TestKosherFoodGuiceModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ActivityController;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class KosherSurfaceActivityTest {

	@Mock
	private KosherSurface mockKosherSurface;

	private ActivityController<KosherSurfaceActivity> kosherSurfaceActivityUnderTestController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(KosherSurface.class, mockKosherSurface);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		kosherSurfaceActivityUnderTestController = Robolectric
				.buildActivity(KosherSurfaceActivity.class);
	}

	@Test
	public void testOnCreate() {
		kosherSurfaceActivityUnderTestController.create();

		verify(mockKosherSurface).setOnTouchListener(mockKosherSurface);
	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
	}
}
