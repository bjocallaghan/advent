(ns net.bjoc.advent.year-2023.day-16-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-16 :refer :all]))

(deftest file->num-energized-by-example
  (is (= 46 (file->num-energized "data/year_2023/day_16_example1.input"))))

(deftest file->num-energized-by-example
  (is (= 51 (file->best-energized "data/year_2023/day_16_example1.input"))))
