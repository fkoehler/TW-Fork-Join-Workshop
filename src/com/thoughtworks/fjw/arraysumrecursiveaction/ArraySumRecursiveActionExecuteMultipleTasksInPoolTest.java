package com.thoughtworks.fjw.arraysumrecursiveaction;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.thoughtworks.fjw.utils.Utils;

public class ArraySumRecursiveActionExecuteMultipleTasksInPoolTest {

	@Test
	public void shouldCalculateTheSumOfAllArrayElements() throws InterruptedException, ExecutionException {
		int[] arrayToCalculateSumOf = Utils.buildRandomIntArray();

		StopWatch stopWatch = new LoggingStopWatch("singlethread");
		long expected = 0;
		for (int value : arrayToCalculateSumOf) {
			Utils.doCpuIntensiveCalculation();

			expected += value;
		}
		stopWatch.stop();

		stopWatch = new LoggingStopWatch("multithread");

		int nofProcessors = Runtime.getRuntime().availableProcessors();
		ForkJoinPool forkJoinPool = new ForkJoinPool(nofProcessors);

		// arrange async execution, we need to get the result later via get()
		// let's start the calculation task a few times
		ArraySumRecursiveAction arraySumCalculator = new ArraySumRecursiveAction(arrayToCalculateSumOf);
		forkJoinPool.execute(arraySumCalculator);

		ArraySumRecursiveAction arraySumCalculator2 = new ArraySumRecursiveAction(arrayToCalculateSumOf);
		forkJoinPool.execute(arraySumCalculator2);

		ArraySumRecursiveAction arraySumCalculator3 = new ArraySumRecursiveAction(arrayToCalculateSumOf);
		forkJoinPool.execute(arraySumCalculator3);

		ArraySumRecursiveAction arraySumCalculator4 = new ArraySumRecursiveAction(arrayToCalculateSumOf);
		forkJoinPool.execute(arraySumCalculator4);

		arraySumCalculator.get();
		arraySumCalculator2.get();
		arraySumCalculator3.get();
		arraySumCalculator4.get();

		stopWatch.stop();

		assertThat(arraySumCalculator.getResult(), is(expected));
		assertThat(arraySumCalculator2.getResult(), is(expected));
		assertThat(arraySumCalculator3.getResult(), is(expected));
		assertThat(arraySumCalculator4.getResult(), is(expected));
	}

}
