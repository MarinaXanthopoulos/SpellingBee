import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        generateHelper("", letters);
    }

    private void generateHelper(String prefix, String remaining){
        // Add the word to the list if prefix isn't empty so every possible combo is stored
        if (!prefix.isEmpty()){
            words.add(prefix);
        }
        // Loop through each character in the remaining
        for (int i = 0; i < remaining.length(); i++){
            // Form a new substring adding each character to prefix then removing the character from remaining
            generateHelper(prefix + remaining.charAt(i), remaining.substring(0, i) + remaining.substring(i+1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE

    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        // Traverse through words list
        for(int i = 0; i < words.size(); i++) {
            // Call and perform binary search
            String word = words.get(i);
            // If it returns false, remove the word from the list of possible words
            if(!binarySearch(word)){
                words.remove(i);
                i--;
            }
        }
    }

    public boolean binarySearch(String word){
        // Set low and high to the dictionary parameters (o, end)
        int low = 0, high = DICTIONARY_SIZE -1;
        while (low <= high) {
            // Start at the middle
            int mid = low + (high - low) / 2;
            // Compare the current index (mid) to the word we're searching for
            int compare = DICTIONARY[mid].compareTo(word);
            // Adjust the midpoint accordingly, return true if there's no difference
            if (compare == 0) {
                return true;
            } else if (compare < 0) {
                low = mid++;
            } else {
                high = mid--;
            }
        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
