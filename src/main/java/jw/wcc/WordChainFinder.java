package jw.wcc;

import java.io.File;
import java.nio.file.Paths;
import java.util.Set;
import jw.wcc.wordchain.WordChain;
import jw.wcc.wordchain.WordChainBuilder;
import jw.wcc.wordchain.wordbuckets.CharacterBucketWordChainBuilder;
import jw.wcc.wordinput.FileWordSupplier;

/**
 * Main entry point for invoking the WordChainBuilder system
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class WordChainFinder {
	private static final int NO_WORD_FILE = -1;
	private static final int INVALID_WORD_FILE = -2;

	public static void main(String[] args) {
		if (args.length < 1) {
			printUsage();
			System.exit(NO_WORD_FILE);
		}
		String filePath = args[0];

		File wordFile = null;
		try {
			wordFile = Paths.get(filePath).toFile();
			if (!wordFile.isFile()) {
				System.err.printf("%s is not a file\n", filePath);
				System.exit(INVALID_WORD_FILE);
			}
		} catch (UnsupportedOperationException e) {
			System.err.printf("Invalid file %s\n", filePath);
			e.printStackTrace();
			System.exit(INVALID_WORD_FILE);
		}

		System.out.println("Reading file...");
		long readStart = System.currentTimeMillis();
		FileWordSupplier wordSupplier = new FileWordSupplier(wordFile);
		wordSupplier.readWords();
		long readTime = System.currentTimeMillis() - readStart;
		System.out.printf("Read file in %dms\n", readTime);

		System.out.println("Building word chains...");
		long chainBuildStart = System.currentTimeMillis();
		WordChainBuilder wcb = new CharacterBucketWordChainBuilder();
		Set<WordChain> wordChains = wcb.buildLongestWordChains(wordSupplier);
		long chainBuildTime = System.currentTimeMillis() - chainBuildStart;

		System.out.printf("Build time %dms (%ds)\n", chainBuildTime, chainBuildTime/1000);
		System.out.printf("Word chains will be displayed in groups, 1 group per line.  Each group represents an interchangeable word (each word could be used as that \"link\" in the chain without affecting the next link)\n");
		System.out.printf("Printing %d chains\n", wordChains.size());
		new WordChainPrinter().printWordChains(wordChains, System.out);
	}

	private static void printUsage() {
		System.err.printf("Usage: java -jar %s wordFilePath\n\n", getMyJar());
		System.err.println("wordFilePath is expected to be the path to a file containing whitespace separated words.  The file should be encoded in UTF-8\n");
	}

	private static String getMyJar() {
		return new File(WordChainFinder.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
	}
}
