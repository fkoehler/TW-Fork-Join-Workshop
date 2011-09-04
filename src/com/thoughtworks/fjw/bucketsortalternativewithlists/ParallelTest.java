package com.thoughtworks.fjw.bucketsortalternativewithlists;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.thoughtworks.fjw.utils.ListGenerator;

public class ParallelTest {

	@Test
	public void testMergeSort() {
		ListGenerator listGenerator = new ListGenerator();
		List<Integer> inputList = listGenerator.createListOfNonNegativeIntegers(1000000, Integer.MAX_VALUE);

		StopWatch stopWatch = new LoggingStopWatch("multithread-bucketsortlistsFAB");
		BucketSortTask bucketSortTask = new BucketSortTask(inputList, 5);

		int nofProcessors = Runtime.getRuntime().availableProcessors();
		ForkJoinPool forkJoinPool = new ForkJoinPool(nofProcessors);
		List<Integer> outputList = forkJoinPool.invoke(bucketSortTask);
		stopWatch.stop();

		Collections.sort(inputList);

		assertEquals(inputList, outputList);
	}

}
