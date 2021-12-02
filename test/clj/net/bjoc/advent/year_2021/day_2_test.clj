(ns net.bjoc.advent.year-2021.day-2-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-2]))

(deftest position-product-by-example
   (is (= 150 (file->location-product "data/year_2021/day_2_example1.input"))))

(deftest position-product*-by-example
   (is (= 900 (file->location-product* "data/year_2021/day_2_example1.input"))))

