(ns net.bjoc.advent.util.coords-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.util.coords]))

(deftest add-coords-sanity
  (testing "Should handle arbitrary number of arguments"
    (is (= [1 2] (add-coords [1 2])))
    (is (= [4 6] (add-coords [1 2] [3 4])))
    (is (= [9 12] (add-coords [1 2] [3 4] [5 6])))
    (is (= [16 20] (add-coords [1 2] [3 4] [5 6] [7 8]))))
  (testing "Should handle arbitrary number of dimensions"
    (is (= [3] (add-coords [1] [2])))
    (is (= [4 6] (add-coords [1 2] [3 4])))
    (is (= [5 7 9] (add-coords [1 2 3] [4 5 6])))
    (is (= [6 8 10 12] (add-coords [1 2 3 4] [5 6 7 8]))))
  (testing "Must have one or more arguments"
    (is (thrown? clojure.lang.ArityException (add-coords))))
  (testing "All arguments must be vectors of the same dimension"
    (is (thrown? Exception (add-coords [1 2] [3 4 5])))))

(deftest scale-sanity
  (is (= [6 9] (scale [2 3] 3)))
  (is (= [2 4 6] (scale [1 2 3] 2)))
  (is (= [-1 -2] (scale [1 2] -1)))
  (is (= [0 0] (scale [1 2] 0))))

(deftest manhattan-distance-sanity
  (testing "Manhattan distance from origin"
    (doseq [[coords expected-distance] [[[3 4] 7]
                                        [[-3 4] 7]
                                        [[-3 -4] 7]
                                        [[3 -4] 7]
                                        [[12 0] 12]
                                        [[0 -12] 12]]]
      (is (= expected-distance (manhattan-distance coords))
          (format "Expected distance of %s: %s"
                  (str coords) expected-distance))))
  (testing "Manhattan distance between two points"
    (is (= 4 (manhattan-distance [1 2] [3 4])))))

(deftest difference-sanity
  (testing "Difference between two sets of coords"
    (is (= [1 2] (difference [1 2] [0 0])))
    (is (= [1 2] (difference [0 0] [-1 -2])))
    (is (= [1 1 1 1 1] (difference [2 3 4 5 6] [1 2 3 4 5])))))
