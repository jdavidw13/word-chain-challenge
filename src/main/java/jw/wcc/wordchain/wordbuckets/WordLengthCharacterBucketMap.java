package jw.wcc.wordchain.wordbuckets;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Maintains a map of groups of DistinctCharacterBuckets by their letter count.  This relation is used to determine potential parents and children for a bucket.<br/>
 * The mapped bucket group is itself a map of DistinctCharacterBucket keyed by its hash
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class WordLengthCharacterBucketMap {
	private final Map<Integer, Map<String, DistinctCharacterBucket>> characterCountBucketMap = new HashMap<>();

	public void addWord(String word) {
		int characterCount = word.length();
		String wordHash = getWordHash(word);
		Map<String, DistinctCharacterBucket> wordBucketMap = characterCountBucketMap.get(characterCount);
		if (wordBucketMap == null) {
			wordBucketMap = new HashMap<>();
			characterCountBucketMap.put(characterCount, wordBucketMap);
		}

		DistinctCharacterBucket wordBucket = wordBucketMap.get(wordHash);
		if (wordBucket == null) {
			wordBucket = new DistinctCharacterBucket(wordHash);
			linkBucket(wordBucket, characterCount);
			wordBucketMap.put(wordHash, wordBucket);
		}
		wordBucket.addWord(word);
	}

	public void addBucketMap(WordLengthCharacterBucketMap map) {
		for (Entry<Integer, Map<String, DistinctCharacterBucket>> lengthBucketEntry : map.characterCountBucketMap.entrySet()) {
			Integer characterCount = lengthBucketEntry.getKey();
			Map<String, DistinctCharacterBucket> myWordBucketMap = characterCountBucketMap.get(characterCount);
			if (myWordBucketMap == null) {
				myWordBucketMap = new HashMap<>();
				characterCountBucketMap.put(characterCount, myWordBucketMap);
			}

			for (DistinctCharacterBucket theirBucket : lengthBucketEntry.getValue().values()) {
				String bucketHash = theirBucket.getBucketHash();
				DistinctCharacterBucket myBucket = myWordBucketMap.get(bucketHash);
				if (myBucket == null) {
					myBucket = new DistinctCharacterBucket(bucketHash);
					linkBucket(myBucket, characterCount);
					myWordBucketMap.put(bucketHash, myBucket);
				}
				myBucket.addAllWords(theirBucket.getWords());
			}
		}
	}
	
	public Set<DistinctCharacterBucketChain> buildLongestChains() {
		int longestChainLength = 0;
		Set<DistinctCharacterBucketChain> longestChains = new HashSet<>();

		List<Integer> characterCountKeys = new ArrayList<>(characterCountBucketMap.keySet());
		Collections.sort(characterCountKeys);
		for (Integer key : characterCountKeys) {
			for (DistinctCharacterBucket bucket : characterCountBucketMap.get(key).values()) {
				if (bucket.isChainStart()) {
					DistinctCharacterBucketWalker walker = new DistinctCharacterBucketWalker(bucket);
					while (walker.hasMoreChains()) {
						DistinctCharacterBucketChain chain = walker.nextChain();
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

	private void linkBucket(DistinctCharacterBucket bucket, int characterCount) {
		Map<String, DistinctCharacterBucket> potentialParents = characterCountBucketMap.get(characterCount - 1);
		if (potentialParents != null) {
			DistinctCharacterBucket parent = potentialParents.get(bucket.getBucketHash());
			if (parent != null) {
				parent.addChildBucket(bucket);
			}
			parent = potentialParents.get(bucket.getBucketHashFront());
			if (parent != null) {
				parent.addChildBucket(bucket);
			}
			parent = potentialParents.get(bucket.getBucketHashBack());
			if (parent != null) {
				parent.addChildBucket(bucket);
			}
		}

		Map<String, DistinctCharacterBucket> potentialChildren = characterCountBucketMap.get(characterCount + 1);
		if (potentialChildren != null) {
			String bucketHash = bucket.getBucketHash();
			for (DistinctCharacterBucket childBucket : potentialChildren.values()) {
				if (childBucket.getBucketHash().equals(bucketHash) 
					|| childBucket.getBucketHashFront().equals(bucketHash)
					|| childBucket.getBucketHashBack().equals(bucketHash))
				{
					bucket.addChildBucket(childBucket);
				}
			}
		}
	}

	private String getWordHash(String word) {
		char[] characters = word.toLowerCase().toCharArray();
		Arrays.sort(characters);
		Deque<Character> uniqueChars = new ArrayDeque<>();
		for (Character c : characters) {
			if (!c.equals(uniqueChars.peek())) {
				uniqueChars.push(c);
			}
		}
		StringBuilder sb = new StringBuilder();
		while (!uniqueChars.isEmpty()) {
			sb.append(uniqueChars.removeLast());
		}
		return sb.toString();
	}
}
