package jw.wcc.wordchain.wordbuckets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import jw.wcc.wordchain.WordChain;

/**
 * Represents DistinctCharacterBuckets that will eventually be strung together as a WordChain<br/>
 * The list of DistinctCharacterBuckets should be ordered such that the first bucket represents the first link of the WordChain 
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class DistinctCharacterBucketChain {
	private List<DistinctCharacterBucket> bucketChain;

	public DistinctCharacterBucketChain() { }
	public DistinctCharacterBucketChain(DistinctCharacterBucket... buckets) {
		bucketChain = new LinkedList<>(Arrays.asList(buckets));
	}

	public void addBucketToChain(DistinctCharacterBucket bucket) {
		if (bucketChain == null) {
			bucketChain = new LinkedList<>();
		}
		bucketChain.add(bucket);
	}

	public List<DistinctCharacterBucket> getBucketChain() {
		return bucketChain;
	}

	public int getChainLength() {
		return bucketChain != null ? bucketChain.size() : 0;
	}

	/**
	 * 
	 * @return a {@link jw.wcc.wordchain.WordChain} built from this bucket chain
	 */
	public WordChain asWordChain() {
		Collection<Collection<String>> wordGroups = new LinkedList<>();
		if (bucketChain != null) {
			for (DistinctCharacterBucket group : bucketChain) {
				wordGroups.add(group.getWords());
			}
		}
		return new WordChain(wordGroups);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 67 * hash + Objects.hashCode(this.bucketChain);
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
		final DistinctCharacterBucketChain other = (DistinctCharacterBucketChain) obj;
		if (!Objects.equals(this.bucketChain, other.bucketChain)) {
			return false;
		}
		return true;
	}
	
}
