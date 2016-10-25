package jw.wcc.wordchain.wordbuckets;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Used to walk the references between CharacterBuckets and create complete WordChains
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class CharacterBucketWalker {
	private final Set<WordBucket> visitedBuckets;
	private final List<WordBucket> currentChain;

	public CharacterBucketWalker(WordBucket root) {
		visitedBuckets = new HashSet<>();
		currentChain = new LinkedList<>();

		// setup first chain
		addToCurrentChain(root);
	}

	public CharacterBucketChain nextChain() {
		CharacterBucketChain chain = new CharacterBucketChain();
		for (WordBucket node : currentChain) {
			chain.addBucketToChain(node);
		}

		advanceNextChain();

		return chain;
	}

	private void advanceNextChain() {
		visitedBuckets.add(currentChain.remove(currentChain.size() - 1));
		outer: while (!currentChain.isEmpty()) {
			WordBucket currentNode = getLast();
			for (WordBucket child : currentNode.getChildBuckets()) {
				if (!visitedBuckets.contains(child)) {
					addToCurrentChain(child);
					break outer;
				}
			}
			visitedBuckets.add(currentChain.remove(currentChain.size() - 1));
		}
	}

	/**
	 * Adds the specified node and every first child node to the current chain
	 * @param current 
	 */
	private void addToCurrentChain(WordBucket current) {
		currentChain.add(current);
		while (current.getChildBuckets() != null && !current.getChildBuckets().isEmpty()) {
			WordBucket child = current.getChildBuckets().iterator().next();
			currentChain.add(child);
			current = child;
		}
	}

	public boolean hasMoreChains() {
		boolean allVisited = true;
		outer: for (int i = currentChain.size() - 1; i > 0; i--) {
			WordBucket current = currentChain.get(i);
			for (WordBucket child : current.getChildBuckets()) {
				if (!visitedBuckets.contains(child)) {
					allVisited = false;
					break outer;
				}
			}
		}
		return !allVisited;
	}

	private WordBucket getLast() {
		return currentChain.get(currentChain.size() - 1);
	}
}
