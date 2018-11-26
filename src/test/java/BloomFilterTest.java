import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BloomFilterTest {
    static int wordLimit = 1000000;
    static List<String> randomWords;
    static BloomFilter bloomFilter;

    @BeforeClass public static void setup() {
        bloomFilter = new BloomFilter();
        randomWords = generateRandomWords();
    }

    @Test public void testContainedWords() {
        for (String word : bloomFilter.words) {
            assertTrue(bloomFilter.isPossiblyInSet(word));
        }
    }

    @Test public void testNotContainedWords() {
        double falsePositiveProbability = bloomFilter.getFalsePositiveProbability(randomWords);

        System.out.println(bloomFilter.falsePositiveProbability + " " + falsePositiveProbability);
        assertEquals(bloomFilter.falsePositiveProbability, falsePositiveProbability, 0.1);
    }

    private static List<String> generateRandomWords() {
        List<String> randomWords = new ArrayList<>();
        int i = 0;
        System.out.println(bloomFilter.filterSize);

        while (i < wordLimit ){
            randomWords.add(generateRandomString());
            i++;
        }
        return randomWords;
    }

    private static String generateRandomString() {
        // Random length
        int length = ThreadLocalRandom.current().nextInt(0, 100);
        // Given alphabet
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789";

        return new Random().ints(length, 0, alphabet.length()).mapToObj(i -> "" + alphabet.charAt(i))
                .collect(Collectors.joining());

    }
}
