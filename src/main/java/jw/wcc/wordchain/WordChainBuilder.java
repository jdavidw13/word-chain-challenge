package jw.wcc.wordchain;

import java.util.List;
import java.util.Set;
import jw.wcc.wordinput.WordSupplier;

/**
 * Creates word chains!
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public interface WordChainBuilder {
	/**
	 * 
	 * @param wordSupplier
	 * @return a set of the longest found chain(s)
	 * There may be multiple chains of equal length that are the longest chains from the given word supplier
	 */
	Set<WordChain> buildLongestWordChains(WordSupplier wordSupplier);
}
