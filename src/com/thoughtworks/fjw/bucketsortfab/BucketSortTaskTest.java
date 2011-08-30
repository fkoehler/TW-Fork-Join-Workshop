package com.thoughtworks.fjw.bucketsortfab;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class BucketSortTaskTest {

	@Test
	public void shouldPrepareAndFillAndSortFirstBucket() {
		int[] input = new int[] { 4, 3, 2, 1 };
		BucketSortTask bucketSortTask = new BucketSortTask(input, 2, 0);
		int[] bucket = bucketSortTask.compute();

		// the buckets are distributed evenly according to the max value and we include the zero as an element
		assertArrayEquals(new int[] { -1, -1, -1, 1 }, bucket);
	}

	@Test
	public void shouldPrepareAndFillAndSortSecondBucket() {
		int[] input = new int[] { 4, 3, 2, 1 };
		BucketSortTask bucketSortTask = new BucketSortTask(input, 2, 1);
		int[] bucket = bucketSortTask.compute();

		// the buckets are distributed evenly according to the max value and we include the zero as an element
		assertArrayEquals(new int[] { -1, 2, 3, 4 }, bucket);
	}

	@Test
	public void shouldReturnMaxValueFromArray() {
		BucketSortTask bucketSortTask = new BucketSortTask(null, 0, 0);
		assertEquals(4, bucketSortTask.getMaxIntFromArray(new int[] { 1, 2, 3, 4 }));
		assertEquals(8, bucketSortTask.getMaxIntFromArray(new int[] { 8, 6, -6, 4 }));
	}

	@Test
	public void shouldCreateTwoSubTasksForTwoBuckets() {
		int[] input = new int[] { 4, 3, 2, 1 };
		BucketSortTask bucketSortTask = new BucketSortTask(input, 2);
		List<BucketSortTask> subTasks = bucketSortTask.createBucketSortTasks();

		assertEquals(2, subTasks.size());

		// first one should be computable and sorted

		int[] bucket1 = subTasks.get(0).compute();
		int[] bucket2 = subTasks.get(1).compute();

		assertArrayEquals(new int[] { -1, -1, -1, 1 }, bucket1);
		assertArrayEquals(new int[] { -1, 2, 3, 4 }, bucket2);
	}

}
