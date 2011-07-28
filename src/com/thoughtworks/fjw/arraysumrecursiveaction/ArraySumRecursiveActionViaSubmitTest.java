package com.thoughtworks.fjw.arraysumrecursiveaction;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.thoughtworks.fjw.utils.Utils;

public class ArraySumRecursiveActionViaSubmitTest {

	@Test
	public void shouldCalculateTheSumOfAllArrayElements() throws InterruptedException, ExecutionException {
		int[] arrayToCalculateSumOf = new int[20000];
		Random generator = new Random();
		for (int i = 0; i < arrayToCalculateSumOf.length; i++) {
			arrayToCalculateSumOf[i] = generator.nextInt(500000);
		}

		StopWatch stopWatch = new LoggingStopWatch("singlethread");
		long expected = 0;
		for (int value : arrayToCalculateSumOf) {
			Utils.doCpuIntensiveCalculation();

			expected += value;
		}
		stopWatch.stop();

		stopWatch = new LoggingStopWatch("multithread");
		ArraySumRecursiveAction arraySumCalculator = new ArraySumRecursiveAction(arrayToCalculateSumOf);

		int nofProcessors = Runtime.getRuntime().availableProcessors();

		ForkJoinPool forkJoinPool = new ForkJoinPool(nofProcessors);

		// arrange exec and obtain Future
		ForkJoinTask<Void> submittedTask = forkJoinPool.submit(arraySumCalculator);

		submittedTask.get();
		stopWatch.stop();

		assertThat(arraySumCalculator.getResult(), is(expected));
	}

}
