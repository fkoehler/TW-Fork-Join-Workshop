package com.thoughtworks.fjw.demo;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

public class ArraySumRecursiveTaskTest {

	@Test
	public void shouldCalculateTheSumOfAllArrayElements() {
		int[] arrayToCalcSumOf = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }; // = 55

		ArraySumRecursiveTask arraySumRecursiveTask = new ArraySumRecursiveTask(arrayToCalcSumOf);
		ForkJoinPool forkJoinPool = new ForkJoinPool();

		int actual = forkJoinPool.invoke(arraySumRecursiveTask);

		assertEquals(55, actual);
	}

}
