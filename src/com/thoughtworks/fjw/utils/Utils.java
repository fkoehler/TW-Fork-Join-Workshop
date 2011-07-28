package com.thoughtworks.fjw.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

}
