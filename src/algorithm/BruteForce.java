package algorithm;

import java.util.ArrayList;
import java.util.List;

import model.Cafe;
import model.Result;

/**
 * Brute Force algorithm for the Study Café Optimizer.
 * Tries every possible combination of cafés.
 */
public class BruteForce {

    public Result solve(List<Cafe> cafes, int budget) {

        long startTime = System.nanoTime();

        List<Cafe> bestSelection = new ArrayList<>();

        int bestScore = 0;
        int bestCost = 0;

        int n = cafes.size();

        // Try every possible subset
        for (int mask = 0; mask < (1 << n); mask++) {

            List<Cafe> currentSelection = new ArrayList<>();

            int currentCost = 0;
            int currentScore = 0;

            for (int i = 0; i < n; i++) {

                if ((mask & (1 << i)) != 0) {

                    Cafe cafe = cafes.get(i);

                    currentSelection.add(cafe);

                    currentCost += cafe.getPrice();

                    currentScore += cafe.getStudyScore();
                }
            }

            if (currentCost <= budget && currentScore > bestScore) {

                bestScore = currentScore;
                bestCost = currentCost;
                bestSelection = new ArrayList<>(currentSelection);
            }
        }

        long endTime = System.nanoTime();

        return new Result(
                bestSelection,
                bestCost,
                bestScore,
                endTime - startTime,
                null
        );
    }
}