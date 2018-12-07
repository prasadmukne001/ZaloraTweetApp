package com.worldline.android.zaloratweetapp.utility;

import com.worldline.android.zaloratweetapp.commons.model.User;

public class LoginMetaDataUtility
{

	private static LoginMetaDataUtility loginMetaDataUtility;
	private static Object object = new Object();
	private static User user;

	private LoginMetaDataUtility()
	{

	}

	public static LoginMetaDataUtility getLoggedInUserData()
	{
		synchronized (object)
		{
			if (null == loginMetaDataUtility)
			{
				loginMetaDataUtility = new LoginMetaDataUtility();
				user = new User();
				user.setUsername("Prasad Mukne");
				user.setTweeterUsername("@MuknePrasad");
			}
			return loginMetaDataUtility;
		}
	}

	public User getDefaultUserData()
	{
		return user;
	}

}
