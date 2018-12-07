package com.worldline.android.zaloratweetapp.modules.base;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.worldline.android.zaloratweetapp.R;
import com.worldline.android.zaloratweetapp.commons.view.BottomNavigationViewHelper;
import com.worldline.android.zaloratweetapp.modules.compose.ComposeTweetFragment;
import com.worldline.android.zaloratweetapp.modules.home.HomeFragment;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, HomeFragment.TaskCallbacks
{

	@InjectView(R.id.toolbar)
	Toolbar toolbar;
	@InjectView(R.id.navigationView)
	BottomNavigationView bottomNavigationView;
	@InjectView(R.id.drawer_layout)
	DrawerLayout drawer;
	@InjectView(R.id.fab)
	FloatingActionButton fab;
	@InjectView(R.id.nav_view)
	NavigationView navigationView;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		ButterKnife.inject(this);
		setSupportActionBar(toolbar);


		try
		{
			initialiseUI();

			if (savedInstanceState == null)
			{
				loadDefaultHomeFragment();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initialiseUI()
	{
		BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
		setFABOnClickListener();

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		navigationView.setNavigationItemSelectedListener(this);
		bottomNavigationView.setOnNavigationItemSelectedListener(this);
	}

	private void setFABOnClickListener()
	{
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

				String TAG = "ComposeTweetFragment";
				Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
				if (null == fragment)
				{
					fragment = new ComposeTweetFragment();
				}
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				//fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);

				if (!fragment.isAdded())
				{
					fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
					fragmentTransaction.replace(R.id.mainFrameLayout, fragment, TAG).addToBackStack(null).commit();
				}
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		try
		{
			if (drawer.isDrawerOpen(GravityCompat.START))
			{
				drawer.closeDrawer(GravityCompat.START);
			}
			else
			{
				if (getSupportFragmentManager().getBackStackEntryCount() == 0)
				{
					logoutDialog();
				}
				else
				{
					getSupportFragmentManager().popBackStack();
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		String TAG = null;
		Fragment fragment = null;
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		switch (id)
		{
			case R.id.navigationHome:
				TAG = "HomeFragment";
				fragment = getSupportFragmentManager().findFragmentByTag(TAG);
				if (null == fragment)
				{
					fragment = new HomeFragment();
				}
				break;
		}
		if (null != fragment)
		{
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			if (!fragment.isAdded())
			{
				fragmentTransaction.replace(R.id.mainFrameLayout, fragment, TAG).addToBackStack(null).commit();
			}
		}
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	public void hideBottomNavigation()
	{
		bottomNavigationView.setVisibility(View.GONE);
		fab.setVisibility(View.GONE);
	}

	public void showBottomNavigation()
	{
		bottomNavigationView.setVisibility(View.VISIBLE);
		fab.setVisibility(View.VISIBLE);
	}

	private void loadDefaultHomeFragment()
	{
		String TAG = "HomeFragment";
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
		if (null == fragment)
		{
			fragment = new HomeFragment();
		}
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		if (!fragment.isAdded())
		{
			fragmentTransaction.replace(R.id.mainFrameLayout, fragment, TAG).commit();
		}
	}

	private void logoutDialog()
	{
		new AlertDialog.Builder(this).setIcon(null).setTitle("Confirm").setCancelable(false).setMessage("Do you want to exit this app ?").setPositiveButton("Yes", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}

		}).setNegativeButton("No", null).show();


	}

	@Override
	public void onCancelled()
	{

	}


}
