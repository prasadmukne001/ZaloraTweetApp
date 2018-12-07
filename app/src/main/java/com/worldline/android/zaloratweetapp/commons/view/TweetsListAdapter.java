package com.worldline.android.zaloratweetapp.commons.view;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.worldline.android.zaloratweetapp.R;
import com.worldline.android.zaloratweetapp.commons.model.TweetModel;
import com.worldline.android.zaloratweetapp.commons.services.database.SQLiteDatabaseManager;
import com.worldline.android.zaloratweetapp.modules.tweet_view.TweetViewFragment;
import com.worldline.android.zaloratweetapp.utility.LoginMetaDataUtility;
import com.worldline.android.zaloratweetapp.utility.ZaloraTweetUtility;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TweetsListAdapter extends RecyclerView.Adapter<TweetsListAdapter.MyViewHolder>
{

	private List<TweetModel> tweetsModelList;
	private Context context;
	private FragmentManager fragmentManager;
	private ActionReader actionReader;

	public TweetsListAdapter(Context context, FragmentManager fragmentManager, List<TweetModel> tweetsModelList, ActionReader actionReader)
	{
		this.tweetsModelList = tweetsModelList;
		this.context = context;
		this.fragmentManager = fragmentManager;
		this.actionReader = actionReader;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder
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
		@InjectView(R.id.reTweetCount)
		TextView reTweetCount;
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

		public MyViewHolder(View view)
		{
			super(view);
			ButterKnife.inject(this, view);
		}
	}

	@Override
	public TweetsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tweet_view, parent, false);

		return new TweetsListAdapter.MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final TweetsListAdapter.MyViewHolder holder, final int position)
	{
		final TweetModel tweetModel = tweetsModelList.get(position);
		holder.userNameTextView.setText(tweetModel.getUsername());
		holder.tweeterUsernameTextView.setText(tweetModel.getTweeterUsername());
		holder.tweeterMessage.setText(tweetModel.getTweeterMessage());
		holder.timeElapsedTextView.setText(" . " + ZaloraTweetUtility.getElapsedTiming(Long.parseLong(tweetModel.getTimestamp())));
		holder.tweeterCommentsCount.setText("" + tweetModel.getComments().size());

		if (Integer.parseInt(tweetModel.getLikesCount()) > 0)
		{
			holder.tweeterLikesCount.setText("" + tweetModel.getLikesCount());
			holder.likeTweetImageView.setImageResource(R.drawable.ic_vector_heart);
			holder.likeTweetImageView.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
		}
		else
		{
			holder.tweeterLikesCount.setText("");
			holder.likeTweetImageView.setImageResource(R.drawable.ic_vector_heart_stroke);
			holder.likeTweetImageView.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);


		}
		if (tweetModel.getReTweetCount() > 0)
		{
			holder.reTweetCount.setText("" + tweetModel.getReTweetCount());
			holder.replyTweetImageView.setImageResource(R.drawable.ic_vector_retweet);
			holder.replyTweetImageView.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
		}
		else
		{
			holder.reTweetCount.setText("");
			holder.replyTweetImageView.setImageResource(R.drawable.ic_vector_retweet_stroke);
			holder.replyTweetImageView.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);


		}
		holder.moreOptionsTweetImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_tweet_actions, null);
				final BottomSheetDialog dialog = new BottomSheetDialog(context);
				dialog.setContentView(view1);
				view1.findViewById(R.id.copyLinkLinearLayout).setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Toast.makeText(context, "Link Copied to Clipboard", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});
				view1.findViewById(R.id.pinProfileLinearLayout).setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Toast.makeText(context, "Pinned to Profile", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});
				view1.findViewById(R.id.deleteTweetLinearLayout).setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View view)
					{
						dialog.dismiss();
						new AlertDialog.Builder(context).setIcon(null).setTitle("Delete").setCancelable(false).setMessage("Do you want to delete this Tweet?").setPositiveButton("Yes", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(final DialogInterface dialog1, int which)
							{
								dialog1.dismiss();
								deleteDataInDatabase(tweetModel);
							}

						}).setNegativeButton("No", null).show();

					}
				});

				dialog.show();
			}
		});

		holder.likeTweetImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (LoginMetaDataUtility.getLoggedInUserData().getDefaultUserData().getUsername().equals("Prasad Mukne") && Integer.parseInt(tweetModel.getLikesCount()) == 0)
				{
					try
					{
						tweetModel.setLikesCount("" + (Integer.parseInt(tweetModel.getLikesCount()) + 1));
						holder.tweeterLikesCount.setText("" + (Integer.parseInt(tweetModel.getLikesCount())));
						//holder.likeTweetImageView.setImageResource(R.drawable.ic_vector_heart);
						//holder.likeTweetImageView.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

						holder.likeTweetImageView.setImageResource(android.R.color.transparent);
						holder.likeTweetImageView.setBackgroundResource(R.drawable.heart);
						final AnimationDrawable anim = (AnimationDrawable) holder.likeTweetImageView.getBackground();
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
									holder.likeTweetImageView.setBackgroundResource(android.R.color.transparent);
									holder.likeTweetImageView.setImageResource(R.drawable.ic_vector_heart);
									holder.likeTweetImageView.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						},duration);

						updateDataInDatabase(tweetModel);
					}
					catch (NumberFormatException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		holder.replyTweetImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (LoginMetaDataUtility.getLoggedInUserData().getDefaultUserData().getUsername().equals("Prasad Mukne") && tweetModel.getReTweetCount() == 0)
				{
					tweetModel.setReTweetCount(tweetModel.getReTweetCount() + 1);
					holder.reTweetCount.setText("" + (tweetModel.getReTweetCount()));
					holder.replyTweetImageView.setImageResource(R.drawable.ic_vector_retweet);
					holder.replyTweetImageView.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
					updateDataInDatabase(tweetModel);
				}

			}
		});
		holder.shareTweetImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_share_tweet_actions, null);
				BottomSheetDialog dialog = new BottomSheetDialog(context);
				dialog.setContentView(view1);
				dialog.show();
			}
		});

		holder.itemView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String TAG = "TweetViewFragment";
				Fragment fragment = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag(TAG);
				if (null == fragment)
				{
					fragment = new TweetViewFragment();
					Bundle bundle = new Bundle();
					bundle.putSerializable("TweetModel", tweetModel);
					fragment.setArguments(bundle);
				}
				FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
				if (!fragment.isAdded())
				{
					fragmentTransaction.replace(R.id.mainFrameLayout, fragment, TAG).addToBackStack(null).commit();
				}
			}
		});

	}

	private void deleteDataInDatabase(final TweetModel tweetModel)
	{
		WeakReference<AsyncTask> asyncTask=new WeakReference(new DataDeletionAsyncTask(context,actionReader,this,tweetModel));
		asyncTask.get().execute();
	}



	private void updateDataInDatabase(final TweetModel tweetModel)
	{
		WeakReference<AsyncTask> asyncTask=new WeakReference(new DataUpdationAsyncTask(context,tweetModel));
		asyncTask.get().execute();

	}

	@Override
	public int getItemCount()
	{
		return tweetsModelList.size();
	}

	public interface ActionReader
	{

		public void updateData(float data);
	}



	static class DataDeletionAsyncTask extends AsyncTask<Object, Void, String>
	{
		private Context context;
		private  ActionReader actionReader;
		private Adapter adapter;
		private TweetModel tweetModel;

		DataDeletionAsyncTask(Context context,ActionReader actionReader,Adapter adapter,TweetModel tweetModel)
		{
			this.context=context;
			this.actionReader=actionReader;
			this.adapter=adapter;
			this.tweetModel=tweetModel;
		}
		@Override
		protected String doInBackground(Object... voids)
		{
			boolean isDeleted = SQLiteDatabaseManager.getInstance(context).delete(SQLiteDatabaseManager.TWEET_TABLE, SQLiteDatabaseManager.TIMESTAMP + " = " + tweetModel.getTimestamp(), null);
			String str = "false";
			if (isDeleted)
			{
				str = "true";
			}
			return str;
		}

		@Override
		protected void onPostExecute(String str)
		{
			super.onPostExecute(str);
			if (str.equals("true"))
			{
				//adapter.notifyDataSetChanged();
				actionReader.updateData(0);
			}
		}
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

			try
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
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
	}
}
