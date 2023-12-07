(ns net.bjoc.advent.year-2023.day-4-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-4 :refer :all]))

(deftest file->score-sum-by-example
  (is (= 13 (file->score-sum "data/year_2023/day_4_example1.input"))))

(deftest file->card-total-by-example
  (is (= 30 (file->card-total "data/year_2023/day_4_example1.input"))))
