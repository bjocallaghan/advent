(ns net.bjoc.advent.year-2022.day-17-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-17 :refer :all]))

(deftest height-by-example
  (is (= 1 (file->height 1 "data/year_2022/day_17_example1.input")))
  (is (= 17 (file->height 10 "data/year_2022/day_17_example1.input")))
  (is (= 3068 (file->height 2022 "data/year_2022/day_17_example1.input"))))
