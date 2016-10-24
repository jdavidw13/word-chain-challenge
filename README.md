# word-chain-challenge

## The Challenge
Given a list of words, construct a chain of words where each word in the chain has each of the previous word's letters, plus one more.  For example:
* no
* one
* tone
* teton

...would be a chain of four words.  Note that the letter order does not matter.  In 'no', the n is before the o, in 'one', the o is before the n.

Code an algorithm that will find the longest chain of words possible, given the list of words in the following input file, in as efficient a manner as possible.

## Input
The following word list should be used as the input to your program.  The file should be read from disk as a part of your program so that alternative word lists can also be used in our evaluation of your solution.  Do not crat a solution specific to t*this* word list.

[http://www-01.sil.org/linguistics/wordlists/english/wordlist/wordsEn.txt](http://www-01.sil.org/linguistics/wordlists/english/wordlist/wordsEn.txt)

## Output
The time that your solution took to reach its conclusion, in milliseconds, as well as the longest chain of words that your solution found.

## Evaluation Criteria
Your solution will be judged by its accuracy, efficiency (i.e. speed), readability, and maintainability.

## Solution Overview
The solution attempts to create word chains by placing words into buckets, then defining relations between these buckets that represent parent and child links within the word chain.  A bucket for a word is chosen by first filtering the buckets by the count of letters in the words they contain, i.e. buckets contain words with 1 letter, buckets containing words with 2 letters, etc.  Next, a hash for the word is created and placed into the filtered group of buckets representing that hash.  If no bucket exists, one is created.  Additionally, when a bucket is created, its relationships are also created.  Potential parents are chosen from the buckets in the previous letter count group and potential children are chosen from the buckets in the next letter count group.

Creating relationships to other buckets is done by matching its hash.  A word's hash (and also bucket's hash) is the distinct set of alphabetically ordered letters of the word.  For example, "not" and "ton" both hash to "not".  Similarly "tone" and "teton" both hash to "enot".  A hash will match another hash either by exact match, by matching the hash without the first character, or by matching the hash without the last character.

For example, the word chain "one" -> "tone" -> "teton" hashes as "eno" -> "enot" -> "enot".  The hash for "tone" matches the hash for "one" by matching "eno" = "eno" - "t" (by removing the last character of the hash).  The hash for "teton" matches the hash for "tone" because their hashes match exactly.

I created two main components in the design of this system.  The first is the WordChainBuilder.  This interface defines various methods that would relate to word chains.  For example, finding the longest word chains.  The second component is the WordSupplier.  This is responsible for supplying a list of words to the WordChainBuilder to potentailly be included in the word chain.  Various implementations of this would supply words from various sources, i.e. a file, a remote resource via http, etc.

## Run
Included in the repo is the jar created by the build.  Invoke via java -jar word-chain-challenge.jar pathToWordFile.  To build, gradle clean build.
