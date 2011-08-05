package com.thoughtworks.fjw.calculatepi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

public class SequentialTest {

	@Test
	public void testShortestPathProblem() {
		StopWatch stopWatch = new LoggingStopWatch("singlethread-calc pi");
		CalculatePiServiceSeq calculatePieService = new CalculatePiServiceSeq(9000000);
		double Pi = calculatePieService.calculatePi();
		stopWatch.stop();

		System.out.println(Math.PI);
		System.out.println(Pi);

		assertEquals(3.14159d, Pi, 0.00001);
	}

}
