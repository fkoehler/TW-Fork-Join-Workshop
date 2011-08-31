package com.thoughtworks.fjw.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Utils {

	public static void doCpuIntensiveCalculation() {
		MessageDigest digest = null;
		try {
			digest = java.security.MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		for (int i = 0; i <= 500; i++) {
			digest.update((byte) i);
			digest.digest();
		}
	}

	public static int[] buildRandomIntArray(final int size) {
		int[] arrayToCalculateSumOf = new int[size];
		Random generator = new Random();
		for (int i = 0; i < arrayToCalculateSumOf.length; i++) {
			arrayToCalculateSumOf[i] = generator.nextInt(Integer.MAX_VALUE);
		}
		return arrayToCalculateSumOf;
	}

	public static int[] buildRandomIntArray() {
		return buildRandomIntArray(20000);
	}

	public static int[] buildRandomIntArrayForLogging() {
		int[] arrayToCalculateSumOf = new int[10];
		Random generator = new Random();
		for (int i = 0; i < arrayToCalculateSumOf.length; i++) {
			arrayToCalculateSumOf[i] = generator.nextInt(500000);
		}
		return arrayToCalculateSumOf;
	}

	public static long calculateSumOfArray(final int[] arrayToCalculateSumOf) {
		long expected = 0;
		for (int value : arrayToCalculateSumOf) {
			expected += value;
		}

		return expected;
	}

}
