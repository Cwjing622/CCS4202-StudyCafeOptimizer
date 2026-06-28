package model;

/**
 * Represents a single Café record collected during café visitation.
 * This immutable model stores the base metrics of a café and serves as 
 * the data object for the Dynamic Programming, Brute Force, and Greedy algorithms.
 */
public class Cafe {

    private final String name;
    private final int price;
    private final int wifiScore;
    private final int distanceScore;
    private final int noiseScore;

    /**
     * Constructs a new Cafe instance with specified metrics.
     *
     * @param name          the name of the café
     * @param price         the price or cost of studying at the café (RM)
     * @param wifiScore     the rating for the café's WiFi quality
     * @param distanceScore the rating for the proximity/distance convenience
     * @param noiseScore    the rating for the environment's quietness
     */
    public Cafe(String name, int price, int wifiScore, int distanceScore, int noiseScore) {
        this.name = name;
        this.price = price;
        this.wifiScore = wifiScore;
        this.distanceScore = distanceScore;
        this.noiseScore = noiseScore;
    }

    /**
     * Gets the name of the café.
     *
     * @return the café name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the price or cost associated with the café.
     *
     * @return the price in RM
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets the WiFi score.
     *
     * @return the WiFi score
     */
    public int getWifiScore() {
        return wifiScore;
    }

    /**
     * Gets the distance score.
     *
     * @return the distance score
     */
    public int getDistanceScore() {
        return distanceScore;
    }

    /**
     * Gets the noise score.
     *
     * @return the noise score
     */
    public int getNoiseScore() {
        return noiseScore;
    }

    /**
     * Calculates and returns the derived total study suitability score.
     * This value acts as the "value" or "benefit" metric in the 0/1 Knapsack optimization.
     *
     * @return the sum of the WiFi, distance, and noise scores
     */
    public int getStudyScore() {
        return wifiScore + distanceScore + noiseScore;
    }

    @Override
    public String toString() {
        return "Cafe{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", wifiScore=" + wifiScore +
                ", distanceScore=" + distanceScore +
                ", noiseScore=" + noiseScore +
                ", studyScore=" + getStudyScore() +
                '}';
    }

    public String getDisplayName() {
    return name + " (RM" + price + ")";
}
}

