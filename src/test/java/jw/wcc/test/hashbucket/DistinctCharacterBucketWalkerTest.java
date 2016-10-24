package jw.wcc.test.hashbucket;

import jw.wcc.wordchain.wordbuckets.DistinctCharacterBucket;
import jw.wcc.wordchain.wordbuckets.DistinctCharacterBucketChain;
import jw.wcc.wordchain.wordbuckets.DistinctCharacterBucketWalker;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for {@link jw.wcc.wordchain.wordbuckets.DistinctCharacterBucketWalker}
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class DistinctCharacterBucketWalkerTest {
	@DataProvider(name = "dcbw-walk-case-provider")
	public Object[][] buildWalkTestCases() {
		DistinctCharacterBucket root = new DistinctCharacterBucket("rootHash");
		DistinctCharacterBucket rc1 = new DistinctCharacterBucket("rc1");
		DistinctCharacterBucket rc2 = new DistinctCharacterBucket("rc2");
		root.addChildBucket(rc1);
		root.addChildBucket(rc2);

		DistinctCharacterBucket c1c1 = new DistinctCharacterBucket("c1c1");
		DistinctCharacterBucket c1c2 = new DistinctCharacterBucket("c1c2");
		rc1.addChildBucket(c1c1);
		rc1.addChildBucket(c1c2);

		DistinctCharacterBucketWalker walker = new DistinctCharacterBucketWalker(root);

		return new Object[][] {
			{
				"first chain",
				new DistinctCharacterBucketChain(root, rc1, c1c1),
				walker
			},
			{
				"second chain",
				new DistinctCharacterBucketChain(root, rc1, c1c2),
				walker
			},
			{
				"third chain",
				new DistinctCharacterBucketChain(root, rc2),
				walker
			}
		};
	}

	@Test(dataProvider = "dcbw-walk-case-provider")
	public void testWalkBuckets(String testCaseName, DistinctCharacterBucketChain expectedChain, DistinctCharacterBucketWalker walker) {
		DistinctCharacterBucketChain actualChain = walker.nextChain();
		AssertJUnit.assertEquals(testCaseName, expectedChain, actualChain);
	}
}
