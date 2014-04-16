package hu.bme.aut.amorg.nyekilajos.kosherfood.core.test;

import static org.mockito.Mockito.verify;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.InitGameAsync;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherController;
import hu.bme.aut.amorg.nyekilajos.kosherfood.core.KosherFoodModel;
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
public class InitGameAsyncTest {

	@Mock
	private KosherController mockKosherController;

	@Mock
	private KosherFoodModel mockKosherFoodModel;

	@Mock
	private RoboActivity mockActivity;

	private InitGameAsync initGameAsyncUnderTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TestKosherFoodGuiceModule testModule = new TestKosherFoodGuiceModule();
		testModule.addBinding(KosherController.class, mockKosherController);
		testModule.addBinding(KosherFoodModel.class, mockKosherFoodModel);
		TestKosherFoodGuiceModule.setUp(this, testModule);

		mockActivity = Robolectric.buildActivity(RoboActivity.class).create()
				.visible().get();

		initGameAsyncUnderTest = new InitGameAsync(mockActivity);

	}

	@Test
	public void testCall() throws Exception {
		initGameAsyncUnderTest.call();
		verify(mockKosherFoodModel).initGame();
	}

	@Test
	public void testOnFinally() {
		initGameAsyncUnderTest.onFinally();
		verify(mockKosherController).startGame();
	}

	@After
	public void tearDown() {
		TestKosherFoodGuiceModule.tearDown();
	}

}
