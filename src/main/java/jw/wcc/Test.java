package jw.wcc;

import jw.wcc.wordchain.wordbuckets.CharacterBucketWalker;
import jw.wcc.wordchain.wordbuckets.WordBucket;

/**
 *
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class Test {
	public static void main(String[] args) {


		WordBucket al = new WordBucket("al");
		al.addWord("al");

		WordBucket ale = new WordBucket("ael");
		ale.addWord("ale");
		al.addChildBucket(ale);

		WordBucket all = new WordBucket("all");
		all.addWord("all");
		al.addChildBucket(all);

		WordBucket alel = new WordBucket("aell");
		alel.addWord("alel");
		all.addChildBucket(alel);
		ale.addChildBucket(alel);

		WordBucket allen = new WordBucket("aelln");
		allen.addWord("allen");
		alel.addChildBucket(allen);

		CharacterBucketWalker alWalker = new CharacterBucketWalker(al);
		boolean runIt = true;
		while (runIt) {
			System.out.printf("hasMore %s, chain %s\n", alWalker.hasMoreChains(), alWalker.nextChain());
		}
	}
}
