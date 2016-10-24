package jw.wcc.wordchain.wordbuckets;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A "bucket" of words that hash to the same code per the algorithm.  These words will have the same distinct set of letter i.e. "no" and "on"<br/>
 * The bucket also contains relations other buckets that would represent "parents" and "children" links in the WordChain
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class DistinctCharacterBucket {
	private final Set<String> words = new HashSet<>();
	private final String bucketHash;
	private final Set<DistinctCharacterBucket> childBuckets = new LinkedHashSet<>();
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

	public void addAllWords(Collection<String> words) {
		this.words.addAll(words);
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
	public String getBucketHashFront() {
		return bucketHash.substring(0, bucketHash.length() - 1);
	}

	/**
	 * 
	 * @return the substring of bucketHash minus the first character
	 */
	public String getBucketHashBack() {
		return bucketHash.substring(1);
	}

	public void addChildBucket(DistinctCharacterBucket bucket) {
		if (childBuckets.add(bucket)) {
			bucket.addParentBucket(this);
		}
	}

	public void addParentBucket(DistinctCharacterBucket parentBucket) {
		parentBuckets.add(parentBucket);
	}

	public boolean isChainStart() {
		return parentBuckets == null || parentBuckets.isEmpty();
	}

	public Set<DistinctCharacterBucket> getChildBuckets() {
		return childBuckets;
	}

	public Set<DistinctCharacterBucket> getParentBuckets() {
		return parentBuckets;
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
