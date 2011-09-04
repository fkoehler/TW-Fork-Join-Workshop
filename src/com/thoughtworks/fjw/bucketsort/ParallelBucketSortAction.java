package com.thoughtworks.fjw.bucketsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

import com.thoughtworks.fjw.utils.LogCode;
import com.thoughtworks.fjw.utils.TimeKeeper;

/*
 * All ForkJoinTasks forked by this class are operating on (partial collections of) buckets that are backed by the same
 * (mother-)collection of buckets. The algorithm ensures that write operations
 */
public class ParallelBucketSortAction extends RecursiveAction {
	private static final long serialVersionUID = -3065536639372469954L;
	private static final Logger LOGGER = Logger.getLogger(ParallelBucketSortAction.class.getCanonicalName());
	private SortedMap<Integer, List<Integer>> bucketMap;

	public ParallelBucketSortAction(final SortedMap<Integer, List<Integer>> aBucketMap) {
		super();
		bucketMap = aBucketMap;
	}

	@Override
	protected void compute() {
		if (bucketMap.size() > 1) {
			LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName()
					+ " creating and invoking sub tasks", Thread.currentThread().getId(), System.currentTimeMillis(),
					LogCode.FORK));

			Set<ParallelBucketSortAction> subTaskSet = createSubTasks();
			invokeAll(subTaskSet);

		} else {
			LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName() + " sorting a bucket",
					Thread.currentThread().getId(), System.currentTimeMillis(), LogCode.SORT_SINGLE_BUCKET));

			Collections.sort(bucketMap.get(bucketMap.firstKey()));

		}
	}

	Set<ParallelBucketSortAction> createSubTasks() {
		Set<ParallelBucketSortAction> result = new HashSet<ParallelBucketSortAction>();

		/*
		 * Allocate individual buckets to tasks. A bit cumbersome but we need to grab two
		 * subsequent keys at a time. 
		 */
		List<Integer> keyList = new ArrayList<Integer>();
		keyList.addAll(bucketMap.keySet());
		SortedMap<Integer, List<Integer>> subBucketMap = null;
		for (int i = 0; i < keyList.size() - 1; i++) {
			subBucketMap = bucketMap.subMap(keyList.get(i), keyList.get(i + 1));
			result.add(new ParallelBucketSortAction(subBucketMap));
		}

		/*
		 * Do not forget the last bucket which cannot be captured by means of the 'subMap' method
		 */
		result.add(new ParallelBucketSortAction(bucketMap.tailMap(keyList.get(keyList.size() - 1))));

		return result;

	}

	@Override
	public String toString() {
		return "ParallelBucketSortTask [bucketMap=" + bucketMap + "]";
	}

}
