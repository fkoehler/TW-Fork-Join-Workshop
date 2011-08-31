package com.thoughtworks.fjw.utils;

import java.util.logging.Logger;

public class TimeKeeper {
	static final String SEPARATOR = " | ";

	public static void logTimes(final Logger logger, final String context, final long threadId, final long startTime,
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
		builder.append(stopTime - startTime);
		logger.info(builder.toString());
	}

	public static void logTimes(final Logger logger, final String context, final long threadId, final long startTime,
			final ActionCode code) {
		StringBuilder builder = new StringBuilder();
		builder.append(context);
		builder.append(SEPARATOR);
		builder.append(threadId);
		builder.append(SEPARATOR);
		builder.append(startTime);
		logger.info(builder.toString());
	}

}
