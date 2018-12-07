package com.worldline.android.zaloratweetapp.utility;

import java.util.LinkedList;

public class MessageSplittingUtility
{

	private static int maxActualCharsForMessage = 50;//50 (initializers of each split string) just for starting calculation at first
	private static int maxCharsAllowedForMessage = 50;//max chars allowed in a message

	public static String[] getSplitMessage(String message)
	{
		maxActualCharsForMessage = 50;
		maxCharsAllowedForMessage = 50;
		String[] messageArray = null;
		try
		{
			message = message.trim();
			int messageLength = message.length();
			String str = message.replaceAll(" ", "");
			if (str.length() == 0 || str.length() > 2147483646)// Check if all characters are blank spaces
			{
				return null;
			}
			if (messageLength <= maxCharsAllowedForMessage)// Check if message length is less than or equal to limit
			{
				messageArray = new String[]{message};
				return messageArray;
			}
			if (-1 == message.indexOf(' ') || message.indexOf(' ') >= 50)//need to test && use index of in code
			{
				return null;
			}
			LinkedList<String> stringArrayList = new LinkedList<>();

			if (messageLength > maxCharsAllowedForMessage) //If message length is greater than limit then start splitting
			{
				int totalNumberOfParts = message.length() / maxActualCharsForMessage;

				if ((message.length() % maxActualCharsForMessage) > 1)
				{
					totalNumberOfParts = totalNumberOfParts + 1;
				}
				maxActualCharsForMessage = maxActualCharsForMessage - ((("" + totalNumberOfParts).length()) + 2);

				int start = 0;
				int end = 0;
				int partValue = 0;
				int prePartValue = ("" + 0).length();
				maxActualCharsForMessage = maxActualCharsForMessage - prePartValue;
				while (end < messageLength)
				{
					if (prePartValue < ("" + partValue).length())
					{
						maxActualCharsForMessage = maxActualCharsForMessage - ((("" + partValue).length() - prePartValue));
						prePartValue = ("" + partValue).length();
					}
					end = start + maxActualCharsForMessage;
					if (end >= messageLength)
					{
						stringArrayList.add(message.substring(start, messageLength));
						break;
					}
					else
					{

						/*String strToRead=message.substring(start, end);
						 int val=strToRead.lastIndexOf(' ');
						 if(val!=-1)
						 {
							 end = val;
						 }
						 else
						 {
							 return null;
						 }*/
						for (int i = end; i > 0; i--)
						{
							if (message.charAt(i) == ' ')
							{
								end = i;
								break;
							}
							if (i == 1)
							{
								return null;
							}
						}
						stringArrayList.add(message.substring(start, end));
						start = end + 1;
					}
					partValue++;
				}
				messageArray = new String[stringArrayList.size()];
				for (int i = 0; i < stringArrayList.size(); i++)
				{
					messageArray[i] = (i + 1) + "/" + stringArrayList.size() + " " + stringArrayList.get(i);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return messageArray;
	}
}
