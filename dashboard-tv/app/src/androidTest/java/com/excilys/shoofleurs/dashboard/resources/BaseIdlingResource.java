package com.excilys.shoofleurs.dashboard.resources;

import android.support.test.espresso.IdlingResource;

/**
 * Base d'un IdlingResource, pour simplifier la création rapide
 *
 * Created by Mickael on 31/10/2015.
 */
public abstract class BaseIdlingResource implements IdlingResource {

	//	private Context mContext;
	private ResourceCallback callback;


	@Override
	public final String getName() {
		return this.getClass().getName();
	}

	@Override
	public final boolean isIdleNow() {
		boolean idle = getIdle();
		if (idle) {
			onTransitionToIdle();
		}
		return idle;
	}

	protected abstract boolean getIdle();

	public final void onTransitionToIdle() {
		if (callback != null) {
			callback.onTransitionToIdle();
		}
	}

	@Override
	public final void registerIdleTransitionCallback(ResourceCallback callback) {
		this.callback = callback;
	}
}

