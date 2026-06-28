package algorithm;

import java.util.ArrayList;
import java.util.List;

import model.Cafe;
import model.Result;

/**
 * Dynamic Programming implementation of the 0/1 Knapsack Problem.
 * Weight = Price
 * Value = Study Score
 */
public class DynamicProgramming {

    public Result solve(List<Cafe> cafes, int budget) {

        long startTime = System.nanoTime();

        int n = cafes.size();

        // DP Table
        int[][] dp = new int[n + 1][budget + 1];

        // Build DP table
        for (int i = 1; i <= n; i++) {

            Cafe cafe = cafes.get(i - 1);

            int weight = cafe.getPrice();
            int value = cafe.getStudyScore();

            for (int w = 0; w <= budget; w++) {

                if (weight <= w) {

                    dp[i][w] = Math.max(
                            dp[i - 1][w],
                            dp[i - 1][w - weight] + value);

                } else {

                    dp[i][w] = dp[i - 1][w];

                }
            }
        }

        // Backtracking
        List<Cafe> selectedCafes = new ArrayList<>();

        int totalCost = 0;
        int totalStudyScore = dp[n][budget];

        int w = budget;

        for (int i = n; i > 0; i--) {

            if (dp[i][w] != dp[i - 1][w]) {

                Cafe cafe = cafes.get(i - 1);

                selectedCafes.add(0, cafe);

                totalCost += cafe.getPrice();

                w -= cafe.getPrice();
            }
        }

        long endTime = System.nanoTime();

        return new Result(
                selectedCafes,
                totalCost,
                totalStudyScore,
                endTime - startTime,
                dp
        );
    }
}