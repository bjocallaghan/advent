(ns net.bjoc.advent.year-2023.day-13-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-13 :refer :all]))

(deftest file->result-by-example
  (is (= 405 (file->result "data/year_2023/day_13_example1.input"))))

(deftest file->result-2-by-example
  (is (= 400 (file->result-2 "data/year_2023/day_13_example1.input"))))
