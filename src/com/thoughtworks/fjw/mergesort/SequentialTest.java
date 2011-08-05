package com.thoughtworks.fjw.mergesort;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.thoughtworks.fjw.utils.Utils;

public class SequentialTest {

	@Test
	public void testShortestPathProblem() {
		int[] arrayToSort = Utils.buildRandomIntArray(20000000);
		int[] expectedArray = Arrays.copyOf(arrayToSort, arrayToSort.length);

		int nofProcessors = Runtime.getRuntime().availableProcessors();

		StopWatch stopWatch = new LoggingStopWatch("singlethread-mergesort algo");
		MergeSortServiceSeq shortestPathServiceSeq = new MergeSortServiceSeq(arrayToSort, nofProcessors);
		int[] actualArray = shortestPathServiceSeq.sequentialSort();
		stopWatch.stop();

		stopWatch = new LoggingStopWatch("singlethread-java std. quicksort");
		Arrays.sort(expectedArray);
		stopWatch.stop();

		assertThat(actualArray, is(expectedArray));
	}

}
