package jw.wcc.wordchain.wordbuckets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Maintains a map of groups of CharacterBuckets by their letter count.  This relation is used to determine potential parents and children for a bucket.<br/>
 The mapped bucket group is itself a map of CharacterBucket keyed by its hash
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class WordLengthCharacterBucketMap {
	private final Map<Integer, Map<String, CharacterBucket>> characterCountBucketMap = new HashMap<>();

	private boolean linksDirty;

	public void addWord(String word) {
		linksDirty = true;

		int characterCount = word.length();
		String wordHash = getWordHash(word);
		Map<String, CharacterBucket> wordBucketMap = characterCountBucketMap.get(characterCount);
		if (wordBucketMap == null) {
			wordBucketMap = new HashMap<>();
			characterCountBucketMap.put(characterCount, wordBucketMap);
		}

		CharacterBucket wordBucket = wordBucketMap.get(wordHash);
		if (wordBucket == null) {
			wordBucket = new CharacterBucket(wordHash);
			wordBucketMap.put(wordHash, wordBucket);
		}
		wordBucket.addWord(word);
	}

	private void makeLinks() {
		if (!linksDirty) {
			return;
		}

		List<Integer> keys = new LinkedList<>(characterCountBucketMap.keySet());
		Collections.sort(keys);
		Collections.reverse(keys);

		ExecutorService pool = Executors.newCachedThreadPool(); 
		List<Future> futures = new LinkedList<>();
		for (Integer key : keys) {
			futures.add(pool.submit(createMakeLinksRunnable(key)));
		}
		try {
			for (Future future : futures) {
				future.get();
			}
			linksDirty = false;
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		} finally {
			pool.shutdown();
		}
	}

	private Runnable createMakeLinksRunnable(final int characterCount) {
		return new Runnable() {
			@Override
			public void run() {
				for (CharacterBucket bucket : characterCountBucketMap.get(characterCount).values()) {
					Map<String, CharacterBucket> potentialParents = characterCountBucketMap.get(characterCount - 1);
					int maxParents = characterCount - 1;
					int foundParents = 0;
					if (potentialParents != null) {
						for (CharacterBucket potentialParent : potentialParents.values()) {
							if (hashesMatchForLink(potentialParent.getBucketHash(), bucket.getBucketHash())) {
								potentialParent.addChildBucket(bucket);
								foundParents++;
								if (foundParents >= maxParents) {
									break;
								}
							}
						}
					}
				}
			}
		};
	}
	
	public Set<CharacterBucketChain> buildLongestChains() {
		makeLinks();

		int longestChainLength = 0;
		Set<CharacterBucketChain> longestChains = new HashSet<>();

		List<Integer> characterCountKeys = new ArrayList<>(characterCountBucketMap.keySet());
		Collections.sort(characterCountKeys);
		for (Integer key : characterCountKeys) {
			for (CharacterBucket bucket : characterCountBucketMap.get(key).values()) {
				if (bucket.isChainStart()) {
					CharacterBucketWalker walker = new CharacterBucketWalker(bucket);
					while (walker.hasMoreChains()) {
						CharacterBucketChain chain = walker.nextChain();
						if (chain.getChainLength() > longestChainLength) {
							longestChains.clear();
							longestChainLength = chain.getChainLength();
						}
						if (chain.getChainLength() == longestChainLength) {
							longestChains.add(chain);
						}
					}
				}
			}
		}

		return longestChains;
	}

	private boolean hashesMatchForLink(String h1, String h2) {
		List<Character> h2Characters = getCharacterList(h2);
		for (Character c : h1.toCharArray()) {
			h2Characters.remove(c);
		}
		return h2Characters.size() <= 1;
	}

	private List<Character> getCharacterList(String s) {
		List<Character> characters = new LinkedList<>();
		for (Character c : s.toCharArray()) {
			characters.add(c);
		}
		return characters;
	}

	private String getWordHash(String word) {
		char[] characters = word.toLowerCase().toCharArray();
		Arrays.sort(characters);
		return String.valueOf(characters, 0, characters.length);
	}
}
