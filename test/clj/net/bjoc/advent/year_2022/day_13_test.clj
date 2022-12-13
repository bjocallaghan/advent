(ns net.bjoc.advent.year-2022.day-13-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-13 :refer :all]))

(deftest good-index-sum-by-example
  (is (= 13 (file->good-index-sum "data/year_2022/day_13_example1.input"))))

(deftest decoder-key-by-example
  (is (= 140 (file->decoder-key "data/year_2022/day_13_example1.input"))))
