package com.thoughtworks.fjw.search;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ParallelSearchTaskTest {
	private static Logger LOGGER = Logger.getLogger(ParallelSearchTaskTest.class.getCanonicalName());
	private String resourceName;

	@Before
	public void setUp() {
		resourceName = System.getProperty("user.dir") + "/src/com/thoughtworks/fjw/search/ManyQuickBrownFoxes.txt";
	}

	@Test
	public void shouldFindSearchStringMultipleTimes() throws IOException {
		ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		ParallelSearchTask task = new ParallelSearchTask(resourceName, "quick", 0, 1000);

		forkJoinPool.invoke(task);
		List<Integer> hits = task.join();

		LOGGER.info(hits.toString());
		Assert.assertEquals(6, hits.size());
	}

	@Test
	public void shouldReadPartialText() throws IOException {
		ParallelSearchTask task = new ParallelSearchTask(resourceName, "quick", 0, 13);

		String toBeSearchedIn = task.readText();
		LOGGER.info(toBeSearchedIn);

		Assert.assertEquals("The quick brown", toBeSearchedIn);
	}

	@Test
	public void shouldFindSearchStringMultipleTimesWhenUsingManyTasks() throws IOException {
		ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		ParallelSearchTask task = new ParallelSearchTask(resourceName, "quick", 0, 13);

		forkJoinPool.invoke(task);
		List<Integer> hits = task.join();

		LOGGER.info(hits.toString());
		Assert.assertEquals(6, hits.size());
	}

	@Test
	public void shouldRecogniseWhiteSpace() throws IOException {
		ParallelSearchTask task = new ParallelSearchTask(resourceName, "quick", 0, 13);

		Assert.assertEquals(true, task.isNeitherEndNorWhiteSpace('a'));
		Assert.assertEquals(false, task.isNeitherEndNorWhiteSpace(-1));
		Assert.assertEquals(false, task.isNeitherEndNorWhiteSpace(' '));
	}

}
