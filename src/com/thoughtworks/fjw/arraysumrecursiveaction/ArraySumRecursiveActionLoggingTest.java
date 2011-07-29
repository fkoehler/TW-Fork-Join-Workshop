package com.thoughtworks.fjw.arraysumrecursiveaction;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.thoughtworks.fjw.utils.Utils;

public class ArraySumRecursiveActionLoggingTest {

	public static StopWatch firstCalcStartWatch;

	@Test
	public void shouldCalculateTheSumOfAllArrayElements() throws InterruptedException, ExecutionException {
		int[] arrayToCalculateSumOf = Utils.buildRandomIntArray();

		//arrayToCalculateSumOf = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

		StopWatch stopWatch = new LoggingStopWatch("singlethread");
		long expected = 0;
		for (int value : arrayToCalculateSumOf) {
			Utils.doCpuIntensiveCalculation();

			expected += value;
		}
		stopWatch.stop();

		stopWatch = new LoggingStopWatch("multithread");
		firstCalcStartWatch = new LoggingStopWatch("calcstartwatch");

		//int nofProcessors = Runtime.getRuntime().availableProcessors();
		ForkJoinPool forkJoinPool = new ForkJoinPool(4);

		// arrange async execution, we need to get the result later via get()
		// let's start the calculation task a few times
		ArraySumRecursiveAction arraySumCalculator = new ArraySumRecursiveAction(arrayToCalculateSumOf);
		forkJoinPool.execute(arraySumCalculator);

		arraySumCalculator.get();

		stopWatch.stop();

		assertThat(arraySumCalculator.getResult(), is(expected));
	}

}
