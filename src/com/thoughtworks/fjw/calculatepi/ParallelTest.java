package com.thoughtworks.fjw.calculatepi;

import org.junit.Test;

public class ParallelTest {

	@Test
	public void testMergeSort() {
		// How do we set the parallel threshold? One fairly obvious idea is that the threshold should be
		// set to the size of the array divided by the number of available CPU cores. That way, if we have
		// 4 cores, each will be assigned one quarter of the overall array to sort sequentially.
		//
		//		int[] arrayToSortSingleThread = Utils.buildRandomIntArray(20000000);
		//		int[] arrayToSortMultiThread = Arrays.copyOf(arrayToSortSingleThread, arrayToSortSingleThread.length);
		//
		//		int nofProcessors = Runtime.getRuntime().availableProcessors();
		//
		//		// SINGLE THREADED
		//		StopWatch stopWatch = new LoggingStopWatch("singlethread-mergesort");
		//		CalculatePiServiceSeq shortestPathServiceSeq = new CalculatePiServiceSeq(arrayToSortSingleThread, nofProcessors);
		//		int[] sortSingleThreadArray = shortestPathServiceSeq.sequentialSort();
		//		stopWatch.stop();
		//
		//		// MULTI THREADED
		//		stopWatch = new LoggingStopWatch("multithread-mergesort");
		//		CalculatePiAction mergeSortAction = new CalculatePiAction(arrayToSortMultiThread, nofProcessors);
		//
		//		ForkJoinPool forkJoinPool = new ForkJoinPool(nofProcessors);
		//		forkJoinPool.invoke(mergeSortAction);
		//		stopWatch.stop();
		//
		//		assertArrayEquals(sortSingleThreadArray, mergeSortAction.getSortedArray());
	}

}
