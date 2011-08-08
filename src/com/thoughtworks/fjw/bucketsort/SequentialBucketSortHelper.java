package com.thoughtworks.fjw.bucketsort;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SequentialBucketSortHelper implements IBucketSortHelper<Integer> {
	@Override
	public void sortBuckets(final Map<Integer, List<Integer>> buckets) {
		for (List<Integer> aList : buckets.values()) {
			Collections.sort(aList);
		}
	}
}
