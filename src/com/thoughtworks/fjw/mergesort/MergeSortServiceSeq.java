package com.thoughtworks.fjw.mergesort;

import java.util.Arrays;

public class MergeSortServiceSeq {

	private final int[] arrayToSort;
	private final int threshold;

	public MergeSortServiceSeq(final int[] arrayToSort, final int threshold) {
		this.arrayToSort = arrayToSort;
		this.threshold = threshold;
	}

	public int[] sequentialSort() {
		return sequentialSort(arrayToSort);
	}

	public int[] sequentialSort(final int[] arrayToSort) {
		if (arrayToSort.length < threshold) {
			Arrays.sort(arrayToSort);
			return arrayToSort;
		}

		int midpoint = arrayToSort.length / 2;

		int[] leftArray = Arrays.copyOfRange(arrayToSort, 0, midpoint);
		int[] rightArray = Arrays.copyOfRange(arrayToSort, midpoint, arrayToSort.length);

		leftArray = sequentialSort(leftArray);
		rightArray = sequentialSort(rightArray);

		return merge(leftArray, rightArray);
	}

	public static int[] merge(final int[] leftArray, final int[] rightArray) {
		int[] mergedArray = new int[leftArray.length + rightArray.length];
		int mergedArrayPos = 0;
		int leftArrayPos = 0;
		int rightArrayPos = 0;
		while (leftArrayPos < leftArray.length && rightArrayPos < rightArray.length) {
			if (leftArray[leftArrayPos] <= rightArray[rightArrayPos]) {
				mergedArray[mergedArrayPos] = leftArray[leftArrayPos];
				leftArrayPos++;
			} else {
				mergedArray[mergedArrayPos] = rightArray[rightArrayPos];
				rightArrayPos++;
			}
			mergedArrayPos++;
		}

		while (leftArrayPos < leftArray.length) {
			mergedArray[mergedArrayPos] = leftArray[leftArrayPos];
			leftArrayPos++;
			mergedArrayPos++;
		}

		while (rightArrayPos < rightArray.length) {
			mergedArray[mergedArrayPos] = rightArray[rightArrayPos];
			rightArrayPos++;
			mergedArrayPos++;
		}

		return mergedArray;

		/*
		 neueListe
		   solange (linkeListe und rechteListe nicht leer)
		   |    falls (erstes Element der linkeListe <= erstes Element der rechteListe)
		   |    dann füge erstes Element linkeListe in die neueListe hinten ein und entferne es aus linkeListe
		   |    sonst füge erstes Element rechteListe in die neueListe hinten ein und entferne es aus rechteListe
		   solange_ende
		   solange (linkeListe nicht leer)
		   |    füge erstes Element linkeListe in die neueListe hinten ein und entferne es aus linkeListe
		   solange_ende
		   solange (rechteListe nicht leer)
		   |    füge erstes Element rechteListe in die neueListe hinten ein und entferne es aus rechteListe
		   solange_ende
		   antworte neueListe
		 */
	}

}
