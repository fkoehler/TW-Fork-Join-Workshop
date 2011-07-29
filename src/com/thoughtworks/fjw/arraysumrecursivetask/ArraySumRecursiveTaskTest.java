package com.thoughtworks.fjw.arraysumrecursivetask;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ForkJoinPool;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.thoughtworks.fjw.utils.Utils;

public class ArraySumRecursiveTaskTest {

	@Test
	public void shouldCalculateTheSumOfAllArrayElements() {
		int[] arrayToCalculateSumOf = Utils.buildRandomIntArray();

		StopWatch stopWatch = new LoggingStopWatch("singlethread");
		long expected = 0;
		for (int value : arrayToCalculateSumOf) {
			Utils.doCpuIntensiveCalculation();

			expected += value;
		}
		stopWatch.stop();

		stopWatch = new LoggingStopWatch("multithread");
		ArraySumRecursiveTask arraySumCalculator = new ArraySumRecursiveTask(arrayToCalculateSumOf);

		int nofProcessors = Runtime.getRuntime().availableProcessors();

		ForkJoinPool forkJoinPool = new ForkJoinPool(nofProcessors);

		long result = forkJoinPool.invoke(arraySumCalculator);
		stopWatch.stop();

		assertThat(result, is(expected));
	}

}
