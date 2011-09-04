package com.thoughtworks.fjw.bucketsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

import com.thoughtworks.fjw.utils.LogCode;
import com.thoughtworks.fjw.utils.TimeKeeper;

public class ParallelBucketSortTask extends RecursiveTask<SortedMap<Integer, List<Integer>>> {
	private static final long serialVersionUID = 7227570740190481380L;
	private static final Logger LOGGER = Logger.getLogger(ParallelBucketSortTask.class.getCanonicalName());
	private SortedMap<Integer, List<Integer>> bucketMap;

	public ParallelBucketSortTask(final SortedMap<Integer, List<Integer>> aBucketMap) {
		super();
		bucketMap = aBucketMap;
	}

	@Override
	protected SortedMap<Integer, List<Integer>> compute() {
		if (bucketMap.size() > 1) {
			LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName()
					+ " creating and invoking sub tasks", Thread.currentThread().getId(), System.currentTimeMillis(),
					LogCode.FORK));

			Set<ParallelBucketSortTask> subTaskSet = createSubTasks();
			invokeAll(subTaskSet);

			/* 
			 * Aggregation of partial results is not required as sub-buckets are backed 
			 * by the mother-bucket
			 */
			return bucketMap;

		} else {
			LOGGER.info(TimeKeeper.createLogMessage(ParallelBucketSortTask.class.getCanonicalName()
					+ " sorting a bucket", Thread.currentThread().getId(), System.currentTimeMillis(),
					LogCode.SORT_SINGLE_BUCKET));

			Collections.sort(bucketMap.get(bucketMap.firstKey()));
			return bucketMap;

		}
	}

	Set<ParallelBucketSortTask> createSubTasks() {
		Set<ParallelBucketSortTask> result = new HashSet<ParallelBucketSortTask>();

		/*
		 * Allocate individual buckets to tasks. A bit cumbersome but we need to grab two
		 * subsequent keys at a time. 
		 */
		List<Integer> keyList = new ArrayList<Integer>();
		keyList.addAll(bucketMap.keySet());
		SortedMap<Integer, List<Integer>> subBucketMap = null;
		for (int i = 0; i < keyList.size() - 1; i++) {
			subBucketMap = bucketMap.subMap(keyList.get(i), keyList.get(i + 1));
			result.add(new ParallelBucketSortTask(subBucketMap));
		}

		/*
		 * Do not forget the last bucket which cannot be captured by means of the 'subMap' method
		 */
		result.add(new ParallelBucketSortTask(bucketMap.tailMap(keyList.get(keyList.size() - 1))));

		return result;

	}

	@Override
	public String toString() {
		return "ParallelBucketSortTask [bucketMap=" + bucketMap + "]";
	}

}
