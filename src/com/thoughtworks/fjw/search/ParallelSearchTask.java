package com.thoughtworks.fjw.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

import com.thoughtworks.fjw.utils.Utils;

public class ParallelSearchTask extends RecursiveTask<List<Integer>> {
	private static final Logger LOGGER = Logger.getLogger(ParallelSearchTask.class.getCanonicalName());

	private String fileName;
	private BufferedReader inReader;
	private int offset;
	private int recommendedBiteSize;
	private String toBeSearchedIn;
	private String toSearchFor;

	public ParallelSearchTask(final String fileName, final String toSearchFor, final int offset,
			final int recommendedBiteSize) throws IOException {
		super();

		this.recommendedBiteSize = recommendedBiteSize;
		this.fileName = fileName;
		this.offset = offset;
		this.toSearchFor = toSearchFor;

		/*
		 * Each task has its own reader, i.e. when creating multiple tasks there are many independent instances
		 * that are reading from the same file.
		 * 
		 * Reading from a task violates the recommendations that are documented in the API description of the
		 * java.util.concurrent.ForkJoinTask class.
		 * 
		 * Alternatively, the tasks could share a common reader. This approach requires synchronizing access to the
		 * shared reader instance though. 
		 */
		inReader = Utils.getBufferedReaderByNIO(fileName);
		inReader.skip(offset);

	}

	@Override
	/*
	 * Each invocation of the compute method looks for the search string in the part of the text allocated to a 
	 * given ParallelSearchTask object. Before doing so, the object may instantiate a new task to cover the remainder
	 * of the overall text to be searched.
	 * 
	 * The task objects used for searching the overall text are instantiated in a one-by-one fashion (as opposed to
	 * instantiating task objects in an avalanche-style, recursive manner). This design is driven by the fact that 
	 * the decision to instantiate a new task is only taken after having read a portion of the text to search.
	 */
	protected List<Integer> compute() {
		List<Integer> hits = new ArrayList<Integer>();

		/*
		 * Read a portion of the text to be searched and - possibly - invoke another task
		 */
		ParallelSearchTask nextTask = null;
		try {
			nextTask = readTextAndInvokeNextTask();
		} catch (IOException ioe) {
			/*
			 * Doh, compute must not throw checked exceptions, therefore wrapping the IOException in a RuntimeException
			 */
			throw new RuntimeException(ioe);
		}
		LOGGER.info(toString());

		/*
		 * Search the portion of the text allocated to this  task
		 */
		hits.addAll(searchStringToBeSearched());

		/*
		 * Add results from the next tasks to local results
		 */
		if (nextTask != null) {
			hits.addAll(nextTask.join());
		}

		return hits;
	}

	List<Integer> searchStringToBeSearched() {
		LOGGER.info("searching for search string in '" + toBeSearchedIn + "'");
		List<Integer> hits = new ArrayList<Integer>();
		int index = -1;
		while ((index = toBeSearchedIn.indexOf(toSearchFor, index + 1)) != -1) {
			LOGGER.info("found search string at index = " + index);
			hits.add(new Integer(index + offset));
		}
		return hits;
	}

	ParallelSearchTask readTextAndInvokeNextTask() throws IOException {
		ParallelSearchTask nextTask = null;
		try {
			/*
			 * Read a chunk of the text to be searched
			 */
			toBeSearchedIn = readText();

			/*
			 * Kick-start a new task if the text to be searched has not been exhausted yet
			 */
			if (anotherSearchTaskIsRequired()) {
				nextTask = new ParallelSearchTask(fileName, toSearchFor, offset + getActualBiteSize(),
						recommendedBiteSize);
				nextTask.invoke();
			}

		} finally {
			inReader.close();
		}

		return nextTask;
	}

	String readText() throws IOException {
		StringBuilder builder = new StringBuilder();
		int aChar = -1;

		/*
		 * Read text from the file until either the file is exhausted or the text allocated to this task becomes too long
		 */
		while (builder.length() < recommendedBiteSize && (aChar = inReader.read()) != 1) {
			builder.append((char) aChar);
			System.out.print((char) aChar);
		}

		/*
		 * Continue reading from the file until the next whitespace character is found
		 */
		while (isNeitherEndNorWhiteSpace(aChar = inReader.read())) {
			builder.append((char) aChar);
		}

		return builder.toString();
	}

	/*
	 * Simplified implementation that checks for spaces (and having reached the end of the file) only
	 */
	boolean isNeitherEndNorWhiteSpace(final int aChar) {
		return aChar != -1 && aChar != 32;
	}

	private boolean anotherSearchTaskIsRequired() throws IOException {
		if (inReader.read() != -1) {
			return true;
		}
		return false;
	}

	private int getActualBiteSize() {
		if (toBeSearchedIn != null) {
			return toBeSearchedIn.length();
		}
		return 0;
	}

	@Override
	public String toString() {
		return "ParallelSearchTask [actualBiteSize=" + getActualBiteSize() + ", fileName=" + fileName + ", inReader="
				+ inReader + ", offset=" + offset + ", recommendedBiteSize=" + recommendedBiteSize
				+ ", toBeSearchedIn=" + toBeSearchedIn + ", toSearchFor=" + toSearchFor + "]";
	}

}
