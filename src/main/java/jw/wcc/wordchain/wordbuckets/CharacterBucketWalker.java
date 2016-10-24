package jw.wcc.wordchain.wordbuckets;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Used to walk the references between CharacterBuckets and create complete WordChains
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class CharacterBucketWalker implements Callable<Set<CharacterBucketChain>> {
	private final Set<CharacterBucket> visitedBuckets;
	private final List<CharacterBucket> currentChain;

	public CharacterBucketWalker(CharacterBucket root) {
		visitedBuckets = new HashSet<>();
		currentChain = new LinkedList<>();

		// setup first chain
		addToCurrentChain(root);
	}

	public CharacterBucketChain nextChain() {
		CharacterBucketChain chain = new CharacterBucketChain();
		for (CharacterBucket node : currentChain) {
			chain.addBucketToChain(node);
		}

		advanceNextChain();

		return chain;
	}

	@Override
	public Set<CharacterBucketChain> call() throws Exception {
		Set<CharacterBucketChain> chains = new HashSet<>();
		while (hasMoreChains()) {
			chains.add(nextChain());
		}
		return chains;
	}
	

	private void advanceNextChain() {
		visitedBuckets.add(currentChain.remove(currentChain.size() - 1));
		outer: while (!currentChain.isEmpty()) {
			CharacterBucket currentNode = getLast();
			for (CharacterBucket child : currentNode.getChildBuckets()) {
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
	private void addToCurrentChain(CharacterBucket current) {
		currentChain.add(current);
		while (current.getChildBuckets() != null && !current.getChildBuckets().isEmpty()) {
			CharacterBucket child = current.getChildBuckets().iterator().next();
			currentChain.add(child);
			current = child;
		}
	}

	public boolean hasMoreChains() {
		boolean allVisited = true;
		outer: for (int i = currentChain.size() - 1; i > 0; i--) {
			CharacterBucket current = currentChain.get(i);
			for (CharacterBucket child : current.getChildBuckets()) {
				if (!visitedBuckets.contains(child)) {
					allVisited = false;
					break outer;
				}
			}
		}
		return !allVisited;
	}

	private CharacterBucket getLast() {
		return currentChain.get(currentChain.size() - 1);
	}
}
