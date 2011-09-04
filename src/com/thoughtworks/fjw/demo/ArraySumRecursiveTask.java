package com.thoughtworks.fjw.demo;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class ArraySumRecursiveTask extends RecursiveTask<Integer> {

	private final int[] arrayToCalculateSumOf;
	private ArraySumRecursiveTask leftTask;
	private ArraySumRecursiveTask rightTask;

	public ArraySumRecursiveTask(final int[] arrayToCalculateSumOf) {
		this.arrayToCalculateSumOf = arrayToCalculateSumOf;
	}

	@Override
	protected Integer compute() {
		if (workingUnitSmallEnough()) {
			return computeDirectly();
		}

		forkTasks();
		return joinTasks();
	}

	private boolean workingUnitSmallEnough() {
		return arrayToCalculateSumOf.length == 1;
	}

	private int computeDirectly() {
		return arrayToCalculateSumOf[0];
	}

	private void forkTasks() {
		int[] left = getLeftPart();
		int[] right = getRightPart();

		leftTask = new ArraySumRecursiveTask(left);
		rightTask = new ArraySumRecursiveTask(right);

		leftTask.fork();
		rightTask.fork();
	}

	int[] getLeftPart() {
		int midpoint = arrayToCalculateSumOf.length / 2;
		return Arrays.copyOfRange(arrayToCalculateSumOf, 0, midpoint);
	}

	int[] getRightPart() {
		int midpoint = arrayToCalculateSumOf.length / 2;
		return Arrays.copyOfRange(arrayToCalculateSumOf, midpoint, arrayToCalculateSumOf.length);
	}

	private int joinTasks() {
		return mergeResults(leftTask.join(), rightTask.join());
	}

	static int mergeResults(final int leftResult, final int rightResult) {
		return leftResult + rightResult;
	}

}