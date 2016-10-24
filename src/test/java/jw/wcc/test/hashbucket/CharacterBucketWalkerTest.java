package jw.wcc.test.hashbucket;

import jw.wcc.wordchain.wordbuckets.CharacterBucket;
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
		CharacterBucket root = new CharacterBucket("rootHash");
		CharacterBucket rc1 = new CharacterBucket("rc1");
		CharacterBucket rc2 = new CharacterBucket("rc2");
		root.addChildBucket(rc1);
		root.addChildBucket(rc2);

		CharacterBucket c1c1 = new CharacterBucket("c1c1");
		CharacterBucket c1c2 = new CharacterBucket("c1c2");
		rc1.addChildBucket(c1c1);
		rc1.addChildBucket(c1c2);

		CharacterBucketWalker walker = new CharacterBucketWalker(root);

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
			}
		};
	}

	@Test(dataProvider = "dcbw-walk-case-provider")
	public void testWalkBuckets(String testCaseName, CharacterBucketChain expectedChain, CharacterBucketWalker walker) {
		CharacterBucketChain actualChain = walker.nextChain();
		AssertJUnit.assertEquals(testCaseName, expectedChain, actualChain);
	}
}
