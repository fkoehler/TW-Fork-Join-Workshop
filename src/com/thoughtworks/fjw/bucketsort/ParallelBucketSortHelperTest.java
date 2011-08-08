package com.thoughtworks.fjw.bucketsort;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.fjw.utils.ListGenerator;

public class ParallelBucketSortHelperTest {
	private final Logger LOGGER = Logger.getLogger(ParallelBucketSortHelperTest.class.getCanonicalName());
	private ListGenerator listGenerator;

	@Before
	public void setUp() {
		listGenerator = new ListGenerator();
	}

	@Test
	public void shouldSortListOfIntegers() {

		BucketSorter bucketSorter = new BucketSorter(new ParallelBucketSortHelper(), 4);
		LOGGER.info(bucketSorter.toString());

		List<Integer> input = listGenerator.createListOfNonNegativeIntegers(170, 1222);
		LOGGER.info(input.toString());

		List<Integer> output = bucketSorter.sort(input);
		LOGGER.info(bucketSorter.toString());
		LOGGER.info(output.toString());

		assertListIsSorted(output);
	}

	private void assertListIsSorted(final List<Integer> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			Assert.assertTrue(list.get(i).compareTo(list.get(i + 1)) <= 0);
		}

	}

}
