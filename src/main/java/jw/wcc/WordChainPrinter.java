package jw.wcc;

import java.io.PrintStream;
import java.util.Set;
import jw.wcc.wordchain.WordChain;

/**
 * Prints WordChains to a PrintStream
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class WordChainPrinter {
	private final String EIGHTY_STAR = "********************************************************************************";

	public void printWordChains(Set<WordChain> chains, PrintStream stream) {
		for (WordChain chain : chains) {
			stream.printf("Chain length %d\n%s\n", chain.getChainLength(), EIGHTY_STAR);
			for (Set<String> wordGroup : chain.getChain()) {
				stream.printf("  %s\n", wordGroup);
			}
			stream.printf("%s\n\n", EIGHTY_STAR);
		}
	}
}
