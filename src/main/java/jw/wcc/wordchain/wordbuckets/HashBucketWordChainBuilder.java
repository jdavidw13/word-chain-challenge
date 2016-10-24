package jw.wcc.wordchain.wordbuckets;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import jw.wcc.wordchain.WordChain;
import jw.wcc.wordchain.WordChainBuilder;
import jw.wcc.wordinput.WordSupplier;

/**
 * A word chain builder that creates word chains by grouping words into buckets of
 * that would fill the same position in a word chain.<br/>
 * These buckets maintain relationships between each other to represent "parents" and "child" links in the WordChain
 * For example, "on" and "no" would be grouped together as they would fill the same spot
 * in a word chain.
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class HashBucketWordChainBuilder implements WordChainBuilder {

	@Override
	public Set<WordChain> buildLongestWordChains(WordSupplier wordSupplier) {
		int pCount = Runtime.getRuntime().availableProcessors();

		// build WordLengthCharacterBucketMap futures
		List<List<String>> splitLists = splitWordList(wordSupplier.getWords(), pCount);
		Collection<Callable<WordLengthCharacterBucketMap>> callables = new ArrayList<>(pCount);
		for (List<String> splitList : splitLists) {
			callables.add(new PartialBucket(splitList));
		}

		ExecutorService pool = Executors.newFixedThreadPool(pCount);
		try {
			// join results
			List<Future<WordLengthCharacterBucketMap>> futures = pool.invokeAll(callables);
			WordLengthCharacterBucketMap bucketMap = null;
			for (Future<WordLengthCharacterBucketMap> futureMap : futures) {
				if (bucketMap == null) {
					bucketMap = futureMap.get();
				} else {
					bucketMap.addBucketMap(futureMap.get());
				}
			}

			// build final chain
			Set<DistinctCharacterBucketChain> longestChains = bucketMap.buildLongestChains();
			Set<WordChain> wordChains = new HashSet<>();
			for (DistinctCharacterBucketChain chain : longestChains) {
				wordChains.add(chain.asWordChain());
			}
			return wordChains;
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		} finally {
			pool.shutdown();
		}
	}

	private List<List<String>> splitWordList(List<String> words, int numWays) {
		List<List<String>> splitWords = new ArrayList<>(numWays);
		int batchSize = (int)Math.ceil((double)words.size() / numWays);
		for (int i = 0; i < words.size(); i += batchSize) {
			int toIndex;
			if (i + batchSize < words.size()) {
				toIndex = i + batchSize;
			} else {
				toIndex = words.size();
			}
			List<String> sublist = words.subList(i, toIndex);
			splitWords.add(sublist);
		}
		return splitWords;
	}
	
	private static class PartialBucket implements Callable<WordLengthCharacterBucketMap> {
		private List<String> words;

		public PartialBucket(List<String> words) {
			this.words = words;
		}

		@Override
		public WordLengthCharacterBucketMap call() throws Exception {
			WordLengthCharacterBucketMap bucketMap = new WordLengthCharacterBucketMap();
			for (String word : words) {
				bucketMap.addWord(word);
			}
			return bucketMap;
		}

	}
}
