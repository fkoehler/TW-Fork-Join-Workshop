package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thoughtworks.fjw.utils.TimeKeeper;

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

		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.fine(anAction.toString());
		}

		LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName() + " invoking forkJoinPool",
				Thread.currentThread().getId(), System.currentTimeMillis(), ActionCode.INVOKE));
		forkJoinPool.invoke(anAction);

	}
}
