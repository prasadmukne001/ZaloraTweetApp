package com.worldline.android.zaloratweetapp.commons.view;

/**
 * Created by prasad.mukne on 11/28/2018.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import com.worldline.android.zaloratweetapp.R;


/**
 * Created by ankur on 2/11/16.
 */
public class ProgressBarHandler
{

	//private ProgressDialog mProgressBar;
	private Context mContext;
	private Dialog mProgressBar;

	public ProgressBarHandler(Context context)
	{
		mContext = context;

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context).setOnKeyListener(new DialogInterface.OnKeyListener()
		{
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
				   /* &&
					event.getAction() == KeyEvent.ACTION_UP &&
                    !event.isCanceled()) {
                    //dialog.cancel();
*/
					return true;
				}
				return false;
			}
		});
		View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_progress_bar, null);
		dialogBuilder.setView(dialogView);
		dialogBuilder.setCancelable(true);
		//        RelativeLayout mainLayout = (RelativeLayout) dialogView.findViewById(R.id.mainLayout);
		//        mainLayout.setBackgroundResource(R.drawable.my_progress_one);
		//        AnimationDrawable frameAnimation = (AnimationDrawable)mainLayout.getBackground();
		//        frameAnimation.start();
		mProgressBar = dialogBuilder.create();

		mProgressBar.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
		mProgressBar.show();
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(mProgressBar.getWindow().getAttributes());
		lp.width = 500;
		lp.height = 500;
		mProgressBar.show();
		mProgressBar.getWindow().setAttributes(lp);
		mProgressBar.setCanceledOnTouchOutside(false);
		hide();
	}

	public void show()
	{
		//mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.show();
	}

	public boolean isShow()
	{
		/*if (mProgressBar.isActivated())
        {
            return true;
        }
        else
            return false;*/
		if (mProgressBar.isShowing())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void hide()
	{
		//mProgressBar.setVisibility(View.INVISIBLE);
		mProgressBar.hide();
	}

	public boolean isVisible()
	{
        /*if (mProgressBar.getVisibility() == View.VISIBLE)
        {
            return true;
        }
        return false;*/
		if (mProgressBar.isShowing())
		{
			return true;
		}
		return false;
	}

}

