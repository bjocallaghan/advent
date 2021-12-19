(ns net.bjoc.advent.year-2021.day-18-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-18]))

(deftest explode-by-examples
  (is (= [[[[0 9] 2] 3] 4] (sf-reduce [[[[[9 8] 1] 2] 3] 4])))
  (is (= [7 [6 [5 [7 0]]]] (sf-reduce [7 [6 [5 [4 [3 2]]]]])))
  (is (= [[6 [5 [7 0]]] 3] (sf-reduce [[6 [5 [4 [3 2]]]] 1])))
  (is (= [[3 [2 [8 0]]] [9 [5 [7 0]]]] (sf-reduce [[3 [2 [8 0]]] [9 [5 [4 [3 2]]]]])))
  )

(deftest splid-by-example
  (is (= [[[[0 7] 4] [[7 8] [6 0]]] [8 1]] (sf-reduce [[[[[4 3] 4] 4] [7 [[8 4] 9]]] [1 1]]))))

(deftest magnitude-by-examples
  (is (= 143 (mag [[1 2] [[3 4] 5]])))
  (is (= 1384 (mag [[[[0 7] 4] [[7 8] [6 0]]] [8 1]])))
  (is (= 445 (mag [[[[1 1] [2 2]] [3 3]] [4 4]])))
  (is (= 791 (mag [[[[3 0] [5 3]] [4 4]] [5 5]])))
  (is (= 1137 (mag [[[[5 0] [7 4]] [5 5]] [6 6]])))
  (is (= 3488 (mag [[[[8 7] [7 7]] [[8 6] [7 7]]] [[[0 7] [6 6]] [8 7]]]))))
