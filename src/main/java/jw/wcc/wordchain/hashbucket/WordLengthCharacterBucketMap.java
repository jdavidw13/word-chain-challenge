package jw.wcc.wordchain.hashbucket;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 *
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

	private void linkBucket(DistinctCharacterBucket bucket, int characterCount) {
		Map<String, DistinctCharacterBucket> potentialParents = characterCountBucketMap.get(characterCount - 1);
		if (potentialParents != null) {
			DistinctCharacterBucket parent = potentialParents.get(bucket.getBucketHash());
			if (parent != null) {
				parent.addChildBucket(bucket);
			}
			parent = potentialParents.get(bucket.getBucketHashMinusOne());
			if (parent != null) {
				parent.addChildBucket(bucket);
			}
		}

		Map<String, DistinctCharacterBucket> potentialChildren = characterCountBucketMap.get(characterCount + 1);
		if (potentialChildren != null) {
			String bucketHash = bucket.getBucketHash();
			for (DistinctCharacterBucket childBucket : potentialChildren.values()) {
				if (childBucket.getBucketHash().equals(bucketHash) 
					|| childBucket.getBucketHashMinusOne().equals(bucketHash))
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
