package com.thoughtworks.fjw.demo;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class ArraySumRecursiveTask extends RecursiveTask<Integer> {

	private final int[] arrayToCalculateSumOf;

	public ArraySumRecursiveTask(final int[] arrayToCalculateSumOf) {
		this.arrayToCalculateSumOf = arrayToCalculateSumOf;
	}

	@Override
	protected Integer compute() {
		// if (my portion of the work is small enough)
		if (arrayToCalculateSumOf.length == 2) {
			// do the work directly
			return arrayToCalculateSumOf[0] + arrayToCalculateSumOf[1];
		} else if (arrayToCalculateSumOf.length == 1) {
			// do the work directly
			return arrayToCalculateSumOf[0];
		}

		//  split my work into two pieces
		int midpoint = arrayToCalculateSumOf.length / 2;
		int[] left = Arrays.copyOfRange(arrayToCalculateSumOf, 0, midpoint);
		ArraySumRecursiveTask leftTask = new ArraySumRecursiveTask(left);

		int[] right = Arrays.copyOfRange(arrayToCalculateSumOf, midpoint, arrayToCalculateSumOf.length);
		ArraySumRecursiveTask rightTask = new ArraySumRecursiveTask(right);

		//  invoke the two pieces and wait for the results and merge them
		leftTask.fork();

		int rightResult = rightTask.compute();
		int leftResult = leftTask.join();

		// merge results
		return leftResult + rightResult;
	}

}