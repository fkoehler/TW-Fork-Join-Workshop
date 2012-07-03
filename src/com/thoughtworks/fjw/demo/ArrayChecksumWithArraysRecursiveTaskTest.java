package com.thoughtworks.fjw.demo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

public class ArrayChecksumWithArraysRecursiveTaskTest {

	@Test
	public void testThatCompleteTaskWorksAsExpected() {
		int[] arrayToCalculateSumOf = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }; // = 55

		ArrayChecksumWithArraysRecursiveTask arrayChecksumWithArraysRecursiveTask = new ArrayChecksumWithArraysRecursiveTask(arrayToCalculateSumOf);
		ForkJoinPool pool = new ForkJoinPool();
		int actual = pool.invoke(arrayChecksumWithArraysRecursiveTask);

        assertThat(actual, is(55));
	}

	@Test
	public void testThatCoreComputationBehavesAsExpected() {
		int[] arrayToCalculateSumOf = new int[] { 5 };
		ArrayChecksumWithArraysRecursiveTask arrayChecksumWithArraysRecursiveTask = new ArrayChecksumWithArraysRecursiveTask(arrayToCalculateSumOf);

		assertEquals(5, arrayChecksumWithArraysRecursiveTask.doCoreComputation());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatCoreComputationOnlyCalculatesOneElement() {
		int[] arrayToCalculateSumOf = new int[] { 5, 6 };
		ArrayChecksumWithArraysRecursiveTask arrayChecksumWithArraysRecursiveTask = new ArrayChecksumWithArraysRecursiveTask(arrayToCalculateSumOf);
		arrayChecksumWithArraysRecursiveTask.doCoreComputation();
	}

	@Test
	public void testSmallestWorkingUnit() {
		int[] arrayToCalculateSumOf = new int[] { 5 };
		ArrayChecksumWithArraysRecursiveTask arrayChecksumWithArraysRecursiveTask = new ArrayChecksumWithArraysRecursiveTask(arrayToCalculateSumOf);

		assertEquals(new Integer(5), arrayChecksumWithArraysRecursiveTask.compute());
	}

	@Test
	public void testThatSplittingTheTaskWorksAsExpected() {
		int[] arrayToCalculateSumOf = new int[] { 1, 2, 3 };
		ArrayChecksumWithArraysRecursiveTask arrayChecksumWithArraysRecursiveTask = new ArrayChecksumWithArraysRecursiveTask(arrayToCalculateSumOf);

		assertArrayEquals(new int[][] { { 1 }, { 2, 3 } }, arrayChecksumWithArraysRecursiveTask.splitArrayInParts());
	}

	@Test
	public void testThatSplittingTheTaskWorksAsExpecteForOneArrayElement() {
		int[] arrayToCalculateSumOf = new int[] { 1 };
		ArrayChecksumWithArraysRecursiveTask arrayChecksumWithArraysRecursiveTask = new ArrayChecksumWithArraysRecursiveTask(arrayToCalculateSumOf);

		assertArrayEquals(new int[][] { {}, { 1 } }, arrayChecksumWithArraysRecursiveTask.splitArrayInParts());
	}

	@Test
	public void testThatMergingResultBehavesAsExpected() {
		assertEquals(6, ArrayChecksumWithArraysRecursiveTask.mergeResults(1, 5));
	}

}
