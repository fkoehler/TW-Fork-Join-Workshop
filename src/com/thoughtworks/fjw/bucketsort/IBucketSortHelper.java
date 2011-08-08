package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.Map;

public interface IBucketSortHelper<T extends Comparable<? super T>> {
	public void sortBuckets(Map<T, List<T>> buckets);
}
