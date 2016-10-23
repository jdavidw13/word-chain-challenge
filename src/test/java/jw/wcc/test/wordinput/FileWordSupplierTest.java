package jw.wcc.test.wordinput;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import jw.wcc.wordinput.FileWordSupplier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 *
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class FileWordSupplierTest {
	private static final List<String> SAMPLE_WORDS = Arrays.asList("word1", "word2", "word3", "word4");
	private static final List<String> WHITESPACE = Arrays.asList(" ", "\t", "\n", "\r", "\r\n");

	private final Random random = new Random();

	private File wordListFile;

	/**
	 * Setup a test file for the FileWordSupplier to read from.
	 * Words in the file are separated by whitespace, and this method will create a random amount of whitespace after each word
	 * @throws IOException 
	 */
	@BeforeClass
	public void setupFileTestData() throws IOException {
		wordListFile = File.createTempFile("worldList", "txt");
		wordListFile.deleteOnExit();

		FileWriter fw = new FileWriter(wordListFile);
		for (String word : SAMPLE_WORDS) {
			fw.write(word);
			fw.write(getRandomWhitespace());
		}
		fw.close();
	}

	@Test
	public void testSupplyWords() {
		FileWordSupplier wordSupplier = new FileWordSupplier(wordListFile);
		List<String> actualWords = new LinkedList<>();
		for (String word : wordSupplier.getWords()) {
			actualWords.add(word);
		}

		assertTrue("expected words did not contain all actual words", SAMPLE_WORDS.containsAll(actualWords));
		assertTrue("actual words did not contain all expected words", actualWords.containsAll(SAMPLE_WORDS));
	}

	private String getRandomWhitespace() {
		String ws = "";
		for (int i = 0; i < random.nextInt(WHITESPACE.size()) + 1; i++) {
			ws += WHITESPACE.get(random.nextInt(WHITESPACE.size()));
		}
		return ws;
	}
}
