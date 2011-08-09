package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.SortedMap;

public class MockBucketSortHelper implements IBucketSortHelper<Integer> {
	@Override
	public void sortBuckets(final SortedMap<Integer, List<Integer>> buckets) {
		/*
		 * Nothing in here (poor man's mocking)
		 */
	}
}
