package data;

import java.util.List;
import model.Cafe;

/**
 * Stores the café dataset used by the application.
 * This class centralizes the data so it can be easily replaced
 * with the real visitation data later.
 */
public class CafeData {

    private CafeData() {
        // Prevent instantiation
    }

    public static List<Cafe> getSampleData() {

        return List.of(
                new Cafe("Starbucks", 20, 5, 3, 3),
                new Cafe("Chagee", 13, 5, 3, 5),
                new Cafe("Tealive", 9, 0, 4, 5),
                new Cafe("ZUS Coffee", 10, 3, 4, 5),
                new Cafe("Bingxue", 8, 0, 5, 1)
        );

    }
}