package jw.wcc.wordinput;

import java.util.List;

/**
 * Supplies a List were each element represents a word for potential inclusion in a word chain
 * @author Josiah Wilkerson <jdavidw13@gmail.com>
 */
public interface WordSupplier {
	List<String> getWords();
}
