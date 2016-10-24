package jw.wcc.wordinput;

import java.util.Arrays;
import java.util.List;

/**
 * A convenience WordSupplier implementation used with tests
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public class ListWordSupplier implements WordSupplier {
	private List<String> words;

	public ListWordSupplier(List<String> words) {
		this.words = words;
	}
	public ListWordSupplier(String... words) {
		this.words = Arrays.asList(words);
	}

	@Override
	public List<String> getWords() {
		return words;
	}

	@Override
	public String toString() {
		return "ListWordSupplier{" + "words=" + words + '}';
	}

}
