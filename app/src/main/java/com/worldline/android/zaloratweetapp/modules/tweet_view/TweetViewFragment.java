package com.worldline.android.zaloratweetapp.modules.tweet_view;


import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.worldline.android.zaloratweetapp.R;
import com.worldline.android.zaloratweetapp.commons.model.TweetModel;
import com.worldline.android.zaloratweetapp.commons.services.database.SQLiteDatabaseManager;
import com.worldline.android.zaloratweetapp.modules.base.BaseActivity;
import com.worldline.android.zaloratweetapp.utility.LoginMetaDataUtility;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TweetViewFragment extends Fragment implements OnClickListener
{

	@InjectView(R.id.userNameTextView)
	TextView userNameTextView;
	@InjectView(R.id.timeElapsedTextView)
	TextView timeElapsedTextView;
	@InjectView(R.id.tweeterUsernameTextView)
	TextView tweeterUsernameTextView;
	@InjectView(R.id.tweeterCommentsCount)
	TextView tweeterCommentsCount;
	@InjectView(R.id.tweeterLikesCount)
	TextView tweeterLikesCount;
	@InjectView(R.id.tweeterMessage)
	TextView tweeterMessage;
	@InjectView(R.id.moreOptionsTweetImageView)
	ImageView moreOptionsTweetImageView;
	@InjectView(R.id.shareTweetImageView)
	ImageView shareTweetImageView;
	@InjectView(R.id.likeTweetImageView)
	ImageView likeTweetImageView;
	@InjectView(R.id.replyTweetImageView)
	ImageView replyTweetImageView;
	@InjectView(R.id.commentTweetImageView)
	ImageView commentTweetImageView;
	@InjectView(R.id.reTweetCount)
	TextView reTweetCount;
	@InjectView(R.id.backImageView)
	ImageView backImageView;


	private TweetModel tweetModel;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_tweet_view, container, false);
		ButterKnife.inject(this, view);

		try
		{
			initialiseUIAndListeners();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return view;
	}

	private void initialiseUIAndListeners()
	{
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		((AppCompatActivity) getActivity()).getSupportActionBar().hide();
		((BaseActivity) getActivity()).hideBottomNavigation();
		backImageView.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);

		tweetModel = (TweetModel) getArguments().get("TweetModel");
		userNameTextView.setText(tweetModel.getUsername());
		tweeterUsernameTextView.setText(tweetModel.getTweeterUsername());
		tweeterMessage.setText(tweetModel.getTweeterMessage());
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a . dd MMM yyyy");
		Date date = new Date(Long.parseLong(tweetModel.getTimestamp()));
		timeElapsedTextView.setText(sdf.format(date));
		tweeterCommentsCount.setText("" + tweetModel.getComments().size());
		tweeterLikesCount.setText("" + tweetModel.getLikesCount());
		reTweetCount.setText("" + tweetModel.getReTweetCount());

		if (Integer.parseInt(tweetModel.getLikesCount()) > 0)
		{
			tweeterLikesCount.setText("" + tweetModel.getLikesCount());
			likeTweetImageView.setImageResource(R.drawable.ic_vector_heart);
			likeTweetImageView.setColorFilter(ContextCompat.getColor(getActivity(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
		}
		else
		{
			tweeterLikesCount.setText("");
			likeTweetImageView.setImageResource(R.drawable.ic_vector_heart_stroke);
			likeTweetImageView.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);


		}
		if (tweetModel.getReTweetCount() > 0)
		{
			reTweetCount.setText("" + tweetModel.getReTweetCount());
			replyTweetImageView.setImageResource(R.drawable.ic_vector_retweet);
			replyTweetImageView.setColorFilter(ContextCompat.getColor(getActivity(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
		}
		else
		{
			reTweetCount.setText("");
			replyTweetImageView.setImageResource(R.drawable.ic_vector_retweet_stroke);
		}

		likeTweetImageView.setOnClickListener(this);
		replyTweetImageView.setOnClickListener(this);
		backImageView.setOnClickListener(this);
		shareTweetImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		try
		{
			switch (view.getId())
			{
				case R.id.replyTweetImageView:
					if (LoginMetaDataUtility.getLoggedInUserData().getDefaultUserData().getUsername().equals("Prasad Mukne") && tweetModel.getReTweetCount() == 0)
					{
						tweetModel.setReTweetCount(tweetModel.getReTweetCount() + 1);
						reTweetCount.setText("" + tweetModel.getReTweetCount());
						replyTweetImageView.setImageResource(R.drawable.ic_vector_retweet);
						replyTweetImageView.setColorFilter(ContextCompat.getColor(getActivity(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
						updateDataInDatabase(tweetModel);
					}

					break;
				case R.id.likeTweetImageView:
					try
					{
						if (LoginMetaDataUtility.getLoggedInUserData().getDefaultUserData().getUsername().equals("Prasad Mukne") && Integer.parseInt(tweetModel.getLikesCount()) == 0)
						{
							tweetModel.setLikesCount("" + (Integer.parseInt(tweetModel.getLikesCount()) + 1));
							tweeterLikesCount.setText("" + (Integer.parseInt(tweetModel.getLikesCount())));
							//likeTweetImageView.setImageResource(R.drawable.ic_vector_heart);
							//likeTweetImageView.setColorFilter(ContextCompat.getColor(getActivity(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
							likeTweetImageView.getLayoutParams().height=(likeTweetImageView.getLayoutParams().height*2);
							likeTweetImageView.getLayoutParams().width=(likeTweetImageView.getLayoutParams().width*2);

							likeTweetImageView.setImageResource(android.R.color.transparent);
							likeTweetImageView.setBackgroundResource(R.drawable.heart);
							AnimationDrawable anim = (AnimationDrawable) likeTweetImageView.getBackground();
							int duration = 0;
							for(int i = 0; i < anim.getNumberOfFrames(); i++){
								duration += anim.getDuration(i);
							}
							anim.start();
							Handler handler=new Handler();
							handler.postDelayed(new Runnable()
							{
								@Override
								public void run()
								{
									try
									{
										likeTweetImageView.getLayoutParams().height=(likeTweetImageView.getLayoutParams().height/2);
										likeTweetImageView.getLayoutParams().width=(likeTweetImageView.getLayoutParams().width/2);
										likeTweetImageView.setBackgroundResource(android.R.color.transparent);
										likeTweetImageView.setImageResource(R.drawable.ic_vector_heart);
										likeTweetImageView.setColorFilter(ContextCompat.getColor(getActivity(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
								}
							},duration);
							updateDataInDatabase(tweetModel);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					break;
				case R.id.backImageView:
					getFragmentManager().popBackStack();
					break;
				case R.id.shareTweetImageView:
					View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_share_tweet_actions, null);
					BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
					dialog.setContentView(view1);
					dialog.show();
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		((AppCompatActivity) getActivity()).getSupportActionBar().show();
		((BaseActivity) getActivity()).showBottomNavigation();
	}

	private void updateDataInDatabase(final TweetModel tweetModel)
	{
		WeakReference<AsyncTask> asyncTask=new WeakReference(new DataUpdationAsyncTask(getActivity(),tweetModel));
		asyncTask.get().execute();
	}


	static class DataUpdationAsyncTask extends AsyncTask<Object, Void, Void>
	{
		private Context context;
		private TweetModel tweetModel;

		DataUpdationAsyncTask(Context context,TweetModel tweetModel)
		{
			this.context=context;
			this.tweetModel=tweetModel;
		}
		@Override
		protected Void doInBackground(Object... voids)
		{
			ContentValues contentValues = new ContentValues();
			contentValues.put(SQLiteDatabaseManager.TWEET_COLUMN, new Gson().toJson(tweetModel));
			boolean isUpdated = SQLiteDatabaseManager.getInstance(context).update(SQLiteDatabaseManager.TWEET_TABLE, contentValues, SQLiteDatabaseManager.TIMESTAMP + " = " + tweetModel.getTimestamp());
			String str = "false";
			if (isUpdated)
			{
				str = "true";
			}
			//return str;
			return null;
		}
	}

}
