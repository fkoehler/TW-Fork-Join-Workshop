package com.thoughtworks.fjw.bucketsort;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.fjw.utils.ListGenerator;

public class ParallelBucketSortHelperTest {
	private static final Logger LOGGER = Logger.getLogger(ParallelBucketSortHelperTest.class.getCanonicalName());
	private ListGenerator listGenerator;

	@Before
	public void setUp() {
		listGenerator = new ListGenerator();
	}

	@Test
	public void shouldSortListOfIntegers() {

		BucketSorter bucketSorter = new BucketSorter(new ParallelBucketSortHelper(), 4);
		LOGGER.info(bucketSorter.toString());

		List<Integer> inputList = listGenerator.createListOfNonNegativeIntegers(170, 1222);
		LOGGER.info(inputList.toString());

		List<Integer> outputList = bucketSorter.sort(inputList);
		LOGGER.info(bucketSorter.toString());
		LOGGER.info(outputList.toString());

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
