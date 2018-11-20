import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all the Bloom filter rules and logic.
 * (see https://en.wikipedia.org/wiki/Bloom_filter)
 */
public class BloomFilter {
    /** name of input file */
    public static final String fileName = "src/main/resources/words.txt";
    /** words present in filter */
    public static final ArrayList<String> words = new ArrayList();

    /** desired false positive probability, p */
    public double falsePositiveProbability = 0.1;
    /** number of elements in filter, n */
    public int numberOfElements;
    /** filter size, m */
    public int filterSize;
    /** number of hash functions applied to element, k */
    public int numberOfHashFunctions;

    static {
        readWords();
    }

    public BloomFilter() {
        numberOfElements = words.size();
    }

    private static void readWords() {
        try (Scanner s = new Scanner(new FileReader(fileName))) {
            while (s.hasNext()) {
                words.add(s.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(words);
    }
}
