import java.util.Arrays;
import java.util.Random;

public class TSPHillClimbing {

    private static final Random random = new Random();

    // Generate a random initial tour
    private static int[] generateInitialTour(int n) {
        int[] tour = new int[n];
        for (int i = 0; i < n; i++) {
            tour[i] = i;
        }
        // Shuffle the array to get a random tour
        for (int i = n - 1; i > 0; i--) {
            int randomIndex = random.nextInt(i + 1);
            int temp = tour[i];
            tour[i] = tour[randomIndex];
            tour[randomIndex] = temp;
        }
        return tour;
    }

    // Calculate the total distance of a tour
    private static double calculateTourDistance(int[] tour, double[][] distanceMatrix) {
        double totalDistance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            totalDistance += distanceMatrix[tour[i]][tour[i + 1]];
        }
        totalDistance += distanceMatrix[tour[tour.length - 1]][tour[0]]; // Return to start
        return totalDistance;
    }

    // Generate a neighboring solution by swapping two cities
    private static int[] generateNeighbor(int[] tour) {
        int n = tour.length;
        int[] newTour = Arrays.copyOf(tour, n);
        int i = random.nextInt(n);
        int j = random.nextInt(n);
        while (i == j) {
            j = random.nextInt(n); // Ensure we swap different cities
        }
        // Swap two cities
        int temp = newTour[i];
        newTour[i] = newTour[j];
        newTour[j] = temp;
        return newTour;
    }

    // Hill Climbing algorithm for TSP
    private static int[] hillClimbing(double[][] distanceMatrix) {
        int n = distanceMatrix.length;
        int[] currentTour = generateInitialTour(n);
        double currentDistance = calculateTourDistance(currentTour, distanceMatrix);

        boolean improvement = true;
        while (improvement) {
            improvement = false;
            int[] bestNeighbor = null;
            double bestNeighborDistance = currentDistance;

            // Explore multiple neighbors for better performance
            for (int k = 0; k < n * 2; k++) {
                int[] newTour = generateNeighbor(currentTour);
                double newDistance = calculateTourDistance(newTour, distanceMatrix);
                if (newDistance < bestNeighborDistance) {
                    bestNeighbor = newTour;
                    bestNeighborDistance = newDistance;
                }
            }

            if (bestNeighbor != null && bestNeighborDistance < currentDistance) {
                currentTour = bestNeighbor;
                currentDistance = bestNeighborDistance;
                improvement = true;
            }
        }
        return currentTour;
    }

    // Example usage
    public static void main(String[] args) {
        // Example 1: 4 cities with specified distances
        double[][] distanceMatrix1 = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };
        int[] bestTour1 = hillClimbing(distanceMatrix1);
        System.out.println("Best tour for example 1: " + Arrays.toString(bestTour1));
        System.out.println("Tour distance: " + calculateTourDistance(bestTour1, distanceMatrix1));

        // Example 2: 5 cities with specified distances
        double[][] distanceMatrix2 = {
                {0, 2, 9, 10, 7},
                {1, 0, 6, 4, 3},
                {15, 7, 0, 8, 5},
                {6, 3, 12, 0, 11},
                {9, 14, 5, 4, 0}
        };
        int[] bestTour2 = hillClimbing(distanceMatrix2);
        System.out.println("Best tour for example 2: " + Arrays.toString(bestTour2));
        System.out.println("Tour distance: " + calculateTourDistance(bestTour2, distanceMatrix2));
    }
}
