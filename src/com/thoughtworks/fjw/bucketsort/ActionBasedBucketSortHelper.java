package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

/*
 * Instantiate ForkJoinPool here and delegate to an action (as opposed to a task).
 */
public class ActionBasedBucketSortHelper implements IBucketSortHelper<Integer> {
	private static final Logger LOGGER = Logger.getLogger(ActionBasedBucketSortHelper.class.getCanonicalName());

	@Override
	public void sortBuckets(final SortedMap<Integer, List<Integer>> aBucketMap) {
		/*
		 * Choose a meaningful pool size
		 */
		ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

		/*
		 * Seed the pool with initial action and get started
		 */
		ParallelBucketSortAction anAction = new ParallelBucketSortAction(aBucketMap);
		LOGGER.info(anAction.toString());
		forkJoinPool.invoke(anAction);

	}
}
