package jw.wcc.test.hashbucket;

import jw.wcc.wordchain.wordbuckets.WordBucket;
import jw.wcc.wordchain.wordbuckets.CharacterBucketChain;
import jw.wcc.wordchain.wordbuckets.CharacterBucketWalker;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for {@link jw.wcc.wordchain.wordbuckets.CharacterBucketWalker}
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class CharacterBucketWalkerTest {
	@DataProvider(name = "dcbw-walk-case-provider")
	public Object[][] buildWalkTestCases() {
		WordBucket root = new WordBucket("rootHash");
		WordBucket rc1 = new WordBucket("rc1");
		WordBucket rc2 = new WordBucket("rc2");
		root.addChildBucket(rc1);
		root.addChildBucket(rc2);

		WordBucket c1c1 = new WordBucket("c1c1");
		WordBucket c1c2 = new WordBucket("c1c2");
		rc1.addChildBucket(c1c1);
		rc1.addChildBucket(c1c2);

		CharacterBucketWalker walker = new CharacterBucketWalker(root);

		WordBucket al = new WordBucket("al");
		al.addWord("al");

		WordBucket ale = new WordBucket("ael");
		ale.addWord("ale");
		al.addChildBucket(ale);

		WordBucket all = new WordBucket("all");
		ale.addWord("all");
		al.addChildBucket(all);

		WordBucket alel = new WordBucket("aell");
		ale.addWord("alel");
		all.addChildBucket(alel);
		ale.addChildBucket(alel);

		CharacterBucketWalker alWalker = new CharacterBucketWalker(al);

		return new Object[][] {
			{
				"first chain",
				new CharacterBucketChain(root, rc1, c1c1),
				walker
			},
			{
				"second chain",
				new CharacterBucketChain(root, rc1, c1c2),
				walker
			},
			{
				"third chain",
				new CharacterBucketChain(root, rc2),
				walker
			},
			{
				"al first chain",
				new CharacterBucketChain(al, ale, alel),
				alWalker
			},
			{
				"al second chain",
				new CharacterBucketChain(al, all, alel),
				alWalker
			},
		};
	}

	@Test(dataProvider = "dcbw-walk-case-provider")
	public void testWalkBuckets(String testCaseName, CharacterBucketChain expectedChain, CharacterBucketWalker walker) {
		CharacterBucketChain actualChain = walker.nextChain();
		AssertJUnit.assertEquals(testCaseName, expectedChain, actualChain);
	}

	public static void main(String[] args) {

		WordBucket al = new WordBucket("al");
		al.addWord("al");

		WordBucket ale = new WordBucket("ael");
		ale.addWord("ale");
		al.addChildBucket(ale);

		WordBucket all = new WordBucket("all");
		ale.addWord("all");
		al.addChildBucket(all);

		WordBucket alel = new WordBucket("aell");
		ale.addWord("alel");
		all.addChildBucket(alel);
		ale.addChildBucket(alel);

		CharacterBucketWalker alWalker = new CharacterBucketWalker(al);
		boolean runIt = true;
		while (runIt) {
			System.out.printf("hasMore %s, chain %s\n", alWalker.hasMoreChains(), alWalker.nextChain());
		}
	}
}
