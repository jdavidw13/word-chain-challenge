package jw.wcc.wordchain.wordbuckets;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A bucket containing a map of WordBuckets.  This bucket will contain WordBuckets that all have the same set of distinct characters
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class DistinctCharacterBucket {
	private final Map<String, WordBucket> wordBucketMap = new HashMap<>();
	private final String bucketHash;

	public DistinctCharacterBucket(String bucketHash) {
		this.bucketHash = bucketHash;
	}

	public void addWord(String word) {
		String wordHash = getWordHash(word);
		WordBucket wordBucket = wordBucketMap.get(wordHash);
		if (wordBucket == null) {
			wordBucket = new WordBucket(wordHash);
			wordBucketMap.put(wordHash, wordBucket);
		}
		wordBucket.addWord(word);
	}

	public Collection<WordBucket> getWordBuckets() {
		return wordBucketMap.values();
	}

	private String getWordHash(String word) {
		char[] characters = word.toLowerCase().toCharArray();
		Arrays.sort(characters);
		return String.valueOf(characters, 0, characters.length);
	}

	public String getBucketHash() {
		return bucketHash;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + Objects.hashCode(this.bucketHash);
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
		final DistinctCharacterBucket other = (DistinctCharacterBucket) obj;
		if (!Objects.equals(this.bucketHash, other.bucketHash)) {
			return false;
		}
		return true;
	}

}
