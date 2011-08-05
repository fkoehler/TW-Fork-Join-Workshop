package com.thoughtworks.fjw.shortestpath;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ForkJoinPool;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

public class ParallelTest {

	@Test
	public void testShortestPathProblem() {
		// input: graph matrix
		// @formatter:off
		int[][] graphMatrix = new int[][] {
				//        0   1   2   3   4   5
				//        A   B   C   D   E   F
				/* A */{ -1, 10, -1, -1, -1, -1 },
				/* B */{ -1, -1,  8, 13, 24, 51 },
				/* C */{ -1, -1, -1, 14, -1, -1 },
				/* D */{ -1, -1, -1, -1,  9, -1 },
				/* E */{ -1, -1, -1, -1, -1, 17 },
				/* F */{ -1, -1, -1, -1, -1, -1 }
		};
		// @formatter:on

		//		Matrix m6 = MatrixFactory.sparse(ValueType.DOUBLE, 5, 5);
		//		graphMatrix = m6.toIntArray();
		//		System.out.println(m6);
		//
		//		graphMatrix[1][0] = -1;
		//		graphMatrix[1][1] = -1;
		//
		//		System.out.println(Arrays.toString(graphMatrix[0]));
		//		System.out.println(Arrays.toString(graphMatrix[1]));

		// SINGLE THREADED
		StopWatch stopWatch = new LoggingStopWatch("singlethread");
		ShortestPathServiceSeq shortestPathServiceSeq = new ShortestPathServiceSeq(graphMatrix, 0,
				graphMatrix.length - 1);
		shortestPathServiceSeq.getShortestDestination();
		stopWatch.stop();

		// MULTI THREADED
		stopWatch = new LoggingStopWatch("multithread");
		// every tasks takes one vertex to do the search for

		// create minimum distances array
		int[] currentMinimumDistances = new int[graphMatrix.length];
		for (int vertex = 0; vertex < graphMatrix.length; vertex++) {
			currentMinimumDistances[vertex] = 0;
		}

		ShortestPathCalculatorAction shortestPathCalculatorAction = new ShortestPathCalculatorAction(graphMatrix, 0,
				graphMatrix.length - 1, currentMinimumDistances);

		int nofProcessors = Runtime.getRuntime().availableProcessors();

		ForkJoinPool forkJoinPool = new ForkJoinPool(nofProcessors);
		forkJoinPool.invoke(shortestPathCalculatorAction);
		stopWatch.stop();

		assertThat(shortestPathCalculatorAction.getShortestDestination(), is(49));
	}

}
