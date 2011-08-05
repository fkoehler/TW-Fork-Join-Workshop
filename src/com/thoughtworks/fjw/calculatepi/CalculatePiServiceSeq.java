package com.thoughtworks.fjw.calculatepi;

import java.util.Random;

public class CalculatePiServiceSeq {

	private final int nofPoints;

	public CalculatePiServiceSeq(final int nofPoints) {
		this.nofPoints = nofPoints;
	}

	public double calculatePi() {
		int circleCount = 0;
		double xCoordPoint, yCoordPoint;

		Random rand = new Random();

		for (int i = 1; i <= nofPoints; i++) {
			double x = Math.random();
			double y = Math.random();

			double magX = .5 - x;
			double magY = .5 - y;
			boolean inUnitCircle = Math.sqrt(magX * magX + magY * magY) < .5;
			if (inUnitCircle) {
				circleCount++;
			}

			//x^2 + y^2 < R^2
			/*
			xCoordPoint = rand.nextDouble() * 2 - 1.0;
			yCoordPoint = rand.nextDouble() * 2 - 1.0;

			double distance = Math.sqrt(Math.pow(xCoordPoint, 2) + Math.pow(yCoordPoint, 2));

			if (distance <= 1.0) {
			//				circleCount++;
			}*/
		}

		return 4.0 * circleCount / nofPoints;
	}
}
