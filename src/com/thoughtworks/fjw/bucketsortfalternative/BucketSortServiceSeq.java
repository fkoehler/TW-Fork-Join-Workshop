package com.thoughtworks.fjw.bucketsortfalternative;

import java.util.Arrays;

public class BucketSortServiceSeq {

	private final int[] arrayToSort;
	private int[][] buckets;
	private final int nofBuckets;

	public BucketSortServiceSeq(final int[] arrayToSort, final int nofBuckets) {
		this.arrayToSort = arrayToSort;
		this.nofBuckets = nofBuckets;
	}

	public int[] bucketSort() {
		return bucketSort(arrayToSort);
	}

	public int[] bucketSort(final int[] arrayToSort) {
		prepareAndFillBuckets();
		sortBuckets();
		return concatenateBuckets();
	}

	private void prepareAndFillBuckets() {
		int maxElementToSort = getMaxIntFromArray(arrayToSort);

		buckets = new int[nofBuckets][arrayToSort.length];

		for (int bucket = 0; bucket < nofBuckets; bucket++) {
			int bucketRangeStart = bucket * (int) Math.ceil((double) maxElementToSort / nofBuckets);
			int bucketRangeEnd = (bucket + 1) * (int) Math.ceil((double) maxElementToSort / nofBuckets);
			if (bucket + 1 == nofBuckets) {
				bucketRangeEnd++;
			}
			int bucketElementCount = 0;

			for (int elementToSort : arrayToSort) {
				if (elementToSort >= bucketRangeStart && elementToSort < bucketRangeEnd) {
					buckets[bucket][bucketElementCount] = elementToSort;
				} else {
					buckets[bucket][bucketElementCount] = -1;
				}

				bucketElementCount++;
			}
		}
	}

	private void sortBuckets() {
		for (int[] bucket : buckets) {
			Arrays.sort(bucket);
		}
	}

	private int[] concatenateBuckets() {
		int[] merged = new int[arrayToSort.length];
		int bucketCount = 0;
		for (int[] bucket : buckets) {
			for (int element : bucket) {
				if (element != -1) {
					merged[bucketCount++] = element;
				}
			}
		}

		return merged;
	}

	private int getMaxIntFromArray(final int[] array) {
		int max = array[0];
		for (int l : array) {
			if (max < l) {
				max = l;
			}
		}

		return max;
	}

}
