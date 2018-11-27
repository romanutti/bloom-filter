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
	
    static int wordLimit = 1200000;
    static List<String> randomWords;
    static BloomFilter bloomFilter;

    @BeforeClass public static void setup() {
        bloomFilter = new BloomFilter(0.01);
        randomWords = generateRandomWords();
    }

    @Test public void testContainedWords() {
        for (String word : bloomFilter.words) {
            assertTrue(bloomFilter.isPossiblyInSet(word));
        }

        // print summary
        System.out.println("################################################");
        System.out.println("#                                               ");
        System.out.println("# testContainedWords                            ");
        System.out.println("#                                               ");
        System.out.println("# Result: success, all words recognized         ");
        System.out.println("################################################");
    }

    @Test public void testNotContainedWords() {
        // test(1)
        BloomFilter t1 = new BloomFilter(0.01);

        double falsePositiveProbability = t1.getFalsePositiveProportion(randomWords);
        assertEquals(t1.falsePositiveProbability, falsePositiveProbability, 0.1);

        // print summary
        System.out.println("################################################");
        System.out.println("#                                               ");
        System.out.println("# testNotContainedWords                         ");
        System.out.println("#                                               ");
        System.out.println("# Test(1)                                       ");
        System.out.println("# Testwords: " + t1.numberOfElements);
        System.out.println("# m: " + t1.filterSize);
        System.out.println("# k: " + t1.numberOfHashFunctions);
        System.out.println("# p expected: " + t1.falsePositiveProbability);
        System.out.println("# p actual: " + falsePositiveProbability);

        // test(2)
        BloomFilter t2 = new BloomFilter(0.05);

        falsePositiveProbability = t2.getFalsePositiveProportion(randomWords);
        assertEquals(t2.falsePositiveProbability, falsePositiveProbability, 0.1);

        // print summary
        System.out.println("#                                               ");
        System.out.println("# Test(2)                                       ");
        System.out.println("# Testwords: " + t2.numberOfElements);
        System.out.println("# m: " + t2.filterSize);
        System.out.println("# k: " + t2.numberOfHashFunctions);
        System.out.println("# p expected: " + t2.falsePositiveProbability);
        System.out.println("# p actual: " + falsePositiveProbability);

        // test(3)
        BloomFilter t3 = new BloomFilter(0.1);

        falsePositiveProbability = t3.getFalsePositiveProportion(randomWords);
        assertEquals(t3.falsePositiveProbability, falsePositiveProbability, 0.1);

        // print summary
        System.out.println("#                                               ");
        System.out.println("# Test(3)                                       ");
        System.out.println("# Testwords: " + t3.numberOfElements);
        System.out.println("# m: " + t3.filterSize);
        System.out.println("# k: " + t3.numberOfHashFunctions);
        System.out.println("# p expected: " + t3.falsePositiveProbability);
        System.out.println("# p actual: " + falsePositiveProbability);
        System.out.println("################################################");
    }

    private static List<String> generateRandomWords() {
        List<String> randomWords = new ArrayList<>();
        int i = 0;

        while (i < wordLimit) {
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
