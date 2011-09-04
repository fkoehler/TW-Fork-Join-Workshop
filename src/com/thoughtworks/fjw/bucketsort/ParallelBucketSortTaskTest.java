package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.fjw.utils.ListGenerator;

public class ParallelBucketSortTaskTest {
	private static final Logger LOGGER = Logger.getLogger(ParallelBucketSortTaskTest.class.getName());
	private ListGenerator listGenerator;

	@Before
	public void setUp() throws Exception {
		listGenerator = new ListGenerator();
	}

	@Test
	public void shouldGenerateSubtasks() {
		int bucketCount = 7;
		int bucketSize = 31;
		ParallelBucketSortTask aTask = new ParallelBucketSortTask(
				listGenerator.createBucketMap(bucketCount, bucketSize));
		LOGGER.info(aTask.toString());

		Set<ParallelBucketSortTask> subTaskSet = aTask.createSubTasks();
		LOGGER.info(subTaskSet.toString());

		Assert.assertEquals("sub-task count should be " + bucketCount, bucketCount, subTaskSet.size());
		for (ParallelBucketSortTask aSubTask : subTaskSet) {
			Assert.assertEquals("sub tasks should contain buckets ot size 1", 1, aSubTask.compute().size());
		}
	}

	@Test
	public void shouldSortSingleBucket() {
		SortedMap<Integer, List<Integer>> bucketMap = listGenerator.createBucketMap(1, 42);
		LOGGER.info(bucketMap.toString());

		ParallelBucketSortTask aTask = new ParallelBucketSortTask(bucketMap);
		SortedMap<Integer, List<Integer>> result = aTask.compute();
		LOGGER.info(bucketMap.toString());

		Assert.assertTrue("", listGenerator.isListSorted(result.get(result.firstKey())));
	}

}
