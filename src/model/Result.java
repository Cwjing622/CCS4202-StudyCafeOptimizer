package model;

import java.util.List;

/**
 * Represents the optimization result returned by the study café optimization algorithms.
 * This class encapsulates data regarding the selected cafés, financial metrics, 
 * performance benefits, and operational execution speed.
 * <p>
 * This object is immutable and acts purely as a data carrier between the business 
 * logic layer and the user interface.
 */
public class Result {

    private final List<Cafe> selectedCafes;
    private final int totalCost;
    private final int totalStudyScore;
    private final long executionTime;

    // Only used by Dynamic Programming
    private final int[][] dpTable;

    /**
     * Constructs a final optimization result metric object.
     *
     * @param selectedCafes    the list of cafés selected by the optimization algorithm
     * @param totalCost        the aggregated monetary cost (RM) of the selected cafés
     * @param totalStudyScore  the combined total derived study suitability score achieved
     * @param executionTime    the total computing time taken by the algorithm in milliseconds (ms) or nanoseconds (ns)
     */
public Result(List<Cafe> selectedCafes, int totalCost, int totalStudyScore, long executionTime, int[][] dpTable) {
        this.selectedCafes = List.copyOf(selectedCafes);
        this.totalCost = totalCost;
        this.totalStudyScore = totalStudyScore;
        this.executionTime = executionTime;
        this.dpTable = dpTable;
    }

    /**
     * Gets the unmodifiable collection of selected cafés.
     *
     * @return the list of chosen {@link Cafe} items
     */
    public List<Cafe> getSelectedCafes() {
        return selectedCafes;
    }

    /**
     * Gets the sum total monetary cost of all selected cafés.
     *
     * @return the total cost in RM
     */
    public int getTotalCost() {
        return totalCost;
    }

    /**
     * Gets the combined total study suitability value score.
     *
     * @return the total accumulated study score
     */
    public int getTotalStudyScore() {
        return totalStudyScore;
    }

    /**
     * Gets the computational execution window metric recorded by the algorithm.
     *
     * @return the engine execution runtime metrics
     */
    public long getExecutionTime() {
        return executionTime;
    }

    @Override
    public String toString() {
        return "Result{" +
                "selectedCafesCount=" + selectedCafes.size() +
                ", totalCost=" + totalCost +
                ", totalStudyScore=" + totalStudyScore +
                ", executionTime=" + executionTime + " ms" +
                '}';
    }

    public int[][] getDpTable() {
    return dpTable;
}
}