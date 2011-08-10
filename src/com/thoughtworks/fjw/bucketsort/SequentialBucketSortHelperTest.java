package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.fjw.utils.LargeInts;
import com.thoughtworks.fjw.utils.ListGenerator;

public class SequentialBucketSortHelperTest {
	private static final Logger LOGGER = Logger.getLogger(SequentialBucketSortHelperTest.class.getCanonicalName());
	private ListGenerator listGenerator;
	private int bucketCount;

	@Before
	public void setUp() {
		listGenerator = new ListGenerator();
		bucketCount = 23;
	}

	@Test
	public void shouldSortListOfIntegers() {

		BucketSorter bucketSorter = new BucketSorter(new SequentialBucketSortHelper(), bucketCount);
		LOGGER.fine(bucketSorter.toString());

		List<Integer> input = listGenerator.createListOfNonNegativeIntegers(LargeInts.ONE_HUNDRED_THOUSAND,
				Integer.MAX_VALUE);
		LOGGER.fine(input.toString());

		List<Integer> output = bucketSorter.sort(input);
		LOGGER.fine(bucketSorter.toString());
		LOGGER.fine(output.toString());

		Assert.assertTrue("list should be sorted", listGenerator.isListSorted(output));
	}

}
