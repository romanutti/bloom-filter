import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles all the Bloom filter rules and logic.
 * (see https://en.wikipedia.org/wiki/Bloom_filter)
 */
public class BloomFilter {
    /**
     * name of input file
     */
    public static final String fileName = "src/main/resources/words.txt";
    /**
     * words present in filter
     */
    public static final ArrayList<String> words = new ArrayList<>();

    /**
     * desired false positive probability, p
     */
    public double falsePositiveProbability;
    
    /**
     * number of elements in filter, n
     */
    public int numberOfElements;
    /**
     * filter size, m
     */
    public int filterSize;
    
    /**
     * number of hash functions applied to element, k
     */
    public int numberOfHashFunctions;
    
    /**
     * array of the result of the hash functions
     */
    public boolean filter[];

    static {
        readWords();
    }

    public BloomFilter(double falsePositiveProbability) {
        this.falsePositiveProbability = falsePositiveProbability;
        numberOfElements = words.size();
        filterSize = calculateFilterSize();
        numberOfHashFunctions = calculateNumberOfHashFunctions();
        filter = new boolean[filterSize];
        seedFilter();
    }

    /**
     * Checks if the given word is in the filter.
     * @param word the word to check
     * @return if it returns true, then the word could be in the set.
     *         if it returns false, then the word is not in the set.
     */
    public boolean isPossiblyInSet(String word) {
        int seed = 0;
        int resultingIndex = 0;

        while (seed < numberOfHashFunctions) {
            resultingIndex = getHashedIndex(word, seed);
            if (resultingIndex > 0 && resultingIndex < filterSize) {
                if (!filter[resultingIndex])
                    return false;
            }
            seed++;
        }
        return true;
    }

    /**
     * Gets false positive probability of the given list.
     * @param input list of strings
     * @return the effective false positive probability
     */
    public double getFalsePositiveProportion(List<String> input) {
        double falsePositiveCount = 0.0;
        for (String word : input) {
            if (isPossiblyInSet(word) && !words.contains(word))
                falsePositiveCount++;
        }
        return falsePositiveCount / input.size();
    }

    /**
     * Reads words once from file.
     */
    private static void readWords() {
        try (Scanner s = new Scanner(new FileReader(fileName))) {
            while (s.hasNext()) {
                words.add(s.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates filter size, m
     * @return m
     */
    private int calculateFilterSize() {
        return (int) Math.ceil((-1 * numberOfElements * Math.log(falsePositiveProbability)) / (Math.pow(Math.log(2), 2)));
    }

    /**
     * Calculates number of hash functions, k.
     * @return k
     */
    private int calculateNumberOfHashFunctions() {
        return (int) Math.ceil(-1 * Math.log(falsePositiveProbability) / Math.log(2));
    }

    /**
     * Initially sets up filter
     */
    private void seedFilter() {
        int seed = 0;
        while (seed < numberOfHashFunctions) {
            for (String word : words) {
                addWordToFilter(word, seed);
            }
            seed++;
        }
    }

    /**
     * Adds a word to the filter.
     * @param word word to be added
     * @param seed seed to be used in hash function
     */
    private void addWordToFilter(String word, int seed) {
        int resultingIndex = getHashedIndex(word, seed);
        if (resultingIndex > 0 && resultingIndex < filterSize) {
            filter[resultingIndex] = true;
        }
    }

    /**
     * Calculates the murmur3 hash for a word and a given seed.
     * @param word Word to be added
     * @param seed Seed to be used in hash function
     * @return murmur3 hash % m
     */
    private int getHashedIndex(String word, int seed) {
        HashFunction murmur3 = Hashing.murmur3_128(seed);
        HashCode hc = murmur3.newHasher().putString(word, StandardCharsets.UTF_8).hash();

        return Math.abs(hc.asInt() % filterSize);
    }
}
