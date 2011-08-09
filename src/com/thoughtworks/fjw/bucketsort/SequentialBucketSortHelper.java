package com.thoughtworks.fjw.bucketsort;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

public class SequentialBucketSortHelper implements IBucketSortHelper<Integer> {
	@Override
	public void sortBuckets(final SortedMap<Integer, List<Integer>> bucketMap) {
		for (List<Integer> aList : bucketMap.values()) {
			Collections.sort(aList);
		}
	}
}
