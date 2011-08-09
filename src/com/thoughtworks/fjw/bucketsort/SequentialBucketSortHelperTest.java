package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.fjw.utils.ListGenerator;

public class SequentialBucketSortHelperTest {
	private static final Logger LOGGER = Logger.getLogger(SequentialBucketSortHelperTest.class.getCanonicalName());
	private ListGenerator listGenerator;

	@Before
	public void setUp() {
		listGenerator = new ListGenerator();
	}

	@Test
	public void shouldSortListOfIntegers() {

		BucketSorter bucketSorter = new BucketSorter(new SequentialBucketSortHelper(), 4);
		LOGGER.info(bucketSorter.toString());

		List<Integer> input = listGenerator.createListOfNonNegativeIntegers(170, 1222);
		LOGGER.info(input.toString());

		List<Integer> output = bucketSorter.sort(input);
		LOGGER.info(bucketSorter.toString());
		LOGGER.info(output.toString());

		Assert.assertTrue("list should be sorted", listGenerator.isListSorted(output));
	}

}
