package com.thoughtworks.fjw.bucketsort;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.thoughtworks.fjw.utils.ListGenerator;

public class ActionBasedBucketSortHelperTest {
	private static final Logger LOGGER = Logger.getLogger(ActionBasedBucketSortHelperTest.class.getCanonicalName());
	private ListGenerator listGenerator;

	@Before
	public void setUp() {
		listGenerator = new ListGenerator();
	}

	@Test
	public void shouldSortListOfIntegers() {
		List<Integer> inputList = listGenerator.createListOfNonNegativeIntegers(1000000, Integer.MAX_VALUE);

		StopWatch stopWatch = new LoggingStopWatch("multithread-bucktesortlistwolf");
		BucketSorter bucketSorter = new BucketSorter(new ActionBasedBucketSortHelper(), 5);
		//		LOGGER.info(bucketSorter.toString());

		List<Integer> outputList = bucketSorter.sort(inputList);
		stopWatch.stop();

		//		LOGGER.info(bucketSorter.toString());
		//		LOGGER.info(outputList.toString());

		Set<Integer> inputSet = new HashSet<Integer>();
		inputSet.addAll(inputList);
		Set<Integer> outputSet = new HashSet<Integer>();
		outputSet.addAll(outputList);

		Assert.assertEquals("input and output list should have the same size", inputList.size(), outputList.size());
		Assert.assertEquals("sets derived from the input and output lists should contain the same elements", inputSet,
				outputSet);
		Assert.assertTrue("list should be sorted", listGenerator.isListSorted(outputList));
	}

}
