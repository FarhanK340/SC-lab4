/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.Collections;
import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
	
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testGetTimespanEmptyList() {
    	Timespan timespan = Extract.getTimespan(Collections.emptyList());
    	
    	assertNull("expected null for empty list", timespan);
    }
    

    @Test
    public void testGetTimespanSingleTweet() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
    	
    	assertEquals("expected start", d1, timespan.getStart());
    	assertEquals("expected end", d1, timespan.getEnd());
    	
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanSameTimespan() {
    	Tweet tweet3 = new Tweet(3, "alyssa", "same time tweet", d1);
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersSingleMention() {
    	Tweet tweetwithMention = new Tweet(1,"alyssa", "Hello @user",d1 );
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetwithMention));
    	
    	assertTrue("expected mention of user", mentionedUsers.contains("user"));
    	assertEquals("expected one mention", 1, mentionedUsers.size());
    }
    
    @Test
    public void testGetMentionedUsersMultipleMentions() {
    	Tweet tweetwithMention = new Tweet(1,"alyssa", "Hello @user1 and @user2",d1 );
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetwithMention));
    	
    	assertTrue("expected mention of user", mentionedUsers.contains("user1"));
    	assertTrue("expected mention of user", mentionedUsers.contains("user2"));
    	assertEquals("expected two mention", 2, mentionedUsers.size());
    }
    
    @Test
    public void testGetMentionedUsersInvalidMentions() {
    	Tweet invalidTweet = new Tweet(1, "alyssa", "invalidmention@gmail.com is not a valid mention", d1);
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(invalidTweet));
    	
    	assertTrue("expected no mentions", mentionedUsers.isEmpty());
    	
    }
    
    @Test
    public void testGetMentionedUsersCaseInsensitive() {
        Tweet caseInsensitiveTweet = new Tweet(1, "alyssa", "Hello @USER", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(caseInsensitiveTweet));
        
        assertTrue("expected mention of user (case-insensitive)", mentionedUsers.contains("user"));
    }

    @Test
    public void testGetMentionedUsersDuplicateMentions() {
        Tweet duplicateMentionTweet = new Tweet(1, "alyssa", "Hello @user @user", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(duplicateMentionTweet));
        
        assertTrue("expected mention of user", mentionedUsers.contains("user"));
        assertEquals("expected one mention", 1, mentionedUsers.size());
    }

    
    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
