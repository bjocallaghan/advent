(ns net.bjoc.advent.year-2016.day-3-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2016.day-3 :refer :all]))

(deftest valid?-by-example
  (is (= false (valid? [5 10 25])))
  (is (= true (valid? [3 4 5]))))

(deftest readers-by-example
  (is (= 0 (row-method "data/year_2016/day_3_example1.input")))
  (is (= 6 (column-method "data/year_2016/day_3_example2.input"))))
