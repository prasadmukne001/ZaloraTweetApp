package com.worldline.android.zaloratweetapp.modules.splash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.worldline.android.zaloratweetapp.R;
import com.worldline.android.zaloratweetapp.modules.base.BaseActivity;
import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity
{

	// integer for permissions results request
	private static final int ALL_PERMISSIONS_RESULT = 100;
	private ArrayList<String> permissionsToRequest;
	private ArrayList<String> permissionsRejected = new ArrayList<>();
	private ArrayList<String> permissions = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen2);
		try
		{
			requestPermissions();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void requestPermissions() throws NameNotFoundException
	{
		PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
		if (info.requestedPermissions != null)
		{
			for (String p : info.requestedPermissions)
			{
				permissions.add(p);
			}
		}

		permissionsToRequest = permissionsToRequest(permissions);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			if (permissionsToRequest.size() > 0)
			{
				requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
			}
			else
			{
				startWaitThread();
			}
		}
		else
		{
			startWaitThread();
		}
	}

	private void startWaitThread()
	{

		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				startActivity(new Intent(SplashScreenActivity.this, BaseActivity.class));
				SplashScreenActivity.this.finish();
			}
		}, 3000);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		switch (requestCode)
		{
			case ALL_PERMISSIONS_RESULT:
				for (String perm : permissionsToRequest)
				{
					if (!hasPermission(perm))
					{
						permissionsRejected.add(perm);
					}
				}

				if (permissionsRejected.size() > 0)
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
					{
						if (shouldShowRequestPermissionRationale(permissionsRejected.get(0)))
						{
							new AlertDialog.Builder(SplashScreenActivity.this).
																				  setMessage(getResources().getString(R.string.allow_permission_message_splash)).
																				  setPositiveButton("OK", new DialogInterface.OnClickListener()
																				  {
																					  @Override
																					  public void onClick(DialogInterface dialogInterface, int i)
																					  {
																						  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
																						  {
																							  requestPermissions(permissionsRejected.
																																		toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
																						  }
																					  }
																				  }).setNegativeButton("Cancel", null).create().show();

							return;
						}
					}
				}
				else
				{
					startWaitThread();
				}

				break;
		}
	}


	private boolean hasPermission(String permission)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
		}

		return true;
	}

	private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions)
	{
		ArrayList<String> result = new ArrayList<>();

		for (String perm : wantedPermissions)
		{
			if (!hasPermission(perm))
			{
				result.add(perm);
			}
		}

		return result;
	}
}
