package jw.wcc.wordinput;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WordSupplier implementation that reads the entire content of a file as a UTF8 string
 * and splits the string via a a whitespace regex "\\s+".  Each token is considered a word.<br/>
 * <b>NOTE</b> This class does not attempt to handle files that can't be fully read into memory.
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class FileWordSupplier implements WordSupplier {
	private final File file;

	private List<String> readWords;

	public FileWordSupplier(File file) {
		this.file = file;
	}

	@Override
	public Iterable<String> getWords() {
		if (readWords == null) {
			try {
				readWords = readWordsFromFile(file);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		return readWords;
	}

	private List<String> readWordsFromFile(File file) throws IOException {
		String fileText = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
		List<String> words = Arrays.asList(fileText.split("\\s+"));
		return words;
	}
}
