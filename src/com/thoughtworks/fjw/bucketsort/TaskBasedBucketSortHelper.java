package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

/*
 * Instantiate ForkJoinPool here and delegate to task (as opposed to an action).
 */
public class TaskBasedBucketSortHelper implements IBucketSortHelper<Integer> {
	private static final Logger LOGGER = Logger.getLogger(TaskBasedBucketSortHelper.class.getCanonicalName());

	@Override
	public void sortBuckets(final SortedMap<Integer, List<Integer>> aBucketMap) {
		/*
		 * Choose a meaningful pool size
		 */
		ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

		/*
		 * Seed the pool with initial task and get started
		 */
		ParallelBucketSortTask aTask = new ParallelBucketSortTask(aBucketMap);
		LOGGER.info(aTask.toString());
		forkJoinPool.invoke(aTask);

	}
}
