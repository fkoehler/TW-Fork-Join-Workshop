package com.thoughtworks.fjw.utils;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ListGeneratorTest {
	private ListGenerator listGenerator;

	@Before
	public void setUp() {
		listGenerator = new ListGenerator();
	}

	@Test
	public void shouldGenerateListWithinMax() {
		int size = 37;
		int max = 42;
		List<Integer> aList = listGenerator.createListOfNonNegativeIntegers(size, max);
		Assert.assertTrue("list size should be " + size, aList.size() == size);
		Assert.assertTrue("largest value should not exceed " + max, (Collections.max(aList).intValue() <= max));
	}

	@Test
	public void shouldGenerateListWithinRange() {
		int size = 37;
		int min = 42;
		int max = 4242;
		List<Integer> aList = listGenerator.createListOfNonNegativeIntegers(size, min, max);
		Assert.assertTrue("list size should be " + size, aList.size() == size);
		Assert.assertTrue("smallest value should be greater than " + (min - 1),
				(Collections.max(aList).intValue() >= min));
		Assert.assertTrue("largest value should not exceed " + max, (Collections.max(aList).intValue() <= max));
	}

	@Test
	public void shouldRecogniseUnsortedList() {
		List<Integer> aList = listGenerator.createListOfNonNegativeIntegers(16, 34, 98);
		aList.add(new Integer(97));
		Assert.assertFalse("list should be un-sorted", listGenerator.isListSorted(aList));
	}

	@Test
	public void shouldRecogniseSortedList() {
		List<Integer> aList = listGenerator.createListOfNonNegativeIntegers(16, 34, 98);
		Collections.sort(aList);
		Assert.assertTrue("list should be sorted", listGenerator.isListSorted(aList));
	}

}
