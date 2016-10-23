package jw.wcc.wordinput;

import java.util.Arrays;
import java.util.List;

/**
 *
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
	public Iterable<String> getWords() {
		return words;
	}

}
