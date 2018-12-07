package com.worldline.android.zaloratweetapp.utility;

import java.util.Calendar;

public class ZaloraTweetUtility
{

	public static String getElapsedTiming(long previousTime)
	{
		long currentTime = Calendar.getInstance().getTimeInMillis();
		if ((previousTime / 1000) == (currentTime / 1000))
		{
			return "Just Now";
		}
		else if (previousTime < currentTime)
		{
			long difference = currentTime - previousTime;
			if ((difference / 1000) < 60)
			{
				return (difference / 1000) + "s";
			}
			else if ((difference / 60000) < 60)
			{
				return ((difference / 60000)) + "m";
			}
			else if ((difference / 3600000) < 60)
			{
				return ((difference / 3600000)) + "h";
			}
			else if ((difference / 86400000) < 60)
			{
				return ((difference / 86400000)) + "d";
			}
		}
		return "N/A";
	}
}
