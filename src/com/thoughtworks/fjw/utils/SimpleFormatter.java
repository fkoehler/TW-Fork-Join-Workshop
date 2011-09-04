package com.thoughtworks.fjw.utils;

import java.util.logging.LogRecord;

public class SimpleFormatter extends java.util.logging.Formatter {

	@Override
	public String format(final LogRecord record) {
		return record.getMessage() + "\n";
	}

}
