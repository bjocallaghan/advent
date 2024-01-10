(ns net.bjoc.advent.year-2023.day-9-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-9 :refer :all]))

(deftest file->nexts-sum-by-example
  (is (= 114 (file->nexts-sum "data/year_2023/day_9_example1.input"))))

(deftest alternate-neg-sanity  
  (is (= [1 -2 3 -4 5 -6] (alternate-neg [1 2 3 4 5 6])))
  (is (= [1 -2 3 -4 5] (alternate-neg [1 2 3 4 5]))))

(deftest file->prevs-sum-by-example
  (is (= 2 (file->prevs-sum "data/year_2023/day_9_example1.input"))))
