package com.thoughtworks.fjw.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.thoughtworks.fjw.utils.Utils;

public class SimpleStringSearchTest {
	private static Logger LOGGER = Logger.getLogger(SimpleStringSearchTest.class.getCanonicalName());

	// this line helps counting ---------------- 0.........10........20........30........40.. 
	private static final String ANIMAL_WISDOM = "The quick brown fox jumps over the lazy dog.";
	private static final String SPLITTABLE_ANIMAL_WISDOM = "The quick brown fox # jumps over the lazy dog.";

	@Test
	public void shouldFindString() {

		// taking a string apart
		String[] parts = SPLITTABLE_ANIMAL_WISDOM.split("# ");
		StringBuilder builder = new StringBuilder();
		for (String part : parts) {
			LOGGER.info(part);
			builder.append(part);
		}
		String text = builder.toString();
		LOGGER.info(text);

		// finding strings within a string
		Assert.assertEquals(0, text.indexOf("The quick"));
		Assert.assertEquals(10, text.indexOf("brown fox"));

		Assert.assertEquals(10, text.indexOf("brown fox", 8));
		Assert.assertEquals(20, text.indexOf("jumps"));

		Assert.assertEquals(26, text.indexOf("over"));
		Assert.assertEquals(30, text.indexOf(" the lazy"));

		Assert.assertEquals(40, text.indexOf("dog"));

		// experimenting with relative indexes
		Assert.assertEquals(26, parts[0].length() + parts[1].indexOf("over"));
	}

	@Test
	public void shouldReadFromFile() throws IOException {
		String resourceName = System.getProperty("user.dir") + "/src/com/thoughtworks/fjw/search/TheQuickBrownFox.txt";

		Assert.assertEquals(ANIMAL_WISDOM, readFileByLine(resourceName));
		Assert.assertEquals(ANIMAL_WISDOM, readFileByCharacter(resourceName));
	}

	private String readFileByCharacter(final String resourceName) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader inReader = Utils.getBufferedReaderByNIO(resourceName);

		int aChar = -1;
		while ((aChar = inReader.read()) != -1) {
			builder.append((char) aChar);
			System.out.print((char) aChar);
		}

		inReader.close();
		return builder.toString();
	}

	private String readFileByLine(final String resourceName) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader inReader = Utils.getBufferedReaderOldFashionedly(resourceName);

		String line = null;
		while ((line = inReader.readLine()) != null) {
			builder.append(line);
			System.out.println(line);
		}

		inReader.close();
		return builder.toString();
	}

	@Test
	public void revisitAsciiCodes() {
		char aChar = 32;
		LOGGER.info("The character belonging to 32 is '" + aChar + "'");
		Assert.assertEquals(" ", "" + aChar);
	}
}
