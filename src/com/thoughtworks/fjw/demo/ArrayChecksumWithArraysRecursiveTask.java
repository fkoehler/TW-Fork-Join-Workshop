package com.thoughtworks.fjw.demo;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class ArrayChecksumWithArraysRecursiveTask extends RecursiveTask<Integer> {

	private final int[] arrayToCalculateSumOf;
	private ArrayChecksumWithArraysRecursiveTask leftTask;
	private ArrayChecksumWithArraysRecursiveTask rightTask;

	public ArrayChecksumWithArraysRecursiveTask(final int[] arrayToCalculateSumOf) {
		this.arrayToCalculateSumOf = arrayToCalculateSumOf;
	}

	@Override
	protected Integer compute() {
		if (workingUnitSmallEnough()) {
			return doCoreComputation();
		}

		forkTasks();
		return joinTasks();
	}

	private boolean workingUnitSmallEnough() {
		return arrayToCalculateSumOf.length == 1;
	}

	int doCoreComputation() {
		if (arrayToCalculateSumOf.length == 1) {
			return arrayToCalculateSumOf[0];
		}

		throw new IllegalArgumentException();
	}

	private void forkTasks() {
		int[][] parts = splitArrayInParts();

		leftTask = new ArrayChecksumWithArraysRecursiveTask(parts[0]);
		rightTask = new ArrayChecksumWithArraysRecursiveTask(parts[1]);

		leftTask.fork();
		rightTask.fork();
	}

	public int[][] splitArrayInParts() {
		int midpoint = arrayToCalculateSumOf.length / 2;

		int[] left = Arrays.copyOfRange(arrayToCalculateSumOf, 0, midpoint);
		int[] right = Arrays.copyOfRange(arrayToCalculateSumOf, midpoint, arrayToCalculateSumOf.length);

		return new int[][] { left, right };
	}

	private int joinTasks() {
		return mergeResults(leftTask.join(), rightTask.join());
	}

	static int mergeResults(final int leftResult, final int rightResult) {
		return leftResult + rightResult;
	}

}