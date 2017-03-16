package cppt87.codility.iterations;

public class Iterations {
	/*
	 * A binary gap within a positive integer N is any maximal sequence of
	 * consecutive zeros that is surrounded by ones at both ends in the binary
	 * representation of N.
	 * 
	 * For example, number 9 has binary representation 1001 and contains a
	 * binary gap of length 2. The number 529 has binary representation
	 * 1000010001 and contains two binary gaps: one of length 4 and one of
	 * length 3. The number 20 has binary representation 10100 and contains one
	 * binary gap of length 1. The number 15 has binary representation 1111 and
	 * has no binary gaps.
	 * 
	 * Write a function:
	 * 
	 * class Solution { public int solution(int N); }
	 * 
	 * that, given a positive integer N, returns the length of its longest
	 * binary gap. The function should return 0 if N doesn't contain a binary
	 * gap.
	 * 
	 * For example, given N = 1041 the function should return 5, because N has
	 * binary representation 10000010001 and so its longest binary gap is of
	 * length 5.
	 * 
	 * Assume that:
	 * 
	 * N is an integer within the range [1..2,147,483,647].
	 * 
	 * Complexity:
	 * 
	 * expected worst-case time complexity is O(log(N));
	 * 
	 * expected worst-case space complexity is O(1).
	 */
	public static int binaryGap(int N) {
		String binary = Integer.toBinaryString(N);
		int longestGap = 0;
		int zeroCount = 0;
		for (int i = 0; i < binary.length(); i++) {
			if (binary.charAt(i) == '1') {
				if (zeroCount > longestGap)
					longestGap = zeroCount;
				zeroCount = 0;
			} else {
				zeroCount++;
			}
		}
		return longestGap;
	}

	/*
	 * A non-empty zero-indexed array A consisting of N integers is given. The
	 * array contains an odd number of elements, and each element of the array
	 * can be paired with another element that has the same value, except for
	 * one element that is left unpaired.
	 * 
	 * For example, in array A such that:
	 * 
	 * A[0] = 9 A[1] = 3 A[2] = 9 A[3] = 3 A[4] = 9 A[5] = 7 A[6] = 9 the
	 * elements at indexes 0 and 2 have value 9, the elements at indexes 1 and 3
	 * have value 3, the elements at indexes 4 and 6 have value 9, the element
	 * at index 5 has value 7 and is unpaired. Write a function:
	 * 
	 * class Solution { public int solution(int[] A); }
	 * 
	 * that, given an array A consisting of N integers fulfilling the above
	 * conditions, returns the value of the unpaired element.
	 * 
	 * For example, given array A such that:
	 * 
	 * A[0] = 9 A[1] = 3 A[2] = 9 A[3] = 3 A[4] = 9 A[5] = 7 A[6] = 9 the
	 * function should return 7, as explained in the example above.
	 * 
	 * Assume that:
	 * 
	 * N is an odd integer within the range [1..1,000,000];
	 * 
	 * each element of array A is an integer within the range
	 * [1..1,000,000,000];
	 * 
	 * all but one of the values in A occur an even number of times.
	 * 
	 * Complexity:
	 * 
	 * expected worst-case time complexity is O(N);
	 * 
	 * expected worst-case space complexity is O(1), beyond input storage (not
	 * counting the storage required for input arguments).
	 * 
	 * Elements of input arrays can be modified.
	 */
//	public static int oddOccurrencesInArray(int[] A) {
//		
//	}

}
