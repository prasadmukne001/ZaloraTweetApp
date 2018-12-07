package com.worldline.android.zaloratweetapp;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import android.content.Context;
import com.worldline.android.zaloratweetapp.utility.MessageSplittingUtility;
import org.junit.Test;
import org.mockito.Mock;

public class MessageSpittingUnitTest
{

	private static final String TEST_STRING = "Zalora Twit App";
	private static final String input1 = "I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.";
	private static final String[] expectedOutput1 = new String[]{"1/2 I can't believe Tweeter now supports chunking", "2/2 my messages, so I don't have to do it myself."};

	private static final String input2 = "I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.";
	private static final String[] expectedOutput2 = new String[]{"1/15 I can't believe Tweeter now supports chunking", "2/15 my messages, so I don't have to do it","3/15 myself.I can't believe Tweeter now supports","4/15 chunking my messages, so I don't have to do","5/15 it myself.I can't believe Tweeter now","6/15 supports chunking my messages, so I don't","7/15 have to do it myself.I can't believe Tweeter","8/15 now supports chunking my messages, so I don't","9/15 have to do it myself.I can't believe Tweeter","10/15 now supports chunking my messages, so I don't","11/15 have to do it myself.I can't believe Tweeter","12/15 now supports chunking my messages, so I","13/15 don't have to do it myself.I can't believe","14/15 Tweeter now supports chunking my messages,","15/15 so I don't have to do it myself."};

	private static final String input3 = "TweeterTweeterTweeterTweeterTweeterTweeterTweeterTw";
	private static final String[] expectedOutput3 = null;

	private static final String input4 = "";
	private static final String[] expectedOutput4 = null;

	private static final String input5 = "                                                                                                   ";
	private static final String[] expectedOutput5 = null;

	private static final String input6 = "12345678901234567890123456789012345678901234567890";
	private static final String[] expectedOutput6 = new String[]{"12345678901234567890123456789012345678901234567890"};

	private static final String input7 = "123456789012345678901234567890123456789012345678901";
	private static final String[] expectedOutput7 =  null;

	private static final String input8 = "123456789012345678901234567890123456789012345678";
	private static final String[] expectedOutput8 = new String[]{"123456789012345678901234567890123456789012345678"};;

	private static final String input9 = "123456789012345678901234567890123456789012345 678901";
	private static final String[] expectedOutput9 = new String[]{"1/2 123456789012345678901234567890123456789012345","2/2 678901"};

	private static final String input10 = "1234567890123456 7890123456 78901234 56789012345 678901";
	private static final String[] expectedOutput10 = new String[]{"1/2 1234567890123456 7890123456 78901234","2/2 56789012345 678901"};

	@Mock
	Context mMockContext;

	@Test
	public void testWithProvidedString()
	{
		String[] result1 = MessageSplittingUtility.getSplitMessage(input1);

		assertThat(result1, is(expectedOutput1));
	}

	@Test
	public void testWithLongTextToTestPrePostValuesConsideration()
	{
		String[] result2 = MessageSplittingUtility.getSplitMessage(input2);
		assertThat(result2, is(expectedOutput2));
	}

	@Test
	public void testWithLengthGreaterThanLimitWithoutSpace()
	{
		String[] result3 = MessageSplittingUtility.getSplitMessage(input3);

		assertThat(result3, is(expectedOutput3));
	}

	@Test
	public void testWithEmptyString()
	{

		String[] result4 = MessageSplittingUtility.getSplitMessage(input4);

		assertThat(result4, is(expectedOutput4));
	}

	@Test
	public void testWithBlankSpaces()
	{
		String[] result5 = MessageSplittingUtility.getSplitMessage(input5);

		assertThat(result5, is(expectedOutput5));
	}

	@Test
	public void testWithSingleTextWithinTheLimit()
	{

		String[] result6 = MessageSplittingUtility.getSplitMessage(input6);

		assertThat(result6, is(expectedOutput6));
	}

	@Test
	public void testWithSingleTextExceedingLimit()
	{
		String[] result7 = MessageSplittingUtility.getSplitMessage(input7);

		assertThat(result7, is(expectedOutput7));
	}

	@Test
	public void testWithSingleTextLessThanLimit()
	{
		String[] result8 = MessageSplittingUtility.getSplitMessage(input8);

		assertThat(result8, is(expectedOutput8));
	}

	@Test
	public void testWithSpaceAtPointer()
	{
		String[] result9 = MessageSplittingUtility.getSplitMessage(input9);

		assertThat(result9, is(expectedOutput9));
	}

	@Test
	public void testWithRandomLimit()
	{
		String[] result10 = MessageSplittingUtility.getSplitMessage(input10);

		assertThat(result10, is(expectedOutput10));
	}
}



