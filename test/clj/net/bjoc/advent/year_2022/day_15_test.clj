(ns net.bjoc.advent.year-2022.day-15-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-15 :refer :all]))

(deftest within-by-example
  (is (= #{[5 5]} (within [5 5] 0)))
  (is (= #{[5 5] [5 6] [5 4] [4 5] [6 5]} (within [5 5] 1))))

(deftest num-empties-by-example
  (is (= 26 (file->num-empties 10 "data/year_2022/day_15_example1.input"))))

(deftest num-empties-2-by-example
  (is (= 26 (file->num-empties-2 10 "data/year_2022/day_15_example1.input"))))

(deftest num-empties-by-example
  (is (= 56000011 (file->tuning-frequency 20 "data/year_2022/day_15_example1.input"))))
