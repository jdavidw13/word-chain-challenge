package jw.wcc.wordchain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A solution to the word chain challenge
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class WordChain {
	/**
	 * Ordered such that the first element is a set of words that could form the first "link" of the chain i.e. "on" or "no"
	 * each subsequent element is a set of words that form the subsequent links
	 */
	private final List<Set<String>> wordChain;

	/**
	 * Should be ordered such that the first element is the first word group of the chain, and so on...
	 * @param wordGroups the solution
	 */
	public WordChain(Collection<Collection<String>> wordGroups) {
		List<Set<String>> chain = new ArrayList<>(wordGroups.size());
		for (Collection<String> wordGroup : wordGroups) {
			Set<String> group = new HashSet<>(wordGroup);
			chain.add(group);
		}
		wordChain = chain;

	}
	/**
	 * Should be ordered such that the first element is the first word group of the chain, and so on...
	 * @param wordGroups the solution
	 */
	public WordChain(Collection<String>... wordGroups) {
		List<Set<String>> chain = new LinkedList<>();
		for (Collection<String> wordGroup : wordGroups) {
			Set<String> group = new HashSet<>(wordGroup);
			chain.add(group);
		}
		wordChain = chain;
	}
	/**
	 * Should be ordered such that the first element is the only word in the first word group of the chain, and so on...
	 * @param singleWordGroups 
	 */
	public WordChain(String... singleWordGroups) {
		List<Set<String>> chain = new LinkedList<>();
		for (String word : singleWordGroups) {
			Set<String> group = new HashSet<>();
			group.add(word);
			chain.add(group);
		}
		wordChain = chain;
	}

	/**
	 * Convenience method to return this WordChain as the only element of a set
	 * @return 
	 */
	public Set<WordChain> asSet() {
		Set<WordChain> mySet = new HashSet<>();
		mySet.add(this);
		return mySet;
	}

	/**
	 * 
	 * @return count of word groups in the chain
	 */
	public int getChainLength() {
		return wordChain.size();
	}

	/**
	 * 
	 * @return a list of word groups in the chain.  The first element of the list is the first word in the chain, and so on...
	 */
	public List<Set<String>> getChain() {
		return wordChain;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 19 * hash + Objects.hashCode(this.wordChain);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final WordChain other = (WordChain) obj;
		if (!Objects.equals(this.wordChain, other.wordChain)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "WordChain{" + "wordChain=" + wordChain + '}';
	}
	
}
