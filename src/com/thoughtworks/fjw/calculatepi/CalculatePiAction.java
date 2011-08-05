package com.thoughtworks.fjw.calculatepi;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

public class CalculatePiAction extends RecursiveAction {

	private static final Logger LOGGER = Logger.getLogger(CalculatePiAction.class.getName());
	private final int threshold;
	private final int[] arrayToSort;

	public CalculatePiAction(final int[] arrayToSort, final int threshold) {
		this.arrayToSort = arrayToSort;
		this.threshold = threshold;
	}

	@Override
	protected void compute() {
		if (arrayToSort.length <= threshold) {
			// sequential sort
			Arrays.sort(arrayToSort);
			return;
		}

		// Sort halves in parallel
		int midpoint = arrayToSort.length / 2;
		int[] leftArray = Arrays.copyOfRange(arrayToSort, 0, midpoint);
		int[] rightArray = Arrays.copyOfRange(arrayToSort, midpoint, arrayToSort.length);

		CalculatePiAction left = new CalculatePiAction(leftArray, threshold);
		CalculatePiAction right = new CalculatePiAction(rightArray, threshold);

		invokeAll(left, right);

		// sequential merge
		//arrayToSort = CalculatePiServiceSeq.merge(left.getSortedArray(), right.getSortedArray());
	}

	public int[] getSortedArray() {
		return arrayToSort;
	}

}
