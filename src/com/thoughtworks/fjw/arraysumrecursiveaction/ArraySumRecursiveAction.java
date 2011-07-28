package com.thoughtworks.fjw.arraysumrecursiveaction;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

import com.thoughtworks.fjw.utils.Utils;

public class ArraySumRecursiveAction extends RecursiveAction {

	private long result;
	private final int[] arrayToCalculateSumOf;

	public ArraySumRecursiveAction(final int[] arrayToCalculateSumOf) {
		this.arrayToCalculateSumOf = arrayToCalculateSumOf;
	}

	public long getResult() {
		return result;
	}

	// TODO write a test for this particular implementation
	//      can i call the task atomatic
	// TODO mock the fork/join stuff
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

			ArraySumRecursiveAction s1 = new ArraySumRecursiveAction(l1);
			ArraySumRecursiveAction s2 = new ArraySumRecursiveAction(l2);

			invokeAll(s1, s2);

			// join the calculated parts and do the real calculation
			Utils.doCpuIntensiveCalculation();

			result += s1.result + s2.result;
		}
	}

}
