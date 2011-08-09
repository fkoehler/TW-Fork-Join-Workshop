package com.thoughtworks.fjw.bucketsortfab;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.thoughtworks.fjw.utils.Utils;

public class SequentialTest {

	@Test
	public void testBucketSort() {
		// test with zeros to sort

		int[] arrayToSort = Utils.buildRandomIntArray(10);
		int[] expectedArray = Arrays.copyOf(arrayToSort, arrayToSort.length);

		StopWatch stopWatch = new LoggingStopWatch("singlethread-bucketsort algo");
		int nofBuckets = 2;
		BucketSortServiceSeq bucketSortServiceSeq = new BucketSortServiceSeq(arrayToSort, nofBuckets);
		int[] actualArray = bucketSortServiceSeq.bucketSort();
		stopWatch.stop();

		stopWatch = new LoggingStopWatch("singlethread-java std. quicksort");
		Arrays.sort(expectedArray);
		stopWatch.stop();

		assertThat(actualArray, is(expectedArray));
	}

	@Test
	public void testBucketSortOddBuckets() {
		int[] arrayToSort = Utils.buildRandomIntArray(200000);
		int[] expectedArray = Arrays.copyOf(arrayToSort, arrayToSort.length);

		int nofBuckets = 5;
		BucketSortServiceSeq bucketSortServiceSeq = new BucketSortServiceSeq(arrayToSort, nofBuckets);
		int[] actualArray = bucketSortServiceSeq.bucketSort();

		Arrays.sort(expectedArray);

		assertThat(actualArray, is(expectedArray));
	}

}
