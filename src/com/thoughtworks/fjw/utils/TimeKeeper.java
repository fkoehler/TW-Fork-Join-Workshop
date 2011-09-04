package com.thoughtworks.fjw.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TimeKeeper {

	static final String SEPARATOR = ";";
	private static final String DEFAULT_PATTERN = "-ParallelJavaWorkshop.csv";
	private static final String N_A = "n/a";

	private FileHandler fileHandler;

	public TimeKeeper() throws SecurityException, IOException {
		this(getLogFileName());
	}

	private static String getLogFileName() {
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String dateTime = df.format(new Date());

		return sanitizeFilename(dateTime) + DEFAULT_PATTERN;
	}

	private static String sanitizeFilename(final String name) {
		return name.replaceAll("[ :\\\\/*?|<>]", "_");
	}

	public TimeKeeper(final String aPattern) throws SecurityException, IOException {
		if (fileHandler != null) {
			fileHandler.close();
		}

		fileHandler = new FileHandler(aPattern);
		fileHandler.setFormatter(new SimpleFormatter());
	}

	public void addFileHandlerToLogger(final Logger aLogger) throws SecurityException, IOException {
		aLogger.addHandler(fileHandler);
	}

	public FileHandler getFileHandler() {
		return fileHandler;
	}

	public static String createLogMessage(final String context, final long threadId, final long startTime,
			final long stopTime, final LogCode aCode) {
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
		builder.append(SEPARATOR);
		builder.append(aCode);

		return builder.toString();
	}

	public static String createLogTitle(final String contextTitle, final String threadIdTitle,
			final String startTimeTitle, final String stopTimeTitle, final String diffTitle, final String aCodeTitle) {
		StringBuilder builder = new StringBuilder();

		builder.append(contextTitle);
		builder.append(SEPARATOR);
		builder.append(threadIdTitle);
		builder.append(SEPARATOR);
		builder.append(startTimeTitle);
		builder.append(SEPARATOR);
		builder.append(stopTimeTitle);
		builder.append(SEPARATOR);
		builder.append(diffTitle);
		builder.append(SEPARATOR);
		builder.append(aCodeTitle);

		return builder.toString();
	}

	public static String createLogMessage(final String context, final long threadId, final long startTime,
			final LogCode aCode) {
		StringBuilder builder = new StringBuilder();

		builder.append(context);
		builder.append(SEPARATOR);
		builder.append(threadId);
		builder.append(SEPARATOR);
		builder.append(startTime);
		builder.append(SEPARATOR);
		builder.append(N_A);
		builder.append(SEPARATOR);
		builder.append(N_A);
		builder.append(SEPARATOR);
		builder.append(aCode);

		return builder.toString();
	}

}
