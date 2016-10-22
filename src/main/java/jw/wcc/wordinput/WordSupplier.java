package jw.wcc.wordinput;

/**
 * Supplies an iterable were each element represents a word for potential inclusion in a word chain
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public interface WordSupplier {
	Iterable<String> getWords();
}
