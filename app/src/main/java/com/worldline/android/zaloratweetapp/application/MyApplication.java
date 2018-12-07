package com.worldline.android.zaloratweetapp.application;

import android.app.Application;
import com.worldline.android.zaloratweetapp.commons.services.database.SQLiteDatabaseManager;


public class MyApplication extends Application
{

	private static MyApplication myApplication;

	@Override
	public void onCreate()
	{
		super.onCreate();
		myApplication = this;
		SQLiteDatabaseManager.getInstance(this).createTweetTable();
	}
}
