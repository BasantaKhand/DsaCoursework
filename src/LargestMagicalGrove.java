// question number 4 b solutions...

public class LargestMagicalGrove {

    // Definition for a binary tree node.
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    // Helper class to store results from the subtree checks
    static class Result {
        boolean isBST;
        int sum;
        int min;
        int max;

        Result(boolean isBST, int sum, int min, int max) {
            this.isBST = isBST;
            this.sum = sum;
            this.min = min;
            this.max = max;
        }
    }

    // Variable to keep track of the maximum sum of a BST subtree
    private static int maxSum = 0;

    public static void main(String[] args) {
        // Example tree: [1,4,3,2,4,2,5,null,null,null,null,null,null,4,6]
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(4);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(2);
        root.right.right = new TreeNode(5);
        root.right.right.left = new TreeNode(4);
        root.right.right.right = new TreeNode(6);

        int result = findLargestMagicalGrove(root);
        System.out.println(result); // Expected Output: 20

        // Additional test cases
        // Test Case 1: Tree with no nodes
        TreeNode root1 = null;
        System.out.println(findLargestMagicalGrove(root1)); // Expected Output: 0

        // Test Case 2: Tree with only one node
        TreeNode root2 = new TreeNode(10);
        System.out.println(findLargestMagicalGrove(root2)); // Expected Output: 10

        // Test Case 3: Tree with multiple nodes but no valid BST subtrees
        TreeNode root3 = new TreeNode(10);
        root3.left = new TreeNode(5);
        root3.right = new TreeNode(15);
        root3.left.right = new TreeNode(20);
        root3.right.left = new TreeNode(6);

        System.out.println(findLargestMagicalGrove(root3)); // Expected Output: 15 (the largest valid BST is the single node with value 15)
    }

    // Main function to find the largest magical grove (largest BST subtree)
    public static int findLargestMagicalGrove(TreeNode root) {
        maxSum = 0; // Reset maxSum for fresh computation
        postOrderTraversal(root); // Start the post-order traversal
        return maxSum; // Return the maximum sum found
    }

    // Helper function to perform post-order traversal and check for BST properties
    private static Result postOrderTraversal(TreeNode node) {
        if (node == null) {
            // Base case: if the node is null, it is a valid BST with sum 0
            return new Result(true, 0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        // Recursively check the left and right subtrees
        Result left = postOrderTraversal(node.left);
        Result right = postOrderTraversal(node.right);

        // Check if the current node forms a BST with its left and right subtrees
        if (left.isBST && right.isBST && node.val > left.max && node.val < right.min) {
            // Calculate the sum of the current BST subtree
            int currentSum = node.val + left.sum + right.sum;
            // Update the maximum sum found so far
            maxSum = Math.max(maxSum, currentSum);
            // Determine the minimum and maximum values in the current BST subtree
            int currentMin = Math.min(node.val, left.min);
            int currentMax = Math.max(node.val, right.max);
            // Return a new Result indicating this subtree is a valid BST
            return new Result(true, currentSum, currentMin, currentMax);
        } else {
            // If the current node doesn't form a valid BST with its subtrees, return false
            return new Result(false, 0, 0, 0);
        }
    }
}

//for four conditions I got the output
//20
//0
//10
//25


