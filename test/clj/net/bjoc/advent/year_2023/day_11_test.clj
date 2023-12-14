(ns net.bjoc.advent.year-2023.day-11-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-11 :refer :all]))

(deftest file->pair-distances-sum-by-example
  (is (= 374 (file->pair-distances-sum "data/year_2023/day_11_example1.input"))))

(deftest file->pair-distances-sum*-by-example
  (is (= 374 (file->pair-distances-sum* 2 "data/year_2023/day_11_example1.input")))
  (is (= 1030 (file->pair-distances-sum* 10 "data/year_2023/day_11_example1.input")))
  (is (= 8410 (file->pair-distances-sum* 100 "data/year_2023/day_11_example1.input"))))
