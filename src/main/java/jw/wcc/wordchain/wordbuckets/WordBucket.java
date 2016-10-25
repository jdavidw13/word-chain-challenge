package jw.wcc.wordchain.wordbuckets;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A "bucket" of words that hash to the same code per the algorithm.  These words will have the same set of letters i.e. "no" and "on"<br/>
 * The bucket also contains relations other buckets that would represent "parents" and "children" links in the WordChain
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class WordBucket {
	private final Set<String> words = new HashSet<>();
	private final String bucketHash;
	private final Set<WordBucket> childBuckets = new LinkedHashSet<>();
	private final Set<WordBucket> parentBuckets = new HashSet<>();

	public WordBucket(String bucketHash) {
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

	public void addChildBucket(WordBucket bucket) {
		if (childBuckets.add(bucket)) {
			bucket.addParentBucket(this);
		}
	}

	public void addParentBucket(WordBucket parentBucket) {
		parentBuckets.add(parentBucket);
	}

	public boolean isChainStart() {
		return parentBuckets == null || parentBuckets.isEmpty();
	}

	public Set<WordBucket> getChildBuckets() {
		return childBuckets;
	}

	public Set<WordBucket> getParentBuckets() {
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
		final WordBucket other = (WordBucket) obj;
		if (!Objects.equals(this.bucketHash, other.bucketHash)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "WordBucket{" + "words=" + words + ", bucketHash=" + bucketHash + '}';
	}
	
}
