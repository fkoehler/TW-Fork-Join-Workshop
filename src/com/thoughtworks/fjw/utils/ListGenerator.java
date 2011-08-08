package com.thoughtworks.fjw.utils;

import java.util.ArrayList;
import java.util.List;

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
