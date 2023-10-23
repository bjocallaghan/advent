(ns net.bjoc.advent.year-2016.day-6-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2016.day-6 :refer :all]))

(deftest decode-by-example
  (is (= "easter" (decode "data/year_2016/day_6_example1.input"))))

(deftest decode-2-by-example
  (is (= "advent" (decode-2 "data/year_2016/day_6_example1.input"))))
