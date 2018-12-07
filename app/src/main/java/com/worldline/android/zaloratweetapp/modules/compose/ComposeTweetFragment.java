package com.worldline.android.zaloratweetapp.modules.compose;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.worldline.android.zaloratweetapp.R;
import com.worldline.android.zaloratweetapp.commons.model.TweetModel;
import com.worldline.android.zaloratweetapp.commons.services.database.ApplicationService;
import com.worldline.android.zaloratweetapp.commons.services.database.DatabaseService;
import com.worldline.android.zaloratweetapp.commons.services.database.SQLiteDatabaseManager;
import com.worldline.android.zaloratweetapp.modules.base.BaseActivity;
import com.worldline.android.zaloratweetapp.utility.LoginMetaDataUtility;
import com.worldline.android.zaloratweetapp.utility.MessageSplittingUtility;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeTweetFragment extends Fragment implements OnClickListener
{

	@InjectView(R.id.closeFragmentImageView)
	public ImageView closeFragmentImageView;

	@InjectView(R.id.draftButton)
	public TextView draftButton;

	@InjectView(R.id.tweetButton)
	public Button tweetButton;

	@InjectView(R.id.tweetMessageEditText)
	public EditText tweetMessageEditText;

	@InjectView(R.id.galleryImageView)
	public ImageView galleryImageView;

	@InjectView(R.id.gifImageView)
	public ImageView gifImageView;

	@InjectView(R.id.pollImageView)
	public ImageView pollImageView;

	@InjectView(R.id.locationImageView)
	public ImageView locationImageView;

	@InjectView(R.id.addAnotherTweetImageView)
	public ImageView addAnotherTweetImageView;

	@InjectView(R.id.circularProgressBar)
	public CircularProgressBar circularProgressBar;



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_compose_tweet, container, false);
		ButterKnife.inject(this, view);
		try
		{
			initialiseUI();

			showInputMethod();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return view;
	}

	private void initialiseUI()
	{
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		//getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		((AppCompatActivity) getActivity()).getSupportActionBar().hide();
		((BaseActivity) getActivity()).hideBottomNavigation();
		tweetButton.setOnClickListener(this);
		tweetMessageEditText.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				int mod=charSequence.length()/50;
				float progress=(((charSequence.length()-(50*(mod)))*100)/50);
				circularProgressBar.setProgress(progress);
				if(charSequence.length()>0)
				{
					tweetButton.setEnabled(true);
					tweetButton.setClickable(true);
				}
				else
				{
					//Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.empty_tweet), Toast.LENGTH_SHORT).show();
					tweetButton.setEnabled(false);
					tweetButton.setClickable(false);
				}
			}

			@Override
			public void afterTextChanged(Editable editable)
			{

			}
		});

		closeFragmentImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		((AppCompatActivity) getActivity()).getSupportActionBar().show();
		((BaseActivity) getActivity()).showBottomNavigation();
		hideInputMethod();

	}

	public void showInputMethod()
	{
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public void hideInputMethod()
	{
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		View view = getActivity().getCurrentFocus();
		if (view == null)
		{
			view = new View(getActivity());
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.tweetButton:
				tweet();
				break;
		}
	}

	public void tweet()
	{
		boolean isMultipartMessage = false;
		String[] messageArray = MessageSplittingUtility.getSplitMessage(tweetMessageEditText.getText().toString());
		if (null != messageArray)
		{
			if (messageArray.length > 1)
			{
				isMultipartMessage = true;
			}
			long time = 0L;
			for (int i = 0; i < messageArray.length; i++)
			{
				if (i == 0)
				{
					time = Calendar.getInstance().getTimeInMillis();
				}
				String str = messageArray[i];
				TweetModel tweetModel = new TweetModel();
				tweetModel.setLikesCount("" + 0);
				tweetModel.setTimestamp("" + time);
				tweetModel.setTweeterMessage(str);
				tweetModel.setReTweetCount(0);
				tweetModel.setComments(new LinkedList<String>());
				tweetModel.setTweetId(Calendar.getInstance().getTimeInMillis());
				tweetModel.setUsername(LoginMetaDataUtility.getLoggedInUserData().getDefaultUserData().getUsername());
				tweetModel.setMultipartMessage(isMultipartMessage);
				if (isMultipartMessage)
				{
					tweetModel.setMessageIndex(i);
				}
				tweetModel.setTweeterUsername(LoginMetaDataUtility.getLoggedInUserData().getDefaultUserData().getUsername());

				addTweetToDatabase(tweetModel);
			}
			getActivity().getSupportFragmentManager().popBackStack();
		}
		else
		{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.invalid_tweet), Toast.LENGTH_SHORT).show();
		}

	}

	private void addTweetToDatabase(final TweetModel tweetModel)
	{
		WeakReference<AsyncTask> asyncTaskWeakReference = new WeakReference<AsyncTask>(new DatabaseService(getActivity(), new ApplicationService()
		{
			@Override
			public void onStart()
			{

			}

			@Override
			public void onStop(Object response)
			{

			}
		})
		{
			@Override
			protected Object doInBackground(Object... strings)
			{
				Gson gson = new Gson();
				ContentValues contentValues = new ContentValues();
				contentValues.put(SQLiteDatabaseManager.TIMESTAMP, tweetModel.getTimestamp());
				contentValues.put(SQLiteDatabaseManager.TWEET_COLUMN, gson.toJson(tweetModel));
				SQLiteDatabaseManager.getInstance(getActivity()).insert(SQLiteDatabaseManager.TWEET_TABLE, contentValues);
				return null;
			}

			@Override
			protected void onCancelled()
			{

			}
		});
		asyncTaskWeakReference.get().execute();
	}

}
