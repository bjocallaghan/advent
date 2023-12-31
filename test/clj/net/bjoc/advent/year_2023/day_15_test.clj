(ns net.bjoc.advent.year-2023.day-15-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-15 :refer :all]))

(deftest hash*-by-example
  (is (= 52 (hash* "HASH"))))

(deftest file-hash-by-example
  (is (= 1320 (file-hash "data/year_2023/day_15_example1.input"))))

(deftest file->focusing-power-by-example
  (is (= 145 (file->focusing-power "data/year_2023/day_15_example1.input"))))
