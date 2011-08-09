package com.thoughtworks.fjw.bucketsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.thoughtworks.fjw.sort.ISorter;

public class BucketSorter implements ISorter<Integer> {
	private static final Logger LOGGER = Logger.getLogger(BucketSorter.class.getCanonicalName());

	private SortedMap<Integer, List<Integer>> bucketMap;
	private IBucketSortHelper<Integer> bucketSorterHelper;
	private int bucketCount;
	private int bucketWidth;
	private int minValue;
	private int maxValue;

	public BucketSorter(final IBucketSortHelper<Integer> aBucketSorterHelper, final int aBucketCount) {
		bucketSorterHelper = aBucketSorterHelper;
		setBucketCount(aBucketCount);
		prepareBucketMapAndBuckets();
	}

	/*
	 * Allows to inject different kinds of bucket maps, e.g. a thread-safe one as opposed to an unsynchronised one. 
	 * 
	 * A bucket map that is injected is always cleared first.
	 */
	public BucketSorter(final IBucketSortHelper<Integer> aBucketSorterHelper, final int aBucketCount,
			final SortedMap<Integer, List<Integer>> aBucketMap) {
		bucketMap = aBucketMap;
		bucketMap.clear();
		bucketSorterHelper = aBucketSorterHelper;
		setBucketCount(aBucketCount);
		prepareBucketMapAndBuckets();
	}

	public BucketSorter(final IBucketSortHelper<Integer> aBucketSorterHelper, final int aBucketCount,
			final int aMinValue, final int aMaxValue) {
		bucketSorterHelper = aBucketSorterHelper;
		setBucketCount(aBucketCount);
		minValue = aMinValue;
		maxValue = aMaxValue;
		prepareBucketMapAndBuckets();
	}

	@Override
	public List<Integer> sort(final List<Integer> aList) {
		List<Integer> result = new ArrayList<Integer>();
		minValue = Collections.min(aList).intValue();
		maxValue = Collections.max(aList).intValue();

		prepareBuckets();
		fillBuckets(aList);
		sortBuckets();
		result = mergeBuckets();

		return result;
	}

	private void prepareBucketMapAndBuckets() {
		bucketMap = new TreeMap<Integer, List<Integer>>();
		prepareBuckets();
	}

	private void prepareBuckets() {
		bucketWidth = calculateBucketWidth(maxValue - minValue);

		/*
		 * Why '+ minValue'? In order to make the index of the first bucket equal to the min value
		 * of the list.
		 */
		for (int i = 0; i < bucketCount; i++) {
			bucketMap.put(new Integer(i * bucketWidth + minValue), new ArrayList<Integer>());
		}
	}

	int calculateBucketWidth(final int aRange) {
		if (aRange < 0) {
			throw new IllegalArgumentException("range must not be less than 0");
		}
		/* 
		 * Why '(bucketCount - 1)'? When dividing the range by the full bucketCount the end of the range may lie 
		 * beyond the last bucket. Adding an extra bucket would solve that problem. However, to be able to define 
		 * the actual number of buckets when using this class the calculation assumes a lower number of buckets. 
		 * 
		 * Why all the 'Math.max' magic? To avoid division by zero and to guarantee a minimum bucket width of 1.
		 */
		int result = Math.max(1, (int) Math.ceil(1.0 * aRange / Math.max(1, bucketCount - 1)));
		return result;
	}

	private void fillBuckets(final List<Integer> aList) {
		for (Integer anInteger : aList) {
			bucketMap.get(calculateIndex(anInteger)).add(anInteger);
		}
	}

	Integer calculateIndex(final Integer anInteger) {
		/*
		 * Why '... / bucketWidth)) * bucketWidth...'? To use an exact multiple of the bucket width.
		 */
		return new Integer((int) Math.floor(((anInteger.intValue() - minValue) / bucketWidth)) * bucketWidth + minValue);
	}

	private void sortBuckets() {
		bucketSorterHelper.sortBuckets(bucketMap);
	}

	private List<Integer> mergeBuckets() {
		List<Integer> result = new ArrayList<Integer>();

		for (List<Integer> aList : bucketMap.values()) {
			result.addAll(aList);
		}
		return result;
	}

	public void setBucketCount(final int aBucketCount) {
		if (aBucketCount < 1) {
			throw new IllegalArgumentException("bucket count must be greater than 0");
		}
		bucketCount = aBucketCount;
		prepareBucketMapAndBuckets();
	}

	public void setBucketSorterHelper(final IBucketSortHelper<Integer> aBucketSorterHelper) {
		bucketSorterHelper = aBucketSorterHelper;
	}

	/*
	 * Allows to inject different kinds of bucket maps, e.g. a thread-safe one as opposed to an unsynchronised one. 
	 * 
	 * A bucket map that is injected is always cleared first.
	 */
	public void setBucketMap(final SortedMap<Integer, List<Integer>> aBucketMap) {
		bucketMap = aBucketMap;
		aBucketMap.clear();
		prepareBuckets();
	}

	public void setMinValue(final int aMinValue) {
		minValue = aMinValue;
		prepareBucketMapAndBuckets();
	}

	public void setMaxValue(final int aMaxValue) {
		maxValue = aMaxValue;
		prepareBucketMapAndBuckets();
	}

	@Override
	public String toString() {
		return "BucketSorter [buckets=" + (bucketMap != null ? bucketMap.toString() : "null") + ", bucketSorterHelper="
				+ (bucketSorterHelper != null ? bucketSorterHelper.toString() : "null") + ", bucketCount="
				+ bucketCount + ", bucketWidth=" + bucketWidth + ", minValue=" + minValue + ", maxValue=" + maxValue
				+ ", range=" + (maxValue - minValue) + "]";
	}

}
