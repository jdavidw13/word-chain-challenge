package jw.wcc.wordchain.hashbucket;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A "bucket" of words that have the same letters i.e. "on" and "no"
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class DistinctCharacterBucket {
	private final Set<String> words = new HashSet<>();
	private final String bucketHash;
	private final Set<DistinctCharacterBucket> childBuckets = new HashSet<>();
	private final Set<DistinctCharacterBucket> parentBuckets = new HashSet<>();

	public DistinctCharacterBucket(String bucketHash) {
		if (bucketHash == null || bucketHash.isEmpty()) {
			throw new IllegalArgumentException("bucketHash is null or empty");
		}
		this.bucketHash = bucketHash;
	}

	public void addWord(String word) {
		words.add(word);
	}

	public Set<String> getWords() {
		return words;
	}

	public String getBucketHash() {
		return bucketHash;
	}

	/**
	 * 
	 * @return the substring of bucketHash minus the last character
	 */
	public String getBucketHashMinusOne() {
		return bucketHash.substring(0, bucketHash.length() - 1);
	}

	public void addChildBucket(DistinctCharacterBucket bucket) {
		if (childBuckets.add(bucket)) {
			bucket.addParentBucket(this);
		}
	}

	public void addParentBucket(DistinctCharacterBucket parentBucket) {
		parentBuckets.add(parentBucket);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.bucketHash);
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
