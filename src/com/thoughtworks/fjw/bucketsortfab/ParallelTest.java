package com.thoughtworks.fjw.bucketsortfab;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.thoughtworks.fjw.utils.Utils;

public class ParallelTest {

	@Test
	public void testBucketSort() {
		int[] arrayToSortSingleThread = Utils.buildRandomIntArray(5000000);
		int[] arrayToSortMultiThread = Arrays.copyOf(arrayToSortSingleThread, arrayToSortSingleThread.length);

		int nofProcessors = Runtime.getRuntime().availableProcessors();

		// SINGLE THREADED
		StopWatch stopWatch = new LoggingStopWatch("singlethread-bucketsort");
		BucketSortServiceSeq bucketSortService = new BucketSortServiceSeq(arrayToSortSingleThread, 5);
		int[] sortedSingleThreadArray = bucketSortService.bucketSort();
		stopWatch.stop();

		// MULTI THREADED
		stopWatch = new LoggingStopWatch("multithread-bucketsort");
		BucketSortTask bucketSortTask = new BucketSortTask(arrayToSortMultiThread, 5);

		ForkJoinPool forkJoinPool = new ForkJoinPool(nofProcessors);
		int[] actual = forkJoinPool.invoke(bucketSortTask);
		stopWatch.stop();

		Arrays.sort(arrayToSortSingleThread);

		assertArrayEquals(arrayToSortSingleThread, actual);
		assertArrayEquals(sortedSingleThreadArray, actual);
	}

}
