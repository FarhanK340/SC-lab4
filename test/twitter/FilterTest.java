package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "talking about coding", d3);

    // Partition strategy for writtenBy:
    // - No tweets: Empty list
    // - Multiple tweets by one author
    // - Case-insensitive matching of username
    // - No matching author
    @Test
    public void testWrittenByNoTweets() {
        List<Tweet> result = Filter.writtenBy(Collections.emptyList(), "alyssa");
        assertTrue("expected empty list", result.isEmpty());
    }

    @Test
    public void testWrittenByCaseInsensitive() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ALYSSA");
        assertEquals("expected single tweet", 1, result.size());
        assertTrue("expected tweet1", result.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleTweetsSameAuthor() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        assertEquals("expected two tweets", 2, result.size());
        assertTrue("expected tweet1", result.contains(tweet1));
        assertTrue("expected tweet3", result.contains(tweet3));
    }

    @Test
    public void testWrittenByNoMatchingAuthor() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "unknown");
        assertTrue("expected empty list", result.isEmpty());
    }

    // Partition strategy for inTimespan:
    // - Tweets before timespan
    // - Tweets after timespan
    // - Tweets within timespan
    // - Tweets partially overlapping with timespan
    @Test
    public void testInTimespanTweetsBeforeTimespan() {
        Instant start = Instant.parse("2016-02-17T13:00:00Z");
        Instant end = Instant.parse("2016-02-17T14:00:00Z");

        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(start, end));
        assertTrue("expected empty list", result.isEmpty());
    }

    @Test
    public void testInTimespanTweetsAfterTimespan() {
        Instant start = Instant.parse("2016-02-17T08:00:00Z");
        Instant end = Instant.parse("2016-02-17T09:00:00Z");

        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(start, end));
        assertTrue("expected empty list", result.isEmpty());
    }

    @Test
    public void testInTimespanTweetsWithinTimespan() {
        Instant start = Instant.parse("2016-02-17T09:30:00Z");
        Instant end = Instant.parse("2016-02-17T11:30:00Z");

        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(start, end));
        assertEquals("expected two tweets", 2, result.size());
        assertTrue("expected tweet1", result.contains(tweet1));
        assertTrue("expected tweet2", result.contains(tweet2));
    }

    @Test
    public void testInTimespanPartiallyOverlapping() {
        Instant start = Instant.parse("2016-02-17T10:30:00Z");
        Instant end = Instant.parse("2016-02-17T12:30:00Z");

        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(start, end));
        assertEquals("expected two tweets", 2, result.size());
        assertTrue("expected tweet2", result.contains(tweet2));
        assertTrue("expected tweet3", result.contains(tweet3));
    }

    // Partition strategy for containing:
    // - No words in the tweets match
    // - Some words match, case-insensitive
    // - No words in the search list
    @Test
    public void testContainingNoWordsMatch() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("football", "basketball"));
        assertTrue("expected empty list", result.isEmpty());
    }

    @Test
    public void testContainingSomeWordsMatch() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk", "coding"));
        assertEquals("expected two tweets", 2, result.size());
        assertTrue("expected tweet1", result.contains(tweet1));
        assertTrue("expected tweet2", result.contains(tweet2));
    }

    @Test
    public void testContainingNoWordsGiven() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList());
        assertTrue("expected empty list", result.isEmpty());
    }

}
