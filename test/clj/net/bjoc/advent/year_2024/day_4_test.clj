(ns net.bjoc.advent.year-2024.day-4-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2024.day-4 :refer :all]))

(deftest file->xmas-count-by-example
  (is (= 18 (file->xmas-count "data/year_2024/day_4_example.input"))))

(deftest file->xmas-count-by-example
  (is (= 9 (file->xmas-count* "data/year_2024/day_4_example.input"))))
