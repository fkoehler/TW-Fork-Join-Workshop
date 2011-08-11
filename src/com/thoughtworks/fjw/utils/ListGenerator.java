package com.thoughtworks.fjw.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ListGenerator {

	public List<Integer> createListOfNonNegativeIntegers(final int size) {
		return createListOfNonNegativeIntegers(size, Integer.MAX_VALUE);
	}

	public List<Integer> createListOfNonNegativeIntegers(final int size, final int max) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			result.add(new Integer((int) (Math.random() * max)));
		}
		return result;
	}

	public List<Integer> createListOfNonNegativeIntegers(final int size, final int min, final int max) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			result.add(new Integer(((int) (Math.random() * (max - min)) + min)));
		}
		return result;
	}

	public boolean isListSorted(final List<Integer> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i).compareTo(list.get(i + 1)) > 0) {
				return false;
			}
		}
		return true;
	}

	public SortedMap<Integer, List<Integer>> createBucketMap(final int bucketCount, final int bucketSize) {
		SortedMap<Integer, List<Integer>> result = new TreeMap<Integer, List<Integer>>();
		int width = 42;
		int min = 0;
		int max = 0;
		for (int i = 0; i < bucketCount; i++) {
			min = i * width;
			max = (i + 1) * width;
			result.put(new Integer(min), createListOfNonNegativeIntegers(bucketSize, min, max));
		}
		return result;
	}

	public List<String> createListOfStrings(final int size, final int length) {
		List<String> result = new ArrayList<String>();
		StringBuilder builder = null;
		for (int i = 0; i < size; i++) {
			builder = new StringBuilder(length);
			for (int j = 0; j < length; j++) {
				builder.append(createRandomCharacter());
			}
			result.add(builder.toString());
		}
		return result;
	}

	private Character createRandomCharacter() {
		return new Character((char) (65 + (int) (Math.random() * 26)));
	}

}
