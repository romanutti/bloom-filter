/**
 * Starts and runs the Bloom filter application
 */
public class BloomFilterApp {

    /**
     * Runs the application
     *
     * @param args what ever you want
     */
    public static void main(String[] args) {
        BloomFilter bf = new BloomFilter();
        System.out.println(bf.isPossiblyInSet("zoom"));

    }

}
