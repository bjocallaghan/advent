(ns net.bjoc.advent.util.matrix-test
  (:require [net.bjoc.advent.util.misc :as misc])
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.util.matrix]))

(def numbers-2x2
  {[0 0] 1  [1 0] 2
   [0 1] 3  [1 1] 4})

(def rotated-2x2
  {[0 0] 3  [1 0] 1
   [0 1] 4  [1 1] 2})

(def flipped-2x2
  {[0 0] 2  [1 0] 1
   [0 1] 4  [1 1] 3})

(def numbers-4x4
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

(def small-strings-2x2
  {[0 0] "a" [1 0] "b"
   [0 1] "c" [1 1] "d"})

(def strings-3x2
  {[0 0] "abcd" [1 0] "efg" [2 0] "hijkl"
   [0 1] "mno" [1 1] "pqrs" [2 1] "tuv"})

(def characters-2x2
  {[0 0] \a [1 0] \b
   [0 1] \c [1 1] \d})

(def keywords-2x2
  {[0 0] :abc [1 0] :def
   [0 1] :ghi [1 1] :jkl})

(def sparse-characters-2x2
  {[0 0] \space [1 0] \x
   [0 1] \space [1 1] \space})

(deftest rotate-sanity
  (testing "Normal rotation operations"
    (is (= rotated-2x2 (rotate numbers-2x2)))
    (is (= rotated-4x4 (rotate numbers-4x4))))
  (testing "Repetitive operations"
    (is (= left-rotated-4x4 (rotate numbers-4x4 3)))
    (is (= numbers-4x4 (rotate numbers-4x4 4)))
    (is (= rotated-4x4 (rotate numbers-4x4 5)))
    (is (= left-rotated-4x4 (rotate numbers-4x4 -1)))))

(deftest flip-sanity
  (testing "Normal flip operations"
    (is (= flipped-2x2 (flip numbers-2x2)))
    (is (= flipped-4x4 (flip numbers-4x4))))
  (testing "Repetitive flip operations"
    (is (= numbers-4x4 (flip numbers-4x4 2)))
    (is (= numbers-4x4 (flip numbers-4x4 4)))
    (is (= flipped-4x4 (flip numbers-4x4 3)))
    (is (= flipped-4x4 (flip numbers-4x4 -1)))))

(deftest size-sanity
  (is (= [2 2] (size small-strings-2x2)))
  (is (= [3 2] (size strings-3x2)))
  (is (= [4 4] (size numbers-4x4))))

(deftest rows-by-examples
  (is (= [[1 2] [3 4]] (rows numbers-2x2)))
  (is (= [["abcd" "efg" "hijkl"] ["mno" "pqrs" "tuv"]] (rows strings-3x2))))

(deftest cols-by-examples
  (is (= [[1 3] [2 4]] (cols numbers-2x2)))
  (is (= [["abcd" "mno"] ["efg" "pqrs"] ["hijkl" "tuv"]] (cols strings-3x2))))

(deftest guess-type-by-examples
  (testing "Guessing matrix types"
    (is (= :numbers (guess-type numbers-2x2)))
    (is (= :characters (guess-type characters-2x2)))
    (is (= :characters (guess-type small-strings-2x2)))
    (is (= :strings (guess-type strings-3x2)))))

(deftest dump-by-examples
  (let [dump* #(dump % :stream-writer misc/null-writer)]
    (testing "Dump by examples"
      (is (= " 1  2  3  4\n 5  6  7  8\n 9 10 11 12\n13 14 15 16"
             (dump* numbers-4x4)))
      (is (= "ab\ncd"
             (dump* small-strings-2x2)))
      (is (= "ab\ncd"
             (dump* characters-2x2)))
      (is (= ":abc :def\n:ghi :jkl"
             (dump* keywords-2x2)))
      (is (= "abcd  efg hijkl\n mno pqrs   tuv"
             (dump* strings-3x2))))
    (testing "Dump/read cycles"
      (is (= (-> numbers-2x2 dump*)
             (-> numbers-2x2 dump* from-string dump*)))
      (is (= (-> numbers-4x4 dump*)
             (-> numbers-4x4 dump* from-string dump*)))
      (is (= (-> strings-3x2 dump*)
             (-> strings-3x2 dump* from-string dump*))))))

(deftest from-string-by-examples
  (is (= numbers-2x2 (from-string "1 2\n3 4")))
  (is (= characters-2x2 (from-string "ab\ncd")))
  (is (= sparse-characters-2x2 (from-string " x\n  " :type :characters))))
