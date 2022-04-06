/*
2.19.19
CS 145
Description:
Initializes the game with the dictionary you chose,
you pick the length of the word, and the maximum number of the wrong guesses.
Then you pick letters until either you narrow the word, and guess it or
you run out of guesses and lose.
*/
import java.util.*;

public class HangmanManager{

public Set<String> words;//The current set of "good" words.
public int miss;//The number of wrong guesses you are allowed in the game.
public SortedSet<Character> guesses;//Set of characters that the you have guessed.
public String pattern;//The current pattern.

   //max is >= 0 and the size of the dictionary is at least 1(throws IllegalArgumentException otherwise),
   //and it initializes the class fields.
   public HangmanManager(List<String> dictionary, int length, int max){
     	if (dictionary.size() < 1 || max < 0){
        	throw new IllegalArgumentException("Invalid Input!");
     	}
     	miss = max;
     	guesses = new TreeSet<>();
     	words = new TreeSet<>();
     	for(String word : dictionary){
        	if(word.length() == length && !words.contains(word)){
           	words.add(word);
        	}
     	}
     	pattern = "";
     	for (int i = 0; i < length; i++) {
        	pattern += "-";
     	}
   }
   //Returns a set of viable words for the game.
   public Set<String> words(){
  	   return words;
   }
   //Returns the number of guesses left in the game.
   public int guessesLeft(){
  	   return miss;
   }
   //Returns the set of character that the user has guessed.
   public SortedSet<Character> guesses(){
  	   return guesses;
   }
   //Returns the current pattern of the game.
   public String pattern(){
     	String result = "";
     	for (int i = 0; i < pattern.length(); i++) {
        	result += pattern.charAt(i) + " ";
     	}
     	return result.trim();
   }
   //Lets the user guess a character in the game and returns how many instances of the character is in the word.
   public int record(char guess){
     	if (words.isEmpty() || miss < 1){
        	throw new IllegalStateException();
     	}
     	else if (guesses.contains(guess)){
        	throw new IllegalArgumentException();
     	}
     	guesses.add(guess);
     	Map<String, Set<String>> parseDict = parseDict(guess);
     	pattern = mostMatch(parseDict);
     	words = parseDict.get(pattern);
     	int count = occurrences(pattern, guess);
     	if(count == 0){
        	miss--;
     	}
     	return count;
   }
   //Builds a map with the pattern as the key, and the set of words for the pattern as the value;
   //for the current set of viable words.
   public Map<String, Set<String>> parseDict(char ch) {
     	Map<String, Set<String>> result = new TreeMap<>();
     	for (String word : words) {
        	String wordPattern = buildPattern(word, ch);
        	if (!result.containsKey(wordPattern)) {
           	result.put(wordPattern, new TreeSet<>());
        	}
        	result.get(wordPattern).add(word);
     	}
     	return result;
   }
   //Returns the pattern with the most matches in the dictionary.
   public String mostMatch(Map<String, Set<String>> parseDict) {
     	int maxLen = 0;
     	String maxPattern = null;
     	for (Map.Entry<String, Set<String>> entry : parseDict.entrySet()) {
        	int length = entry.getValue().size();
        	if (length > maxLen) {
           	maxLen = length;
           	maxPattern = entry.getKey();
        	}
     	}
     	return maxPattern;
   }
   //Returns the number of occurrences of a character in a string.
   public int occurrences(String str, char ch) {
     	int result = 0;
     	for (int i = 0; i < str.length(); i++) {
        	if (str.charAt(i) == ch) {
           	result++;
        	}
     	}
     	return result;
   }
   //Builds a pattern for a word in regards to the current pattern of guessed characters.
   public String buildPattern(String word, char ch) {
     	String result = "";
     	for (int i = 0; i < word.length(); i++) {
        	if (pattern.charAt(i) != '-') {
           	result += pattern.charAt(i);
        	}
        	else if (word.charAt(i) == ch) {
           	result += ch;
        	}
        	else {
           	result += '-';
        	}
     	}
     	return result;
   }
}
