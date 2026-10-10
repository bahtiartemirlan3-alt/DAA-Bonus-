BST vs Hash Table (Java)

I implemented a Binary Search Tree and a Hash Table (chaining) in Java and compared insert, search and delete on 100, 1,000 and 10,000 elements.

Files: BST.java, HashTable.java, Benchmark.java, results.csv Run: javac *.java then java Benchmark

Complexity
Operation	BST best	BST average	 BST worst	Hash average	Hash worst
Insert	    O(1)	     O(log n)	   O(n)	       O(1)	           O(n)
Search	    O(1)	     O(log n)	   O(n)	       O(1)	           O(n)
Delete	    O(1)	     O(log n)	   O(n)	       O(1)	           O(n)
BST: cost depends on tree height. Random keys give height ~log n. Sorted keys turn the tree into a list, so O(n). Best case is the key at the root.
Hash table: O(1) if keys are spread evenly. Worst case is all keys in one bucket (O(n)). Resizing is O(n) but rare, so average insert stays O(1).
Results (ms for n operations)

Random keys:

n	BST insert	BST search	BST delete	Hash insert	Hash search	Hash delete
100	  0.004	      0.004	      0.004	     0.028	      0.002	      0.002
1,000	0.047	  0.035	      0.059      0.112	      0.013	      0.014
10,000	0.821	  0.560	      0.600	     0.449	      0.081	      0.108

Sorted keys (worst case for BST):

n	  BST insert	BST search	BST delete	Hash insert	Hash search	Hash delete
100	    0.012	      0.012	     0.001	      0.020	      0.001	      0.001
1,000	0.723	      0.965	     0.007	      0.049	      0.010	      0.021
10,000	63.806	     91.328	     0.028	      0.150	      0.020	      0.038

Full data is in results.csv.

Conclusions
With random keys and n = 10,000 the hash table is about 7x faster for search and about 5x faster for delete. Insert is only about 2x faster.
With sorted keys the BST becomes very slow: for n = 10,000 insert took 64 ms and search 91 ms, while the hash table took 0.15 ms and 0.02 ms. From n = 1,000 to 10,000 the BST time grew about 90 times, which fits O(n) per operation.
BST delete on sorted keys is fast only because I delete in the same order, so I always remove the root. It is a side effect of the test.
At small n (100, 1,000) the BST is even faster for insert, because the hash table has extra cost (hashing, resizing). The times are tiny, so noise matters.
In practice the BST is slower because of tree depth and nodes scattered in memory. The hash table computes the position directly. The BST is still useful when keys must stay ordered, but it needs balancing (AVL, red-black) to avoid the sorted-input problem.