package com.worldline.android.zaloratweetapp.commons.model;

import java.io.Serializable;
import java.util.LinkedList;

public class TweetModel implements Serializable
{

	private long tweetId;
	private String username;
	private String tweeterUsername;
	private String tweeterMessage;
	private String timestamp;
	private LinkedList<String> comments;
	private String likesCount;
	private boolean isMultipartMessage;
	private int messageIndex;
	private int reTweetCount;

	public int getReTweetCount()
	{
		return reTweetCount;
	}

	public void setReTweetCount(int reTweetCount)
	{
		this.reTweetCount = reTweetCount;
	}

	public boolean isMultipartMessage()
	{
		return isMultipartMessage;
	}

	public void setMultipartMessage(boolean multipartMessage)
	{
		isMultipartMessage = multipartMessage;
	}

	public int getMessageIndex()
	{
		return messageIndex;
	}

	public void setMessageIndex(int messageIndex)
	{
		this.messageIndex = messageIndex;
	}


	public long getTweetId()
	{
		return tweetId;
	}

	public void setTweetId(long tweetId)
	{
		this.tweetId = tweetId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getTweeterUsername()
	{
		return tweeterUsername;
	}

	public void setTweeterUsername(String tweeterUsername)
	{
		this.tweeterUsername = tweeterUsername;
	}

	public String getTweeterMessage()
	{
		return tweeterMessage;
	}

	public void setTweeterMessage(String tweeterMessage)
	{
		this.tweeterMessage = tweeterMessage;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public LinkedList<String> getComments()
	{
		return comments;
	}

	public void setComments(LinkedList<String> comments)
	{
		this.comments = comments;
	}

	public String getLikesCount()
	{
		return likesCount;
	}

	public void setLikesCount(String likesCount)
	{
		this.likesCount = likesCount;
	}


}
