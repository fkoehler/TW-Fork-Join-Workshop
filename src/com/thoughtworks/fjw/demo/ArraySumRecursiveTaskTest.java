package com.thoughtworks.fjw.demo;

import static org.junit.Assert.*;

import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

public class ArraySumRecursiveTaskTest {

	@Test
	public void testThatCompleteTaskWorksAsExpected() {
		int[] arrayToCalculateSumOf = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }; // = 55

		ArraySumRecursiveTask arraySumRecursiveTask = new ArraySumRecursiveTask(arrayToCalculateSumOf);
		ForkJoinPool pool = new ForkJoinPool();
		int actual = pool.invoke(arraySumRecursiveTask);

		assertEquals(55, actual);
	}

	@Test
	public void testThatCoreComputationBehavesAsExpected() {
		int[] arrayToCalculateSumOf = new int[] { 5 };
		ArraySumRecursiveTask arraySumRecursiveTask = new ArraySumRecursiveTask(arrayToCalculateSumOf);

		assertEquals(new Integer(5), arraySumRecursiveTask.compute());
	}

	@Test
	public void testThatSplittingTheTaskWorksAsExpected() {
		int[] arrayToCalculateSumOf = new int[] { 1, 2, 3 };
		ArraySumRecursiveTask arraySumRecursiveTask = new ArraySumRecursiveTask(arrayToCalculateSumOf);

		assertArrayEquals(new int[] { 1 }, arraySumRecursiveTask.getLeftPart());
		assertArrayEquals(new int[] { 2, 3 }, arraySumRecursiveTask.getRightPart());
	}

	@Test
	public void testThatMergingResultBehavesAsExpected() {
		assertEquals(6, ArraySumRecursiveTask.mergeResults(1, 5));
	}

}
