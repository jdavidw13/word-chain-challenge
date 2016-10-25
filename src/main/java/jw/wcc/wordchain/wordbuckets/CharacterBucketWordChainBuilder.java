package jw.wcc.wordchain.wordbuckets;

import java.util.HashSet;
import java.util.Set;
import jw.wcc.wordchain.WordChain;
import jw.wcc.wordchain.WordChainBuilder;
import jw.wcc.wordinput.WordSupplier;

/**
 * A word chain builder that creates word chains by grouping words into buckets of
 * that would fill the same position in a word chain.<br/>
 * These buckets maintain relationships between each other to represent "parents" and "child" links in the WordChain
 * For example, "on" and "no" would be grouped together as they would fill the same spot
 * in a word chain.
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class CharacterBucketWordChainBuilder implements WordChainBuilder {

	public Set<WordChain> buildLongestWordChains(WordSupplier wordSupplier) {
		WordLengthBucketMap bucketMap = new WordLengthBucketMap();
		for (String word : wordSupplier.getWords()) {
			bucketMap.addWord(word);
		}
		Set<CharacterBucketChain> longestChains = bucketMap.buildLongestChains();
		Set<WordChain> wordChains = new HashSet<>();
		for (CharacterBucketChain chain : longestChains) {
			wordChains.add(chain.asWordChain());
		}
		return wordChains;
	}

}
