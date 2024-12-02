(ns net.bjoc.advent.year-2024.day-1-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2024.day-1 :refer :all]))

(deftest file->total-distance-by-example
  (is (= 11 (file->total-distance "data/year_2024/day_1_example.input"))))

(deftest file->similarity-score-by-example
  (is (= 31 (file->similarity-score "data/year_2024/day_1_example.input"))))
