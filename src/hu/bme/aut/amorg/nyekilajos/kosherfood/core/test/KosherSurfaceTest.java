package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.InitGameAsync;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherController;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherSurface;
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
import android.view.SurfaceHolder;

@RunWith(RobolectricTestRunner.class)
public class KosherSurfaceTest {

	@Mock
	private KosherController mockKosherController;

	@Mock
	private InitGameAsync mockInitGameAsync;

	@Mock
	private SurfaceSize mockSurfaceSize;

	@Mock
	private SurfaceHolder mockHolder;

	private RoboActivity mockActivity;

	private KosherSurface kosherSurfaceUnderTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(KosherController.class, mockKosherController);
		testModule.addBinding(InitGameAsync.class, mockInitGameAsync);
		testModule.addBinding(SurfaceSize.class, mockSurfaceSize);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		kosherSurfaceUnderTest = new KosherSurface(mockActivity);
	}

	@Test
	public void testSurfaceCreated() {
		kosherSurfaceUnderTest.surfaceCreated(mockHolder);
		verify(mockSurfaceSize).setSurfaceHeight(anyInt());
		verify(mockSurfaceSize).setSurfaceWidth(anyInt());
		verify(mockKosherController).setHolder(mockHolder);
		verify(mockInitGameAsync).execute();
	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
	}

}
