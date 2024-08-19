import java.util.ArrayList;
import java.util.List;

public class FriendRequests {

    // Define the Union-Find data structure
    static class UnionFind {
        private int[] parent;
        private int[] rank;

        // Constructor to initialize the Union-Find data structure
        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i; // Each node is initially its own parent
                rank[i] = 1;   // Rank is used to keep the tree flat
            }
        }

        // Find the representative (root) of the set containing x
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        // Union operation to merge sets containing x and y
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return false; // x and y are already in the same set
            }
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX; // Attach smaller tree under larger tree
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY; // Attach smaller tree under larger tree
            } else {
                parent[rootY] = rootX; // Same rank, so attach and increase rank
                rank[rootX]++;
            }
            return true;
        }

        // Check if x and y are in the same set
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }

    // Function to process friend requests and determine if they can be accepted
    public static List<String> processFriendRequests(int n, int[][] restrictions, int[][] requests) {
        UnionFind uf = new UnionFind(n); // Initialize Union-Find for n houses
        List<String> results = new ArrayList<>();

        // Iterate through each friend request
        for (int[] request : requests) {
            int house1 = request[0];
            int house2 = request[1];
            boolean canBeFriends = true;

            // Check if the new friendship would violate any restriction
            for (int[] restriction : restrictions) {
                int restrictedHouse1 = restriction[0];
                int restrictedHouse2 = restriction[1];
                if ((uf.isConnected(house1, restrictedHouse1) && uf.isConnected(house2, restrictedHouse2)) ||
                        (uf.isConnected(house1, restrictedHouse2) && uf.isConnected(house2, restrictedHouse1))) {
                    canBeFriends = false;
                    break; // No need to check further restrictions
                }
            }

            // If the friendship does not violate any restriction, approve it
            if (canBeFriends) {
                uf.union(house1, house2);
                results.add("approved");
            } else {
                // Otherwise, deny the request
                results.add("denied");
            }
        }

        return results; // Return the list of results
    }

    // Main method to test the solution
    public static void main(String[] args) {
        int n = 5; // Number of houses
        int[][] restrictions = {{0, 1}, {1, 2}, {2, 3}}; // Restrictions list
        int[][] requests = {{0, 4}, {1, 2}, {3, 1}, {3, 4}}; // Friend requests list
        List<String> result = processFriendRequests(n, restrictions, requests);

        // Print the results of each friend request
        for (String res : result) {
            System.out.println(res);
        }
    }
}
