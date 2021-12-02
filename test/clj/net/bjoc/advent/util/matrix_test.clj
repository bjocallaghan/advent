(ns net.bjoc.advent.util.matrix-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.util.matrix]))

(def normal-2x2
  {[0 0] 1  [1 0] 2
   [0 1] 3  [1 1] 4})

(def rotated-2x2
  {[0 0] 3  [1 0] 1
   [0 1] 4  [1 1] 2})

(def flipped-2x2
  {[0 0] 2  [1 0] 1
   [0 1] 4  [1 1] 3})

(def normal-4x4
  {[0 0] 1   [1 0] 2   [2 0] 3   [3 0] 4
   [0 1] 5   [1 1] 6   [2 1] 7   [3 1] 8
   [0 2] 9   [1 2] 10  [2 2] 11  [3 2] 12
   [0 3] 13  [1 3] 14  [2 3] 15  [3 3] 16})

(def rotated-4x4
  {[0 0] 13  [1 0] 9   [2 0] 5   [3 0] 1
   [0 1] 14  [1 1] 10  [2 1] 6   [3 1] 2
   [0 2] 15  [1 2] 11  [2 2] 7   [3 2] 3
   [0 3] 16  [1 3] 12  [2 3] 8   [3 3] 4})

(def left-rotated-4x4
  {[0 0] 4   [1 0] 8   [2 0] 12  [3 0] 16
   [0 1] 3   [1 1] 7   [2 1] 11  [3 1] 15
   [0 2] 2   [1 2] 6   [2 2] 10  [3 2] 14
   [0 3] 1   [1 3] 5   [2 3] 9   [3 3] 13})

(def flipped-4x4
  {[0 0] 4   [1 0] 3   [2 0] 2   [3 0] 1
   [0 1] 8   [1 1] 7   [2 1] 6   [3 1] 5
   [0 2] 12  [1 2] 11  [2 2] 10  [3 2] 9
   [0 3] 16  [1 3] 15  [2 3] 14  [3 3] 13})

(deftest rotate-sanity
  (testing "Normal rotation operations"
    (is (= rotated-2x2 (rotate normal-2x2)))
    (is (= rotated-4x4 (rotate normal-4x4))))
  (testing "Repetitive operations"
    (is (= left-rotated-4x4 (rotate normal-4x4 3)))
    (is (= normal-4x4 (rotate normal-4x4 4)))
    (is (= rotated-4x4 (rotate normal-4x4 5)))
    (is (= left-rotated-4x4 (rotate normal-4x4 -1)))))

(deftest flip-sanity
  (testing "Normal flip operations"
    (is (= flipped-2x2 (flip normal-2x2)))
    (is (= flipped-4x4 (flip normal-4x4))))
  (testing "Repetitive flip operations"
    (is (= normal-4x4 (flip normal-4x4 2)))
    (is (= normal-4x4 (flip normal-4x4 4)))
    (is (= flipped-4x4 (flip normal-4x4 3)))
    (is (= flipped-4x4 (flip normal-4x4 -1)))))
