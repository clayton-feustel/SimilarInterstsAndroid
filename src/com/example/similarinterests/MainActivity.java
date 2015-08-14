package com.example.similarinterests;

import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	private static GoogleApiClient mClient;	
	/**
	 *  Track whether an authorization activity is stacking over the current activity, i.e. when
	 *  a known auth error is being resolved, such as showing the account chooser or presenting a
	 *  consent dialog. This avoids common duplications as might happen on screen rotations, etc.
	 */
	private static final String AUTH_PENDING = "auth_state_pending";
	private static final int REQUEST_OAUTH = 1;
	private boolean authInProgress = false;
	
	private static final String TAG = "Authorization";
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState != null) {
	        authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
	    }

	    buildFitnessClient();

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putBoolean(AUTH_PENDING, authInProgress);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	private void buildFitnessClient(){
		// Create the Google API Client
	    mClient = new GoogleApiClient.Builder(this)
	            .addApi(Fitness.HISTORY_API)
	            .addScope(new Scope(Scopes.FITNESS_BODY_READ))
	            .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
	            .addConnectionCallbacks(
	                    new GoogleApiClient.ConnectionCallbacks() {

	                        @Override
	                        public void onConnected(Bundle bundle) {
	                        	MainActivity.getData();
	                        }

	                        @Override
	                        public void onConnectionSuspended(int i) {
	                            // If your connection to the sensor gets lost at some point,
	                            // you'll be able to determine the reason and react to it here.
	                            if (i == ConnectionCallbacks.CAUSE_NETWORK_LOST) {
	                                Log.i(TAG, "Connection lost.  Cause: Network Lost.");
	                            } else if (i == ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
	                                Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
	                            }
	                        }
	                    }
	            )
	            .addOnConnectionFailedListener(
	                    new GoogleApiClient.OnConnectionFailedListener() {
	                        // Called whenever the API client fails to connect.
	                        @Override
	                        public void onConnectionFailed(ConnectionResult result) {
	                            Log.i(TAG, "Connection failed. Cause: " + result.toString());
	                            if (!result.hasResolution()) {
	                                // Show the localized error dialog
	                                GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
	                                        MainActivity.this, 0).show();
	                                return;
	                            }
	                            // The failure has a resolution. Resolve it.
	                            // Called typically when the app is not yet authorized, and an
	                            // authorization dialog is displayed to the user.
	                            if (!authInProgress) {
	                                try {
	                                    Log.i(TAG, "Attempting to resolve failed connection");
	                                    authInProgress = true;
	                                    result.startResolutionForResult(MainActivity.this, REQUEST_OAUTH);
	                                } catch (IntentSender.SendIntentException e) {
	                                    Log.e(TAG, "Exception while starting resolution activity", e);
	                                }
	                            }
	                        }
	                    }
	            )
	            .build();
	}
	
	public static void getData(){
		HealthData.requestCurrentWeight(mClient);
		HealthData.requestBurnedCalories(mClient);
		HealthData.requestSteps(mClient);
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    // Connect to the Fitness API
	    Log.i(TAG, "Connecting...");
	    mClient.connect();
	}

	@Override
	protected void onStop() {
	    super.onStop();
	    if (mClient.isConnected()) {
	        mClient.disconnect();
	    }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_OAUTH) {
	        authInProgress = false;
	        if (resultCode == RESULT_OK) {
	            // Make sure the app is not already connected or attempting to connect
	            if (!mClient.isConnecting() && !mClient.isConnected()) {
	                mClient.connect();
	            }
	        }
	    }
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Log.i("Position", Integer.toString(position));
			switch(position){
				case 0:
					return new NotificationActivity();
				case 1:
				default:
					return new HealthDataActivity();
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

}
