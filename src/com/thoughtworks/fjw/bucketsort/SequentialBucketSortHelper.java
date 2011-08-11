package com.thoughtworks.fjw.bucketsort;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.logging.Logger;

import com.thoughtworks.fjw.utils.TimeKeeper;

public class SequentialBucketSortHelper implements IBucketSortHelper<Integer> {
	private static final Logger LOGGER = Logger.getLogger(SequentialBucketSortHelper.class.getCanonicalName());

	@Override
	public void sortBuckets(final SortedMap<Integer, List<Integer>> bucketMap) {
		LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName() + " sorting all buckets",
				Thread.currentThread().getId(), System.currentTimeMillis(), ActionCode.SORT_ALL_BUCKETS));

		for (List<Integer> aList : bucketMap.values()) {
			Collections.sort(aList);
		}
	}
}
