package com.thoughtworks.fjw.bucketsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.thoughtworks.fjw.sort.ISorter;
import com.thoughtworks.fjw.utils.TimeKeeper;

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
		bucketCount = aBucketCount;
		prepareBucketMapAndBuckets();
	}

	/*  
	 * Allows to inject different kinds of bucket maps, e.g. a thread-safe one as opposed to an unsynchronised one. 
	 * 
	 * A bucket map that is injected is always cleared first.
	 */
	public BucketSorter(final IBucketSortHelper<Integer> aBucketSorterHelper, final int aBucketCount,
			final SortedMap<Integer, List<Integer>> aBucketMap) {
		bucketSorterHelper = aBucketSorterHelper;
		bucketCount = aBucketCount;
		bucketMap = aBucketMap;
		prepareBucketMapAndBuckets();
	}

	public BucketSorter(final IBucketSortHelper<Integer> aBucketSorterHelper, final int aBucketCount,
			final int aMinValue, final int aMaxValue) {
		bucketSorterHelper = aBucketSorterHelper;
		bucketCount = aBucketCount;
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

		LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName() + " finished merging buckets",
				Thread.currentThread().getId(), System.currentTimeMillis(), LogCode.END_OF_MERGING_BUCKETS));

		return result;
	}

	private void prepareBucketMapAndBuckets() {
		if (bucketMap == null) {
			bucketMap = new TreeMap<Integer, List<Integer>>();
		} else {
			bucketMap.clear();
		}
		prepareBuckets();
	}

	private void prepareBuckets() {
		LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName() + " preparing buckets",
				Thread.currentThread().getId(), System.currentTimeMillis(), LogCode.PREPARE_BUCKETS));

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
		 * Why '(..., bucketCount - 1)'? When dividing the range by the full bucketCount the end of the range may lie 
		 * beyond the last bucket. Adding an extra bucket would solve that problem. However, to be able to define 
		 * the actual number of buckets when using this class the calculation assumes a lower number of buckets. 
		 * 
		 * Why all the 'Math.max' magic? To avoid division by zero and to guarantee a minimum bucket width of 1.
		 * 
		 * Why the last '... + 1'? To make the largest value of a list fit into the (only) bucket of a bucket map
		 * that contains only one bucket. 
		 */
		int result = Math.max(1, (int) Math.ceil(1.0 * aRange / Math.max(1, bucketCount - 1)) + 1);
		return result;
	}

	private void fillBuckets(final List<Integer> aList) {
		LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName() + " filling buckets",
				Thread.currentThread().getId(), System.currentTimeMillis(), LogCode.FILL_BUCKETS));

		Integer bucketIndex = null;
		for (Integer anInteger : aList) {
			/*
			 * Why try-catch? Left-over from fine tuning the way buckets are created, in particular the way 
			 * the bucket width is calculated.
			 */
			try {
				bucketIndex = calculateIndex(anInteger);
				bucketMap.get(bucketIndex).add(anInteger);
			} catch (RuntimeException re) {
				StringBuilder builder = new StringBuilder();
				builder.append("failed to find bucket index with [");
				builder.append("anInteger=");
				builder.append(anInteger);
				builder.append(", calulated bucketIndex=");
				builder.append(bucketIndex);
				builder.append(", this.toSlimString()= ");
				builder.append(toSlimString());
				LOGGER.severe(builder.toString());
				throw re;
			}
		}
	}

	Integer calculateIndex(final Integer anInteger) {
		/*
		 * Why '... / bucketWidth)) * bucketWidth...'? To use an exact multiple of the bucket width.
		 */
		return new Integer((int) Math.floor((anInteger.intValue() - minValue) / bucketWidth) * bucketWidth + minValue);
	}

	private void sortBuckets() {
		LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName() + " sorting all buckets",
				Thread.currentThread().getId(), System.currentTimeMillis(), LogCode.SORT_ALL_BUCKETS));

		bucketSorterHelper.sortBuckets(bucketMap);
	}

	private List<Integer> mergeBuckets() {
		LOGGER.info(TimeKeeper.createLogMessage(this.getClass().getCanonicalName() + " merging buckets",
				Thread.currentThread().getId(), System.currentTimeMillis(), LogCode.MERGE_BUCKETS));

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

	public String toSlimString() {
		return "BucketSorter [bucket keys=" + (bucketMap != null ? bucketMap.keySet().toString() : "null")
				+ ", bucketSorterHelper=" + (bucketSorterHelper != null ? bucketSorterHelper.toString() : "null")
				+ ", bucketCount=" + bucketCount + ", bucketWidth=" + bucketWidth + ", minValue=" + minValue
				+ ", maxValue=" + maxValue + ", range=" + (maxValue - minValue) + "]";
	}

}
