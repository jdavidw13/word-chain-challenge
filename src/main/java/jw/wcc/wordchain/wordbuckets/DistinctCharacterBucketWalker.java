package jw.wcc.wordchain.wordbuckets;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Used to walk the references between DistinctCharacterBuckets and create complete WordChains
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class DistinctCharacterBucketWalker implements Callable<Set<DistinctCharacterBucketChain>> {
	private final Set<DistinctCharacterBucket> visitedBuckets;
	private final List<DistinctCharacterBucket> currentChain;

	public DistinctCharacterBucketWalker(DistinctCharacterBucket root) {
		visitedBuckets = new HashSet<>();
		currentChain = new LinkedList<>();

		// setup first chain
		addToCurrentChain(root);
	}

	public DistinctCharacterBucketChain nextChain() {
		DistinctCharacterBucketChain chain = new DistinctCharacterBucketChain();
		for (DistinctCharacterBucket node : currentChain) {
			chain.addBucketToChain(node);
		}

		advanceNextChain();

		return chain;
	}

	@Override
	public Set<DistinctCharacterBucketChain> call() throws Exception {
		Set<DistinctCharacterBucketChain> chains = new HashSet<>();
		while (hasMoreChains()) {
			chains.add(nextChain());
		}
		return chains;
	}
	

	private void advanceNextChain() {
		visitedBuckets.add(currentChain.remove(currentChain.size() - 1));
		outer: while (!currentChain.isEmpty()) {
			DistinctCharacterBucket currentNode = getLast();
			for (DistinctCharacterBucket child : currentNode.getChildBuckets()) {
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
	private void addToCurrentChain(DistinctCharacterBucket current) {
		currentChain.add(current);
		while (current.getChildBuckets() != null && !current.getChildBuckets().isEmpty()) {
			DistinctCharacterBucket child = current.getChildBuckets().iterator().next();
			currentChain.add(child);
			current = child;
		}
	}

	public boolean hasMoreChains() {
		boolean allVisited = true;
		outer: for (int i = currentChain.size() - 1; i > 0; i--) {
			DistinctCharacterBucket current = currentChain.get(i);
			for (DistinctCharacterBucket child : current.getChildBuckets()) {
				if (!visitedBuckets.contains(child)) {
					allVisited = false;
					break outer;
				}
			}
		}
		return !allVisited;
	}

	private DistinctCharacterBucket getLast() {
		return currentChain.get(currentChain.size() - 1);
	}
}
