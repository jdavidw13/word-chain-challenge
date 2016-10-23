package jw.wcc.wordchain.hashbucket;

import java.util.Set;
import jw.wcc.wordchain.WordChain;
import jw.wcc.wordchain.WordChainBuilder;
import jw.wcc.wordinput.WordSupplier;

/**
 * A word chain builder that creates word chains by grouping words into buckets of
 * equivalent words that would fill the same position in a word chain.<br/>
 * For example, "on" and "no" would be grouped together as they would fill the same spot
 * in a word chain.
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class HashBucketWordChainBuilder implements WordChainBuilder {

	@Override
	public Set<WordChain> buildLongestWordChains(WordSupplier wordSupplier) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
