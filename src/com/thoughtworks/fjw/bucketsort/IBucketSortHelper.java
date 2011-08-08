package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.SortedMap;

public interface IBucketSortHelper<T extends Comparable<? super T>> {
	public void sortBuckets(SortedMap<T, List<T>> buckets);
}
