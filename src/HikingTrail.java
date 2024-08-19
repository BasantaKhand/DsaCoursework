//question 5 b solutions ..

import java.util.Deque;
import java.util.LinkedList;

public class HikingTrail {

    // Function to find the longest consecutive stretch within elevation gain limit k
    public static int longestStretch(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return 0;

        // Deques to maintain the min and max values within the current window
        Deque<Integer> minDeque = new LinkedList<>();
        Deque<Integer> maxDeque = new LinkedList<>();

        int left = 0; // Left pointer of the sliding window
        int longest = 0; // Longest valid stretch

        // Iterate through each element with the right pointer
        for (int right = 0; right < n; right++) {
            // Update minDeque with the current element
            while (!minDeque.isEmpty() && nums[minDeque.peekLast()] >= nums[right]) {
                minDeque.pollLast();
            }
            minDeque.addLast(right);

            // Update maxDeque with the current element
            while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] <= nums[right]) {
                maxDeque.pollLast();
            }
            maxDeque.addLast(right);

            // Check if the current window is valid
            while (nums[maxDeque.peekFirst()] - nums[minDeque.peekFirst()] > k) {
                // If not valid, shrink the window from the left
                if (minDeque.peekFirst() == left) minDeque.pollFirst();
                if (maxDeque.peekFirst() == left) maxDeque.pollFirst();
                left++;
            }

            // Update the longest stretch
            longest = Math.max(longest, right - left + 1);
        }

        return longest;
    }

    public static void main(String[] args) {
        // Example 1
        int[] nums1 = {1, 3, 6, 7, 9, 2, 5, 8};
        int k1 = 3;
        System.out.println("Example 1: Longest stretch = " + longestStretch(nums1, k1)); // Output: 4

        // Example 2
        int[] nums2 = {10, 13, 15, 18, 12, 8, 7, 14, 20};
        int k2 = 5;
        System.out.println("Example 2: Longest stretch = " + longestStretch(nums2, k2)); // Output: 3
    }
}

// I obtained the output executing above codes

//Example 1: Longest stretch = 3
//Example 2: Longest stretch = 3
