package com.thoughtworks.fjw.bucketsort;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BucketSorterTest {
	private static final Logger LOGGER = Logger.getLogger(BucketSorterTest.class.getCanonicalName());
	private BucketSorter bucketSorter;

	@Before
	public void setUp() {
		bucketSorter = new BucketSorter(new MockBucketSortHelper(), 4);
	}

	@Test
	public void shouldCalculateSufficientlyLargeBucketWidth() {
		for (int bucketCount = 1; bucketCount < 21; bucketCount++) {
			bucketSorter.setBucketCount(bucketCount);
			for (int range = 0; range < 42; range++) {
				Assert.assertTrue("bucket width times bucket count should not be less than range",
						bucketSorter.calculateBucketWidth(range) * bucketCount >= range);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectNegativeRange() {
		bucketSorter.calculateBucketWidth(-42);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectNegativeBucketCount() {
		bucketSorter.setBucketCount(-42);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectZeroBucketCount() {
		bucketSorter.setBucketCount(0);
	}

	@Test
	public void shouldCalculateBucketIndex() {
		int bucketCount = 4;
		int minValue = 7;
		int maxValue = 44;
		bucketSorter = new BucketSorter(new MockBucketSortHelper(), bucketCount, minValue, maxValue);
		LOGGER.info(bucketSorter.toString());

		Assert.assertEquals("index for smalles integer should be equal to that integer", minValue,
				bucketSorter.calculateIndex(new Integer(minValue)).intValue());

		Assert.assertEquals("20 should be largest integer where index equals 7", 7,
				bucketSorter.calculateIndex(new Integer(20)).intValue());

		Assert.assertEquals("21 should be smallest integer where index equals 21", 21,
				bucketSorter.calculateIndex(new Integer(21)).intValue());

		Assert.assertEquals("index for largest integer in range should be 35", 35,
				bucketSorter.calculateIndex(new Integer(maxValue)).intValue());
	}
}
