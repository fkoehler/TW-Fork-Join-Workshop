package com.thoughtworks.fjw.arraysumrecursivetask;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

import com.thoughtworks.fjw.utils.Utils;

public class ArraySumRecursiveTask extends RecursiveTask<Long> {

	private static final long serialVersionUID = 1L;
	private final int[] arrayToCalculateSumOf;

	public ArraySumRecursiveTask(final int[] arrayToCalculateSumOf) {
		this.arrayToCalculateSumOf = arrayToCalculateSumOf;
	}

	@Override
	protected Long compute() {
		if (arrayToCalculateSumOf.length == 1) {
			// working unit is small enough, do the work
			return (long) arrayToCalculateSumOf[0];
		}

		// split up the work into smaller parts (fork)
		int midpoint = arrayToCalculateSumOf.length / 2;

		int[] l1 = Arrays.copyOfRange(arrayToCalculateSumOf, 0, midpoint);
		int[] l2 = Arrays.copyOfRange(arrayToCalculateSumOf, midpoint, arrayToCalculateSumOf.length);

		ArraySumRecursiveTask s1 = new ArraySumRecursiveTask(l1);
		ArraySumRecursiveTask s2 = new ArraySumRecursiveTask(l2);

		s1.fork();
		long result2 = s2.compute();

		// join the calculated parts and do the real calculation
		long result1 = s1.join();

		Utils.doCpuIntensiveCalculation();

		// merge/join the calculation
		return result1 + result2;
	}

}
