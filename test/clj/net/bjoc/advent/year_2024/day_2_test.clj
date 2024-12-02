(ns net.bjoc.advent.year-2024.day-2-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2024.day-2 :refer :all]))

(deftest safe?-by-example
  (is (= true (safe? [7 6 4 2 1])))
  (is (= false (safe? [1 2 7 8 9])))
  (is (= false (safe? [9 7 6 2 1])))
  (is (= false (safe? [1 3 2 4 5])))
  (is (= false (safe? [8 6 4 4 1])))
  (is (= true (safe? [1 3 6 7 9]))))

(deftest safe-count-by-example
  (is (= 2 (file->safe-count "data/year_2024/day_2_example.input"))))

(deftest expand-via-drop-by-example
  (is (= [[2 3] [1 3] [1 2]] (expand-via-drop [1 2 3]))))

(deftest safe*-count-by-example
  (is (= 4 (file->safe*-count "data/year_2024/day_2_example.input"))))
