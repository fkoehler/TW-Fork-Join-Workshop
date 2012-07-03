package com.thoughtworks.fjw.bucketsortalternativewithlists;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class BucketSortTaskTest {

	@Test
	public void shouldPrepareAndFillAndSortFirstBucket() {
		List<Integer> input = asList(4, 3, 2, 1);
		BucketSortTask bucketSortTask = new BucketSortTask(input, 2, 0);
		List<Integer> bucket = bucketSortTask.compute();

		// the buckets are distributed evenly according to the max value and we include the zero as an element
		assertEquals(asList(-1, -1, -1, 1), bucket);
	}

	@Test
	public void shouldPrepareAndFillAndSortSecondBucket() {
		List<Integer> input = asList(4, 3, 2, 1);
		BucketSortTask bucketSortTask = new BucketSortTask(input, 2, 1);
		List<Integer> bucket = bucketSortTask.compute();

		// the buckets are distributed evenly according to the max value and we include the zero as an element
		assertEquals(asList(-1, 2, 3, 4), bucket);
	}

	@Test
	public void shouldCreateTwoSubTasksForTwoBuckets() {
		List<Integer> input = asList(4, 3, 2, 1);
		BucketSortTask bucketSortTask = new BucketSortTask(input, 2);
		List<BucketSortTask> subTasks = bucketSortTask.createBucketSortTasks();

		assertEquals(2, subTasks.size());

		// first one should be computable and sorted

		List<Integer> bucket1 = subTasks.get(0).compute();
		List<Integer> bucket2 = subTasks.get(1).compute();

		assertEquals(asList(-1, -1, -1, 1), bucket1);
		assertEquals(asList(-1, 2, 3, 4), bucket2);
	}

}
