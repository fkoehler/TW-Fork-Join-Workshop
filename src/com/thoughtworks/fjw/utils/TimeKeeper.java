package com.thoughtworks.fjw.utils;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.thoughtworks.fjw.bucketsort.ActionCode;

public class TimeKeeper {
	static final String SEPARATOR = " | ";
	private static final String DEFAULT_PATTERN = "-ParallelJavaWorkshop.csv";

	private FileHandler fileHandler;

	public TimeKeeper() throws SecurityException, IOException {
		this(new Date().toString() + DEFAULT_PATTERN);
	}

	public TimeKeeper(final String aPattern) throws SecurityException, IOException {
		fileHandler = new FileHandler(aPattern);
		fileHandler.setFormatter(new SimpleFormatter());
	}

	public void addFileHandlerToLogger(final Logger aLogger) throws SecurityException, IOException {
		aLogger.addHandler(fileHandler);
	}

	public static String createLogMessage(final String context, final long threadId, final long startTime,
			final long stopTime) {
		StringBuilder builder = new StringBuilder();

		builder.append(context);
		builder.append(SEPARATOR);
		builder.append(threadId);
		builder.append(SEPARATOR);
		builder.append(startTime);
		builder.append(SEPARATOR);
		builder.append(stopTime);
		builder.append(SEPARATOR);
		builder.append((stopTime - startTime));

		return builder.toString();
	}

	public static String createLogMessage(final String context, final long threadId, final long startTime,
			final ActionCode code) {
		StringBuilder builder = new StringBuilder();

		builder.append(context);
		builder.append(SEPARATOR);
		builder.append(threadId);
		builder.append(SEPARATOR);
		builder.append(startTime);

		return builder.toString();
	}
}
