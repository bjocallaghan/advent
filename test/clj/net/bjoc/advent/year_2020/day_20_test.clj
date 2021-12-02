(ns net.bjoc.advent.year-2020.day-20-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2020.day-20]))

(deftest product-of-corners-by-example
  (is (= 20899048083289
         (file->product-of-corners "data/year_2020/day_20_example1.input"))))
