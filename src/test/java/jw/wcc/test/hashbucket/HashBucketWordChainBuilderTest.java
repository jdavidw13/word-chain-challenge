package jw.wcc.test.hashbucket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import jw.wcc.wordchain.WordChain;
import jw.wcc.wordchain.wordbuckets.HashBucketWordChainBuilder;
import jw.wcc.wordinput.ListWordSupplier;
import jw.wcc.wordinput.WordSupplier;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for {@link jw.wcc.wordchain.wordbuckets.HashBucketWordChainBuilder}
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class HashBucketWordChainBuilderTest {

	@DataProvider(name = "hbwcb-longestChain-testCases")
	public Object[][] buildLongestChainTestCases() {
		return new Object[][] {
			{
				"from example",
				new ListWordSupplier("one", "no", "teton", "tone"),
				new WordChain("no", "one", "tone", "teton").asSet()
			},
			{
				"\"disjoint\" chains of same length",
				new ListWordSupplier("x", "xy", "xyz", "a", "ab", "abc"),
				new HashSet<WordChain>(Arrays.asList(
					new WordChain("x", "xy", "xyz"),
					new WordChain("a", "ab", "abc")
				))
			},
			{
				"grouped links",
				new ListWordSupplier("on", "no", "one", "noe", "none"),
				new WordChain(
					Arrays.asList("on", "no"), // first link group
					Arrays.asList("one", "noe"), // second link group
					Arrays.asList("none") // third link group
				).asSet()
			},
			{
				"longest chain",
				new ListWordSupplier("x", "xy", "xyz", "a", "ab", "abc", "abcd"),
				new WordChain("a", "ab", "abc", "abcd").asSet()
			}
		};
	}

	@Test(dataProvider = "hbwcb-longestChain-testCases")
	public void testBuildCorrectLongestChain(String testCaseName, WordSupplier wordSupplier, Set<WordChain> expectedChains) {
		HashBucketWordChainBuilder wcb = new HashBucketWordChainBuilder();
		Set<WordChain> actualChains = wcb.buildLongestWordChains(wordSupplier);
		if (!expectedChains.equals(actualChains)) {
			System.out.printf("TestCase: %s\nExpected: %s\nActual: %s\nWordSupplier: %s\n-----------------------------------------------------------------------\n", testCaseName, expectedChains, actualChains, wordSupplier);
		}
		AssertJUnit.assertEquals(testCaseName, expectedChains, actualChains);
	}

}
