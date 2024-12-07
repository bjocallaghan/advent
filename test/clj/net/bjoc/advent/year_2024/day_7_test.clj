(ns net.bjoc.advent.year-2024.day-7-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2024.day-7 :refer :all]))

(deftest file->good-sum-by-example
  (is (= 3740 (file->good-sum "data/year_2024/day_7_example.input"))))

(deftest file->good-sum*-by-example
  (is (= 11387 (file->good-sum* "data/year_2024/day_7_example.input"))))
