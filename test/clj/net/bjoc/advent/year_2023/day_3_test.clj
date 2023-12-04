(ns net.bjoc.advent.year-2023.day-3-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-3 :refer :all]))

(deftest part-number-sum-by-example
  (is (= 4361 (part-number-sum "data/year_2023/day_3_example1.input"))))

(deftest gear-ratio-sum-by-example
  (is (= 467835 (gear-ratio-sum "data/year_2023/day_3_example1.input"))))
