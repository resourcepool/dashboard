package com.excilys.shoofleurs.dashboard.resources;

import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.excilys.shoofleurs.dashboard.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.activities.MainActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by excilys on 20/04/16.
 */
@RunWith(AndroidJUnit4.class)
public abstract class BaseDashBoardActivityTest {
	@Rule
	public ActivityTestRule<DashboardActivity> mActivityTestRule = new ActivityTestRule<>(DashboardActivity.class);
	@Rule
	public ServiceTestRule mServiceTestRule = new ServiceTestRule();

	/**
	 * Lance une boucle pour que le programme ne ferme pas
	 *
	 * SEULEMENT EN CAS DE DEBUG !
	 */
	protected void loop() {
		Looper.prepare();
		Looper.loop();
	}

	protected void checkToast(final String pTxt) {
		onView(withText(pTxt)).inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
	}

	protected void checkToast(final int resourceId) {
		onView(withText(resourceId)).inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
	}
}
