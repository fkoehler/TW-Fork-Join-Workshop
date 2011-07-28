package com.thoughtworks.fjw.arraysumrecursiveaction;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

import com.thoughtworks.fjw.utils.Utils;

public class ArraySumRecursiveActionForkJoin extends RecursiveAction {

	private long result;
	private final int[] arrayToCalculateSumOf;

	public ArraySumRecursiveActionForkJoin(final int[] arrayToCalculateSumOf) {
		this.arrayToCalculateSumOf = arrayToCalculateSumOf;
	}

	public long getResult() {
		return result;
	}

	@Override
	protected void compute() {
		if (arrayToCalculateSumOf.length == 1) {
			// working unit is small enough, do the work
			result = arrayToCalculateSumOf[0];
		} else {
			// split up the work into smaller parts (fork)
			int midpoint = arrayToCalculateSumOf.length / 2;

			int[] l1 = Arrays.copyOfRange(arrayToCalculateSumOf, 0, midpoint);
			int[] l2 = Arrays.copyOfRange(arrayToCalculateSumOf, midpoint, arrayToCalculateSumOf.length);

			ArraySumRecursiveActionForkJoin s1 = new ArraySumRecursiveActionForkJoin(l1);
			ArraySumRecursiveActionForkJoin s2 = new ArraySumRecursiveActionForkJoin(l2);

			s1.fork();
			s2.compute();

			// join the calculated parts and do the real calculation
			s1.join();
			Utils.doCpuIntensiveCalculation();

			result += s1.result + s2.result;
		}
	}

}
