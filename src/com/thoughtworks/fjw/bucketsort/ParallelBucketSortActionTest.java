package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.fjw.utils.ListGenerator;

public class ParallelBucketSortActionTest {
	private static final Logger LOGGER = Logger.getLogger(ParallelBucketSortActionTest.class.getName());
	private ListGenerator listGenerator;

	@Before
	public void setUp() throws Exception {
		listGenerator = new ListGenerator();
	}

	@Test
	public void shouldGenerateSubtasks() {
		int bucketCount = 7;
		int bucketSize = 31;
		ParallelBucketSortAction anAction = new ParallelBucketSortAction(listGenerator.createBucketMap(bucketCount,
				bucketSize));
		LOGGER.info(anAction.toString());

		Set<ParallelBucketSortAction> subActionSet = anAction.createSubTasks();
		LOGGER.info(subActionSet.toString());

		Assert.assertEquals("sub-action count should be " + bucketCount, bucketCount, subActionSet.size());

	}

	@Test
	public void shouldSortSingleBucket() {
		SortedMap<Integer, List<Integer>> bucketMap = listGenerator.createBucketMap(1, 42);
		LOGGER.info(bucketMap.toString());

		ParallelBucketSortAction anAction = new ParallelBucketSortAction(bucketMap);
		anAction.compute();
		LOGGER.info(bucketMap.toString());

		Assert.assertTrue("", listGenerator.isListSorted(bucketMap.get(bucketMap.firstKey())));

	}
}
