(ns net.bjoc.advent.year-2024.day-6-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2024.day-6 :refer :all]))

(deftest file->visted-count-by-example
  (is (= 41 (file->visted-count "data/year_2024/day_6_example.input"))))

(deftest safe-insertions-count-by-example
  (is (= 6 (file->safe-insertions-count "data/year_2024/day_6_example.input"))))
