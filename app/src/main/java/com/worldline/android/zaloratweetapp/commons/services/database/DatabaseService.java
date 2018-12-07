package com.worldline.android.zaloratweetapp.commons.services.database;

import android.content.Context;
import android.os.AsyncTask;
import com.worldline.android.zaloratweetapp.commons.view.ProgressBarHandler;


/**
 * Created by prasad.mukne on 11/28/2018.
 */

public abstract class DatabaseService extends AsyncTask<Object, Object, Object>
{

	private Context context;
	private ProgressBarHandler mProgressBarHandler;
	private ApplicationService applicationService;

	public DatabaseService(Context context, ApplicationService applicationService)
	{
		this.context = context;
		this.applicationService = applicationService;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		mProgressBarHandler = new ProgressBarHandler(context);
		mProgressBarHandler.show();
		applicationService.onStart();
	}

	@Override
	protected abstract Object doInBackground(Object... strings);

	@Override
	protected void onPostExecute(Object s)
	{
		super.onPostExecute(s);
		mProgressBarHandler.hide();
		applicationService.onStop(s);
	}

	@Override
	protected abstract void onCancelled();
}
