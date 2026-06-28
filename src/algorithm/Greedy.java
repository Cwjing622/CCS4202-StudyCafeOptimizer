package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.Cafe;
import model.Result;

/**
 * Greedy Algorithm for the Study Café Optimizer.
 * Selects cafés based on the highest Study Score / Price ratio.
 */
public class Greedy {

    /**
     * Solves the study café selection problem using a Greedy heuristic.
     *
     * @param cafes  List of available cafés
     * @param budget Maximum budget (RM)
     * @return Optimization result
     */
    public Result solve(List<Cafe> cafes, int budget) {

        long startTime = System.nanoTime();

        // Copy original list so it won't be modified
        List<Cafe> sortedCafes = new ArrayList<>(cafes);

        // Sort by Study Score / Price (highest first)
        sortedCafes.sort(
                Comparator.comparingDouble(
                        (Cafe cafe) -> (double) cafe.getStudyScore() / cafe.getPrice())
                        .reversed());

        List<Cafe> selectedCafes = new ArrayList<>();

        int totalCost = 0;
        int totalStudyScore = 0;

        // Greedy selection
        for (Cafe cafe : sortedCafes) {

            if (totalCost + cafe.getPrice() <= budget) {

                selectedCafes.add(cafe);

                totalCost += cafe.getPrice();

                totalStudyScore += cafe.getStudyScore();
            }
        }

        long endTime = System.nanoTime();

        return new Result(
                selectedCafes,
                totalCost,
                totalStudyScore,
                endTime - startTime,
                null
            );
    }
}