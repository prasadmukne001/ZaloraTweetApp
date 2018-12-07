package com.worldline.android.zaloratweetapp.modules.home;


import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.worldline.android.zaloratweetapp.R;
import com.worldline.android.zaloratweetapp.commons.model.TweetModel;
import com.worldline.android.zaloratweetapp.commons.services.database.ApplicationService;
import com.worldline.android.zaloratweetapp.commons.services.database.DatabaseService;
import com.worldline.android.zaloratweetapp.commons.services.database.SQLiteDatabaseManager;
import com.worldline.android.zaloratweetapp.commons.view.TweetsListAdapter;
import com.worldline.android.zaloratweetapp.commons.view.TweetsListAdapter.ActionReader;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ActionReader
{

	private RecyclerView recyclerView;
	private TextView noDataTextView;
	private LinkedList<TweetModel> tweetModelLinkedList;

	private TaskCallbacks mCallbacks;
	private WeakReference<AsyncTask> mTask;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);

	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		mCallbacks = (TaskCallbacks) activity;
	}


	@Override
	public void onDetach()
	{
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		try
		{
			initialiseUI(view);

			getDataFromDatabase();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return view;
	}

	private void initialiseUI(View view)
	{
		tweetModelLinkedList = new LinkedList<>();
		noDataTextView = (TextView) view.findViewById(R.id.noDataTextView);
		recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		if (tweetModelLinkedList.size() > 0)
		{
			noDataTextView.setVisibility(View.INVISIBLE);
			recyclerView.setVisibility(View.VISIBLE);
		}
		else
		{
			noDataTextView.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.INVISIBLE);
		}
		tweetModelLinkedList = new LinkedList<>();
		TweetsListAdapter tweetsListAdapter = new TweetsListAdapter(getActivity(), getFragmentManager(), tweetModelLinkedList, this);

		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
		itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
		recyclerView.addItemDecoration(itemDecorator);
		recyclerView.setAdapter(tweetsListAdapter);
	}

	private void getDataFromDatabase()
	{

		mTask = new WeakReference(new DatabaseService(getActivity(), new ApplicationService()
		{
			@Override
			public void onStart()
			{

			}

			@Override
			public void onStop(Object response)
			{
				if (null != mCallbacks)
				{
					tweetModelLinkedList = new LinkedList<>();
					tweetModelLinkedList.addAll((LinkedList<TweetModel>) response);
					updateList();
				}

			}
		})
		{
			@Override
			protected Object doInBackground(Object... strings)
			{
				LinkedList<TweetModel> tweetModelLinkedList = new LinkedList<>();
				Cursor cursor = SQLiteDatabaseManager.getInstance(getActivity()).search("Select " + SQLiteDatabaseManager.TWEET_COLUMN + " from " + SQLiteDatabaseManager.TWEET_TABLE + " order by " + SQLiteDatabaseManager.TIMESTAMP + " desc");
				while (cursor.moveToNext())
				{
					tweetModelLinkedList.add(new Gson().fromJson(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseManager.TWEET_COLUMN)), TweetModel.class));
				}
				return tweetModelLinkedList;
			}

			@Override
			protected void onCancelled()
			{
				if (null != mCallbacks)
				{
					mCallbacks.onCancelled();
				}
			}
		});
		mTask.get().execute();

	}

	@Override
	public void updateData(float data)
	{
		getDataFromDatabase();

		updateList();
	}

	private void updateList()
	{
		if (tweetModelLinkedList.size() > 0)
		{
			noDataTextView.setVisibility(View.INVISIBLE);
			recyclerView.setVisibility(View.VISIBLE);
		}
		else
		{
			noDataTextView.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.INVISIBLE);
		}
		TweetsListAdapter tweetsListAdapter = new TweetsListAdapter(getActivity(), getFragmentManager(), tweetModelLinkedList, this);
		recyclerView.setAdapter(tweetsListAdapter);
	}

	public interface TaskCallbacks
	{
		void onCancelled();
	}
}
