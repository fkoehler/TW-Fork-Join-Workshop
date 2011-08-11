package com.thoughtworks.fjw.bucketsort;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.fjw.utils.LargeInts;
import com.thoughtworks.fjw.utils.ListGenerator;
import com.thoughtworks.fjw.utils.TimeKeeper;

/*
 * TODO - distinguish between non-synchronised and synchronised bucket maps
 */
public class TaskBasedBucketSortHelperTest {
	private static final Logger LOGGER = Logger.getLogger(TaskBasedBucketSortHelperTest.class.getCanonicalName());
	private ListGenerator listGenerator;
	private int listSize;
	private int min;
	private int max;
	private int processorCount;

	/*
	 * Change list size, min and max values here
	 */
	@Before
	public void setUp() {
		listGenerator = new ListGenerator();
		min = Integer.MAX_VALUE * 4 / 5;
		max = Integer.MAX_VALUE;
		listSize = LargeInts.ONE_MILLION;
		//processorCount = Runtime.getRuntime().availableProcessors();
		processorCount = 1;
	}

	/*
	 * Adjust characteristics of the input list here, e.g. by producing lists the elements of which are not evenly distributed
	 */
	private List<Integer> createInputList() {
		List<Integer> inputList = listGenerator.createListOfNonNegativeIntegers(listSize, min, max);
		inputList.add(0, new Integer(min));
		LOGGER.fine(inputList.toString());
		return inputList;
	}

	@Test
	public void shouldSortListOfIntegersSequentially() {
		String context = "Sorting " + listSize + " integers in a sequential fashion";

		SortedMap<Integer, List<Integer>> unsynchronisedBucketMap = new TreeMap<Integer, List<Integer>>();

		BucketSorter bucketSorter = new BucketSorter(new SequentialBucketSortHelper(), processorCount,
				unsynchronisedBucketMap);
		shouldSortListOfIntegers(context, bucketSorter);
	}

	@Test
	public void shouldSortListOfIntegersWithUnsynchronisedMap() {
		String context = "Sorting " + listSize + " integers with unsynchronised buckets";

		SortedMap<Integer, List<Integer>> unsynchronisedBucketMap = new TreeMap<Integer, List<Integer>>();

		BucketSorter bucketSorter = new BucketSorter(new TaskBasedBucketSortHelper(), processorCount,
				unsynchronisedBucketMap);
		shouldSortListOfIntegers(context, bucketSorter);
	}

	@Test
	public void shouldSortListOfIntegersWithThreadSafeMap() {

		String context = "Sorting " + listSize + " integers with threadsafe buckets";

		SortedMap<Integer, List<Integer>> threadsafeBucketMap = Collections.synchronizedSortedMap(new TreeMap<Integer, List<Integer>>());

		BucketSorter bucketSorter = new BucketSorter(new TaskBasedBucketSortHelper(), processorCount,
				threadsafeBucketMap);
		shouldSortListOfIntegers(context, bucketSorter);
	}

	private void shouldSortListOfIntegers(final String context, final BucketSorter bucketSorter) {
		LOGGER.fine(bucketSorter.toString());

		List<Integer> inputList = createInputList();
		LOGGER.fine(inputList.toString());

		long startTime = System.currentTimeMillis();
		List<Integer> outputList = bucketSorter.sort(inputList);
		long stopTime = System.currentTimeMillis();
		logTimes(context, startTime, stopTime);

		LOGGER.fine(bucketSorter.toString());
		LOGGER.fine(outputList.toString());

		assertSortedList(inputList, outputList);
	}

	private void logTimes(final String context, final long startTime, final long stopTime) {
		TimeKeeper.logTimes(LOGGER, context, Thread.currentThread().getId(), startTime, stopTime);
	}

	private void assertSortedList(final List<Integer> inputList, final List<Integer> outputList) {
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
